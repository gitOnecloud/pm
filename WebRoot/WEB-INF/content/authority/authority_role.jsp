<%@ page contentType="text/html; charset=UTF-8"  %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var auro_data = <s:property value="json" escapeHtml="false"/>;
</script>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.把权限赋予对应角色。
	</div>
</div>
<div class="auro_body">
	<div class="auro_choose">
		<span>当前权限：<b id="auro_crole">未选择</b></span>
	</div>
	<div class="auro_aTree">
		<ul id="auro_aTree" class="ztree"></ul>
	</div>
	<div class="auro_center">
		<span>&lArr; 权限<br >&emsp; 角色 &rArr;</span>
	</div>
	<div class="auro_change">
		<a href="javascript:;" onclick="auro_change()"
			class="easyui-linkbutton" data-options="iconCls:'icon-edit'">更新</a>
		<a href="javascript:;" onclick="auro_chkall()" class="easyui-linkbutton" >全选</a>
	</div>
	<div class="auro_aTree auro_rTree">
		<ul id="auro_rTree" class="ztree"></ul>
	</div>
</div>
