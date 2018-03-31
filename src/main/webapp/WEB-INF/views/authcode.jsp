<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow" %>
<%@ page import="com.google.api.client.googleapis.auth.oauth2.GoogleCredential" %>
<%@ page import="com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page import="com.google.api.services.drive.Drive" %>
<%@ page import="com.google.api.services.drive.DriveScopes" %>
<%@ page import="com.google.api.client.http.HttpTransport" %>
<%@ page import="com.google.api.client.http.javanet.NetHttpTransport" %>
<%@ page import="com.google.api.client.json.JsonFactory" %>
<%@ page import="com.google.api.client.json.jackson2.JacksonFactory" %>
<%@ page import="com.google.api.services.drive.model.File" %>
<%@ page import="com.google.api.services.drive.model.FileList" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Spring Boot - hello</title>
</head>
<%!

String CLIENT_ID = "324390312543-plvf9j69lv9rqbbtobq45ebr7vs07vuo.apps.googleusercontent.com";
String CLIENT_SECRET = "EN-bpaKx3nOBGMEERQFIpCxM";
final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_FILE, DriveScopes.DRIVE,
		DriveScopes.DRIVE_APPDATA);

%>
<body>
<%
	HttpTransport httpTransport = new NetHttpTransport();
	JsonFactory jsonFactory = new JacksonFactory();
	String code = request.getParameter("code");
	System.out.println("code = "+code);
	String REDIRECT_URI = "http://localhost:8888/authcode";
	//received authorization code
	GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory,
			CLIENT_ID, CLIENT_SECRET, SCOPES).build();
	GoogleTokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
	//GoogleTokenResponse tokenResponse = flow.newTokenRequest(code).execute();
	GoogleCredential credential = new GoogleCredential().setFromTokenResponse(tokenResponse); 

	//Create a new authorized API client 
	Drive service = new Drive.Builder(httpTransport, jsonFactory, credential).build(); 
	System.out.println("service = "+service);
	FileList result = service.files().list().execute();
	List<File> files = result.getItems();// .getFiles();
	if (files == null || files.size() == 0) {
		System.out.println("No files found.");
	} else {
		System.out.println("Files:");
		for (File file : files) {
			System.out.printf("%s (%s)\n", file.getTitle(), file.getId());
			if("test.txt".equalsIgnoreCase(file.getTitle()))
			{
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				service.files().get(file.getId()).executeMediaAndDownloadTo(outputStream);
				out.println("ByteArrayOutputStream string value = " + outputStream.toString());
			}
		}
	}

%>
</body>
</html>