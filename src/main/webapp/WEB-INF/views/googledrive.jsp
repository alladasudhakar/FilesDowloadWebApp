<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow" %>
<%@ page import="com.google.api.client.googleapis.auth.oauth2.GoogleCredential" %>
<%@ page import="com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="com.google.api.services.drive.Drive" %>
<%@ page import="com.google.api.services.drive.DriveScopes" %>
<%@ page import="com.google.api.client.http.HttpTransport" %>
<%@ page import="com.google.api.client.http.javanet.NetHttpTransport" %>
<%@ page import="com.google.api.client.json.JsonFactory" %>
<%@ page import="com.google.api.client.json.jackson2.JacksonFactory" %>
<%@ page import="" %>
<%@ page import="" %>
<%@ page import="" %>
<%@ page import="" %>


<%!

private static String CLIENT_ID = "324390312543-plvf9j69lv9rqbbtobq45ebr7vs07vuo.apps.googleusercontent.com";
private static String CLIENT_SECRET = "EN-bpaKx3nOBGMEERQFIpCxM";
private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_FILE, DriveScopes.DRIVE,
		DriveScopes.DRIVE_APPDATA);

%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>google drive access</title>
</head>

<body>
<%
	HttpTransport httpTransport = new NetHttpTransport();
	JsonFactory jsonFactory = new JacksonFactory();
	
	GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory,
			CLIENT_ID, CLIENT_SECRET, SCOPES).setAccessType("online")
					.setApprovalPrompt("auto").build();
	String REDIRECT_URI = "http://localhost:8888/authcode";

%>
</body>
</html>