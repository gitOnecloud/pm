var index_setting = {
	view: {
		showLine: false,
		showIcon: false,
		selectedMulti: false,
		dblClickExpand: false,
		addDiyDom: index_addDiyDom
	}, data: {
		simpleData : {
			enable : true
		}
	}, callback: {
		beforeClick: function(treeId, treeNode, clickFlag) {
			//点击菜单前先的判断，如果是父节点，就展开或折叠树，否则响应点击
			if(treeNode.isParent) {
				index_treeObj.expandNode(treeNode);
				return false;
			} else {
				return true;
			}
		}, onClick: function(event, treeId, treeNode) {
			index_openTab(treeNode.name, treeNode.action, true);
		}
	}
};
/**
 * 打开页面
 * @param title 标题
 * @param href 地址
 * @param load 是否加载同名javascript脚本
 */
function index_openTab(title, href, load) {
	index_mess("载入 - " + title, 0);
	if(href.lastIndexOf("_rf.action") != -1) {
		$.get(href + "?_=" + Math.random(), function(data) {
			if(data.status == 1) {
				index_mess(data.mess, 1);
				if(data.login == false) {
					index_login();
				}
				return;
			}
			index_mess("刷新内存成功！", 2);
		}, "json");
		return;
	} else if(href.lastIndexOf("_rd.action") != -1) {
		window.open(href);
		index_mess("另一页面打开中...", 2);
		return;
	}
	if (index_tabs.tabs("exists", title)) {
		index_mess("已经打开", 2);
		index_tabs.tabs("select", title);
	} else {
		index_createTab(title, href, load);
	}
}
/**
 * 更新页面
 * newtitle不等于title时，将关闭后重新建立，否则就是刷新原来已打开的
 * 页面不存在，将新建一个
 */
function index_updateTab(title, href, load, newtitle) {
	index_mess("载入 - " + title, 0);
	if(newtitle == null)
		newtitle = title;
	if (index_tabs.tabs("exists", title)) {
		if(newtitle != title && index_tabs.tabs("exists", newtitle)) {
			//更新后的如果存在，要关闭
			index_tabs.tabs("close", newtitle);
		}
		index_tabs.tabs("select", title);
		index_createTab(title, href, load, newtitle);
	} else {
		index_createTab(newtitle, href, load);
	}
}
/**
 * 展示页面
 * ajax取数据时可能取到html页面，也可能是json出错提示，所以要特殊处理
 */
function index_createTab(title, href, load, newtitle) {
	$.get(href + "?_=" + Math.random(), function(data) {
		if(data.substring(0,8).indexOf("{") != -1) {
			data = $.parseJSON(data);
			if(data.status == 1) {
				index_mess(data.mess, 1);
				if(data.login == false)index_login();
				return;
			}
		}
		var params = {
			content: data,
			tools:[{
		    	iconCls: 'icon-mini-refresh',
		    	handler: function() {
		    		index_updateTab(newtitle==null? title: newtitle, href, load);
		}}]};
		if(newtitle == null) {
			index_tabs.tabs("add", {title: title, closable: true});
		} else {
			params.title = newtitle;
		}
		index_tabs.tabs('update', {
			tab: index_tabs.tabs('getTab', title),
			options: params
		});
		if(load) {
			var url = href.split(".")[0];
			$.ajax({
				url: "script/baodian/" + url.split("_")[0] + "/" + url + ".js",
				//cache: true,//?_20121224
				dataType: "script"
			});
		}
		index_mess("载入成功", 2);
	});
}
/**
 * 关闭页面
 */
function index_closeTab(title) {
	$("#mainbody").tabs("close", title);
}
/**
 * 菜单样式
 */
function index_addDiyDom(treeId, treeNode) {
	var spaceWidth = 10;
	var switchObj = $("#" + treeNode.tId + "_switch");
	var icoObj = $("#" + treeNode.tId + "_ico");
	switchObj.remove();
	icoObj.before(switchObj);
	if (treeNode.level > 0) {
		var spaceStr = "<span style='display: inline-block;width:" + (spaceWidth * treeNode.level)+ "px'></span>";
		switchObj.before(spaceStr);
	}
}
//菜单, 中间页面
var index_treeObj, index_tabs;
$(function() {
	//菜单
	var index_tree_ul = $("#index_treeDemo");
	index_treeObj = $.fn.zTree.init(index_tree_ul, index_setting, index_zNodes);
	index_tree_ul.hover(function () {
		index_tree_ul.toggleClass("showIcon");
	});
	//首页布局
	index_tabs = $("#mainbody");
	loadIndex();
	//按下enter键登录
	$("#j_username").keydown(function(e) {
		if(e.keyCode==13) index_fmlg();
	});
	$("#j_password").keydown(function(e) {
		if(e.keyCode==13) index_fmlg();
	});
	//登陆框
	$('#index_dd').dialog({
		closed: true,
		title: '&nbsp;登录系统',
		buttons:[{
			text:'登录',
			iconCls:'icon-ok',
			handler: index_fmlg,
		},{
			text:'&nbsp;取消&nbsp;',
			handler:function(){
				$('#index_dd').dialog('close');
			}
		}]
	});
	//定时访问，防止掉线
	setInterval(function() {
		//$('#index_pgrid').panel('expand').panel('refresh');
	}, 1080000);
});
var index_panel = [];
/**
 * 首页布局
 * @param reload true表示重新加载，false表示展示
 */
function loadIndex(reload) {
	if(index_user.id == "") {
		$("#index_user").html("未登录&nbsp;");
	} else {
		$("#index_user").html("欢迎 " + index_user.name + " (" + index_user.depm + ")&ensp;");
	}
}
/**
 * 打开登录系统框
 */
function index_login() {
	$('#index_dd').dialog('open');
	$("#j_username").val("").focus();
	$("#j_password").val("");
}
/**
 * 登录系统
 */
function index_fmlg() {
	if($("#j_username").val() == "") {
		index_mess("用户名为空", 3);
		$("#j_username").focus();
		return;
	}
	if($("#j_password").val() == "") {
		index_mess("密码为空", 3);
		$("#j_password").focus();
		return;
	}
	index_mess("登录中...", 0);
	serverLogin();
	/*$('#index_form').form('submit',{
		url: "j_spring_security_check",
		success: function(data){
			data = $.parseJSON(data);
			if(data.status == 0) {
				index_mess("登录成功！", 2);
				$('#index_dd').dialog('close');
				$.fn.zTree.destroy("index_treeDemo");
				index_zNodes =data.menu;
				index_treeObj = $.fn.zTree.init($("#index_treeDemo"), index_setting, index_zNodes);
				index_user.id = data.id;
				index_user.name = data.name;
				loadIndex(true);
			} else {
				index_mess(data.mess, 1);
			}
		}
	});*/
	/*$.post("j_spring_security_check", $("#index_form").serialize(), function(data) {}, "json");*/
}
/**
 * 服务器端登录，先取得公钥，然后再验证账号密码
 */
function serverLogin() {
	var params = {};
	params.j_ajax = true;
	params.renew = true;//强制为重新登录
	params.service = index_clientUrl;
	$.ajax({
		url: index_serverUrl,
		data: params,
		dataType: 'jsonp',
		success: function(data) {
			params = {};
			params.execution = data.execution;
			params.lt = data.lt;
			//params.submit = "登录";
			params._eventId = 'submit';
			params.j_ajax = true;
			params.renew = true;
			//rsa加密
			var rsa = new RSAKey();
			rsa.setPublic(data.rsa_modKey, data.rsa_pubKey);
			var username = $('#j_username').val();
			username = username.toLowerCase();
			var password = $('#j_password').val();
			password = $.encoding.digests.hexSha1Str(username + password);
			params.username = username;
			params.password = rsa.encrypt(password);
			$.ajax({
				url: index_serverUrl,
				data: params,
				dataType: 'jsonp',
				success: function(data) {
					if(data.status == 1) {
						index_mess(data.mess, 1);
						return;
					}
					//服务端登录成功，然后到客户端进行验证
					clientCheck(data.clientUrl);
				}
			});
		}
	});
}
/**
 * 客户端验证登录
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
				index_mess(data.mess, 1);
				return;
			}
			if(needAdd) {//注册
				if(data.login == false) {
					index_mess(data.mess, 1);
					return;
				}
			} else {//登录
				if(data.login == false) {
					index_mess('请先补充用户信息！如果页面无跳转，请刷新此页面！', 1);
					$('#index_dd').dialog('close');
					window.open(index_noUserUrl.substring(1));
					return;
				}
			}
			//data = $.parseJSON(data);
			index_mess('登录成功，欢迎 ' + data.name, 2);
			$('#index_dd').dialog('close');
			$.fn.zTree.destroy("index_treeDemo");
			index_zNodes =data.menu;
			index_treeObj = $.fn.zTree.init($("#index_treeDemo"), index_setting, index_zNodes);
			index_user.id = data.id;
			index_user.name = data.name;
			index_user.account = data.account;
			index_user.depm = data.depm;
			loadIndex(true);
		}
	});
}
/**
 * 弹出更改密码框
 */
function index_changePS() {
	$("#index_pw_span").html("");
	$('#index_fm div input').val("");
	//使用easyUI的清除方法会出现提示框不消失的情况
	//$('#index_fm').form('clear');
	$('#index_ps').dialog('open');
}
/**
 * 更改密码
 */
function index_changePW() {
	if(!$("#index_fm").form('validate')) {
		return;
	}
	index_mess("更改中...", 0);
	//0旧密码 1新密码 2确定密码
	var params = $("#index_fm").serializeArray();
	if(params[1].value != "" && (params[1].value != params[2].value)) {
		index_mess("两次密码不相同！", 1);
		return;
	}
	//置为空，防止密码明文传送
	params[2].value = '';
	if(index_user.id == "") {
		index_mess("请先登录", 2);
		index_login();
		return;
	}
	//获取RSA公钥
	var isError = false;
	$.ajax({
		url: 'util_rsaKey_no.action',
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
			//旧密码
			var password = $.encoding.digests.hexSha1Str(index_user.account + params[0].value);
			params[0].value = rsa.encrypt(password);
			//新密码
			password = $.encoding.digests.hexSha1Str(index_user.account + params[1].value);
			params[1].value = rsa.encrypt(password);
		}
	});
	if(isError) {
		return;
	}
	$.post("user_changePW_js.action", params, function(data) {
		if(data.status == 0) {
			index_mess("更改成功", 2);
			$('#index_ps').dialog('close');
		} else {
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
			if(data.password == false) {
				$("#index_pw_span").html(data.mess);
			}
		}
		
	}, "json");
}
/**
 * 将ztree转换为easyui树
 * @param ztree [{id,name,pId}]
 * @returns etree [{id,text,children:[{..}]}]
 */
function index_changeTree(ztree, data) {
	if(ztree.length == 0)
		return [];
	var etree = [];
	$.each(ztree, function(k, v) {
		if(v.pId == null) {
			etree.push(index_node(v, data));
		}
	});
	index_createTree(etree, ztree, data);
	return etree;
}
/**
 * 生成一个节点
 * @param data {state:"closed"} 为null时从znode取，否则直接用
 */
function index_node(znode, data) {
	var node = {
		id: znode.id,
		text: znode.name
	};
	if(data != null) {
		$.each(data, function(k, v) {
			if(v == null) {
				node[k] = znode.k;
			} else {
				node[k] = v;
			}
		});
	}
	return node;
}
/**
 * 递归从ztree树取数据，然后构建easyui树
 */
function index_createTree(etree, ztree, data) {
	$.each(etree, function(ek, ev) {
		$.each(ztree, function(zk, zv) {
			if(zv.pId == ev.id) {
				if(ev.children == null) {
					ev.children = [index_node(zv, data)];
				} else {
					ev.children.push(index_node(zv, data));
				}
			}
		});
		if(ev.children != null) {
			index_createTree(ev.children, ztree, data);
		}
	});
}