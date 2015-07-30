$(function() {
	var now = new Date();
	var year = now.getFullYear();
	var month = now.getMonth()+1;
	var date = now.getDate();
	var nowtime = now.getDay();
	var week = new Array("星期天","星期一","星期二","星期三","星期四","星期五","星期六");
	var today = "今天是"+year+"年"+month+"月"+date+"日"+"&nbsp;&nbsp;&nbsp;"+week[nowtime];
	document.getElementById("spantoday").innerHTML = today;
	document.login.j_username.focus();
	//document.login.onsubmit = function() {};
	var url = location.href;
	var error = url.substring(url.indexOf("json=") + 5);
	if(error == "error") {
		document.getElementById("loginmess").innerHTML = "账号或密码错误！";
	} else if(error == "invalid") {
		document.getElementById("loginmess").innerHTML = "登录超时，请重新登录！";
	} else if(error == "expired") {
		document.getElementById("loginmess").innerHTML = "登录被弹出，请重新登录！";
	}
	var arrStr = document.cookie.split(";");
	for(var i=0; i<arrStr.length; i++) {
		var temp = arrStr[i].split("=");
		if(temp[0] == "lastaccount") {
			document.login.j_username.value = unescape(temp[1]);
			document.login.j_password.focus();
		}
	}
	//按下enter键登录
	$("#j_username").keydown(function(e) {
		if(e.keyCode==13) cas_login();
	});
	$("#j_password").keydown(function(e) {
		if(e.keyCode==13) cas_login();
	});
});

var interval;
/**
 * 输入框得到焦点，转换边框样式和提示信息
 * type 0-去除 1-添加
 * domId 输入框id 0-nameInput 1-passInput
 */
function toggle(type, domId) {
	if (type == 1) {
		interval = setInterval(function() {
			inputKeyup(0);
			inputKeyup(1);
		}, 100);
	} else {
		clearInterval(interval);
	}
	var targetId;
	if (domId == 0) {
		targetId = [ "nameInput", "namePrompt" ];
	} else {
		targetId = [ "passInput", "passPrompt" ];
	}
	document.getElementById(targetId[0]).className = "formIpt inputLine"
			+ (type == 0 ? "" : " inputIn");
	document.getElementById(targetId[1]).className = "loginPrompt"
			+ (type == 0 ? "" : " inputOn");
}
/**
 * 输入框改变时的动作
 * domId 输入框id 0-namePrompt 1-passPrompt
 */
function inputKeyup(domId) {
	var targetId;
	if (domId == 0) {
		targetId = [ document.login.j_username.value, "namePrompt", "请输入账号" ];
	} else {
		targetId = [ document.login.j_password.value, "passPrompt", "密码" ];
	}
	if (targetId[0] == null || targetId[0] == "") {
		document.getElementById(targetId[1]).innerHTML = targetId[2];
	} else {
		document.getElementById(targetId[1]).innerHTML = "";
	}
}
/**
 * 登录
 */
function cas_login() {
	var user = document.login.j_username.value;
	var psw = document.login.j_password.value;
	if(user==''){
		document.getElementById("loginmess").innerHTML = "账号不能为空！";
		document.login.j_username.focus();
		return false;
	}
	if(user.length > 20) {
		document.getElementById("loginmess").innerHTML = "账号字数不能超过20个！";
		document.login.j_username.focus();
		return false;
	}
	if(psw==''){
		document.getElementById("loginmess").innerHTML = "密码不能为空！";
		document.login.j_password.focus();
		return false;
	}
	if(psw.length > 32){
		document.getElementById("loginmess").innerHTML = "密码字数不能超过32个！";
		document.login.j_password.focus();
		return false;
	}
	document.getElementById("loginmess").innerHTML = "登录中...";
	document.cookie = "lastaccount=" + escape(user);
	serverLogin();
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
						$('#loginmess').html(data.mess);
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
		url: clientUrl + '&j_ajax=true&j_onlyLogin=true',
		data: user,
		type: 'post',
		async: false,
		cache: false,
		dataType: 'json',
		success: function(data) {
			$('#loginmess').html(data.mess);
			if(data.status == 1) {
				return;
			}
			if(needAdd) {//注册
				if(data.login == false) {
					return;
				}
			} else {//登录
				if(data.login == false) {
					$('#loginmess').html('请先补充完整用户信息！');
					window.open(index_noUserUrl.substring(1));
					return;
				}
			}
			window.location.href = index_indexUrl.substring(1);
		}
	});
}