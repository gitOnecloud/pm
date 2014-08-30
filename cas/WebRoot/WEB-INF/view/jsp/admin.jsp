<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<!DOCTYPE html><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><spring:message code="cas.admin.title" /></title>
<base href="<%=basePath%>"/>
<link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
</head>
<body>
<ul>
	<li>
		<a href="client/list"><spring:message code="cas.admin.clientManager" /></a>
	</li>
	<li>
		<a href="services/manage.html"><spring:message code="cas.admin.serviceManager" /></a>
	</li>
	<li>
		<a href="status"><spring:message code="cas.admin.serviceStatus" /></a>
	</li>
	<li>
		<a href="services/logout.html" title="logout of current session"><spring:message code="management.services.link.logout" /></a>
	</li>
</ul>
</body>
</html>