<%@ page contentType="text/html; charset=UTF-8"  %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
var rosh_zNodes = <s:property value="json" escapeHtml="false"/>;
var rosh_rid = <s:property value="role.id"/>;
var rosh_rname = "<s:property value="role.name" escapeHtml="false"/>";
</script>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.更新完角色后，再在菜单栏点击“刷新角色内存”，才会对系统生效。</div>
</div>
<div class="gdli_input">
	<label for="rosh_rname">角色名称： </label><input type="text" id="rosh_rname">
	<a href="javascript:;" class="easyui-linkbutton" onclick="rosh_changeRole()"
		data-options="plain:true,iconCls:'icon-edit'">更新名称</a>
	<a href="javascript:;" class="easyui-linkbutton" onclick="rosh_removeRole()"
		data-options="plain:true,iconCls:'icon-cancel'">删除</a>
</div>
<br>
<div style="width: 110px;float: left;padding-left: 20px;">
	<br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="rosh_treeObj.expandAll(true);"
		data-options="plain:true,iconCls:'icon-undo'">展开</a><br><br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="rosh_treeObj.expandAll(false);"
		data-options="plain:true,iconCls:'icon-redo'">折叠</a><br><br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="rosh_checkAll()"
		 data-options="plain:true,iconCls:'icon-add'">全选</a><br><br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="rosh_changeRA();"
		data-options="plain:true,iconCls:'icon-edit'">更新权限</a><br><br>
</div>
<div style="float: left;padding: 0 30px 20px;border-left: 1px solid #C2D5E3;">
	<ul id="rosh_treeDemo" class="ztree"></ul>
</div>
