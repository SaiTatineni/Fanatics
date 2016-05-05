package com.fanatics.sterling.api;

import org.w3c.dom.Document;

import com.fanatics.sterling.util.CommonUtil;
import com.fanatics.sterling.util.FANConstants;
import com.fanatics.sterling.util.XMLUtil;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class AutoResolve {

	private static YFCLogCategory logger= YFCLogCategory.instance(YFCLogCategory.class);
	
	public void resolveHolds(YFSEnvironment yfsEnv, Document inXML){
		
		
		logger.info("Inside resolveHolds");
		logger.info("resolveHolds Input xml is: "+ XMLUtil.getXMLString(inXML));
		
		FraudCheck obj = new FraudCheck();
		Document docIPChangeOrder = obj.resolveAllHolds(yfsEnv, inXML);
		
		// invoke changeOrder API
		try {
			CommonUtil.invokeAPI(yfsEnv, FANConstants.API_CHANGE_ORDER, docIPChangeOrder);
			} catch (Exception e) {
			
			e.printStackTrace();
			}
		
	}
	
}
