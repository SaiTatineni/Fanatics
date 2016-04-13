package com.fanatics.sterling.api;

/**
 * This class contains the Vendor Location Feed functionality required for the fanatics Etaildirect Implementation.
 * 
 *  This code is called when the Vendor Location Feed is received by the Fanatics OMS system, to perform the below mentioned functionalities:
 *  1. Remove Vendor from the Distribution Group before the input Vendor Location is deleted from the OMS.
 *  2. Add Vendor to the  Distribution Group before updating the Vendor information, as per the input xml.
 *  
 *  Note: Method "processNodeForMonitoringDG" performs the above mentioned functions
 *  
 * @(#) manageDistributionGroupAPI.java    
 * Created on   April 13, 2016
 *              
 *
 * Package Declaration: 
 * File Name:       manageDistributionGroupAPI.java
 * Package Name:    com.fanatics.sterling.api;
 * Project name:    Fanatics
 * Type Declaration:    
 * Class Name:      manageDistributionGroupAPI
 * 
 * @author Neeraj Sharda
 * @version 1.0
 * @history none
 *     
 * 
 *  
 *
 * (C) Copyright 2006-2007 by owner.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information
 * of the owner. ("Confidential Information").
 * Redistribution of the source code or binary form is not permitted
 * without prior authorization from the owner.
 *
 */

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fanatics.sterling.util.CommonUtil;
import com.fanatics.sterling.constants.VendorFeedConstants;
import com.fanatics.sterling.util.XMLUtil;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfs.japi.YFSEnvironment;

public class ManageDistributionGroupAPI implements YIFCustomApi {

	private Properties props = null;
	private Document docOutXML = null;

	public Document processNodeForMonitoringDG(YFSEnvironment env, Document inXML) throws Exception{

		System.out.println("inXML is "+ XMLUtil.getXMLString(inXML));
		// get the value of RTAM DG and the Calling Organization Code from the Arguments of the service
		final String strRTAMDGCode = getPropertyVal(VendorFeedConstants.ARG_fanatics_RTAM_DISTRIBUTION_GROUP);
		final String strCallingOrgCode = getPropertyVal(VendorFeedConstants.ARG_fanatics_CALLING_ORG_CODE);

		System.out.println("strRTAMDGCode is "+strRTAMDGCode);
		System.out.println("strCallingOrgCode is "+strCallingOrgCode);

		// set both the properties in the inXML as DefaultDistributionRuleId and CallingOrganizationCode
		Element rootInXML = inXML.getDocumentElement();
		rootInXML.setAttribute(VendorFeedConstants.ATT_fanatics_DEFAULT_DISTRIBUTION_RULE_ID, strRTAMDGCode);
		rootInXML.setAttribute(VendorFeedConstants.ARG_fanatics_CALLING_ORG_CODE, strCallingOrgCode);
		System.out.println("inXML is 1"+ XMLUtil.getXMLString(inXML));

		// find out the required action
		String strOperation = rootInXML.getAttribute(VendorFeedConstants.ATT_fanatics_OPERATION);
		System.out.println("strOperation is "+ strOperation);
		/**
		 *  invoke the service getDistributionList
		 */
		docOutXML = CommonUtil.invokeService(env, VendorFeedConstants.SERVICE_fanatics_GET_DISTRIBUTION_LIST, inXML);
		System.out.println("docOutXML is 1"+ XMLUtil.getXMLString(docOutXML));


		// check if the vendor is a part of DG already
		Element rootdocOPgetDistributionList = docOutXML.getDocumentElement();
		System.out.println("first condition " + rootdocOPgetDistributionList.hasChildNodes());
		System.out.println("sec condition "+strOperation.equalsIgnoreCase(VendorFeedConstants.CONSTANT_fanatics_DELETE));

		if (rootdocOPgetDistributionList.hasChildNodes() && strOperation.equalsIgnoreCase(VendorFeedConstants.CONSTANT_fanatics_DELETE)){
			// if the vendor is a part of DG 
			/**
			 *  invoke deleteDistribution
			 */
			System.out.println("point 1");
			docOutXML = CommonUtil.invokeService(env, VendorFeedConstants.SERVICE_fanatics_DELETE_DISTRIBUTION, inXML);	
		}

		// else if the vendor is not present in the DG and the operation is not Delete: Add the vendor to the DG

		else if ( !rootdocOPgetDistributionList.hasChildNodes() && !strOperation.equalsIgnoreCase(VendorFeedConstants.CONSTANT_fanatics_DELETE)){
			/**
			 *  invoke createDistributionList
			 */
			System.out.println("point 2");
			docOutXML = CommonUtil.invokeService(env, VendorFeedConstants.SERVICE_fanatics_CREATE_DISTRIBUTION, inXML);
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
