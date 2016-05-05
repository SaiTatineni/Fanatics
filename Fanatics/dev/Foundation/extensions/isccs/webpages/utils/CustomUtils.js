scDefine([
		"scbase/loader!dojo/_base/lang",
		"scbase/loader!isccs",
	"scbase/loader!dojo/date/locale",
	"scbase/loader!sc/plat/dojo/utils/BundleUtils", "scbase/loader!sc/plat/dojo/utils/BaseUtils", "scbase/loader!sc/plat/dojo/utils/ModelUtils"
	],
	function (dLang, isccs, _dLocale, _scBundleUtils, _scBaseUtils,_scModelUtils) {
	var CustomUtils = dLang.getObject("extn.utils.CustomUtils", true, isccs);
	
	CustomUtils.getFormatedDate = function (date) {
		var fmto = "EEEE, MMMM d, yyyy h:m:s a";
    	var val = _dLocale.format(date, {selector:'date', datePattern:fmto});
		return val;
	};
	
	CustomUtils.getActualDateFromOrderDate = function(gridReference, rowIndex, columnIndex, gridRowJSON, unformattedValue, modifiedValue,gridRowRecord,colConfig){
		var matchingDate = null;
		var dateTypeId = null;
		var dateAttribute = null;
		var date = null;
		if(_scBaseUtils.equals(colConfig.field, "extn_order_date")) {
			date = _scModelUtils.getDateValueFromPath("OrderDate", gridRowJSON);
			return this.getFormatedDate(date);
		}	
		
		if(_scBaseUtils.equals("FRAUD_APPROVAL_DATE",colConfig.bindingData.sFieldBinding.path)) {
			dateTypeId = "FRAUD_APPROVAL_DATE";
			dateAttribute = "ActualDate";
		} else if(_scBaseUtils.equals("PROMISED_SHIP_DATE",colConfig.bindingData.sFieldBinding.path)) {
			dateTypeId = "PROMISED_SHIP_DATE";
			dateAttribute = "CommittedDate";
		}
		var lstOrderDate = null;
		lstOrderDate = _scModelUtils.getModelListFromPath("OrderDates.OrderDate", gridRowJSON);
		if (!_scBaseUtils.isVoid(lstOrderDate)) {
			for (var i = 0; _scBaseUtils.lessThan(i, _scBaseUtils.getAttributeCount(lstOrderDate)); i = _scBaseUtils.incrementNumber(i, 1)) {
				var orderDate = null;
				orderDate = _scModelUtils.getModelFromList(lstOrderDate, i);
				if (!_scBaseUtils.isVoid(orderDate)) {
					var srcDateTypeId = null;
					srcDateTypeId = _scModelUtils.getStringValueFromPath("DateTypeId", orderDate);
					if(_scBaseUtils.equals(dateTypeId, srcDateTypeId)) {
						matchingDate = _scModelUtils.getDateValueFromPath(dateAttribute, orderDate);
						if (!_scBaseUtils.isVoid(matchingDate)) {
							return this.getFormatedDate(matchingDate);
						}
					}
				}
			}
		}
		
	}
	
	return CustomUtils;
});
