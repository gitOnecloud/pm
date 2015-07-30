var depm_zSetting = {
	edit: {
		enable: true,
		renameTitle: '更改部门名称',
		removeTitle: '删除部门'
	}, data: {
		simpleData: {
			enable: true
		}
	}, view: {
		addHoverDom: depm_addHoverDom,
		removeHoverDom: depm_removeHoverDom
	}, callback: {
		beforeRemove: depm_beforeRemove,
		beforeRename: depm_beforeRename
	}
};
var depm_treeObj = $.fn.zTree.init($("#depm_treeDemo"), depm_zSetting, depm_zNodes);

function depm_beforeRemove(treeId, treeNode) {
	depm_treeObj.selectNode(treeNode);
	if(treeNode.isParent) {
		index_mess("存在子部门！", 3);
		return false;
	}
	var isSucc = false;
	if(confirm( "确认删除《" + treeNode.name + "》吗？")) {
		index_mess("删除中...", 0);
		$.ajax({
			async: false,
			url: "department_remove_js.action?_=" + Math.random(),
			data: {"department.id": treeNode.id},
			dataType: "json",
			success: function(data) {
				if(data.status == 0) {
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
function depm_beforeRename(treeId, treeNode, newName) {
	if (newName.length == 0) {
		index_mess("部门名称不能为空！", 3);
		setTimeout(function() {
			depm_treeObj.editName(treeNode);
		}, 10);
		return false;
	}
	if(treeNode.name == newName)//未更改
		return true;
	if(confirm("更改《" + treeNode.name + "》为《" + newName + "》吗？")) {
		index_mess("更改中...", 0);
		var isSucc = false;
		$.ajax({
			async: false,
			url: "department_change_js.action?_=" + Math.random(),
			data: {
				"department.id": treeNode.id,
				"department.name": newName
			}, type: "post",dataType: "json",
			success: function(data) {
				if(data.status == 0) {
					index_mess("更改成功！", 2);
					isSucc = true;
				} else {
					index_mess(data.mess, 1);
					if(data.login == false)
						index_login();				
				}
			}
		});
		if(isSucc) {
			return true;
		} else {
			depm_cancelEdit();
			return false;
		}
	} else {
		depm_cancelEdit();
		return false;
	}
}
function depm_cancelEdit() {
	setTimeout(function(){
		depm_treeObj.cancelEditName();
	}, 100);
}

function depm_addHoverDom(treeId, treeNode) {
	var sObj = $("#" + treeNode.tId + "_span");
	if (treeNode.editNameFlag || $("#depm_addBtn_" + treeNode.id).length > 0)
		return;
	var addStr = "<span class='button add' id='depm_addBtn_" + treeNode.id +
			"' title='增加' onfocus='this.blur();'></span>";
	sObj.append(addStr);
	var btn = $("#depm_addBtn_" + treeNode.id);
	if (btn) {
		btn.bind("click", function() {
			$.messager.prompt("添加部门", "请输入添加在《"+treeNode.name+"》下的部门名称：", function(treeName){
				if(treeName){
					index_mess("添加中...", 0);
					$.ajax({
						url: "department_add_js.action?_=" + Math.random(),
						data: {
							"department.name": treeName,
							"department.parent.id": treeNode.id,
							"department.sort": depm_zNodes.length
						}, type: "post",
						dataType: "json",
						success: function(data) {
							if(data.status == 0) {
								//刚添加必须将isParent设置为false，否则会出现添加两个叶子节点的现象
								depm_treeObj.addNodes(treeNode, {
									id : data.id,
									pId : treeNode.id,
									name : treeName,
									isParent : false
								});
								index_mess("添加成功！", 2);
							} else {
								index_mess(data.mess, 1);
								if(data.login == false) {
									index_login();
								}
							}
						}
					});
				}
			});
		});
	}
};
function depm_removeHoverDom(treeId, treeNode) {
	$("#depm_addBtn_" + treeNode.id).unbind().remove();
};
function depm_addDepment() {
	$.messager.prompt("添加部门", "请输入添加到顶层的公司名称：", function(treeName){
		if(treeName) {
			index_mess("添加中...", 0);
			$.ajax({
				url: "department_add_js.action?_=" + Math.random(),
				data: {
					"department.name": treeName,
					"department.sort": depm_zNodes.length
				}, type: "post", dataType: "json",
				success: function(data) {
					if(data.status == 0) {
						depm_treeObj.addNodes(null, {
							id : data.id,
							name : treeName
						});
						index_mess("添加成功！", 2);
					} else {
						index_mess(data.mess, 1);
						if(data.login == false)
							index_login();
					}
				}
			});
		}
	});
}
function depm_changeNodes() {
	index_mess("更新中...", 0);
	var nodes = depm_treeObj.transformToArray(depm_treeObj.getNodes());
	var newNodes = new Array();
	var zNodes = new Array();
	var newNode;
	$.each(nodes, function(key, node) {
		newNode = {"id": node.id, "pId": -1, "sort": -1};
		if(key<depm_zNodes.length && node.id==depm_zNodes[key].id) {
			if(depm_zNodes[key].sort != key)
				newNode.sort = key;
			if(node.pId != depm_zNodes[key].pId) {
				if(node.pId == null)
					newNode.pId = 0;
				else
					newNode.pId = node.pId;
			}
		} else {
			var oldNode = depm_findNodeById(node.id);
			if(oldNode == null) {
				newNode.sort = key;
				newNode.pId = node.pId;
			} else {
				if(oldNode.sort != key)
					newNode.sort = key;
				if(node.pId != oldNode.pId) {
					if(node.pId == null)
						newNode.pId = 0;
					else
						newNode.pId = node.pId;
				}
			}
		}
		newNodes.push(newNode);
		zNodes.push({
			id: node.id,
			name: node.name,
			sort: key,
			pId: node.pId
		});
	});
	var str = "";
	$.each(newNodes, function(key, val) {
		if(val.pId!=-1 || val.sort!=-1) {
			str = str + "dpms=" + val.id + "_" + val.pId + "_" + val.sort + "&";
		}
	});
	if(str != "") {
		$.post("department_changeSort_js.action?_=" + Math.random(), str, function(data) {
			if(data.status == 1) {
				index_mess(data.mess, 1);
				if(data.login == false)
					index_login();
			} else {
				index_mess("更新成功！", 2);
				depm_zNodes = zNodes;
			}
		}, "json");
	} else {
		index_mess("未更新！", 2);
	}
}
function depm_findNodeById(id) {
	for(var i in depm_zNodes) {
		if(depm_zNodes[i].id == id) {
			return depm_zNodes[i];
		}
	}
	return null;
}