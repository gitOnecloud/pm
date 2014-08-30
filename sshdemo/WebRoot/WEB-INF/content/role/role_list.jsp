<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var roli_zNodes = <s:property value="json" escapeHtml="false"/>;
</script>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.双击查看；2.勾选的角色作为自助注册用户的默认角色。</div>
</div>

<div style="width: 110px;float: left;padding-left: 20px;">
	<br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="roli_treeObj.expandAll(true);"
		data-options="plain:true,iconCls:'icon-undo'">展开</a><br><br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="roli_treeObj.expandAll(false);"
		data-options="plain:true,iconCls:'icon-redo'">折叠</a><br><br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="roli_newRole()"
		data-options="plain:true,iconCls:'icon-edit'">添加到顶层</a><br><br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="roli_changeNodes();"
		data-options="plain:true,iconCls:'icon-save'">保存角色顺序</a><br><br>
</div>
<div style="float: left;padding-left: 30px;border-left: 1px solid #C2D5E3;">
	<div style="float: left;padding-top: 10px;">
		<label>角色列表： </label>
	</div>
	<ul id="roli_treeDemo" class="ztree" style="padding: 10px 0 20px 65px;"></ul>
</div>




