<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">

</script>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.项目管理。</div>
</div>

<div id="pjl_toolbar" style="padding:5px;height:auto">
	<div>
		<a href="javascript:;" class="easyui-linkbutton" onclick="pjl_addProject()"
			data-options="iconCls:'icon-add',plain:true">添加</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="pjl_editProject()"
			data-options="iconCls:'icon-edit',plain:true">修改</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="pjl_removeProject()"
			data-options="iconCls:'icon-remove',plain:true">删除</a>
	</div>
	<div style="padding-top: 5px; padding-left: 4px;">
		项目名称: <input id="pjl_name" class="index_input200">
		&ensp;<a href="javascript:;" class="easyui-linkbutton" onclick="pjl_search()"
			data-options="iconCls:'icon-search'">查找</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="pjl_clear()">清除</a>
	</div>
</div>

<table id="pjl_table"></table>

<div id="pjl_dlg" style="padding:15px 20px">
	<div id="pjl_dlg_content"></div>
	<form id="pjl_fm" method="post">
		<table class="index_inputTable"><tr>
			<td><input name="project.id" hidden="hidden"/></td>
		</tr><tr>
			<td><b>*</b>简称:</td>
			<td><input name="project.name" class="easyui-textbox" 
				data-options="required:true,validType:'length[1,50]'"/></td>
		</tr><tr>
			<td><b>*</b>全称:</td>
			<td><input name="project.fname" class="easyui-textbox"
				data-options="required:true,validType:'length[1,50]'"/></td>
		</tr><tr>
			<td>项目开始:</td>
			<td><input name="project.beginTime" class="easyui-datetimebox"
				data-options="editable:false"/></td>
		</tr><tr>
			<td>项目结束:</td>
			<td><input name="project.endTime" class="easyui-datetimebox"
				data-options="editable:false"/></td>
		</tr><tr>
			<td>备注:</td>
			<td><input name="project.remark" class="easyui-textbox" style="height:60px"
				data-options="multiline:true,validType:'length[1,80]'"/></td>
		</tr></table>
	</form>
</div>
