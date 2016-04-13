<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<Order>
			<xsl:attribute name="OrderNo">
			<xsl:value-of select="//Order/@OrderNo" />
			</xsl:attribute>
			<xsl:attribute name="EntryType"> 
            <xsl:value-of select="//Order/@EntryType" /> 
            </xsl:attribute> 
            <xsl:attribute name="SellerOrganizationCode"> 
            <xsl:value-of select="//Order/@SellerOrganizationCode" /> 
            </xsl:attribute> 
			<OrderLines>
				<xsl:for-each select="Order/OrderLines/OrderLine">
				<OrderLine>
					<xsl:attribute name="OrderedQty"><xsl:value-of
					select="//Order/OrderLines/OrderLine/@OrderedQty" /></xsl:attribute>
					<xsl:attribute name="PrimeLineNo"><xsl:value-of
					select="//Order/OrderLines/OrderLine/@PrimeLineNo" /></xsl:attribute>
					<xsl:attribute name="SubLineNo"><xsl:value-of
					select="//Order/OrderLines/OrderLine/@SubLineNo" /></xsl:attribute>
					<xsl:attribute name="Status"><xsl:value-of
					select="//Order/OrderLines/OrderLine/@Status" /></xsl:attribute>
					
					<Item>
						<xsl:attribute name="ItemID"><xsl:value-of
						select="Item/@ItemID" /></xsl:attribute>
						<xsl:attribute name="ProductClass"><xsl:value-of
						select="Item/@ProductClass" /></xsl:attribute>
						<xsl:attribute name="UnitOfMeasure"><xsl:value-of
						select="Item/@UnitOfMeasure" /></xsl:attribute>
					</Item>
					</OrderLine>
				</xsl:for-each>
			</OrderLines>
			<CustomAttributes>
				<xsl:attribute name="CustomerOrderNo"><xsl:value-of
				select="//Order/CustomAttributes/@CustomerOrderNo" /></xsl:attribute>
				<xsl:attribute name="CartID"><xsl:value-of
				select="//Order/CustomAttributes/@CartID" /></xsl:attribute>
			</CustomAttributes>
		</Order>
	</xsl:template>
</xsl:stylesheet>