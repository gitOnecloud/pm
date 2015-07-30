<%@ page contentType="text/plain; charset=UTF-8" %>
<%
	String json = (String) request.getAttribute("json");
	if(json != null) out.print(json);
	else out.print(request.getParameter("json"));
%>