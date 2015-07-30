<%@ page contentType="text/html; charset=UTF-8"  %>
<%
	//使用iframe上传文件的提示
	String json = (String) request.getAttribute("json");
	if(json != null) out.print(json);
	else out.print(request.getParameter("json"));
%>