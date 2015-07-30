//日志类型
$('#loglist_type').combobox({
	width: 60,
	panelHeight: 100,
	editable: false,
	data: loglist_logtype
});
$('#loglist_beginDate').datebox({
	width: 100,
});
$('#loglist_endDate').datebox({
	width: 100,
});
//分页数据
$('#loglist_table').datagrid({
    url: 'log_list_js.action?_=' + Math.random(),
    iconCls: "icon-edit",
    title: "日志列表",
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    //singleSelect: true,//单选
    pagination: true,//分页
    toolbar: '#loglist_toolbar',
    queryParams: {
    	"page.content": {"id":"loglist_content","type":"text"},
    	"page.type": {"id":"loglist_type","type":"combobox"},
    	"page.beginDate": {"id":"loglist_beginDate","type":"datebox"},
    	"page.endDate": {"id":"loglist_endDate","type":"datebox"},
    }, paramsName: {
    	page: "page.page",
    	num: "page.num"
    }, columns: [[
        {field: 'ck', checkbox:true},
        {field: 'id', title: 'ID', align: 'center'},
        {field: 'content', title: '内容', width: 80},
        {field: 'type', title: '类型', width: 10, align: 'center'},
        {field: 'date', title: '时间', width: 20},
    ]], onDblClickRow: function(index, data) {
    	loglist_editLog(data);
    }
});
function loglist_search() {
	$('#loglist_table').datagrid("load");
}
/**
 * 删除日志
 */
function loglist_removeLog() {
	var row =  $('#loglist_table').datagrid('getChecked');
	if (row.length > 0){
		$.messager.confirm('提示', '确定要删除选中的 ' + row.length + ' 个日志吗?', function(r){
			if (r){
				var logs = '';
				$.each(row, function(k, log) {
					logs = logs + log.id + '-';
				});
				index_mess("删除中...", 0);
				$.post("log_remove_js.action?json=" + logs, function(data) {
					if(data.status == 0) {
						index_mess("删除成功！", 2);
						$('#loglist_table').datagrid('reload');
					} else {
						index_mess(data.mess, 1);
						if(data.login == false) {
							index_login();
						}
					}
				}, "json");
			}
		});
	} else {
		index_mess("请先选择日志！", 4);
	}
}
/**
 * 查看日志窗口
 */
$('#loglist_win').window({
	title: '查看日志',
    width: 400,
    height: 240,
    collapsible: false,
    minimizable: false,
    closed: true,
});
/**
 * 查看日志
 */
function loglist_editLog(data) {
	$('#loglist_win').window('open');
	$('#loglist_win_content').html('<span style="color:#8f5700;">类型：</span>' + data.type +
			'<br /><br /><span style="color:#8f5700;">时间：</span>' + data.date + 
			'<br /><br /><span style="color:#8f5700;">内容：</span>' + data.content);
}
/**
 * 清除查询条件
 */
function loglist_clear() {
	$('#loglist_content').val('');
	$('#loglist_type').combobox('clear');
	$('#loglist_beginDate').datebox('clear');
	$('#loglist_endDate').datebox('clear');
}
/**
 * 导出日志excel
 */
function loglist_download(isAll) {
	var form = $('<form>');
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', 'log_download_rd.action');
    if(! isAll) {
	    form.append(loglist_createInput('page.content', $('#loglist_content').val()));
	    form.append(loglist_createInput('page.type', $('#loglist_type').combobox('getText')));
	    form.append(loglist_createInput('page.beginDate', $('#loglist_beginDate').datebox('getText')));
	    form.append(loglist_createInput('page.endDate', $('#loglist_endDate').datebox('getText')));
    }
    $('body').append(form);
    form.submit();
    form.remove();
}
/**
 * 生成input框
 */
function loglist_createInput(name, value) {
	 var input = $('<input>');  
     input.attr('type', 'hidden');  
     input.attr('name', name);  
     input.attr('value', value);
     return input;
}
