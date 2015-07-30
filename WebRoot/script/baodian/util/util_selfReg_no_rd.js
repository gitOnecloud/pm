var depm_treeObj = null;
$(function() {
	var depm_zSetting = {
		check: {
				enable: true,
				chkStyle: "radio",
				radioType: "all"
		}, view: {
			dblClickExpand: false
		}, data: {
			simpleData: {
				enable: true
			}
		}, callback: {
			onClick: onClick,
			onCheck: onCheck
		}
	};
	depm_treeObj = $.fn.zTree.init($("#depmTree"), depm_zSetting, depms);
	
});
function onClick(e, treeId, treeNode) {
	depm_treeObj.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}

function onCheck(e, treeId, treeNode) {
	var checks = new Array();
	$.each(depm_treeObj.getCheckedNodes(true), function(k, v) {
		checks.push(v.name);
	});
	$('#depm').val(checks.join(','));
}
/**
 * 注册用户
 */
function regedit() {
	var name = $('#name').val();
	if(name.length < 2 || name.length > 20) {
		index_mess('请正确输入姓名，2~20个字符！', 4);
		return;
	}
	var nodes = depm_treeObj.getCheckedNodes(true);
	if(nodes.length == 0) {
		index_mess('请先选择部门！', 4);
		return;
	}
	var user = {};
	user.name = name;
	user.depm = nodes[0].id;
	//客户端退出，但服务器端未退出，对客户端进行重新验证
	var params = {};
	params.j_ajax = true;
	params.j_ajax_validate = true;
	params.service = index_clientUrl;
	index_mess("验证登录...", 0);
	$.ajax({
		url: index_serverUrl,
		data: params,
		dataType: 'jsonp',
		success: function(data) {
			if(data.clientUrl != null) {
				index_mess("注册中...", 0);
				clientCheck(data.clientUrl, user, true);
				return;
			}
			//index_mess('请先登录！', 1);
			window.location.href = 'message.jsp?json=login';
		}
	});
}
/**
 * 客户端验证登录
 * @param needAdd 添加用户
 */
function clientCheck(clientUrl, user, needAdd) {
	$.ajax({
		url: clientUrl + '&j_ajax=true',
		data: user,
		type: 'post',
		async: false,
		cache: false,
		dataType: 'json',
		success: function(data) {
			if(data.status == 1) {
				index_mess('客户端: ' + data.mess, 1);
				return;
			}
			if(needAdd) {//注册
				if(data.login == false) {
					index_mess('注册失败，请刷新后重试！', 1);
					return;
				}
			} else {//登录
				if(data.login == false) {
					index_mess('客户端未注册！', 1);
					return;
				}
			}
			//index_mess('注册成功: ' + data.name, 2);
			window.location.href = 'message.jsp?json=注册成功';
		}
	});
}