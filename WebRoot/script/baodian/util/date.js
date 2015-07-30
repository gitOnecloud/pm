/**
 * 将时间从字符串格式:"2012-12-9 12:40:00.20" 转换为Date类型
 */
function splitDate(date) {
	var d = date.split(" ", 2);
	var dd = d[0].split("-", 3).concat(d[1].split(":", 3));
	//月份范围是0~11,所以实际月份要减一
	return new Date(dd[0],dd[1]-1,dd[2],dd[3],dd[4],dd[5]);
}
/**
 * 计算输入时间是当前时间的哪天
 * @returns {"time":0-8(今天-更早),"diff":"5秒钟前"}
 */
function diffDate(newDate) {
	var diff = newDate - Today[1];//与凌晨相差时间
	var dd = 0;
	if(diff >= 0) {//今天
		diff = Today[0] - newDate;
		dd = parseInt(diff/1000);
		if(dd < 60) {
			if(dd >= 0) return {"time": 0, "diff": dd + "秒钟前"};
			else return {"time": 0, "diff": null};
		}
		dd = parseInt(dd/60);
		if(dd < 60) return {"time": 0, "diff": dd + "分钟前"};
		dd = parseInt(dd/60);
		if(dd < 4) return {"time": 0, "diff": dd + "小时前"};
		else return {"time": 0, "diff": null};
	}
	diff = newDate - Today[2];
	var d = 0;
	if(diff >= 0) {//本周
		d = Today[0].getDay();
		if(d == 0) d = 7;
		dd = d - newDate.getDay();
		switch(dd) {
			case 1: return {"time": 1};break;
			case 2: return {"time": 2};break;
			default: return {"time": 7 - newDate.getDay()};
		}
	}
	diff = newDate - Today[3];
	if(diff >= 0) {//上周
		return {"time": 7};
	}
	return {"time": 8};
}
/**
 * 格式化输出时间 2012年12月12日 12:12 (星期三)
 */
function dateToString(date, time) {
	var str = date.getFullYear() + "年" + (date.getMonth()+1) + "月" +
		date.getDate() + "日 " + time;
	switch(date.getDay()) {
		case 0 : return str + " (星期日)";
		case 1 : return str + " (星期一)";
		case 2 : return str + " (星期二)";
		case 3 : return str + " (星期三)";
		case 4 : return str + " (星期四)";
		case 5 : return str + " (星期五)";
		case 6 : return str + " (星期六)";
	}
}
/**
 * 初始化今天、凌晨、星期一以及上周星期一的时间
 */
function initDate(date) {
	if(date instanceof Date) Today[0] = date;
	else Today[0] = splitDate(date);
	Today[1] = new Date(Today[0].getFullYear(), Today[0].getMonth(), Today[0].getDate());
	var d = Today[0].getDay();
	if(d == 0) d = 7;//一周从星期一开始
	Today[2] = new Date(Today[0].getFullYear(), Today[0].getMonth(), (Today[0].getDate() - d + 1));
	Today[3] = new Date(Today[0].getFullYear(), Today[0].getMonth(), (Today[0].getDate() - d -6));
}
//时间段
var initTime = ["今天","昨天","前天","星期四","星期三","星期二","星期一","上周","更早"];
//时间段 0当前时间(服务器返回较理想)-1今天凌晨-2本周一凌晨-3上周一凌晨-4刷新时间
var Today = [];

/**
 * 输出时间是当天的哪一天
 */
function exportDate(dateTime, pbtime) {
	var dd = diffDate(dateTime);
	switch(dd.time) {
	case 0:
		if(dd.diff==null) return "今天" + pbtime.substring(11,16);
		else return dd.diff;
	case 1:
		return "昨天" + pbtime.substring(11,16);
	case 2:
		return "前天" + pbtime.substring(11,16);
	default:
		return (dateTime.getMonth()+1) + "月" + dateTime.getDate() + "日";
	}
}
/**
 * 新闻列表中时间输出
 * @param pbtime
 */
function newsDate (pbtime, short){
	var dateTime = splitDate(pbtime);
	var dd = diffDate(dateTime);
	switch(dd.time) {
	case 0:
		if(dd.diff==null) return "今天" + pbtime.substring(11,16);
		else return dd.diff;
	case 1:
		return "昨天" + pbtime.substring(11,16);
	case 2:
		return "前天" + pbtime.substring(11,16);
	default:
		switch(short) {
		case 1: return pbtime.substring(0, 16);
		case 2: return pbtime.substring(0, 10);
		default: return pbtime;
			
		}
	}
}