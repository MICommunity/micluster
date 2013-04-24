<%@ page language="java" contentType="text/plain; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page session="false" %>
<%@ page import="java.io.*" %>
<%
	response.setContentType("text/plain");
	PrintWriter writer = response.getWriter();
	String msg = (String) request.getAttribute("msg");	
	File file = (File) request.getAttribute("file");
	
	if(file != null){
		FileInputStream input = new FileInputStream(file);
		BufferedInputStream buf = new BufferedInputStream(input);
	    
		int readBytes = 0;
	    while((readBytes = buf.read()) != -1)
	    	writer.write(readBytes);
	    
	}else if(msg != null){
		writer.write(msg);
	}
%>