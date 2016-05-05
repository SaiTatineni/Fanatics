scDefine(["scbase/loader!dojo/_base/declare","scbase/loader!extn/order/details/OrderSummaryExtnUI", "scbase/loader!sc/plat/dojo/utils/ScreenUtils","scbase/loader!sc/plat/dojo/utils/EditorUtils","scbase/loader!sc/plat/dojo/utils/ModelUtils"]
,
function(			 
			    _dojodeclare
			 ,
			    _extnOrderSummaryExtnUI, _scScreenUtils, _scEditorUtils, _scModelUtils
){ 
	return _dojodeclare("extn.order.details.OrderSummaryExtn", [_extnOrderSummaryExtnUI],{
	// custom code here
	getCustomerInfo : function (event, bEvent, ctrl, args) {
		var editor = _scEditorUtils.getCurrentEditor();
		var businessCardScreen = _scScreenUtils.getChildScreen(editor, "pnlCustomerContext");
		var custInfoModel = _scScreenUtils.getModel(businessCardScreen, "CustomerInfo");
		var custFullName = _scModelUtils.getStringValueFromPath("Customer.CustomerContactList.CustomerContact.0.FullName", custInfoModel);
		var custPhonenumber = _scModelUtils.getStringValueFromPath("Customer.CustomerContactList.CustomerContact.0.MobilePhone", custInfoModel);
		var custEmailid = _scModelUtils.getStringValueFromPath("Customer.CustomerContactList.CustomerContact.0.EmailID", custInfoModel);
		//var custAddress = _scModelUtils.getStringValueFromPath("Customer.CustomerContactList.CustomerContact.0.Address", custInfoModel);
		var custFancashbalance=_scModelUtils.getStringValueFromPath("Customer.Extn.ExtnFancashBalance",custInfoModel);
		var custAccountbalance=_scModelUtils.getStringValueFromPath("Customer.Extn.ExtnAccountBalance",custInfoModel);
		var customerInput = null;
		customerInput = _scModelUtils.createNewModelObjectWithRootKey("Customer");
		_scModelUtils.setStringValueAtModelPath("Customer.CustomerName", custFullName, customerInput);
		_scModelUtils.setStringValueAtModelPath("Customer.PhoneNumber", custPhonenumber, customerInput);
		_scModelUtils.setStringValueAtModelPath("Customer.EmailID", custEmailid, customerInput);
        _scModelUtils.setStringValueAtModelPath("Customer.ExtnFancashBalance", custFancashbalance, customerInput);
		_scModelUtils.setStringValueAtModelPath("Customer.ExtnAccountBalance", custAccountbalance, customerInput);
		_scScreenUtils.setModel(this, "extn_customerdetails_output", customerInput, null);
	}
});
});

