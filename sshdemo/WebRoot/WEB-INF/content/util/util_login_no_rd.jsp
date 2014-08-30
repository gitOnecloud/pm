<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<!DOCTYPE html><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>文件管理系统-登录</title>
<base href="<%=basePath%>"/>
<link rel="shortcut icon" href="images/favicon.ico" />
<style type="text/css">
	.main{ margin:0 auto; width:1007px; padding-top:200px; height:auto; overflow:hidden;}
	.main_left{width:635px; height:280px; float:left;}
	.leftinfo{ margin-top:20px; background:url(images/mainleft.jpg) no-repeat; height:366px; padding-top:15px;}
	.today{ color:#FFFFFF; font-size:16px; font-weight:bold; padding-left:260px;}
	.main_right{ background:url(images/mainright.jpg) no-repeat; width:372px; height:349px; float:right;}
* {
	padding: 0;
	margin: 0;
}
body {
	background: url("images/body.jpg") no-repeat scroll center top #FFFFFF;
	font-family: verdana;
}
input {
	outline: none
}
a {
	color: #111111;
	text-decoration: none;
}
a:hover {
	text-decoration: underline;
}
.loginform {
	position: relative;
	margin: 108px 0 0 30px;
}
.formIpt {
	position: relative;
	width: 230px;
}
.inputtext{
	background-color: #EEF3F8;
	border: 0 none;
	color: #333333;
	font-size: 16px;
	font-weight: bold;
	height: 21px;
	line-height: 21px;
	padding: 6px 7px 4px;
	vertical-align: middle;
	width: 216px;
}
.inputLine {
	background-color: #FFFFFF;
	border: 1px solid #BBBBBB;
	border-color: #BAC5D4 #D5DBE2 #D5DBE2 #BAC5D4;
	box-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1) inset;
}
.inputLine:hover {
	border-color: #A6B4C9 #BAC5D4 #BAC5D4 #A6B4C9;
}
.inputIn, .inputIn:hover {
	border-color: #60A4E8 #84B4FC #84B4FC #60A4E8;
}
.loginPrompt {
	color: #92A4BF;
	cursor: text;
	left: 9px;
	line-height: 20px;
	position: absolute;
	top: 7px;
}
.inputOn {
	color: #B4C0D2;
}
.inputBtn {
	border: 0 none;
    cursor: pointer;
    height: 44px;
    position: absolute;
    width: 89px;
    top: 13px;
}
.submit {
	background: url("images/subimg.jpg");
}
.reset {
	background: url("images/resetimg.jpg");
    right: 0px;
}
.footer{ 
	background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAA7CAIAAAA1lncVAAAAJ0lEQVQImWN49+odw7dP3xjevUPQMPzt0zfy8TfK8P9f/4nH/4nCAB+Wq1f1C5qVAAAAAElFTkSuQmCC") repeat-x scroll 0 0 transparent;
	color: #6D6D6D;
	font-size: 12px;
	height: 60px;
	margin-top: 25px;
	padding-top: 15px;
	width: 100%;
}
.ft_center {
	width: 1007px;
	margin: 0 auto;
}
.footer .ft_left {
	float: left;
}
.footer .ft_right {
	float: right;
}
.footer p.ft_inc {
	font-size: 11px;
}
.leftinfo a {
	color: #FFFFFF;
	font-style: italic;
}
</style>

<script type="text/javascript" src="script/jquery.min.js"></script>
<script type="text/javascript" src="script/baodian/util/rsa-encode.js"></script>
<script type="text/javascript" src="script/jquery/jquery.encoding.digests.sha1.js"></script>
<script type="text/javascript" src="script/baodian/util/util_login_no_rd.js"></script>

<script type="text/javascript">
var index_serverUrl = '<s:property value="casData.serverLoginUrl" escapeHtml="false"/>';
var index_clientUrl = '<s:property value="casData.clientCheckUrl" escapeHtml="false"/>';
var index_noUserUrl = '<s:property value="casData.noUserFailureUrl" escapeHtml="false"/>';
var index_indexUrl = '<s:property value="casData.clientIndexUrl" escapeHtml="false"/>';
</script>
</head>
<body>
<div class="main">
	<div class="main_left">
		<div class="leftinfo">
			<span class="today" id="spantoday"></span> &emsp;
			<a href="<s:property value="casData.clientIndexUrl.substring(1)" escapeHtml="false"/>">首页</a>
			<a href="<s:property value="casData.clientLogoutUrl.substring(1)" escapeHtml="false"/>">退出</a>
		</div>
	</div>
	<div class="main_right">
		<form name="login" action="j_spring_security_check" method="post"
			class="loginform" >
			<div id="nameInput" class="formIpt inputLine">
				<input id="j_username" name="j_username" type="text" class="inputtext"
					onblur="toggle(0,0)" onfocus="toggle(1,0)">
				<label id="namePrompt" for="username" class="loginPrompt"></label>
			</div>
			<div style="height: 16px;"></div>
			<div id="passInput" class="formIpt inputLine">
				<input id="j_password" name="j_password" type="password" class="inputtext"
					onblur="toggle(0,1)" onfocus="toggle(1,1)">
				<label id="passPrompt" for="password" class="loginPrompt"></label>
			</div>
			<div style="height: 10px;"></div>
			<div class="formIpt">
				<input id="rememberMe" name="_spring_security_remember_me" type="checkbox"
					style="vertical-align: middle;">
				<label for="rememberMe" style="font-size: 12px;">记住密码</label>
				<span id="loginmess" style="padding-left: 5px;font-size: 12px;color: #FF4340;"></span>
			</div>
			<div class="formIpt">
				<input type="button" class="inputBtn submit" onclick="cas_login()" value="">
				<input type="reset" class="inputBtn reset" value="">
			</div>
		</form>
	</div>
</div>
<div class="footer">
	<div class="ft_center">
		<div class="ft_left">
			<p>荷树园电厂信息部</p>
			<p class="ft_inc">最后更新&copy;2014年04月10日</p>
		</div>
		<div class="ft_right">
			<p><strong><a href="http://www.baodian.com/" target="_blank">广东宝丽华电力有限公司</a></strong></p>
			<p class="ft_inc">
				本站支持
				<a href="http://windows.microsoft.com/zh-CN/internet-explorer/downloads/ie-8" target="_blank">IE8</a>
				|
				<a href="http://www.firefox.com.cn/download/" target="_blank">Firefox</a>
				|
				<a href="http://www.google.cn/chrome/intl/zh-CN/landing_chrome.html?hl=zh-CN" target="_blank">Google</a>
				等浏览器</p>
		</div>
	</div>
</div>
</body>
</html>
