package com.fanatics.sterling.api;

import java.io.StringReader;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fanatics.sterling.constants.FanaticsFraudCheckConstants;
import com.fanatics.sterling.util.CommonUtil;
import com.fanatics.sterling.util.FANConstants;
import com.fanatics.sterling.util.FANDBUtil;
import com.fanatics.sterling.util.RESTClient;
import com.fanatics.sterling.util.XMLUtil;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

public class FraudCheck {

	private static YFCLogCategory logger= YFCLogCategory.instance(YFCLogCategory.class);
	
	public Document processFraudResponse(YFSEnvironment yfsEnv, Document inXML) throws ParserConfigurationException{

		logger.info("Inside processFraudResponse");
		logger.info("processFraudResponse Input xml is 1: "+ XMLUtil.getXMLString(inXML));

		// Make REST call to the Fraud Engine
//		RESTClient restObj = new RESTClient();
//		
//		String fanFraudEngineBaseUrl = YFSSystem.getProperty(FanaticsFraudCheckConstants.FRAUD_PROPS_fanatics_BASE_URL); 
//		String fanFraudEngineUser =  YFSSystem.getProperty(FanaticsFraudCheckConstants.FRAUD_PROPS_fanatics_USERNAME);
//		String fanFraudEnginePwd =  YFSSystem.getProperty(FanaticsFraudCheckConstants.FRAUD_PROPS_fanatics_PASSWORD);
//		
//		restObj.setBaseUrl(fanFraudEngineBaseUrl); //("https://192.168.56.101:9443");
//		restObj.setUsername(fanFraudEngineUser); //("admin");
//		restObj.setPassword(fanFraudEnginePwd); //("password");

		String strFraudResponse = "";
		Document docResponseFanFraudEngine = null;

		/**
		 * REST call starts
		 */
//		try {
//			strFraudResponse = restObj.putDataToServer(FanaticsFraudCheckConstants.FRAUD_PROPS_fanatics_PATH, inXML.toString());
//
//
//			if (!strFraudResponse.equals(""))
//				docResponseFanFraudEngine = convertStringToDocument(strFraudResponse);
//			else {
//
//				logger.info("Fraud Engine is down");
//				Exception e = new Exception();
//				throw e;
//			}
//
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
		/**
		 * REST call ends
		 */		
			docResponseFanFraudEngine = inXML;
		
		// fetch the root element from the fraud Response
		Element eleFraudResponseRoot = docResponseFanFraudEngine.getDocumentElement();

		// check the sender information
		String strSender = eleFraudResponseRoot.getAttribute(FanaticsFraudCheckConstants.ATT_fanatics_Sender);
		logger.info("Sender "+ strSender);

		logger.info("FanaticsFraudCheckConstants.CONSTANT_fanatics_Accertify is "+FanaticsFraudCheckConstants.CONSTANT_fanatics_Accertify);
		logger.info("condition is "+ strSender.equals(FanaticsFraudCheckConstants.CONSTANT_fanatics_Accertify));
		
		// if its a secondary response
		if (strSender.equals(FanaticsFraudCheckConstants.CONSTANT_fanatics_Accertify)){

			// call getOrderDetails to fetch the token
			String strInputGetOrderDet = "<Order OrderNo='"+eleFraudResponseRoot.getAttribute(FANConstants.ORDER_NO)+"' " +
					"EnterpriseCode='"+eleFraudResponseRoot.getAttribute(FANConstants.ATT_EnterpriseCode)+"' " +
					"DocumentType='"+eleFraudResponseRoot.getAttribute(FANConstants.ATT_DocumentType)+"'/>";
			logger.info("inputXML loop : "+ strInputGetOrderDet);
			Document docInputGetOrderDet = convertStringToDocument(strInputGetOrderDet);

			String templateInputGetOrderDet = "<Order OrderName='' Status=''><CustomAttributes FraudToken='' /></Order>" ; // OrderName is temporarily being used to store the token from the Fraud Engine
			Document docTemplateGetOrderDet = convertStringToDocument(templateInputGetOrderDet);
			logger.info("docTemplateGetOrderDet "+ XMLUtil.getXMLString(docTemplateGetOrderDet));
			// invoke getOrderDetails
			Document docOutputGetOrderDet = null;
			try {
				docOutputGetOrderDet = CommonUtil.invokeAPI(yfsEnv, docTemplateGetOrderDet, FANConstants.API_GET_ORDER_DET, docInputGetOrderDet);
				logger.info("docOutputGetOrderDet "+ XMLUtil.getXMLString(docOutputGetOrderDet));

			} catch (Exception e) {

				e.printStackTrace();
			}

			// process this response only of the token received in the response matches the one on the order already and the order is not in the cancelled state
			Element eleOutputGetOrderDetRoot = docOutputGetOrderDet.getDocumentElement();
			String strFraudTokenOPGetOrdDet = XMLUtil.getXpathProperty(docOutputGetOrderDet, FanaticsFraudCheckConstants.XPATH_fanatics_FraudTokenOMS);
			logger.info("strFraudTokenOPGetOrdDet is "+ strFraudTokenOPGetOrdDet);

			// Fraud Token from the Fraud Engine
			String strFraudTokenResponseDoc = XMLUtil.getXpathProperty(docResponseFanFraudEngine,FanaticsFraudCheckConstants.XPATH_fanatics_FraudTokenFraudEngine);
			logger.info("strFraudTokenResponseDoc is "+strFraudTokenResponseDoc);
			
			logger.info(" cond 1 "+ !FANConstants.CANCELLED_STATUSES_DESCRIPTION.equals(eleOutputGetOrderDetRoot.getAttribute(FANConstants.ATT_STATUS)));
			logger.info("cond 2 "+ strFraudTokenOPGetOrdDet.equals(strFraudTokenResponseDoc));

			if (!FANConstants.CANCELLED_STATUSES_DESCRIPTION.equals(eleOutputGetOrderDetRoot.getAttribute(FANConstants.ATT_STATUS)) 
					&& strFraudTokenOPGetOrdDet.equals(strFraudTokenResponseDoc)) {
				logger.info("1 is true");
				processResponse(yfsEnv, docResponseFanFraudEngine);

			}

		}else{
			logger.info("2 is true");
			processResponse(yfsEnv, docResponseFanFraudEngine);

		}

		return inXML;


	}



	private void processResponse(YFSEnvironment yfsEnv, Document inXML) {

		logger.info("processResponse inXML 1"+ XMLUtil.getXMLString(inXML));
		Document docIPChangeOrder = null;
		Element eleInputChangeOrderRoot = inXML.getDocumentElement();
		// check the Fraud Response Code
		String strResponseCode = inXML.getDocumentElement().getAttribute(FanaticsFraudCheckConstants.ATT_fanatics_FraudResponseCode);
		logger.info("strResponseCode "+strResponseCode);

		if (strResponseCode.equals(FANConstants.CONSTANT_ZERO)) {

			logger.info("response is accept");
			// Resolve all holds 
			docIPChangeOrder = resolveAllHolds(yfsEnv, inXML);

			logger.info("docIPChangeOrder inXML 1"+ XMLUtil.getXMLString(docIPChangeOrder));

			// stampOrder with System Date-Time and the user who resolve this hold
			docIPChangeOrder = stampDateTimeUser(yfsEnv, docIPChangeOrder, strResponseCode);

			// Add Notes
			docIPChangeOrder = addNotes(yfsEnv, docIPChangeOrder, strResponseCode);
			logger.info("docIPChangeOrder inXML 2"+ XMLUtil.getXMLString(docIPChangeOrder));

			// Invoke FanaticsBuyersRemorse service
			try {
				CommonUtil.invokeService(yfsEnv, FanaticsFraudCheckConstants.ATT_fanatics_FanaticsBuyersRemorse, docIPChangeOrder);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		else if (strResponseCode.equals(FANConstants.CONSTANT_TWO)) {

			logger.info("response is reject");

			// resolve all holds
			docIPChangeOrder = resolveAllHolds(yfsEnv, inXML);

			// Add Notes
			logger.info("point 1");
			docIPChangeOrder = addNotes(yfsEnv, docIPChangeOrder, strResponseCode);

			// stampOrder with System Date-Time and the user who resolve this hold
			docIPChangeOrder = stampDateTimeUser(yfsEnv, docIPChangeOrder, strResponseCode);			

			// invoke changeOrder API
			logger.info("point 2");
			try {
				CommonUtil.invokeAPI(yfsEnv, FANConstants.API_CHANGE_ORDER, docIPChangeOrder);
			} catch (Exception e) {

				e.printStackTrace();
			}

			/**
			 * Call FanaticsCancelOrder service to cancel the lines on the order
			 */
			logger.info("point 3");
			try {
				CommonUtil.invokeService(yfsEnv, FanaticsFraudCheckConstants.SERVICE_fanatics_FanaticsCancelOrder, docIPChangeOrder);
			} catch (Exception e) {
				logger.info(e.getMessage());
				e.printStackTrace();
			}

			/**
			 * Post cancellation email trigger to fanatics
			 */
			logger.info("point 4");
			try {
				CommonUtil.invokeService(yfsEnv, FanaticsFraudCheckConstants.SERVICE_fanatics_FanaticsPostEmailTrigger, docIPChangeOrder);
			} catch (Exception e) {
				logger.info(e.getMessage());
				e.printStackTrace();
			}

		}

		else if (strResponseCode.equals(FANConstants.CONSTANT_ONE)){

			logger.info("response is review");
			// fetch the Fraud Response Token
			String strResponseToken = XMLUtil.getXpathProperty(inXML,FanaticsFraudCheckConstants.XPATH_fanatics_FraudTokenFraudEngine);//inXML.getDocumentElement().getAttribute("FraudToken");
            
			String strIPChangeOrder = "<Order OrderNo='"+eleInputChangeOrderRoot.getAttribute(FANConstants.ORDER_NO)+"' " +
					"EnterpriseCode='"+eleInputChangeOrderRoot.getAttribute(FANConstants.ATT_EnterpriseCode)+"' " +
					"DocumentType='"+eleInputChangeOrderRoot.getAttribute(FANConstants.ATT_DocumentType)+"'><CustomAttributes FraudToken='"+strResponseToken+"'/>" +
					"<OrderHoldTypes><OrderHoldType HoldType='PENDINGREVIEW' ReasonText='' ResolverUserId='' Status='1100'/>" +
					"<OrderHoldType HoldType='PENDINGEVALUATION' ReasonText='' ResolverUserId='' Status='1300'/>" +
					"</OrderHoldTypes></Order>";
			docIPChangeOrder = convertStringToDocument(strIPChangeOrder);
			// invoke changeOrder API
			logger.info("review docIPChangeOrder xml is: "+ XMLUtil.getXMLString(docIPChangeOrder));
			try {
				CommonUtil.invokeAPI(yfsEnv, FANConstants.API_CHANGE_ORDER, docIPChangeOrder);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

	}


	private Document stampDateTimeUser(YFSEnvironment yfsEnv,
			Document docIPChangeOrder, String strResponseCode) {

		// create the new element in the input xml
		Element eleCustomAttributes = docIPChangeOrder.createElement(FanaticsFraudCheckConstants.ATT_fanatics_CustomAttributes);

		// get the current time stamp from the DB
		ArrayList<Object[]> alTimeStamp = new ArrayList<Object[]>();

		try {

			alTimeStamp = FANDBUtil.getDBResult(yfsEnv, FanaticsFraudCheckConstants.SQL_fanatics_GetDBTime, 1);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Object[] resultRow = alTimeStamp.get(0);
		String strCurrentDBDateTime = (String) resultRow[0]; 
		logger.info("strCurrentDBDateTime is "+ strCurrentDBDateTime);

		// get the current user from the environment
		String strUserID = yfsEnv.getUserId();

		// set the attributes in the xml
		eleCustomAttributes.setAttribute(FanaticsFraudCheckConstants.ATT_fanatics_FraudStatusTS, strCurrentDBDateTime);
		eleCustomAttributes.setAttribute(FanaticsFraudCheckConstants.ATT_fanatics_FraudStatusUserID, strUserID);
		eleCustomAttributes.setAttribute(FanaticsFraudCheckConstants.ATT_fanatics_FraudStatus, strResponseCode);

		docIPChangeOrder.getDocumentElement().appendChild(eleCustomAttributes);

		logger.info("docIPChangeOrder inXML here is "+ XMLUtil.getXMLString(docIPChangeOrder));

		return docIPChangeOrder;
	}



	private Document addNotes(YFSEnvironment yfsEnv, Document docIPChangeOrder, String strResponseCode) {

		logger.info("response code is "+ strResponseCode);
		Element eleInputChangeOrderRoot = docIPChangeOrder.getDocumentElement();

		Element eleNotes = docIPChangeOrder.createElement(FANConstants.ATT_Notes);
		Element eleNote = docIPChangeOrder.createElement(FANConstants.ATT_Note);

		if (strResponseCode.equals(FANConstants.CONSTANT_ZERO))
			eleNote.setAttribute(FANConstants.ATT_NoteText, "Fraud Engine passed the order");
		else if (strResponseCode.equals(FANConstants.CONSTANT_ONE))
			eleNote.setAttribute(FANConstants.ATT_NoteText, "Fraud Engine passed the request to Accertify");
		else if (strResponseCode.equals(FANConstants.CONSTANT_TWO))
			eleNote.setAttribute(FANConstants.ATT_NoteText, "Fraud Engine rejected the order");

		eleNotes.appendChild(eleNote);

		eleInputChangeOrderRoot.appendChild(eleNotes);

		logger.info("Input xml is: "+ XMLUtil.getXMLString(docIPChangeOrder));

		return docIPChangeOrder;
	}



	public Document resolveAllHolds(YFSEnvironment yfsEnv, Document inXML) {

		logger.info("resolveAllHolds inXML 1"+ XMLUtil.getXMLString(inXML));
		Document docIPChangeOrder = null;
		Element eleInputChangeOrderRoot = inXML.getDocumentElement();

		String strIPChangeOrder = "<Order OrderNo='"+eleInputChangeOrderRoot.getAttribute(FANConstants.ORDER_NO)+"' " +
				"EnterpriseCode='"+eleInputChangeOrderRoot.getAttribute(FANConstants.ATT_EnterpriseCode)+"' " +
				"DocumentType='"+eleInputChangeOrderRoot.getAttribute(FANConstants.ATT_DocumentType)+"'></Order>";

		docIPChangeOrder = convertStringToDocument(strIPChangeOrder);

		logger.info("resolveAllHolds docIPChangeOrder is: "+ XMLUtil.getXMLString(docIPChangeOrder));

		// call getOrderDetails to get the present holds

		String templateGetOrderDetails = "<Order EnterpriseCode=''><OrderHoldTypes><OrderHoldType HoldType='' Status=''></OrderHoldType></OrderHoldTypes></Order>" ;
		Document docGetOrderDetails = convertStringToDocument(templateGetOrderDetails);

		Document docOPGetOrderDetails = null;
		try {
			docOPGetOrderDetails = CommonUtil.invokeAPI(yfsEnv, docGetOrderDetails, FANConstants.API_GET_ORDER_DET, docIPChangeOrder);
			logger.info("resolveAllHolds docOPGetOrderDetails is: "+ XMLUtil.getXMLString(docOPGetOrderDetails));
		} catch (Exception e) {

			e.printStackTrace();
		}

		// add OrderHoldTypes Element to the xml
		Element eleOrderHoldTypes = docIPChangeOrder.createElement(FanaticsFraudCheckConstants.ATT_fanatics_OrderHoldTypes);
		Node tempNode = docIPChangeOrder.importNode(eleOrderHoldTypes, false);
		docIPChangeOrder.getDocumentElement().appendChild(tempNode);
		logger.info("resolveAllHolds docIPChangeOrder is 11: "+ XMLUtil.getXMLString(docIPChangeOrder));

		// get the handle of the OrderHoldTypes element present in the docIPChangeOrder
		eleOrderHoldTypes = (Element) docIPChangeOrder.getDocumentElement().getElementsByTagName(FanaticsFraudCheckConstants.ATT_fanatics_OrderHoldTypes).item(0);

		// add the Hold Types (to be resolved) to the changeOrder input xml
		NodeList nlOrderHoldType = docOPGetOrderDetails.getElementsByTagName(FanaticsFraudCheckConstants.ATT_fanatics_OrderHoldType);
		int nlOrderHoldTypeLength = nlOrderHoldType.getLength();
		logger.info("nlOrderHoldTypeLength" + nlOrderHoldTypeLength);
		Element eleOrderHoldType = null;

		for(int i=0;i<nlOrderHoldTypeLength;i++){
			logger.info("inside the loop ");
			eleOrderHoldType = (Element) nlOrderHoldType.item(i);

			// add the current hold to the input doc only if the status is created (1100)
			logger.info("status here is "+ eleOrderHoldType.getAttribute(FANConstants.ATT_STATUS));
			if (eleOrderHoldType.getAttribute(FANConstants.ATT_STATUS).equals(FANConstants.STR_1100)){
				logger.info("inside the if cond ");
				// create new element OrderHoldType
				Element eleIPOrderHoldType = docIPChangeOrder.createElement(FanaticsFraudCheckConstants.ATT_fanatics_OrderHoldType);
				eleIPOrderHoldType.setAttribute(FanaticsFraudCheckConstants.ATT_fanatics_HoldType, eleOrderHoldType.getAttribute(FanaticsFraudCheckConstants.ATT_fanatics_HoldType));
				eleIPOrderHoldType.setAttribute(FANConstants.ATT_STATUS, FANConstants.STR_1300);

				tempNode = docIPChangeOrder.importNode(eleIPOrderHoldType, false);
				eleOrderHoldTypes.appendChild(tempNode);

				logger.info("resolveAllHolds docIPChangeOrder is 12: "+ XMLUtil.getXMLString(docIPChangeOrder));
			}

		}

		// invoke changeOrder API
		/*try {
				CommonUtil.invokeAPI(yfsEnv, FANConstants.API_CHANGE_ORDER, docIPChangeOrder);
				} catch (Exception e) {

				e.printStackTrace();
				}*/

		return docIPChangeOrder;
	}



	private static Document convertStringToDocument(String xmlStr) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
		DocumentBuilder builder;  
		try 
		{  
			builder = factory.newDocumentBuilder();  
			Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
			return doc;
		} catch (Exception e) {  
			e.printStackTrace();  
		} 
		return null;
	}
}
