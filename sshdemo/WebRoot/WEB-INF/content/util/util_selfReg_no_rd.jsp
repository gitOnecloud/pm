<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<!DOCTYPE html><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>xx系统-注册</title>
<base href="<%=basePath%>"/>
<link rel="shortcut icon" href="images/favicon.ico" />
<script type="text/javascript" src="script/jquery.min.js"></script>
<script type="text/javascript" src="script/ztree/jquery.ztree.all.min.js"></script>
<script type="text/javascript" src="script/baodian/util/mess.js"></script>
<script type="text/javascript" src="script/baodian/util/util_selfReg_no_rd.js"></script>
<link rel="stylesheet" type="text/css" href="script/ztree/css/zTreeStyle.css">
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
.index_prompt {
	font-size: 12px;
	background-color: #F9EDBE;/* 蓝#D6E9F8 */
    border-color: #F0C36D; /* 蓝#4D90F0 */
    border-radius: 2px 2px 2px 2px;
    border-style: solid;
    border-width: 1px;
    color: #333333;
    display: inline-block;
    font-weight: bold;
    height: 14px;
    left: 56%;
    overflow: hidden;
    padding: 6px 16px;
    position: fixed;
    top: 0;
    transition: opacity 0.218s ease 0s;
    z-index: 10000;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}
/* 输入框样式 */
.index_input200 {
	height: 18px;
	line-height: 18px;
	vertical-align: middle;
}
.index_input200 {
	width: 200px;
}
</style>
<script type="text/javascript">
var depms = <s:property value="json" escapeHtml="false"/>;
var index_serverUrl = '<s:property value="casData.serverLoginUrl" escapeHtml="false"/>';
var index_clientUrl = '<s:property value="casData.clientCheckUrl" escapeHtml="false"/>';

</script>
</head>
<body>
<div class="index_prompt" id="index_prompt" style="display: none"></div>
<div style="margin: 80px 40px;">
	<table>
		<tr>
			<td colspan="3">补充完信息后，就可以使用了！</td></tr>
		<tr>
			<td>姓名：</td>
			<td><input id="name" class="index_input200"></td>
			<td></td></tr>
		<tr>
			<td>部门：</td>
			<td><input id="depm" class="index_input200" readonly="readonly"></td>
			<td>
				<button onclick="regedit()">注册</button>
				<a href="<s:property value="casData.clientIndexUrl.substring(1)" escapeHtml="false"/>">首页</a>
				<a href="<s:property value="casData.clientLoginUrl.substring(1)" escapeHtml="false"/>">重新登录</a>
				<a href="<s:property value="casData.clientLogoutUrl.substring(1)" escapeHtml="false"/>">退出</a>
				</td></tr>
		<tr>
			<td></td>
			<td><ul id="depmTree" class="ztree"></ul></td>
			<td></td></tr>
	</table>
</div>
</body>
</html>