var roleai_setting = {
	check : {
		enable : true,
		nocheckInherit : true,
		chkboxType: {"Y": "s","N": "ps"}
	},
	data : {
		simpleData : {
			enable : true
		}
	}
};
var roleai_treeObj;
$(function() {
	roleai_treeObj = $.fn.zTree.init($("#roleai_treeDemo"), roleai_setting, roai_.auths);
	var role = roai_.parent.split('_');
	if(role.length == 3) {
		roai_.pId = role[0].replace(/\D/g,'');
		roai_.sort = role[1].replace(/\D/g,'');
	} else {
		role[2] = "无";
	}
	$("#roai_parent").html(role[2]);
});

var roleai_check = false;
function roleai_checkAll() {
	if(roleai_check) {
		roleai_treeObj.checkAllNodes(false);
		roleai_check = false;
	} else {
		roleai_treeObj.checkAllNodes(true);
		roleai_check = true;
	}
}
function roleai_addRole() {
	index_mess("添加中...", 0);
	var name = $("#roleai_rname").val();
	if(name.length < 1) {
		index_mess("名字不能为空！", 1);
		return;
	}
	if(name.length > 20) {
		index_mess("名字长度超出20个！", 1);
		return;
	}
	var str = "";
	var nodes = roleai_treeObj.getCheckedNodes(true);
	$.each(nodes, function(key, node) {
		str = str + "ids="  + node.id + "&";
	});
	str = str + "role.name=" + name;
	if(roai_.sort!=null && roai_.sort!=0) {
		str = str + "&role.sort=" + roai_.sort;
	}
	if(roai_.pId!=null && roai_.pId!=0) {
		str = str + "&role.parent.id=" + roai_.pId;
	}
	$.post("role_add_js.action?_=" + Math.random(), str, function(data) {
		if(data.status == 0) {
			index_mess("添加成功！", 2);
			index_updateTab("添加角色", "role_show.action?role.id=" + data.id + "&", true, "查看角色");
		} else {
			index_mess(data.mess, 1);
			if(data.login == false)
				index_login();
		}
	}, "json");
}