<%@ page language="java" contentType="text/plain; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page session="false" %>
<% 
	String msg = (String) request.getAttribute("msg");	

	if(msg != null){
		response.getWriter().write(msg);
	}
%>