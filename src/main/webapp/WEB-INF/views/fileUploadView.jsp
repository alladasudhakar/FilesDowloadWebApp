<!DOCTYPE html>
<html>
<body>

	<h1>Spring Boot - Upload Status</h1>
	<%
		String message = (String)session.getAttribute("message");
		if(message != null)
		{
			out.println("<h3>"+message+"</h3>");
		}

	%>

</body>
</html>