if ($.fn.pagination) {
	$.fn.pagination.defaults.beforePageText = '第';
	$.fn.pagination.defaults.afterPageText = '共{pages}页';
	$.fn.pagination.defaults.displayMsg = '显示{from}到{to},共{total}记录';
}
if ($.fn.datagrid) {
	$.fn.datagrid.defaults.loadMsg = '正在处理，请稍等。。。';
}
if ($.fn.treegrid && $.fn.datagrid) {
	$.fn.treegrid.defaults.loadMsg = $.fn.datagrid.defaults.loadMsg;
}
$.messager.defaults = {ok:'确定',cancel:'取消'};
if ($.fn.validatebox) {
	$.fn.validatebox.defaults.missingMessage = '此输入框不能为空';
	$.fn.validatebox.defaults.rules.email.message = '请输入有效的电子邮件地址';
	$.fn.validatebox.defaults.rules.url.message = '请输入有效的URL地址';
	$.fn.validatebox.defaults.rules.length.message = '长度必须在{0}和{1}之间';
	$.fn.validatebox.defaults.rules.remote.message = '请修正该字段';
}
if ($.fn.numberbox) {
	$.fn.numberbox.defaults.missingMessage = '此输入框不能为空';
}
if ($.fn.combobox) {
	$.fn.combobox.defaults.missingMessage = '此输入框不能为空';
}
if ($.fn.combotree) {
	$.fn.combotree.defaults.missingMessage = '此输入框不能为空';
}
if ($.fn.combogrid) {
	$.fn.combogrid.defaults.missingMessage = '此输入框不能为空';
}
if ($.fn.calendar) {
	$.fn.calendar.defaults.weeks = [ '日', '一', '二', '三', '四', '五', '六' ];
	$.fn.calendar.defaults.months = [ '一月', '二月', '三月', '四月', '五月', '六月', '七月',
			'八月', '九月', '十月', '十一月', '十二月' ];
}
if ($.fn.datebox) {
	$.fn.datebox.defaults.currentText = '今天';
	$.fn.datebox.defaults.closeText = '关闭';
	$.fn.datebox.defaults.okText = '确定';
	$.fn.datebox.defaults.missingMessage = '此输入框不能为空';
	$.fn.datebox.defaults.formatter = function(date) {
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();
		return y + '-' + (m < 10 ? ('0' + m) : m) + '-'
				+ (d < 10 ? ('0' + d) : d);
	};
	$.fn.datebox.defaults.parser = function(s) {
		if (!s)
			return new Date();
		var ss = s.split('-');
		var y = parseInt(ss[0], 10);
		var m = parseInt(ss[1], 10);
		var d = parseInt(ss[2], 10);
		if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
			return new Date(y, m - 1, d);
		} else {
			return new Date();
		}
	};
}
if ($.fn.datetimebox && $.fn.datebox) {
	$.extend($.fn.datetimebox.defaults, {
		currentText : $.fn.datebox.defaults.currentText,
		closeText : $.fn.datebox.defaults.closeText,
		okText : $.fn.datebox.defaults.okText,
		missingMessage : $.fn.datebox.defaults.missingMessage
	});
}
/**
 * datagrid在载入数据时可自定义参数名，以及加入参数，eg:
 *	 queryParams: {
 *		//从页面上取: $("#grli_name").val();
 *		//type可为: text combobox datebox datetimebox numberbox
 *   	"page.name": {"id":"grli_name","type":"text"}
 *   }
 * 	 paramsName: {
 *   	page: "page.page",
 *   	num: "page.num"
 *   }
 *   发送url将变为: ***.action?page.page=*&page.num=*&page.name=***,
 *   默认为page和num: ***.action?page=*&num=*
 */
$.fn.datagrid.defaults.paramsName = {
	page : "page",
	num : "num"
};
/**
 * 未登录等出错处理
 */
$.fn.datagrid.defaults.loader = function(data, succfn, errfn) {
	var opts = $(this).datagrid("options");
	if (!opts.url) {
		return false;
	}
	$.ajax({
		type : opts.method,
		url : opts.url,
		data : data,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				errfn.apply(this, arguments);
				index_mess(data.mess, 3);
				if (data.login == false)
					index_login();
				return;
			}
			succfn(data);
		},
		error : function() {
			errfn.apply(this, arguments);
		}
	});
};
//增加日期时间编辑种类
$.fn.datagrid.defaults.editors.datetimebox = {
	init : function(_4f1, _4f2) {
		var _4f3 = $("<input type=\"text\">").appendTo(_4f1);
		_4f3.datetimebox(_4f2);
		return _4f3;
	},
	destroy : function(_4f4) {
		$(_4f4).datetimebox("destroy");
	},
	getValue : function(_4f5) {
		return $(_4f5).datetimebox("getValue");
	},
	setValue : function(_4f6, _4f7) {
		$(_4f6).datetimebox("setValue", _4f7);
	},
	resize : function(_4f8, _4f9) {
		$(_4f8).datetimebox("resize", _4f9);
	}
};