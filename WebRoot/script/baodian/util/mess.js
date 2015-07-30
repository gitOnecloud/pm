$(function() {
	$("#index_prompt").ajaxError(function(event,xhr,opt,exc) {
		index_mess("系统错误=>" + opt.url + ": " + xhr.status + " " + xhr.statusText + " / " + exc, 5);
		//index_mess("系统错误，请刷新或者联系管理员！", 5);
	
	});
});
var index_time;
/**
 * 信息提示
 * @param text 提示内容
 * @param type 提示方式 0-只打开 1-出错关闭 2-正常关闭 3-出错打开并关闭 4-打开并关闭 5-出错只打开
 * @param seconds 关闭秒数
 * 例：
 *	index_mess("更改中...", 0);
 *	index_mess("更改成功", 1);
 *	index_mess(data.mess, 3, 6000);
 */
function index_mess(text, type, seconds) {
	switch(type) {
		case 1:;
		case 3:;
		case 5:;
			text = '<span style="color:#E53333;">' + text + '</span>';
			if(seconds == null) seconds = 3200;
			break;
		default:
			text = '<span style="color:#15428B;">' + text + '</span>';
			if(seconds == null) seconds = 2400;
	}
	$("#index_prompt").html(text);
	clearTimeout(index_time);
	switch(type) {
		case 0:
		case 5:
			$("#index_prompt").show();
			break;
		case 1: ;
		case 2:
			index_time = setTimeout(function() {$("#index_prompt").hide();}, seconds);
			break;
		case 3: ;
		case 4:
			$("#index_prompt").show();
			index_time = setTimeout(function() {$("#index_prompt").hide();}, seconds);
			break;
		default: $("#index_prompt").hide();
	}
}
/**
 * 遍历输出
 * @param data
 */
function index_each(data) {
	var str = "";
	$.each(data, function(k, v) {
		str = str + k + " " + v + "\n";
	});
	alert(str);
}