<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.baodian.util.StaticMethod" %>
<!DOCTYPE html><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>文件管理系统</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico" /> 
<style type="text/css">
body {
	background: url("images/background.jpg") repeat-x scroll 0 0 transparent;
}
a {
	color: blue;
	font-size: 18px;
	font-weight: normal;
	text-decoration: none;
}
a:hover {
	text-decoration: underline;
}
.mess {
	color: #8F5700;
	font-size: 32px;
	font-weight: bold;
	left: 20%;
	position: absolute;
	top: 25%;
}
.jump {
	font-size: 18px;
	font-weight: normal;
}
</style>
<script type="text/javascript">
var count = 4;
function loaded() {
	setTimeout(function() {
		if(count == 0) {
			window.location.href = "index_rd.action";
		} else {
			document.getElementById("jumpsecond").innerHTML = count;
			count --;
			loaded();	
		}
	},1000);
}
</script>
</head>
<body onload="loaded()">
<div class="mess">
<%
	String json =  StaticMethod.tomcatDecode(request.getParameter("json"));
	if(json != null) {
		if(json.equals("expired"))
			out.print("此账号同时登陆超出5个限制！");
		else if(json.equals("invalid"))
			out.print("连接超时，请重新登录！");
		else if(json.equals("deny"))
			out.print("没有权限访问！");
		else if(json.equals("login"))
			out.print("请先登录！");
		else if(json.equals("authfail"))
			out.print("认证失败！");
		else
			out.print(json);
	} else {
		out.print("未知错误！");
	}
%>
<span class="jump"><b id="jumpsecond">5</b> 秒后跳转到<a href="index_rd.action">首页</a></span>
<img border="0" src="script/kindeditor/plugins/emoticons/images/0.gif" alt="">
<a href="common_login_no_rd.action">重新登录</a>
<a href="j_spring_security_logout">退出</a>
</div>
</body>
</html>