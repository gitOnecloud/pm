<%@ page contentType="text/plain; charset=UTF-8" %>
<%
	//让ie跨域时能保存cookie
	response.setHeader("P3P", "CP=CAO PSA OUR");
%>
${param.callback}({"lt": "${loginTicket}", "execution": "${flowExecutionKey}", "rsa_pubKey": "${rsa_pubKey}", "rsa_modKey": "${rsa_modKey}", "status": 0});