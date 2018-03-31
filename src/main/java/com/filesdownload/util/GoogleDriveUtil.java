package com.filesdownload.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

public class GoogleDriveUtil {

	private static String CLIENT_ID = "324390312543-plvf9j69lv9rqbbtobq45ebr7vs07vuo.apps.googleusercontent.com";
	private static String CLIENT_SECRET = "EN-bpaKx3nOBGMEERQFIpCxM";
	private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_FILE, DriveScopes.DRIVE,
			DriveScopes.DRIVE_APPDATA);
	
	public static Drive getDrive() {
		Drive service = null;
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		try {
			GoogleAuthorizationCodeFlow flow = null;
			
			flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory,
					CLIENT_ID, CLIENT_SECRET, SCOPES).setAccessType("online")
							.setApprovalPrompt("auto").build();
			String REDIRECT_URI = "http://localhost:8888/authcode";
			/*
			GoogleTokenResponse response = flow
					.newTokenRequest(
							"ya29.GluNBRBJeQM75_vDVjJwcWpXzAau5zXBZcb-O2COQmQKrnqLC6OFiLGYubei7VEQ4LPx6MiEpnJHD4RIT-neIHTkJxXsC9iPPNSzOj5q0am8zA3VUuqgdZ7YpEVr")
					.setRedirectUri(REDIRECT_URI).execute();
			GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);
			*/
			Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("");
			
			// Create a new authorized API client
			service = new Drive.Builder(httpTransport, jsonFactory, credential).build();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return service;
	}
}
