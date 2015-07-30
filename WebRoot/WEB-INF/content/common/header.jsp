<%@ page contentType="text/html; charset=UTF-8"  %>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<base href="<%=basePath%>"/>
<script type="text/javascript" src="script/jquery.min.js"></script>
<script type="text/javascript" src="script/baodian/util/mess.js"></script>
<script type="text/javascript" src="script/baodian/util/date.js"></script>
<link rel="shortcut icon" href="images/favicon.ico" /> 