<%@ page contentType="text/html; charset=UTF-8"  %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var auli_zNodes = <s:property value="json" escapeHtml="false"/>;
</script>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.勾选的权限会在主界面的右边显示成菜单；
		2.地址为#表示上级菜单；
		3.权限列表允许拖曳排序。
	</div>
</div>
<br>
<div style="width: 110px;float: left;padding-left: 20px;">
	<br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="auli_treeObj.expandAll(true);"
		data-options="plain:true,iconCls:'icon-undo'">展开</a><br><br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="auli_treeObj.expandAll(false);"
		data-options="plain:true,iconCls:'icon-redo'">折叠</a><br><br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="auli_checkAll()"
		 data-options="plain:true,iconCls:'icon-add'">全选</a><br><br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="auli_addAuth()"
		data-options="plain:true,iconCls:'icon-edit'">添加到顶层</a><br><br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="auli_changeNodes();"
		data-options="plain:true,iconCls:'icon-save'">更新菜单顺序</a><br><br>
</div>
<div style="float: left;padding-left: 30px;border-left: 1px solid #C2D5E3;">
	<div class="auli_input">
		<input type="hidden" id="auli_aid" name="authority.id">
		<input type="hidden" id="auli_atid">
		<label>名称：</label>
		<input type="text" id="auli_aname"><br>
		<label>地址：</label>
		<input type="text" style="width: 280px;margin-top: 6px;" id="auli_aurl" >
		<a href="javascript:;" class="easyui-linkbutton" onclick="auli_changeAuth()"
			data-options="plain:true,iconCls:'icon-edit'">更新权限名称</a>
	</div>
	<div style="float: left;padding-top: 10px;">
		<label>权限菜单： </label>
	</div>
	<ul id="auli_treeDemo" class="ztree" style="padding: 10px 0 20px 65px;"></ul>
</div>

<div id="auli_dd" style="width:350px;height:180px;padding:25px 0 0 30px">
	<form id='auli_form' method='POST'>
		<div class="usli_fitem">
			<label>名称:</label><input name="authority.name" id="auli_name" class="easyui-validatebox" data-options="required:true">
			<span id="usli_n_span"></span>
		</div>
		<div class="usli_fitem">
			<label>地址:</label><input name="authority.url" id="auli_url" class="easyui-validatebox"
				data-options="required:true" style="width: 220px;">
			<span id="usli_url_span"></span>
		</div>
	</form>
</div>
