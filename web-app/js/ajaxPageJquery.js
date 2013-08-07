function jqueryUpdate1(update, url, parameters,selectMax) {
	var max = selectMax.value;
	parameters.max=max;
	jQuery.ajax({type:'POST',data:parameters,
		url:url,success:function(data,textStatus){
		jQuery(update).html(data);},error:function(XMLHttpRequest,textStatus,errorThrown){}}
	)
	return false;
}
function jqueryUpdate(update, url, parameters,offset) {
	parameters.offset=(offset.value-1)*parameters.max;
	jQuery.ajax({type:'POST',data:parameters,
		url:url,success:function(data,textStatus){
		jQuery(update).html(data);},error:function(XMLHttpRequest,textStatus,errorThrown){}}
	)
	return false;
}