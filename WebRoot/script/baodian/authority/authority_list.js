var auli_setting = {
	edit : {
		enable : true,
		showRenameBtn : false,
		removeTitle: '删除权限'
	},
	check : {
		enable : true,
		nocheckInherit : true
	},
	data : {
		simpleData : {
			enable : true
		}
	}, view: {
		addHoverDom: auli_addHoverDom,
		removeHoverDom: auli_removeHoverDom
	
	}, callback: {
		beforeRemove: auli_beforeRemove,
		onClick: auli_onClick
	}
};

var auli_treeObj = $.fn.zTree.init($("#auli_treeDemo"), auli_setting, auli_zNodes);

function auli_beforeRemove(treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj(treeId);
	zTree.selectNode(treeNode);
	if(treeNode.isParent) {
		index_mess("存在子权限！", 3);
		return false;
	}
	var isSucc = false;
	if(confirm("确认删除《" + treeNode.name + "》吗？")) {
		index_mess("删除中...", 0);
		$.ajax({
			async: false,
			url: "authority_remove_js.action?_=" + Math.random(),
			data: {"authority.id": treeNode.id},
			dataType: "json",
			success: function(data) {
				if(data.status == "0") {
					$("#auli_aid").val("");
					$("#auli_atid").val("");
					$("#auli_aname").val("");
					$("#auli_aurl").val("");
					index_mess("删除成功！", 2);
					isSucc = true;
				} else {
					index_mess(data.mess, 1);
					if(data.login == false)
						index_login();
				}
			}
		});
	}
	return isSucc;
}
function auli_onClick(event, treeId, treeNode) {
    var node = auli_findNodeById(treeNode.id);
    $("#auli_aid").val(node.id);
    $("#auli_atid").val(treeNode.tId);
    $("#auli_aname").val(node.name);
    $("#auli_aurl").val(node.url);
}
function auli_changeAuth() {
	index_mess("更新中...", 0);
	var aid = $("#auli_aid").val();
	if(aid == "") {
		index_mess("未选择权限！", 1);
		return;
	}
	var aname = $("#auli_aname").val();
	if(aname == "") {
		index_mess("权限名字为空！", 1);
		return;
	}
	var aurl = $("#auli_aurl").val();
	if(aurl == "") {
		index_mess("权限地址为空！", 1);
		return;
	}
	var paras = {"authority.id": aid, "authority.name": aname, "authority.url": aurl};
	$.post("authority_change_js.action?_=" + Math.random(), paras, function(data) {
		if(data.status == 0) {
			var node = auli_treeObj.getNodeByTId($("#auli_atid").val());
			node.name = aname;
			auli_treeObj.updateNode(node);
			auli_addHoverDom(0, node);
			var old = auli_findNodeById(aid);
			old.name = aname;
			old.url = aurl;
			index_mess("更新成功！", 2);
		} else {
			index_mess(data.mess, 1);
			if(data.login == false)
				index_login();
		}
	}, "json");
}
function auli_addHoverDom(treeId, treeNode) {
	var sObj = $("#" + treeNode.tId + "_span");
	if (treeNode.editNameFlag || $("#auli_addBtn_" + treeNode.id).length > 0)
		return;
	var addStr = "<span class='button add' id='auli_addBtn_"
		+ treeNode.id + "' title='增加' onfocus='this.blur();'></span>";
	sObj.append(addStr);
	var btn = $("#auli_addBtn_" + treeNode.id);
	if (btn) {
		btn.bind("click", function() {
			$('#auli_dd').dialog('open').dialog('setTitle', "添加权限到《"+treeNode.name+"》下");
			$('#auli_form').form('clear');
			auli_addNode = treeNode;
		});
	}
};
function auli_removeHoverDom(treeId, treeNode) {
	$("#auli_addBtn_" + treeNode.id).unbind().remove();
};
var auli_addNode;
/**
 * 打开添加到顶部框
 */
function auli_addAuth() {
	$('#auli_dd').dialog('open').dialog('setTitle','添加权限到顶部');
	$('#auli_form').form('clear');
	auli_addNode = null;
}
/**
 * 更新权限菜单顺序
 */
function auli_changeNodes() {
	index_mess("更新中...", 0);
	var nodes = auli_treeObj.transformToArray(auli_treeObj.getNodes());
	var newNodes = new Array();
	var zNodes = new Array();
	$.each(nodes, function(key, node) {
		var newNode = {"id": node.id, "pid": -1, "check": -1, "sort": -1};
		if(key<auli_zNodes.length && node.id==auli_zNodes[key].id) {
			if(auli_zNodes[key].sort != key)
				newNode.sort = key;
			if(node.pId!=auli_zNodes[key].pId) {
				if(node.pId == null)
					newNode.pid = 0;
				else
					newNode.pid = node.pId;
			}
			if(auli_zNodes[key].checked) {
				if(!node.checked && !node.getCheckStatus().half) {
					newNode.check = 0;
				}
			} else {
				if(node.checked || node.getCheckStatus().half) {
					newNode.check = 1;
				}
			}
		} else {
			var oldNode = auli_findNodeById(node.id);
			if(oldNode == null) {
				newNode.sort = key;
				newNode.pid = node.pId;
				newNode.check = node.checked ? 1 : 0;
			} else {
				if(oldNode.sort != key)
					newNode.sort = key;
				if(node.pId != oldNode.pId) {
					if(node.pId == null)
						newNode.pid = 0;
					else
						newNode.pid = node.pId;
				}
				if(oldNode.checked) {
					if(!node.checked && !node.getCheckStatus().half) {
						newNode.check = 0;
					}
				} else {
					if(node.checked || node.getCheckStatus().half) {
						newNode.check = 1;
					}
				}
			}
		}
		newNodes.push(newNode);
		zNodes.push({
			id: node.id,
			name: node.name,
			url: node.url,
			sort: key,
			checked: (node.checked||node.getCheckStatus().half),
			pId: node.pId
		});
	});
	var str = "";
	$.each(newNodes, function(key, val) {
		if(val.pid!=-1 || val.check!=-1 || val.sort!=-1) {
			str = str + "auths=" + val.id + "_" + val.pid + "_" + val.check + "_" + val.sort + "&";
		}
	});
	if(str != "") {
		$.post("authority_changeMenu_js.action?_=" + Math.random(), str, function(data) {
			if(data.status == 0) {
				index_mess("更新成功！", 2);
				auli_zNodes = zNodes;
			} else {
				index_mess(data.mess, 1);
				if(data.login == false)
					index_login();
			}
		}, "json");
	} else {
		index_mess("未更新！", 2);
	}
}
function auli_findNodeById(id) {
	for(var i in auli_zNodes) {
		if(auli_zNodes[i].id == id) {
			return auli_zNodes[i];
		}
	}
	return null;
}
var auli_check = false;
function auli_checkAll() {
	if(auli_check) {
		auli_treeObj.checkAllNodes(false);
		auli_check = false;
	} else {
		auli_treeObj.checkAllNodes(true);
		auli_check = true;
	}
}

$('#auli_dd').dialog({
	closed: true,
	buttons:[{
		text:'添加',
		iconCls:'icon-ok',
		handler:function(){
			if(!$("#auli_form").form('validate')) {
				return;
			}
			index_mess("正在添加...", 0);
			var treeName = $("#auli_name").val();
			var url = $("#auli_url").val();
			var params; 
			if(auli_addNode) {
				params = {
					"authority.name": treeName,
					"authority.url": url,
					"authority.sort": auli_zNodes.length,
					"authority.parent.id": auli_addNode.id
				};
			} else {
				params = {
					"authority.name": treeName,
					"authority.url": url,
					"authority.sort": auli_zNodes.length
				};
			}
			$.post("authority_add_js.action?_=" + Math.random(), params, function(data) {
				if(data.status == 0) {
					var newNode = {
						id: data.id,
						name: treeName,
						url: url,
						sort: auli_zNodes.length,
						checked: true
					};
					if(auli_addNode) {
						//刚添加必须将isParent设置为false，否则会出现添加两个叶子节点的现象
						newNode.pId = auli_addNode.id;
						newNode.isParent = false;
						auli_treeObj.addNodes(auli_addNode, newNode);
						auli_zNodes.push(newNode);
					} else {
						auli_treeObj.addNodes(null, newNode);
						auli_zNodes.push(newNode);
					}
					index_mess("添加成功！", 2);
					$('#auli_dd').dialog('close');
				} else {
					index_mess(data.mess, 1);
					if(data.login == false)
						index_login();
				}
			}, "json");
		}
	},{
		text:'&nbsp;取消&nbsp;',
		handler:function(){
			$('#auli_dd').dialog('close');
		}
	}]
});