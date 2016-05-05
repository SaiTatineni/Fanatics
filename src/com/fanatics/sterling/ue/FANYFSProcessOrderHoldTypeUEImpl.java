package com.fanatics.sterling.ue;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.fanatics.sterling.util.CommonUtil;
import com.fanatics.sterling.util.FANConstants;
import com.fanatics.sterling.constants.FanaticsFraudCheckConstants;
import com.fanatics.sterling.util.XMLUtil;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSUserExitException;
import com.yantra.yfs.japi.ue.YFSProcessOrderHoldTypeUE;

public class FANYFSProcessOrderHoldTypeUEImpl implements YFSProcessOrderHoldTypeUE{

	private static YFCLogCategory logger= YFCLogCategory.instance(YFCLogCategory.class);
	@Override
	public Document processOrderHoldType(YFSEnvironment yfsEnv, Document inXML)
			throws YFSUserExitException {
		
		logger.verbose("Inside processOrderHoldType");
		logger.verbose("Input xml is: "+ XMLUtil.getXMLString(inXML));
		
		// fetch the root element of the document
		Element eleRootInXML = inXML.getDocumentElement();
		logger.verbose("processOrderHoldType point 1");
		// fetch the status
		String strHoldStatus = FANConstants.NO_VALUE;
		String strHoldType = FANConstants.NO_VALUE; 
		
		// fetch the HoldType
		NodeList nlOrderHoldType = eleRootInXML.getElementsByTagName(FANConstants.ELEM_fanatics_OrderHoldType);
		logger.verbose("processOrderHoldType point 2");
		int nlOrderHoldTypeLength = nlOrderHoldType.getLength();
		Element eleOrderHoldType = null;
		
		for(int i=0;i<nlOrderHoldTypeLength;i++)
		{
			logger.verbose("processOrderHoldType inside loop "+i);
			eleOrderHoldType = (Element) nlOrderHoldType.item(i);
			strHoldStatus = eleOrderHoldType.getAttribute(FANConstants.ATT_STATUS);
			strHoldType = eleOrderHoldType.getAttribute(FANConstants.ATT_fanatics_HoldType);
			
			logger.verbose("strHoldType "+strHoldType + strHoldType.equals(FanaticsFraudCheckConstants.HOLDTYPE_fanatics_PENDINGEVALUATION));
			logger.verbose("strHoldStatus "+strHoldStatus + strHoldStatus.equals(FANConstants.STR_1100));
			
			if (strHoldType.equals(FanaticsFraudCheckConstants.HOLDTYPE_fanatics_PENDINGEVALUATION) && strHoldStatus.equals(FANConstants.STR_1100)) {
			      //Invoke FanaticsFraudCheck service;
				try {
					Document docFraudResponse = CommonUtil.invokeService(yfsEnv, FanaticsFraudCheckConstants.SERVICE_fanatics_FanaticsFraudCheck, inXML);
				} catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
				break;
			}
		
			
		}
		return inXML;
	}

}
