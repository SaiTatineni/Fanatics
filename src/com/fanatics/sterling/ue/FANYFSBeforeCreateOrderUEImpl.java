package com.fanatics.sterling.ue;

import java.text.ParseException;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fanatics.sterling.util.CommonUtil;
import com.fanatics.sterling.constants.CreateOrderConstants;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSUserExitException;
import com.yantra.yfs.japi.ue.YFSBeforeCreateOrderUE;

/**
 * 
 * This class provides the Use Exit implementation for the
 * YFSBeforeCreateOrderUE The extended item attributes are queried from the item
 * database and populated as orderline extensions
 * 
 * @(#) FANYFSBeforeCreateOrderUEImpl.java Created on Apr 11, 2016 11:30:22 AM
 * 
 *      Package Declaration: File Name: FANYFSBeforeCreateOrderUEImpl.java
 *      Package Name: com.fanatics.sterling.ue Project name: Fanatics Type
 *      Declaration: Class Name: FANYFSBeforeCreateOrderUEImpl Type Comment:
 * 
 * 
 * @author kntagkas
 * @version 1.0
 * @history
 * 
 * 
 * 
 * 			(C) Copyright 2016 by owner. All Rights Reserved.
 * 
 *          This software is the confidential and proprietary information of the
 *          owner. ("Confidential Information"). Redistribution of the source
 *          code or binary form is not permitted without prior authorization
 *          from the owner.
 * 
 */

public class FANYFSBeforeCreateOrderUEImpl implements YFSBeforeCreateOrderUE, YIFCustomApi {

	private static YFCLogCategory log = YFCLogCategory.instance("com.yantra.yfc.log.YFCLogCategory");

	private Properties props = null;

	@Override
	public String beforeCreateOrder(YFSEnvironment env, String inStr) throws YFSUserExitException {
		
		Document inDoc  = SCXmlUtil.createFromString(inStr);
		Document outDoc = beforeCreateOrder(env, inDoc);
		inStr 			= SCXmlUtil.getString(outDoc);
		
		return inStr;
	}

	@Override
	public Document beforeCreateOrder(YFSEnvironment env, Document inDoc) throws YFSUserExitException {

		log.verbose("FANYFSBeforeCreateOrderUEImpl -> Begin");

		try {

			String carrierServiceCode = SCXmlUtil.getXpathAttribute(inDoc.getDocumentElement(),
					CreateOrderConstants.XPATH_CARRIER_SERVICE_CODE);
			Document listCarrierServiceInput = SCXmlUtil.createDocument(CreateOrderConstants.EL_CARRIER_SERVICE);
			listCarrierServiceInput.getDocumentElement().setAttribute(CreateOrderConstants.ATT_CARRIER_SERVICE_CODE, carrierServiceCode);

			log.verbose("Calling listCarrierService API for CarrierServiceCode : " + carrierServiceCode);
			
			Document listCarrierServiceOutput = CommonUtil.invokeAPI(env, CreateOrderConstants.API_LIST_CARRIER_SERVICE,
					listCarrierServiceInput);
			
			boolean carrierListNotEmpty = listCarrierServiceOutput.getDocumentElement().hasChildNodes();
			
			Element rootMessageNode = inDoc.getDocumentElement();
			
			if (rootMessageNode != null) {
				
				NodeList orderLines = rootMessageNode.getElementsByTagName(CreateOrderConstants.EL_ORDER_LINE);
				
				if (orderLines != null && orderLines.getLength() > 0) {
					for (int i = 0; i < orderLines.getLength(); i++) {
						if (orderLines.item(i).getNodeType() == Node.ELEMENT_NODE) {
							
							Element orderLine   = (Element) orderLines.item(i);
							Element orderDates  = SCXmlUtil.getXpathElement(orderLine, CreateOrderConstants.XPATH_ORDER_DATES);
							Element orderDate   = SCXmlUtil.getXpathElement(orderLine,
									CreateOrderConstants.XPATH_PROMISHED_SHIP_DATE);
							
							if(null == orderDate){
								
								log.error("FANYFSBeforeCreateOrderUEImpl --> ERROR: OrderDate Mandatory field missing!");
								
							}
							
							String expectedDate = orderDate.getAttribute(CreateOrderConstants.ATT_COMMITTED_DATE);
							
							String minDelivery = expectedDate;
							String maxDelivery = expectedDate;

							if (carrierListNotEmpty) {

								String maxTransitDays = SCXmlUtil.getXpathAttribute(
										listCarrierServiceOutput.getDocumentElement(),
										CreateOrderConstants.XPATH_MAX_TRANSIT_DAYS);
								String fixedTransitDays = SCXmlUtil.getXpathAttribute(
										listCarrierServiceOutput.getDocumentElement(),
										CreateOrderConstants.XPATH_FIXED_TRANSIT_DAYS);

								minDelivery = calcDate(expectedDate, fixedTransitDays);
								maxDelivery = calcDate(expectedDate, maxTransitDays);

							} else {
								log.verbose("Carrier Service not found - defaulting to PROMISED_SHIP_DATE");
							}

							Element EleMinDelivery = (Element) orderDate.cloneNode(false);
							Element EleMaxDelivery = (Element) orderDate.cloneNode(false);
							
							orderDates.appendChild(EleMinDelivery);
							orderDates.appendChild(EleMaxDelivery);
							
							changeElements(EleMinDelivery, minDelivery, CreateOrderConstants.ELE_MIN_DELIVERY);
							changeElements(EleMaxDelivery, maxDelivery, CreateOrderConstants.ELE_MAX_DELIVERY);
														
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("FANYFSBeforeCreateOrderUEImpl --> ERROR: " + e.getMessage(), e);
			throw new YFSUserExitException("FANYFSBeforeCreateOrderUEImpl");
		}

		log.verbose("FANYFSBeforeCreateOrderUEImpl -> End");
		
		return inDoc;
	}

	public String calcDate(String expectedDate, String transitDays) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat(CreateOrderConstants.SIMPLE_DATE_FORMAT);
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(expectedDate));
		c.add(Calendar.DATE, Integer.parseInt(transitDays)); 
															
		String Delivery = sdf.format(c.getTime());
		return Delivery;

	}

	public void changeElements(Element EleToBeChanged, String deliveryDate, String dateTypeId) {

		EleToBeChanged.setAttribute(CreateOrderConstants.ATT_COMMITTED_DATE, deliveryDate);
		EleToBeChanged.setAttribute(CreateOrderConstants.ATT_DATE_TYPE_ID, dateTypeId);

	}
	
	@Override
	public void setProperties(Properties arg0) throws Exception {
		
		props = arg0;
	}
	
	protected String getProperty(String name) {

		return props.getProperty(name);
	}

}
