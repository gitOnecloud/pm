$('#usli_table').datagrid({
    url: 'user_list_js.action?_=' + Math.random(),
    iconCls: "icon-edit",
    title: "用户列表",
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    //singleSelect: true,//单选
    pagination: true,//分页
    toolbar: '#usli_toolbar',
    queryParams: {
    	"page.name": {"id":"usli_name","type":"text"},
    	"page.account": {"id":"usli_account","type":"text"},
    }, paramsName: {
    	page: "page.page",
    	num: "page.num"
    }, columns: [[
        {field: 'ck', checkbox:true},
        {field: 'id', title: 'ID', width: 8, align: 'center'},
        {field: 'user.name', title: '名字', width: 20},
        {field: 'user.account', title: '账号', width: 20},
        {field: 'role', title: '角色', width: 25},
        {field: 'dpmName', title:'部门', width: 25},
        {field: 'status', title:'本应用状态', width: 10, align: 'center',
    	   formatter: function(value, row, index) {
    		   if(value == 0) {
    			   return '正常';
    		   } else {
    			   return '锁定';
    		   }
    	   }},
    	{field: 'casStatus', title:'全部应用状态', width: 10, align: 'center',
        	   formatter: function(value, row, index) {
        		   if(value == null) {
        			   return '不存在';
        		   } else if(value == 0) {
        			   return '正常';
        		   } else {
        			   return '锁定';
        		   }
        	   }},
    	{field: 'date', title:'注册时间', width: 20, align: 'center',
        	   formatter: function(value, row, index) {
        		   return value.substring(0, 10);
        	   }},
        {field: 'roleId', hidden: true},
        {field: 'depmId', hidden: true}
    ]], onDblClickRow: function(index, data) {
    	usli_editUser(data);
    }
});
function usli_search() {
	$('#usli_table').datagrid("load");
}
var usli_data = {};
/**
 * 弹出添加用户窗
 */
function usli_newUser() {
	$("#usli_ac_span").html("");
	$("#usli_pw_span").html("为空，使用默认123456。");
	$('#usli_dlg').dialog('open').dialog('setTitle','添加用户');
	$('#usli_fm').form('clear');
	usli_data.url = 'user_save_js.action';
	usli_data.isSave = true;
	//0:未更改 1:添加 2:修改
	usli_row.edit = 1;
}
var usli_row = {};//更改前row数据
/**
 * 弹出修改用户窗
 */
function usli_editUser(data) {
	if(data == null) usli_row = $('#usli_table').datagrid('getSelected');
	else usli_row = data;
	if (usli_row){
		$("#usli_ac_span").html("更改后，密码为空置123456。");
		$("#usli_pw_span").html("为空，不更改。");
		$('#usli_dlg').dialog('open').dialog('setTitle','修改用户');
		usli_row["user.password"] = "";
		$('#usli_fm').form('load',usli_row);
		usli_data.url = 'user_change_js.action?user.id='+usli_row.id;
		usli_data.isSave = false;
		usli_row.edit = 2;
	} else {
		index_mess("请先选择", 4);
	}
}
function usli_removeUser() {
	if(usli_row && usli_row.edit==2) {
		index_mess("正在修改，不能删除！", 4);
		return;
	}
	var row =  $('#usli_table').datagrid('getSelected');
	if (row){
		$.messager.prompt('删除用户-' + row["user.name"] + '-确定', '请输入你的密码:', function(r){
			if (r){
				index_mess("删除中...", 0);
				$.post("user_remove_js.action", {"user.id":row.id,"user.password":r}, function(data) {
					if(data.status == 0) {
						index_mess("删除成功！", 2);
						$('#usli_table').datagrid('reload');
					} else {
						index_mess(data.mess, 1);
						if(data.login == false) {
							index_login();
						}
						if(data.password == false) {
							return false;
						}
					}
				}, "json");
			}
		},"password");
	} else {
		index_mess("请先选择用户！", 4);
	}
}
/**
 * 添加或更改用户到数据库
 */
function usli_saveUser() {
	if(!$("#usli_fm").form('validate')) {
		return;
	}
	//0:user.name 1:user.account 2:user.password 3-Last:roleId Last:depmId
	//var params = $("#usli_fm").serializeArray();
	var params = {};
	var name = $('#usli_addName').val();
	var account = $('#usli_addAccount').val().toLowerCase();
	var password = $('#usli_addPass').val();
	var depm = $('#usli_depm').combotree('getValue');
	var rids = $('#usli_roles').combobox('getValues');
	if(rids.length != 0) {
		params['json'] = rids.join('-');
	}
	if(! usli_data.isSave) {
		if(name != usli_row["user.name"]) {
			params['user.name'] = name;
		}
		if(account != usli_row["user.account"] || password != '') {
			params['user.account'] = account;
		}
		if(depm != usli_row.depmId) {
			params['user.dpm.id'] = depm;
		}
	} else {
		params['user.name'] = name;
		params['user.account'] = account;
		params['user.dpm.id'] = depm;
	}
	var isError = false;
	//需要更新密码
	if(password != '' ) {
		$.ajax({
			url: 'util_rsaKey.action',
			cache: false,
			async: false,
			type: 'get',
			dataType: 'json',
			success: function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 1);
					if(data.login == false) {
						index_login();
					}
					isError = true;
				}
				//rsa加密
				var rsa = new RSAKey();
				rsa.setPublic(data.rsa_modKey, data.rsa_pubKey);
				password = $.encoding.digests.hexSha1Str(account + password);
				params['user.account'] = account;
				params['user.password'] = rsa.encrypt(password);
			}
		});
	}
	if(isError) {
		return;
	}
	index_mess("操作中...", 0);
	$.post(usli_data.url, params, function(data) {
		if(data.status == 0) {
			$('#usli_dlg').dialog('close');
			if(usli_data.isSave) {
				index_mess("添加成功！", 2);
			} else {
				index_mess("修改成功！", 2);
			}
			$('#usli_table').datagrid('reload');
		} else {
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
			if(data.account == false) {
				$("#usli_ac_span").html(data.mess);
			}
		}
	}, "json");
}
setTimeout(function() {
	//角色下拉多选框
	$('#usli_roles').combobox({
		valueField: 'id',
		textField: 'name',
		data: usli_RDs.roles,
		editable: false,
		multiple: true,
		formatter: function(row){
			return '<img class="usli_item" src="script/easyui/themes/icons/role.png"/><span class="usli_item">'+row.name+'</span>';
		}
	});
	//更改角色树
	$('#usli_role').tree({
		checkbox: true,
		cascadeCheck: false,
		data: index_changeTree(usli_RDs.roles),
		onClick: function(node) {
			$('#usli_role').tree(node.checked? 'uncheck': 'check', node.target);
		}, onDblClick: function(node) {
			$('#usli_role').tree('toggle', node.target);
		}
	});
	var dpms = index_changeTree(usli_RDs.dpms);
	//部门下拉树
	$('#usli_depm').combotree({
	    required: true,
	    data: dpms
	});
	//移动部门树
	$('#usli_dpm').tree({
		data: dpms,
		onDblClick: function(node) {
			$('#usli_dpm').tree('toggle', node.target);
		}
	});
}, 500);
/**
 * 打开移动部门对话框
 */
function usli_openChdpm() {
	$('#usli_chdpm').dialog('open');
}
/**
 * 为用户移动部门
 */
function usli_chdpm() {
	var users = $('#usli_table').datagrid("getChecked");
	if(users.length == 0) {
		index_mess("请先选择用户！", 4);
		return;
	}
	var dpm = $('#usli_dpm').tree('getSelected');
	if(dpm == null) {
		index_mess("请先选择部门！", 4);
		return;
	}
	var uids = "";
	$.each(users, function(k, user) {
		uids = uids + user.id + "a";
	});
	index_mess("移动中...", 0);
	$.getJSON("user_changeDpm_js.action?json=" + dpm.id + "A" + uids + "&_=" + Math.random(), function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
		} else {
			index_mess("移动成功！", 2);
			$('#usli_chdpm').dialog('close');
			$('#usli_table').datagrid('reload');
		}
	});
}
/**
 * 打开更改角色对话框
 */
function usli_openChrole() {
	$('#usli_chrole').dialog('open');
}
/**
 * 为用户更改角色
 */
function usli_chrole() {
	var users = $('#usli_table').datagrid("getChecked");
	if(users.length == 0) {
		index_mess("请先选择用户！", 4);
		return;
	}
	var roles = $('#usli_role').tree('getChecked');
	if(roles.length == 0) {
		index_mess("请先选择角色！", 4);
		return;
	}
	var param = $('#usli_chtype').combobox('getValue') + "A";
	$.each(roles, function(k, role) {
		param = param + role.id + "a";
	});
	param = param + "A";
	$.each(users, function(k, user) {
		param = param + user.id + "a";
	});
	index_mess("更改中...", 0);
	$.getJSON("user_changeRole_js.action?json=" + param + "&_=" + Math.random(), function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
		} else {
			index_mess("更改成功！", 2);
			$('#usli_chrole').dialog('close');
			$('#usli_table').datagrid('reload');
		}
	});
}
/**
 * 解锁或锁定用户
 * @param type 0为本应用 1为全部应用
 */
function usli_changeStatus(status, type) {
	var users = $('#usli_table').datagrid("getChecked");
	if(users.length == 0) {
		index_mess("请先选择用户！", 4);
		return;
	}
	var param = status + 'A' + type + 'A';
	$.each(users, function(k, user) {
		param = param + user.id + "a";
	});
	index_mess("更改中...", 0);
	$.getJSON("user_changeStatus_js.action?json=" + param + "&_=" + Math.random(), function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
		} else {
			index_mess("更改成功！", 2);
			$('#usli_table').datagrid('reload');
		}
	});
}