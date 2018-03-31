<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- Static content -->
<link rel="stylesheet" href="/resources/css/style.css">
<script type="text/javascript" src="/resources/js/app.js"></script>

<title>Google Drive API</title>
</head>
<body>
	<h1>Google Drive file upload or download</h1>
	<hr>

	<div class="form">
		<form action="googledrive" method="post" onsubmit="return validate()">
			<table>
				<tr>
					<td>User name</td>
					<td><input id="name" type="text" name="name"></td>
				</tr>
				<tr>
					<td>Password</td>
					<td><input id="password" type="password" name="password"></td>
				</tr>
				<tr>
					<td></td>
					<td><input type="submit" value="Submit"></td>
				</tr>
			</table>
		</form>
	</div>

</body>
</html>