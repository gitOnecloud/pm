<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var depm_zNodes = <s:property value="json" escapeHtml="false"/>;
</script>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.双击一行编辑或保存；&nbsp;&nbsp;2.点击"操作"栏的一项进行修改。</div>
</div>
<div>
	<a href="javascript:;" class="easyui-linkbutton" onclick="depm_treeObj.expandAll(true);">展开</a>
	<a href="javascript:;" class="easyui-linkbutton" onclick="depm_addDepment()"
		data-options="iconCls:'icon-edit',plain:true">添加到顶层</a>
	<a href="javascript:;" class="easyui-linkbutton" onclick="depm_treeObj.expandAll(false);">折叠</a>
	<a href="javascript:;" class="easyui-linkbutton" onclick="depm_changeNodes()" >保存顺序</a>
</div>
<ul id="depm_treeDemo" class="ztree"></ul>