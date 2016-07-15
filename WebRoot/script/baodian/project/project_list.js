var pjl_data = [];
//分页数据
$('#pjl_table').datagrid({
    url: 'project_list_js.action?_=' + Math.random(),
    iconCls: "icon-edit",
    title: "项目列表",
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    //singleSelect: true,//单选
    pagination: true,//分页
    toolbar: '#pjl_toolbar',
    queryParams: {
    	"page.name": {"id":"pjl_name","type":"text"},
    }, paramsName: {
    	page: "page.page",
    	num: "page.num"
    }, columns: [[
        {field: 'ck', checkbox:true},
        {field: 'id', title: 'ID', align: 'center'},
        {field: 'name', title: '简称', width: 40},
        {field: 'fname', title: '全称', width: 40},
        {field: 'beginTime', title: '项目开始', width: 20, align: 'center'},
        {field: 'endTime', title: '项目结束', width: 20, align: 'center'},
    ]], onDblClickRow: function(index, data) {
    	pjl_editProject(data);
    }
});
function pjl_search() {
	$('#pjl_table').datagrid("load");
}
/**
 * 删除项目
 */
function pjl_removeProject() {
	var row =  $('#pjl_table').datagrid('getChecked');
	if (row.length > 0) {
		$.messager.confirm('提示', '确定要删除选中的 ' + row.length + ' 个项目吗?', function(r){
			if (r){
				var projects = '';
				$.each(row, function(k, project) {
					projects = projects + project.id + '-';
				});
				index_mess("删除中...", 0);
				$.post("project_remove_js.action?json=" + projects, function(data) {
					if(data.status == 0) {
						index_mess("删除成功！", 2);
						$('#pjl_table').datagrid('reload');
					} else {
						index_mess(data.mess, 1);
						if(data.login == false) index_login();
					}
				}, "json");
			}
		});
	} else {
		index_mess("请先选择项目！", 4);
	}
}
/**
 * 查看项目窗口
 */
$('#pjl_dlg').dialog({
	title: '查看项目',
    width: 400,
    height: 280,
    closed: true,
    buttons: [{
		text:'保存',
		iconCls: 'icon-ok',
		handler:function() {
			pjl_saveProject();
	}}, {
		text:'取消',
		handler:function(){
			$('#pjl_dlg').dialog('close');
	}}]
});
/**
 * 清除查询条件
 */
function pjl_clear() {
	$('#pjl_name').val('');
}

/**
 * 添加项目
 */
function pjl_addProject() {
	$('#pjl_dlg').dialog('open').dialog('setTitle','添加项目');
	$('#pjl_fm').form('clear');
	pjl_data.url = 'project_save_js.action';
	pjl_data.isAdd = true;
	return;
}

/**
 * 修改项目
 */
function pjl_editProject(row) {
	if(! row) {
		var rows =  $('#pjl_table').datagrid('getChecked');
		if (rows.length > 0){
			row = rows[0];
		} else {
			index_mess("请先选择项目！", 4);
			return;
		}
	}
	var rowData = [];
	$.each(row, function(k, v) {
		rowData['project.'+k] = v;
	});
	$('#pjl_dlg').dialog('open').dialog('setTitle','修改项目');
	$('#pjl_fm').form('clear').form('load', rowData);
	pjl_data.url = 'project_change_js.action';
	pjl_data.isAdd = false;
}
/**
 * 保存项目
 */
function pjl_saveProject() {
	if(! $('#pjl_fm').form('validate')) {
		return;
	}
	if(pjl_data.isAdd) {
		index_mess("添加中...", 0);
	} else {
		index_mess("修改中...", 0);
	}
	$('#pjl_fm').form('submit', {
		url: pjl_data.url,
		success: function(data) {
			data = $.parseJSON(data);
			if(data.status == 0) {
				$('#pjl_dlg').dialog('close');
				index_mess(data.mess, 2);
				$('#pjl_table').datagrid('load');
			} else {
				index_mess(data.mess, 1);
			}
		}
	});
}
