function myUpdate(update1, url1, parameters1, max) {
	var selectValue = document.getElementById("ajaxSelect").value;
	var offset = max * (selectValue - 1);
	var newParams = "offset=" + offset + parameters1;
	new Ajax.Updater(update1, url1, {
		asynchronous : true,
		evalScripts : true,
		parameters : newParams
	});
	return false;
}
function myUpdate2(update1, url1, parameters1,selectMax) {
	var max = selectMax.value;
	var newParams = "max=" + max + parameters1;
	new Ajax.Updater(update1, url1, {
		asynchronous : true,
		evalScripts : true,
		parameters : newParams
	});
	return false;
}
