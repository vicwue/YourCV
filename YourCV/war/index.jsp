<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page
	import="com.google.appengine.api.users.User,
 com.google.appengine.api.users.UserService,
com.google.appengine.api.users.UserServiceFactory"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
		UserService UserService = UserServiceFactory.getUserService();
			User user = UserService.getCurrentUser();
			if (user == null) {
		out.print("<p><a href='"+UserService.createLoginURL("/")+"'>Login</a></p>");
			} else { 
			out.print("<p>Welcome, "+user.getNickname()+"! <a href=\""+UserService.createLogoutURL("/")+"\">Logout</a><p>");
			}
	%>
</body>
</html>