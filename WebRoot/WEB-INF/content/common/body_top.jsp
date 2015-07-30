<%@ page contentType="text/html; charset=UTF-8"  %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!-- logo以及登录框 -->
<div class="index_prompt" id="index_prompt" style="display: none"></div>
<div class="header">
	<div class="head">
		<div class="logo">
			<a title="BaoDian" href="javascript:;" class="abutton">
				<img alt="BaoDian" src="images/logo0.gif">
			</a>
		</div>
		<div class="hdr">
			<s:if test="user">
				<span>欢迎&nbsp;<s:property value="user.str[0]"/>&nbsp;</span>
			</s:if>
			<s:else>
				<span>未登录</span>
			</s:else>
			<a href="login.html">登录</a>
			<a href="j_spring_security_logout">退出</a>
		</div>
	</div>
</div>
<div class="clear"></div>