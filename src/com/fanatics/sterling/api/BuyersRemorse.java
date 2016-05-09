package com.fanatics.sterling.api;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.fanatics.sterling.constants.FanaticsFraudCheckConstants;
import com.fanatics.sterling.util.CommonUtil;
import com.fanatics.sterling.util.FANConstants;
import com.fanatics.sterling.util.FANDBUtil;
import com.fanatics.sterling.util.XMLUtil;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class BuyersRemorse {

	private static YFCLogCategory logger= YFCLogCategory.instance(YFCLogCategory.class);
	
	public Document addBuyersRemorse(YFSEnvironment yfsEnv, Document inXML) throws ParseException{
		

		logger.info("Inside addBuyersRemorse");
		logger.info("addBuyersRemorse Input xml is 1: "+ XMLUtil.getXMLString(inXML));
		
		Element eleOrderHoldTypes = (Element) inXML.getElementsByTagName(FanaticsFraudCheckConstants.ATT_fanatics_OrderHoldTypes).item(0);
		
		// Create the element for the Buyers Remorse Hold
		/*Element eleOrderHoldType = inXML.createElement("OrderHoldType");
		inXML.importNode(eleOrderHoldType, true);
		logger.info("BR point 1");
		
		eleOrderHoldType.setAttribute("HoldType", "BUYERSREMORSE");
		eleOrderHoldType.setAttribute("Status", "1100");
		logger.info("BR point 2");
		eleOrderHoldTypes.appendChild(eleOrderHoldType);
		logger.info("BR point 3");*/
		
		Element eleOrderHoldType = inXML.createElement(FanaticsFraudCheckConstants.ATT_fanatics_OrderHoldType);
		
		logger.info("BR point 1");
		
		eleOrderHoldType.setAttribute(FanaticsFraudCheckConstants.ATT_fanatics_HoldType, FanaticsFraudCheckConstants.CONSTANT_fanatics_BUYERSREMORSE);
		eleOrderHoldType.setAttribute(FANConstants.ATT_STATUS, FANConstants.STR_1100);
		logger.info("BR point 2");
		
		Node tempNode = inXML.importNode(eleOrderHoldType, false);
		logger.info("eleOrderHoldTypes is "+ XMLUtil.getElementXMLString(eleOrderHoldTypes));
		logger.info("tempNode is "+ XMLUtil.getNodeValue(tempNode));
		
		eleOrderHoldTypes.appendChild(tempNode);
		
		logger.info("BR point 3");
		
		// Get the Remorse period in minutes from common codes
		String strIpGetCommonCodeList = "<CommonCode CodeType='BuyersRemorse' CodeValue='FANATICS_US'></CommonCode>";
		Document docIpGetCommonCodeList = null;
		try 
		{  
			docIpGetCommonCodeList = XMLUtil.getDocument(strIpGetCommonCodeList);
		} catch (Exception e) {  
			e.printStackTrace();  
		} 
		
		logger.info("addBuyersRemorse Input xml is 2: "+ XMLUtil.getXMLString(docIpGetCommonCodeList));
		
		Document docOPGetCommonCodeList=null;
		try {
			docOPGetCommonCodeList = CommonUtil.invokeAPI(yfsEnv, FANConstants.API_getCommonCodeList, docIpGetCommonCodeList);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		Element eleCommonCode = (Element) docOPGetCommonCodeList.getElementsByTagName(FANConstants.ATT_CommonCode).item(0) ;
		String strCodeShortDescription = eleCommonCode.getAttribute(FANConstants.ATT_CodeShortDescription);
		
		/**
		 *  Get current time and add remorse Period to the same
		 */
		
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
					
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date();
//					 String myTime = dateFormat.format(strCurrentDBDateTime);
					 
					 //logger.info("BR point 6 "+ myTime);
					 
					 date = dateFormat.parse(strCurrentDBDateTime); 
					 Calendar cal = Calendar.getInstance();
					 cal.setTime(date);
					 logger.info("BR point 7");
					 cal.add(Calendar.MINUTE, Integer.parseInt(strCodeShortDescription));
		
		 String strRemorsePeriod = dateFormat.format(cal.getTime());
		 logger.info("BR point 8");
		 logger.info("addBuyersRemorse new time is 3: "+ strRemorsePeriod);
		
		 /**
		  * Add Buyers Remorse Expiry Date to the input XML
		  */
		 logger.info("BR point 9");
		 Element eleOrderDates = inXML.createElement(FANConstants.ATT_OrderDates);
		 
		 logger.info("BR point 10");
		 Element eleOrderDate = inXML.createElement(FANConstants.ATT_OrderDate);
		 
		 logger.info("BR point 11");
		 eleOrderDate.setAttribute(FANConstants.ATT_DateTypeId, FanaticsFraudCheckConstants.CONSTANT_fanatics_BUYERS_REMORSE);
		 eleOrderDate.setAttribute(FANConstants.ATT_ExpectedDate, strRemorsePeriod);
		 logger.info("BR point 12");
				 
		 eleOrderDates.appendChild(eleOrderDate);
		 inXML.getDocumentElement().appendChild(eleOrderDates);
		 
		 logger.info("addBuyersRemorse Input xml is 4: "+ XMLUtil.getXMLString(inXML));
		 
		 // invoke changeOrder API
		try {
			CommonUtil.invokeAPI(yfsEnv, FANConstants.API_CHANGE_ORDER, inXML);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
 
		return inXML;
		
	}
	
}
