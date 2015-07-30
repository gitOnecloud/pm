<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var roai_ = <s:property value="json" escapeHtml="false"/>;
</script>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.添加角色。</div>
</div>
<br>
<div style="width: 110px;float: left;padding-left: 20px;">
	<br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="roleai_treeObj.expandAll(true);"
		data-options="plain:true,iconCls:'icon-undo'">展开</a><br><br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="roleai_treeObj.expandAll(false);"
		data-options="plain:true,iconCls:'icon-redo'">折叠</a><br><br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="roleai_checkAll()"
		 data-options="plain:true,iconCls:'icon-add'">全选</a><br><br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="roleai_addRole();"
		data-options="plain:true,iconCls:'icon-edit'">添加角色</a><br><br>
</div>
<div style="float: left;padding-left: 30px;border-left: 1px solid #C2D5E3;">
	<div style="padding-bottom:10px;">
		<label>上级角色： </label>
		<span id="roai_parent"></span>
	</div>
	<div class="gdli_input">
		<label>角色名称： </label>
		<input type="text" id="roleai_rname">
	</div>
	<div style="float: left;padding-top: 10px;">
		<label>角色权限： </label>
	</div>
	<ul id="roleai_treeDemo" class="ztree" style="padding: 10px 0 20px 65px;"></ul>
</div>
