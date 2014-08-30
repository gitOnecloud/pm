<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<!DOCTYPE html><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>文件管理系统</title>
<base href="<%=basePath%>"/>

<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico" /> 
<link rel="stylesheet" type="text/css" href="script/easyui/themes/default/easyui.css">  
<link rel="stylesheet" type="text/css" href="script/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="script/ztree/css/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="css/index.css">
<link rel="stylesheet" type="text/css" href="script/kindeditor/themes/default/default.css" >
<link rel="stylesheet" type="text/css" href="script/uploadify/uploadify.css"/>

<script type="text/javascript" src="script/jquery.min.js"></script>
<script type="text/javascript" src="script/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="script/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="script/ztree/jquery.ztree.all.min.js"></script>
<script type="text/javascript" src="script/uploadify/jquery.uploadify.min.js"></script>

<script type="text/javascript" src="script/kindeditor/kindeditor-min.js"></script>
<script type="text/javascript" src="script/kindeditor/lang/zh_CN.js"></script>
<script type="text/javascript" src="script/baodian/util/mess.js"></script>
<script type="text/javascript" src="script/baodian/util/date.js"></script>
<script type="text/javascript" src="script/index.js"></script>

<script type="text/javascript" src="script/baodian/util/rsa-encode.js"></script>
<script type="text/javascript" src="script/jquery/jquery.encoding.digests.sha1.js"></script>

<script type="text/javascript">
var index_datas = <s:property value="json" escapeHtml="false"/>;
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
.datagrid-header td {
	font-size: 14px;
} .datagrid-body td, .datagrid-footer td {
	font-size: 17px;
}
.datagrid-cell,
.datagrid-cell-group,
.datagrid-cell-rownumber {
	height: 36px;
}
.datagrid-header-rownumber {
	height: 20px;
}
.datagrid-cell-rownumber {
	line-height: 36px;
}

.datagrid-header .datagrid-cell {
	height: auto;
	line-height: auto;
}
.datagrid-body td, .datagrid-footer td {
    border-right: none;
}
.datagrid-body td.datagrid-td-rownumber {
    border-right: 1px dotted #CCCCCC;
}
a.fileName {
	color: #000;
	text-decoration: none;
}
a.fileName:hover {
	text-decoration: underline;
}
.fileupload {
	margin: 20px 50px;
}
.fileupload p {
	margin-bottom: 8px;
}
.fileupload span {
	color: #E53333;
}
.tree-node  {
    height: 22px;
}
.tree-title {
    font-size: 15px;
}
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
				<a href="admin.jsp" class="easyui-linkbutton">管理</a>
			</div>
		</div>
	</div>
	<div title="目录" style="width: 240px;overflow-y:scroll;"
		data-options="region:'west',split:true">
		<ul id="file_tree"></ul>
	</div>
	<div title="文件列表" data-options="region:'center',onResize:function(w,h){file_resize(w,h)}">
		<div id="index_toolbar" style="padding:5px;height:auto;">
			<a href="javascript:;" class="easyui-linkbutton" onclick="upLoad()"
				data-options="iconCls:'icon-save',plain:true">上传</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="mkdir()"
				data-options="iconCls:'icon-add',plain:true">新建文件夹</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="file_openMove();"
				data-options="plain:true,iconCls:'icon-edit'">移动位置</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="loadFile(1)"
				data-options="iconCls:'icon-undo',plain:true">根目录</a>
				<a href="javascript:;" class="easyui-linkbutton" onclick="loadFile(2)"
				data-options="iconCls:'icon-reload',plain:true">刷新</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="loadFile(3)"
				data-options="iconCls:'icon-redo',plain:true">上一目录</a>
			<span style="float: right;padding: 7px 30px 0 0;font-size: 14px;">
				<b id="index_fileSize"></b>&emsp;
				路径:
				<b id="index_path"></b>
				</span>
		</div>
		<table id="index_table"></table>
		<form id="file_form" action="file_download_rd.action" method="get">
			<input type="hidden" name="path" id="file_path">
		</form>
		<div id="file_move_dlg" title="移动选中的文件或目录到新文件夹" class="easyui-dialog"
			style="width:460px;height:300px;padding:10px 20px;"
			data-options="closed:true,buttons:'#file_move_dlg-buttons'">
			<div class="usli_ftitle">
				<span>请选择要移动到的目录</span>
			</div>
			<ul id="file_move_tree" style="margin-bottom: 18px;"></ul>
		</div>
		<div id="file_move_dlg-buttons">
			<a href="javascript:;" class="easyui-linkbutton"
				onclick="file_move_saveDir()"
				data-options="iconCls:'icon-ok'">移动</a>
			<a href="javascript:;" class="easyui-linkbutton"
				onclick="$('#file_move_dlg').dialog('close')"
				data-options="iconCls:'icon-cancel'">取消</a>
		</div>
		<div id="file_uploads_dlg" title="上传文件" class="easyui-dialog"
			style="width:460px;height:300px;padding:10px 20px;"
			data-options="closed:true,buttons:'#file_uploads_dlg-buttons',onClose:function(){$('#file_uploads').uploadify('destroy');}">
			<input class="imgbut" id="file_uploads"/>
		</div>
		<div id="file_uploads_dlg-buttons">
			<a href="javascript:;" class="easyui-linkbutton"
				onclick="file_upload()"
				data-options="iconCls:'icon-ok'">上传</a>
			<a href="javascript:;" class="easyui-linkbutton"
				onclick="$('#file_uploads').uploadify('cancel','*');"
				data-options="iconCls:'icon-cancel'">清除</a>
			<a href="javascript:;" class="easyui-linkbutton"
				onclick="$('#file_uploads_dlg').dialog('close')">关闭</a>
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