<%@ page contentType="text/plain; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
	//让ie跨域时能保存cookie
	response.setHeader("P3P", "CP=CAO PSA OUR");
%>
${param.callback}({"mess": "<spring:message code="cas.login.${loginMess}" />", "status": 1, "login": false});
