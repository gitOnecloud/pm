<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var loglist_logtype = <s:property value="json" escapeHtml="false"/>;
</script>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.日志管理。</div>
</div>

<div id="loglist_toolbar" style="padding:5px;height:auto">
	<div>
		<a href="javascript:;" class="easyui-linkbutton" onclick="loglist_removeLog()"
			data-options="iconCls:'icon-remove',plain:true">删除日志</a>
		<a href="javascript:;" class="easyui-menubutton"
			data-options="menu:'#loglist_dl_menu',iconCls:'icon-download',plain:true">导出</a>
	</div>
	<div style="padding-top: 5px; padding-left: 4px;">
		内容: <input id="loglist_content" class="index_input200">
		类型: <input id="loglist_type" >&ensp;
		从<input id="loglist_beginDate">
		到<input id="loglist_endDate">
		&ensp;<a href="javascript:;" class="easyui-linkbutton" onclick="loglist_search()"
			data-options="iconCls:'icon-search'">查找</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="loglist_clear()">清除</a>
	</div>
</div>

<table id="loglist_table"></table>

<div id="loglist_win">
	<div id="loglist_win_content" style="padding:15px 20px"></div>
</div>

<div id="loglist_dl_menu" style="width:100px;">
	<div onclick="loglist_download(false)">导出当前</div>
	<div onclick="loglist_download(true)">导出全部</div>
</div>
