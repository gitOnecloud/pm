<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<!DOCTYPE html><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>xx系统-账号被锁定</title>
<base href="<%=basePath%>"/>
<link rel="shortcut icon" href="images/favicon.ico" />
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
</style>
<script type="text/javascript">
</script>
</head>
<body>
<div class="mess">账号被锁定，请联系管理员
<img border="0" src="script/kindeditor/plugins/emoticons/images/0.gif" alt="">
<a href="login.html">重新登录</a>
<a href="j_spring_security_logout">退出</a>
</div>
</body>
</html>