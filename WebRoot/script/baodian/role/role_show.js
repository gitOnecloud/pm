var rosh_setting = {
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
function rosh_changeRA() {
	index_mess("更新中...", 0);
	var nodes = rosh_treeObj.transformToArray(rosh_treeObj.getNodes());
	var newNodes = new Array();
	$.each(nodes, function(key, node) {
		if(node.checked) {//半选去掉node.getCheckStatus().half
			newNodes.push(node.id);
		}
	});
	if(newNodes.length == rosh_lastNodes.length) {
		if(newNodes.length == 0) {
			index_mess("未更新！", 2);
			return;
		}
		var flag = true;
		for(var i=0;i<newNodes.length;i++) {
			if(newNodes[i] != rosh_lastNodes[i]) {
				flag = false;
				break;
			}
		}
		if(flag) {
			index_mess("未更新！", 2);
			return;
		}
	}
	var str = "";
	$.each(newNodes, function(key, val) {
		str = str + "ids="  + val + "&";
	});
	str = str + "role.id=" + rosh_rid;
	$.post("role_changeRA_js.action?_=" + Math.random(), str, function(data) {
		if(data.status == "0") {
			rosh_lastNodes = newNodes;
			index_mess("更新成功！", 2);
		} else {
			index_mess(data.mess, 1);
			if(data.login == false)
				index_login();
		}
	}, "json");
}
function rosh_findNodeById(id) {
	for(var i in rosh_zNodes) {
		if(rosh_zNodes[i].id == id) {
			return rosh_zNodes[i];
		}
	}
	return null;
}
var rosh_treeObj;
var rosh_lastNodes = new Array();
$(function() {
	$("#rosh_rname").val(rosh_rname);
	rosh_treeObj = $.fn.zTree.init($("#rosh_treeDemo"), rosh_setting, rosh_zNodes);
	$.each(rosh_zNodes, function(k, node) {
		if(node.checked)
			rosh_lastNodes.push(node.id);
	});
});
var rosh_check = false;
function rosh_checkAll() {
	if(rosh_check) {
		rosh_treeObj.checkAllNodes(false);
		rosh_check = false;
	} else {
		rosh_treeObj.checkAllNodes(true);
		rosh_check = true;
	}
}
function rosh_changeRole() {
	index_mess("更新中...", 0);
	var name = $("#rosh_rname").val();
	if(name.length < 1) {
		index_mess("名字不能为空！", 1);
		return;
	}
	if(name.length > 20) {
		index_mess("名字长度超出20个！", 1);
		return;
	}
	var str = {"role.id": rosh_rid, "role.name": name};
	$.post("role_changeN_js.action?_=" + Math.random(), str, function(data) {
		if(data.status == 0) {
			index_mess("更新成功！", 2);
		} else {
			index_mess(data.mess, 1);
			if(data.login == false)
				index_login();
		}
	}, "json");
}
function rosh_removeRole() {
	$.messager.confirm("删除角色", "你确定要删除这个角色吗？", function(r){
		if(r) {
			index_mess("删除中...", 0);
			var str = {"role.id": rosh_rid};
			$.getJSON("role_remove_js.action?_=" + Math.random(), str, function(data) {
				if(data.status == "0") {
					index_mess("删除成功！", 2);
					index_closeTab("查看角色");
					index_updateTab("角色列表", "role_list.action", true);
				} else {
					index_mess(data.mess, 1);
					if(data.login == false)
						index_login();
				}
			});
		}
	});
}