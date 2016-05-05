<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
	<OrderRelease>
	<Order>
		<xsl:attribute name="OrderDate">
         <xsl:value-of select="//OrderRelease/Order/@OrderDate"/>
  		</xsl:attribute>
  		<xsl:attribute name="OrderNo">
         <xsl:value-of select="//OrderRelease/Order/@OrderNo"/>
  		</xsl:attribute>
  		<xsl:attribute name="EntryType">
         <xsl:value-of select="//OrderRelease/Order/@EntryType"/>
  		</xsl:attribute>
  		<xsl:attribute name="LevelOfService">
         <xsl:value-of select="//OrderRelease/Order/@LevelOfService"/>
  		</xsl:attribute>
  		<xsl:attribute name="PaymentStatus">
         <xsl:value-of select="//OrderRelease/Order/@PaymentStatus"/>
  		</xsl:attribute>
  		<Instructions>
  		<Instruction>
  			<xsl:attribute name="InstructionText">
         		<xsl:value-of select="//OrderRelease/Order/Instructions/Instruction/@InstructionText"/>
  			</xsl:attribute>
  			<xsl:attribute name="InstructionType">
         		<xsl:value-of select="//OrderRelease/Order/Instructions/Instruction/@InstructionType"/>
  			</xsl:attribute>
  			<xsl:attribute name="SequenceNo">
         		<xsl:value-of select="//OrderRelease/Order/Instructions/Instruction/@SequenceNo"/>
  			</xsl:attribute>
  		</Instruction>
  		</Instructions>
  		<ChargeTransactionDetails>
  			<PaymentMethod>
  				<xsl:attribute name="CreditCardName">
         			<xsl:value-of select="//OrderRelease/Order/ChargeTransactionDetails/PaymentMethod/@CreditCardName"/>
  				</xsl:attribute>
  				<xsl:attribute name="PaymentType">
         			<xsl:value-of select="//OrderRelease/Order/ChargeTransactionDetails/PaymentMethod/@PaymentType"/>
  				</xsl:attribute>
  				<xsl:attribute name="CreditCardType">
         			<xsl:value-of select="//OrderRelease/Order/ChargeTransactionDetails/PaymentMethod/@CreditCardType"/>
  				</xsl:attribute>
  				<xsl:attribute name="DisplayCreditCardNo">
         			<xsl:value-of select="//OrderRelease/Order/ChargeTransactionDetails/PaymentMethod/@DisplayCreditCardNo"/>
  				</xsl:attribute>
  				<xsl:attribute name="DisplayPaymentReference1">
         			<xsl:value-of select="//OrderRelease/Order/ChargeTransactionDetails/PaymentMethod/@DisplayPaymentReference1"/>
  				</xsl:attribute>
  				<xsl:attribute name="DisplaySvcNo">
         			<xsl:value-of select="//OrderRelease/Order/ChargeTransactionDetails/PaymentMethod/@DisplaySvcNo"/>
  				</xsl:attribute>
  				<xsl:attribute name="FirstName">
         			<xsl:value-of select="//OrderRelease/Order/ChargeTransactionDetails/PaymentMethod/@FirstName"/>
  				</xsl:attribute>
  				<xsl:attribute name="LastName">
         			<xsl:value-of select="//OrderRelease/Order/ChargeTransactionDetails/PaymentMethod/@LastName"/>
  				</xsl:attribute>
  			</PaymentMethod>
  		</ChargeTransactionDetails>
	</Order>
	<PersonInfoShipTo>
		<xsl:attribute name="AddressLine1">
         		<xsl:value-of select="//OrderRelease/PersonInfoShipTo/@AddressLine1"/>
  			</xsl:attribute>
  			<xsl:attribute name="AddressLine2">
         		<xsl:value-of select="//OrderRelease/PersonInfoShipTo/@AddressLine2"/>
  			</xsl:attribute>
  			<xsl:attribute name="AddressLine3">
         		<xsl:value-of select="//OrderRelease/PersonInfoShipTo/@AddressLine4"/>
  			</xsl:attribute>
  			<xsl:attribute name="AddressLine4">
         		<xsl:value-of select="//OrderRelease/PersonInfoShipTo/@AddressLine4"/>
  			</xsl:attribute>
  			<xsl:attribute name="AddressLine5">
         		<xsl:value-of select="//OrderRelease/PersonInfoShipTo/@AddressLine5"/>
  			</xsl:attribute>
  			<xsl:attribute name="AddressLine6">
         		<xsl:value-of select="//OrderRelease/PersonInfoShipTo/@AddressLine6"/>
  			</xsl:attribute>
  			<xsl:attribute name="City">
         		<xsl:value-of select="//OrderRelease/PersonInfoShipTo/@City"/>
  			</xsl:attribute>
  			<xsl:attribute name="Country">
         		<xsl:value-of select="//OrderRelease/PersonInfoShipTo/@Country"/>
  			</xsl:attribute>
  			<xsl:attribute name="DayPhone">
         		<xsl:value-of select="//OrderRelease/PersonInfoShipTo/@DayPhone"/>
  			</xsl:attribute>
  			<xsl:attribute name="EMailID">
         		<xsl:value-of select="//OrderRelease/PersonInfoShipTo/@EMailID"/>
  			</xsl:attribute>
  			<xsl:attribute name="JobTitle">
         		<xsl:value-of select="//OrderRelease/PersonInfoShipTo/@JobTitle"/>
  			</xsl:attribute>
  			<xsl:attribute name="FirstName">
         		<xsl:value-of select="//OrderRelease/PersonInfoShipTo/@FirstName"/>
  			</xsl:attribute>
  			<xsl:attribute name="LastName">
         		<xsl:value-of select="//OrderRelease/PersonInfoShipTo/@LastName"/>
  			</xsl:attribute>
  			<xsl:attribute name="MiddleName">
         		<xsl:value-of select="//OrderRelease/PersonInfoShipTo/@MiddleName"/>
  			</xsl:attribute>
  			<xsl:attribute name="OtherPhone">
         		<xsl:value-of select="//OrderRelease/PersonInfoShipTo/@OtherPhone"/>
  			</xsl:attribute>
  			<xsl:attribute name="State">
         		<xsl:value-of select="//OrderRelease/PersonInfoShipTo/@State"/>
  			</xsl:attribute>
  			<xsl:attribute name="Suffix">
         		<xsl:value-of select="//OrderRelease/PersonInfoShipTo/@Suffix"/>
  			</xsl:attribute>
  			<xsl:attribute name="ZipCode">
         		<xsl:value-of select="//OrderRelease/PersonInfoShipTo/@ZipCode"/>
  			</xsl:attribute>
	</PersonInfoShipTo>
	<xsl:for-each select="OrderRelease/OrderLine">			
	<OrderLine>
			<xsl:attribute name="FulfillmentType">
         		<xsl:value-of select="./@FulfillmentType"/>
  			</xsl:attribute>
  			<xsl:attribute name="GiftFlag">
         		<xsl:value-of select="./@GiftFlag"/>
  			</xsl:attribute>
  			<xsl:attribute name="GiftWrap">
         		<xsl:value-of select="./@GiftWrap"/>
  			</xsl:attribute>
  			<xsl:attribute name="LevelOfService">
         		<xsl:value-of select="./@LevelOfService"/>
  			</xsl:attribute>
  			<xsl:attribute name="LineSeqNo">
         		<xsl:value-of select="./@LineSeqNo"/>
  			</xsl:attribute>
  			<xsl:attribute name="LineType">
         		<xsl:value-of select="./@LineType"/>
  			</xsl:attribute>
  			<xsl:attribute name="OrderedQty">
         		<xsl:value-of select="./@OrderedQty"/>
  			</xsl:attribute>
  			<xsl:attribute name="OtherCharges">
         		<xsl:value-of select="./@OtherCharges"/>
  			</xsl:attribute>
  			<xsl:attribute name="PackListType">
         		<xsl:value-of select="./@PackListType"/>
  			</xsl:attribute>
  			<xsl:attribute name="PromisedApptStartDate">
         		<xsl:value-of select="./@PromisedApptStartDate"/>
  			</xsl:attribute>
  			<xsl:attribute name="PromisedApptEndDate">
         		<xsl:value-of select="./@PromisedApptEndDate"/>
  			</xsl:attribute>
  			<xsl:attribute name="ReqDeliveryDate">
         		<xsl:value-of select="./@ReqDeliveryDate"/>
  			</xsl:attribute>
  			<xsl:attribute name="ReqShipDate">
         		<xsl:value-of select="./@ReqShipDate"/>
  			</xsl:attribute>
  			<xsl:attribute name="PrimeLineNo">
         		<xsl:value-of select="./@PrimeLineNo"/>
  			</xsl:attribute>
  			<xsl:attribute name="ReservationID">
         		<xsl:value-of select="./@ReservationID"/>
  			</xsl:attribute>
  			<xsl:attribute name="SCAC">
         		<xsl:value-of select="./@SCAC"/>
  			</xsl:attribute>
  			<xsl:attribute name="ScacAndService">
         		<xsl:value-of select="./@ScacAndService"/>
  			</xsl:attribute>
  			<xsl:attribute name="SubLineNo">
         		<xsl:value-of select="./@SubLineNo"/>
  			</xsl:attribute>
  			<xsl:attribute name="ShipNode">
         		<xsl:value-of select="//OrderRelease/@ShipNode"/>
  			</xsl:attribute>
  				<!-- put this under order line?-->
  			<xsl:attribute name="Status">
         		<xsl:value-of select="./OrderStatuses/OrderStatus/@Status"/>
  			</xsl:attribute>
  			<xsl:attribute name="StatusQty">
         		<xsl:value-of select="./OrderStatuses/OrderStatus/@StatusQty"/>
  			</xsl:attribute>
  			<xsl:attribute name="Charges">
         		<xsl:value-of select="./LinePackListPriceInfo/@Charges"/>
  			</xsl:attribute>
  			<xsl:attribute name="ExtendedPrice">
         		<xsl:value-of select="./LinePackListPriceInfo/@ExtendedPrice"/>
  			</xsl:attribute>
  			<xsl:attribute name="LineTotal">
         		<xsl:value-of select="./LinePackListPriceInfo/@LineTotal"/>
  			</xsl:attribute>
  			<xsl:attribute name="ListPrice">
         		<xsl:value-of select="./LinePackListPriceInfo/@ListPrice"/>
  			</xsl:attribute>
  			<xsl:attribute name="RetailPrice">
         		<xsl:value-of select="./LinePackListPriceInfo/@RetailPrice"/>
  			</xsl:attribute>
  			<xsl:attribute name="Tax">
         		<xsl:value-of select="./LinePackListPriceInfo/@Tax"/>
  			</xsl:attribute>
  			<xsl:attribute name="UnitPrice">
         		<xsl:value-of select="./LinePackListPriceInfo/@UnitPrice"/>
  			</xsl:attribute>
  			<Item>
  				<xsl:attribute name="CountryOfOrigin">
         			<xsl:value-of select="./Item/@CountryOfOrigin"/>
  				</xsl:attribute>
  				<xsl:attribute name="HarmonizedCode">
         			<xsl:value-of select="./Item/@HarmonizedCode"/>
  				</xsl:attribute>
  				<xsl:attribute name="ItemDesc">
         			<xsl:value-of select="./Item/@ItemDesc"/>
  				</xsl:attribute>
  				<xsl:attribute name="ItemID">
         			<xsl:value-of select="./Item/@ItemID"/>
  				</xsl:attribute>
  				<xsl:attribute name="ItemShortDesc">
         			<xsl:value-of select="./Item/@ItemShortDesc"/>
  				</xsl:attribute>
  				<xsl:attribute name="ItemWeight">
         			<xsl:value-of select="./Item/@ItemWeight"/>
  				</xsl:attribute>
  				<xsl:attribute name="ItemWeightUOM">
         			<xsl:value-of select="./Item/@ItemWeightUOM"/>
  				</xsl:attribute>
  				<xsl:attribute name="ProductClass">
         			<xsl:value-of select="./Item/@ProductClass"/>
  				</xsl:attribute>
  				<xsl:attribute name="UnitCost">
         			<xsl:value-of select="./Item/@UnitCost"/>
  				</xsl:attribute>
  				<xsl:attribute name="UnitOfMeasure">
         			<xsl:value-of select="./Item/@UnitOfMeasure"/>
  				</xsl:attribute>
  			</Item>
  			<!--  
  			<OrderStatuses>
  			<OrderStatus>
  				<xsl:attribute name="Status">
         			<xsl:value-of select="//OrderRelease/OrderLine/OrderStatuses/OrderStatus/@Status"/>
  				</xsl:attribute>
  				<xsl:attribute name="StatusQty">
         			<xsl:value-of select="//OrderRelease/OrderLine/OrderStatuses/OrderStatus/@StatusQty"/>
  				</xsl:attribute>
  			</OrderStatus>  			
  			</OrderStatuses>
  			 -->
	</OrderLine>
	</xsl:for-each>
		<PersonInfoBillTo>
			<xsl:attribute name="AddressLine1">
         		<xsl:value-of select="//OrderRelease/Order/PersonInfoBillTo/@AddressLine1"/>
  			</xsl:attribute>
  			<xsl:attribute name="AddressLine2">
         		<xsl:value-of select="//OrderRelease/Order/PersonInfoBillTo/@AddressLine2"/>
  			</xsl:attribute>
  			<xsl:attribute name="AddressLine3">
         		<xsl:value-of select="//OrderRelease/Order/PersonInfoBillTo/@AddressLine3"/>
  			</xsl:attribute>
  			<xsl:attribute name="AddressLine4">
         		<xsl:value-of select="//OrderRelease/Order/PersonInfoBillTo/@AddressLine4"/>
  			</xsl:attribute>
  			<xsl:attribute name="AddressLine5">
         		<xsl:value-of select="//OrderRelease/Order/PersonInfoBillTo/@AddressLine5"/>
  			</xsl:attribute>
  			<xsl:attribute name="AddressLine6">
         		<xsl:value-of select="//OrderRelease/Order/PersonInfoBillTo/@AddressLine6"/>
  			</xsl:attribute>
  			<xsl:attribute name="City">
         		<xsl:value-of select="//OrderRelease/Order/PersonInfoBillTo/@City"/>
  			</xsl:attribute>
  			<xsl:attribute name="Country">
         		<xsl:value-of select="//OrderRelease/Order/PersonInfoBillTo/@Country"/>
  			</xsl:attribute>
  			<xsl:attribute name="DayPhone">
         		<xsl:value-of select="//OrderRelease/Order/PersonInfoBillTo/@DayPhone"/>
  			</xsl:attribute>
  			<xsl:attribute name="EMailID">
         		<xsl:value-of select="//OrderRelease/Order/PersonInfoBillTo/@EMailID"/>
  			</xsl:attribute>
  			<xsl:attribute name="JobTitle">
         		<xsl:value-of select="//OrderRelease/Order/PersonInfoBillTo/@JobTitle"/>
  			</xsl:attribute>
  			<xsl:attribute name="FirstName">
         		<xsl:value-of select="//OrderRelease/Order/PersonInfoBillTo/@FirstName"/>
  			</xsl:attribute>
  			<xsl:attribute name="LastName">
         		<xsl:value-of select="//OrderRelease/Order/PersonInfoBillTo/@LastName"/>
  			</xsl:attribute>
  			<xsl:attribute name="MiddleName">
         		<xsl:value-of select="//OrderRelease/Order/PersonInfoBillTo/@MiddleName"/>
  			</xsl:attribute>
  			<xsl:attribute name="OtherPhone">
         		<xsl:value-of select="//OrderRelease/Order/PersonInfoBillTo/@OtherPhone"/>
  			</xsl:attribute>
  			<xsl:attribute name="State">
         		<xsl:value-of select="//OrderRelease/Order/PersonInfoBillTo/@State"/>
  			</xsl:attribute>
  			<xsl:attribute name="Suffix">
         		<xsl:value-of select="//OrderRelease/Order/PersonInfoBillTo/@Suffix"/>
  			</xsl:attribute>
  			<xsl:attribute name="ZipCode">
         		<xsl:value-of select="//OrderRelease/Order/PersonInfoBillTo/@ZipCode"/>
  			</xsl:attribute>
		</PersonInfoBillTo>
		<CustomAttributes>
  			<xsl:attribute name="ReleaseControlNbr">
         		<xsl:value-of select="//OrderRelease/CustomAttributes/@ReleaseControlNbr"/>
  			</xsl:attribute>
		</CustomAttributes>
	</OrderRelease>
	
	</xsl:template>
</xsl:stylesheet>


