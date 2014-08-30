<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<!DOCTYPE html><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><spring:message code="cas.admin.clientManager" /></title>
<base href="<%=basePath%>"/>

<link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
<!-- <link rel="stylesheet" type="text/css" href="css/.css">   -->

<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="js/baodian/clientView.js"></script>

<script type="text/javascript">
</script>
<style type="text/css">
</style>
</head>
<body>
<div style="padding:5px;margin:5px;border:1px #0000FF solid;">
	<!-- <button onclick="client_reflesh()">刷新</button> -->
	<a href="services/logout.html" title="logout of current session"><spring:message code="management.services.link.logout" /></a>
</div>
<div style="padding:20px;margin:5px;border:1px #FF0000 solid;">
	<table id="client_table"></table>
</div>
<div style="padding:20px;margin:5px;border:1px #0000FF solid;">
	<table>
		<tr><td>name:</td><td><input id="client_name" style="width:300px;"></td></tr>
		<tr><td>domain:</td><td><input id="client_domain" style="width:300px;"></td></tr>
		<tr><td>ip:</td><td><input id="client_ip" style="width:300px;"><spring:message code="cas.clientView.ipMess" /></td></tr>
		<tr><td></td>
			<td>
				<button onclick="client_saveOrChange(false)"><spring:message code="cas.clientView.add" /></button>
				<button onclick="client_saveOrChange(true)"><spring:message code="cas.clientView.change" /></button>
				<button onclick="client_changeRsa()"><spring:message code="cas.clientView.changeRsa" /></button>
			</td></tr>
	</table>
</div>
<div>
	<textarea id="client_mess" style="width:600px;height:74px"></textarea>
</div>
</body>
</html>