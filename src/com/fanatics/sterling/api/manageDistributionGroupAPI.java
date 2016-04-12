/**
 *  @author Neeraj Sharda
 *  
 *  This code is called when the Vendor Location Feed is received by the Fanatics OMS system, to perform the below mentioned functionalities:
 *  1. Remove Vendor from the Distribution Group before the input Vendor Location is deleted from the OMS.
 *  2. Add Vendor to the  Distribution Group before updating the Vendor information, as per the input xml.
 *  
 *  Note: Method "processNodeForMonitoringDG" performs the above mentioned functions
 *  
 */

package com.fanatics.sterling.api;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fanatics.sterling.util.CommonUtil;
import com.fanatics.sterling.util.FANConstants;
import com.fanatics.sterling.util.XMLUtil;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfs.japi.YFSEnvironment;

public class manageDistributionGroupAPI implements YIFCustomApi {

	private Properties props = null;
	private Document docOutXML = null;

	public Document processNodeForMonitoringDG(YFSEnvironment env, Document inXML) throws Exception{

		System.out.println("inXML is "+ XMLUtil.getXMLString(inXML));
		// get the value of RTAM DG and the Calling Organization Code from the Arguments of the service
		final String strRTAMDGCode = getPropertyVal(FANConstants.ARG_fanatics_RTAM_DISTRIBUTION_GROUP);
		final String strCallingOrgCode = getPropertyVal(FANConstants.ARG_fanatics_CALLING_ORG_CODE);

		System.out.println("strRTAMDGCode is "+strRTAMDGCode);
		System.out.println("strCallingOrgCode is "+strCallingOrgCode);

		// set both the properties in the inXML as DefaultDistributionRuleId and CallingOrganizationCode
		Element rootInXML = inXML.getDocumentElement();
		rootInXML.setAttribute(FANConstants.ATT_fanatics_DEFAULT_DISTRIBUTION_RULE_ID, strRTAMDGCode);
		rootInXML.setAttribute(FANConstants.ARG_fanatics_CALLING_ORG_CODE, strCallingOrgCode);
		System.out.println("inXML is 1"+ XMLUtil.getXMLString(inXML));

		// find out the required action
		String strOperation = rootInXML.getAttribute(FANConstants.ATT_fanatics_OPERATION);
		System.out.println("strOperation is "+ strOperation);
		/**
		 *  invoke the service getDistributionList
		 */
		docOutXML = CommonUtil.invokeService(env, FANConstants.SERVICE_fanatics_GET_DISTRIBUTION_LIST, inXML);
		System.out.println("docOutXML is 1"+ XMLUtil.getXMLString(docOutXML));


		// check if the vendor is a part of DG already
		Element rootdocOPgetDistributionList = docOutXML.getDocumentElement();
		System.out.println("first condition " + rootdocOPgetDistributionList.hasChildNodes());
		System.out.println("sec condition "+strOperation.equalsIgnoreCase(FANConstants.CONSTANT_fanatics_DELETE));

		if (rootdocOPgetDistributionList.hasChildNodes() && strOperation.equalsIgnoreCase(FANConstants.CONSTANT_fanatics_DELETE)){
			// if the vendor is a part of DG 
			/**
			 *  invoke deleteDistribution
			 */
			System.out.println("point 1");
			docOutXML = CommonUtil.invokeService(env, FANConstants.SERVICE_fanatics_DELETE_DISTRIBUTION, inXML);	
		}

		// else if the vendor is not present in the DG and the operation is not Delete: Add the vendor to the DG

		else if ( !rootdocOPgetDistributionList.hasChildNodes() && !strOperation.equalsIgnoreCase(FANConstants.CONSTANT_fanatics_DELETE)){
			/**
			 *  invoke createDistributionList
			 */
			System.out.println("point 2");
			docOutXML = CommonUtil.invokeService(env, FANConstants.SERVICE_fanatics_CREATE_DISTRIBUTION, inXML);
		}

		return docOutXML;
	}


	protected String getPropertyVal(String name) {

		return props.getProperty(name);
	}

	public void setProperties(Properties arg0) throws Exception {

		props = arg0;

	}

}
