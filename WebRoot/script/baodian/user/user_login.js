$('#uslg_table').datagrid({
	title : '在线用户',
	fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    pagination: true,//分页
	url : 'user_login_js.action',
	paramsName: {
    	page: "page.page",
    	num: "page.num"
    }, columns : [[
	    {field: 'account', title: '账号', width: 10, align: 'center'},
	    {field: 'name', title: '用户名', width: 10, align: 'center'},
	    {field: 'depm', title: '部门', width: 12, align: 'center'},
	    {field: 'date', title: '最后访问时间', width: 20, align: 'center'},
	    {field: 'ip', title: 'IP', width: 15, align: 'center' },
	    {field : 'session', hidden: true,},
	    {field: 'browser', title: '浏览器', width: 40, align: 'center' },
	    {field: 'status', title: '状态', width: 7, align: 'center',
	    	formatter: function(value, row, index) {
	    		if(value) {
	    			return '退出';
	    		}
	    		return '正常';
	    	}},
	    {field: 'action', title: '操作', width: 7, align: 'center',
	    	formatter: function(value, row, index) {
	    		return '<a href="javascript:;" onclick="uslg_logout(' + index + ')">退出</a>';
	    	}},
	] ],
	groupField : 'account',
	view : groupview,
	groupFormatter : function(value, rows) {
		return value + ' - ' + rows.length + ' 处登录';
	}, onDblClickRow: function(index, data) {
    	uslg_editLogin(data);
    }
});
/**
 * 将用户退出平台
 */
function uslg_logout(index) {
	var row = $('#uslg_table').datagrid('getRows')[index];
	index_mess("退出中...", 0);
	$.getJSON("user_logout_js.action?json=" + row.session + "&_=" + Math.random(), function(data) {
		if(data.status == 0) {
			$('#uslg_table').datagrid('reload');
    		index_mess("退出成功!", 2);
		} else {
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
		}
	});
}
/**
 * 查看用户窗口
 */
$('#uslg_win').window({
	title: '查看用户登录详细信息',
    width: 400,
    height: 280,
    collapsible: false,
    minimizable: false,
    closed: true,
});
/**
 * 查看用户
 */
function uslg_editLogin(data) {
	$('#uslg_win').window('open');
	$('#uslg_win_content').html('<span style="color:#8f5700;">账号：</span>' + data.account +
			'<br /><br /><span style="color:#8f5700;">用户：</span>' + data.name + '(' + data.depm + ')' +
			'<br /><br /><span style="color:#8f5700;">时间：</span>' + data.date + 
			'<br /><br /><span style="color:#8f5700;">IP：</span>' + data.ip + 
			'<br /><br /><span style="color:#8f5700;">SESSION：</span>' + data.session + 
			'<br /><br /><span style="color:#8f5700;">浏览器：</span>' + data.browser);
}
