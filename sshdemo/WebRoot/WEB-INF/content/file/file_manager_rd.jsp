<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>文件管理</title>
<%@ include file="../common/header.jsp"%>
<script src="script/baodian/file/file_manager.js" type="text/javascript"></script>
<script src="script/kindeditor/kindeditor-min.js" type="text/javascript"></script>
<script src="script/uploadify/jquery.uploadify.min.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="script/kindeditor/themes/default/default.css"/>
<link rel="stylesheet" type="text/css" href="css/news.css"/>
<link rel="stylesheet" type="text/css" href="script/uploadify/uploadify.css"/>
<script type="text/javascript">
</script>
</head>
<body>
<%@ include file="../common/body_top.jsp"%>
<%-- <%@ include file="../common/body_menu.jsp"%> --%>
<!-- 当前位置以及搜索框 -->
<div class="navi frame">
	<div class="posi">
		<a href="index.action">首页</a>&nbsp;&gt;
		<span>文件管理</span>
	</div>
	<div class="clear"></div>
	<%-- <%@ include file="../common/body_search.jsp"%> --%>
</div>
<!-- 上传 -->
<div class="page">
	<div class="p_left">
		<button class="imgbut" onclick="loadFile(parentDir)">上一目录</button>
		<button class="imgbut" id="upload">上传</button>
		<button class="imgbut" id="mkdir">新建文件夹</button>
		<button class="imgbut" onclick="file_upload()">uploadify</button>
		<button class="imgbut" onclick="$('#file_uploads').uploadify('cancel', '*')">cancel</button>
		<input class="imgbut" id="file_uploads"/>
	</div>
	<div class="p_right">
		当前路径：<span id="currentDir"></span>&nbsp;&nbsp;
		<a href="javascript:;" onclick="loadFile(parentDir)">返回上一目录</a>
	</div>
</div>
<div class="content frame">
	<div class="newshead">
		<table class="n_table">
			<tbody>
				<tr>
					<td class="ficon"></td>
					<td class="fname">名称</td>
					<td class="faction">操作</td>
					<td class="ftime">上传时间</td>
					<td class="ftype">类型</td>
					<td class="fsize">大小</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="n_body">
		<table class="n_table">
			<tbody id="f_tbody"></tbody>
		</table>
	</div>
</div>
<%@ include file="../common/body_tail.jsp"%>
</body>
</html>