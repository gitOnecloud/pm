<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<!DOCTYPE html><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>文件管理系统-后台管理</title>
<base href="<%=basePath%>"/>

<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico" /> 
<link rel="stylesheet" type="text/css" href="script/easyui/themes/default/easyui.css">  
<link rel="stylesheet" type="text/css" href="script/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="script/ztree/css/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="css/index.css">
<link rel="stylesheet" type="text/css" href="script/kindeditor/themes/default/default.css" >


<script type="text/javascript" src="script/jquery.min.js"></script>
<script type="text/javascript" src="script/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="script/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="script/ztree/jquery.ztree.all.min.js"></script>

<script type="text/javascript" src="script/kindeditor/kindeditor-min.js"></script>
<script type="text/javascript" src="script/kindeditor/lang/zh_CN.js"></script>
<script type="text/javascript" src="script/baodian/util/mess.js"></script>
<script type="text/javascript" src="script/baodian/util/date.js"></script>
<script type="text/javascript" src="script/admin.js"></script>

<script type="text/javascript" src="script/baodian/util/rsa-encode.js"></script>
<script type="text/javascript" src="script/jquery/jquery.encoding.digests.sha1.js"></script>

<script type="text/javascript">
var index_zNodes = <s:property value="json" escapeHtml="false"/>;
var index_user = {"id": "<s:property value="user.id[0]" escapeHtml="false"/>",
	"name": "<s:property value="user.str[0]" escapeHtml="false"/>",
	"account": "<s:property value="user.username" escapeHtml="false"/>",
	"depm": "<s:property value="user.department" escapeHtml="false"/>"
};
var index_serverUrl = '<s:property value="casData.serverLoginUrl" escapeHtml="false"/>';
var index_clientUrl = '<s:property value="casData.clientCheckUrl" escapeHtml="false"/>';
var index_noUserUrl = '<s:property value="casData.noUserFailureUrl" escapeHtml="false"/>';
</script>
<style type="text/css">
</style>
</head>
<body>
<div class="index_prompt" id="index_prompt" style="display: none"></div>
<!-- 首页主体 begin 分为上 下左 下右 -->
<div class="easyui-layout" data-options="fit: true">
	<div style="height: 80px;padding: 0 30px;" data-options="region:'north',border: false">
		<div style="float: left;margin-top: 10px;">
			<!-- <a href="http://www.baodian.com" target="_blank"></a> -->
			<img src="images/logo0.gif" style="border: 0;"/>
			<!-- <img src="images/yaan.gif" style="border: 0;"/> -->
		</div>
		<div style="float: right;">
			<div style="float: left;padding-top: 45px;">
				<span id="index_user"></span>
				<a href="javascript:;" class="easyui-linkbutton" onclick="index_login()">登录</a>
				<a id="index_loa" href="j_spring_security_logout" class="easyui-linkbutton" >退出</a>
				<a id="index_cpa" href="javascript:;" class="easyui-linkbutton" onclick="index_changePS()">修改密码</a>
				<a href="index.jsp" class="easyui-linkbutton">首页</a>
			</div>
		</div>
	</div>
	<div title="菜单栏" style="width:178px;overflow-y:scroll;"
		data-options="region:'west',split:true">
		<ul id="index_treeDemo" class="ztree"></ul>
	</div>
	<div data-options="region:'center'">
		<div id="mainbody" class="easyui-tabs" data-options="fit:true,border:false">
			<div title="主体部分" class="tabcontent"
				data-options="tools:[{
			        iconCls:'icon-mini-refresh',
			        handler:function(){loadIndex(true);}}]">
				<div id="index_pp" style="padding:8px 0;overflow-x:hidden;"><div></div><div></div><div></div></div>
			</div>
		</div>
	</div>
</div>
<!-- 首页主体 end -->
<!-- 登录框 -->
<div id="index_dd" style="width:350px;height:200px;padding:25px 0 30px 30px">
	<form id='index_form' action='j_spring_security_check' method='POST'>
	<table>
		<tr><td>用户名：</td>
			<td>
				<input id="j_username" type='text' name='j_username' class="index_input160"/>
				<input type="hidden" name="j_ajax"/>
			</td>
		</tr>
		<tr style="height: 30px;"><td>密&emsp;码：</td>
			<td><input id="j_password" type='password' name='j_password' class="index_input160"/></td>
		</tr>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;<input type='checkbox' name='_spring_security_remember_me' /></td>
			<td>记住密码</td>
		</tr>
	</table>
	</form>
</div>
<!-- 更改密码框 begin-->
<div id="index_ps" class="easyui-dialog" style="width:360px;height:190px;padding:20px 30px"
	data-options="closed:true,title:'更改密码',buttons:'#index_dlg-buttons'">
	<form id="index_fm" method="post">
		<div class="usli_fitem">
			<label>原始密码:</label><input name="user.account" type="password" style="width:150px;" class="easyui-validatebox" data-options="required:true">
			<span id="index_pw_span"></span>
		</div>
		<div class="usli_fitem">
			<label>新的密码:</label><input name="user.password" type="password" style="width:150px;" class="easyui-validatebox" data-options="required:true">
		</div>
		<div class="usli_fitem">
			<label>确定密码:</label><input name="user.name" type="password" style="width:150px;" class="easyui-validatebox" data-options="required:true">
		</div>
	</form>
</div>
<div id="index_dlg-buttons">
	<a href="javascript:;" class="easyui-linkbutton" onclick="index_changePW()"
		data-options="iconCls:'icon-ok'">保存</a>
	<a href="javascript:;" class="easyui-linkbutton" onclick="javascript:$('#index_ps').dialog('close')"
		data-options="iconCls:'icon-cancel'">取消</a>
</div>
<!-- 更改密码框 end-->

</body>
</html>