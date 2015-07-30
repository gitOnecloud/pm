var auro_aTree = $.fn.zTree.init($("#auro_aTree"), {
	data : {
		simpleData : {
			enable : true
		}
	}, callback: {
		beforeClick: function(event, treeNode) {
			if(auro_chk()) {
				$.messager.confirm('提示','要保存修改吗?',function(r){
					if(r) {
						auro_change(true);
					} else {
						auro_onSelect(treeNode);
					}
				});
			} else {
				auro_onSelect(treeNode);
			}
			return false;
		}
	}
}, auro_data.auths);
function auro_onSelect(treeNode) {
	auro_aTree.selectNode(treeNode);
	$("#auro_crole").html(treeNode.name);
	var rids = auro_data.a_rid[treeNode.id];
	auro_rTree.checkAllNodes(false);
	if(rids != null) {
		$.each(rids, function(k, rid) {
			auro_rTree.checkNode(auro_rTree.getNodeByParam("id", rid), true);
		});
	}
}
var auro_rTree = $.fn.zTree.init($("#auro_rTree"), {
	check: {
		enable: true,
		chkboxType: {"Y": "", "N": ""}
	}, data : {
		simpleData : {
			enable : true
		}
	}
}, auro_data.roles);
auro_data.a_rid = {};
$.each(auro_data.ro_aus, function(key, auro) {
	if(auro_data.a_rid[auro.aid] == null) {
		auro_data.a_rid[auro.aid] = [auro.rid];
	} else {
		auro_data.a_rid[auro.aid].push(auro.rid);
	}
});
/**
 * 全选角色
 */
function auro_chkall() {
	var nodes = auro_rTree.getCheckedNodes(true);
	if(nodes.length == auro_data.roles.length) {
		auro_rTree.checkAllNodes(false);
	} else {
		auro_rTree.checkAllNodes(true);
	}
}
/**
 * 更新角色
 */
function auro_change(flag) {
	if(! flag) {
		var auth = auro_aTree.getSelectedNodes();
		if(auth.length == 0) {
			index_mess("未选择权限！", 4);
			return;
		}
		if(!auro_chk(auth)) {
			index_mess("未更新！", 4);
			return;
		}
	}
	index_mess("更新中...", 0);
	$.get("authority_changeRole_js.action?json=" + auro_data.aid + "A" + auro_data.rids.join("a") +
			"&_=" + Math.random(), function(data) {
		if(data.status == 0) {
			auro_data.a_rid[auro_data.aid] = auro_data.rids;
			index_mess("更新成功！", 2);
		} else {
			index_mess(data.mess, 1);
			if(data.login == false)
				index_login();
		}
	}, "json");
}
/**
 * 检查角色是否更改
 * @returns {Boolean} true 有更新 false 未更新
 */
function auro_chk(selectAuth) {
	var auth;
	if(selectAuth == null) {
		auth = auro_aTree.getSelectedNodes();
	} else {
		auth = selectAuth;
	}
	if(auth.length == 0) {
		return false;
	}
	var rids = auro_data.a_rid[auth[0].id];
	if(rids == null) {
		rids = new Array();
	}
	auro_data.aid = auth[0].id;
	auro_data.rids = new Array();
	var nodes = auro_rTree.getCheckedNodes(true);
	$.each(nodes, function(k, node) {
		auro_data.rids.push(node.id);
	});
	if(rids.length != nodes.length) {
		return true;
	}
	var ischange = false;
	$.each(rids, function(k, rid) {
		ischange = true;
		$.each(nodes, function(k, node) {
			if(rid == node.id) {
				ischange = false;
				return false;
			}
		});
		if(ischange) {
			return false;
		}
	});
	return ischange;
}
