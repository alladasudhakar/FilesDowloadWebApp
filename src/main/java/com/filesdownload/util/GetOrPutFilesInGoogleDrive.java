package com.filesdownload.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class GetOrPutFilesInGoogleDrive {
	/** Application name. */
	private static final String APPLICATION_NAME = "Drive API Java";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".credentials/drive-java-quickstart");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	//private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY);
	private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_FILE, DriveScopes.DRIVE, DriveScopes.DRIVE_APPDATA);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	public static Credential authorize() throws IOException {
		// Load client secrets.
		InputStream in = GetOrPutFilesInGoogleDrive.class.getResourceAsStream("/client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}

	public static Drive getDriveService() throws IOException {
		Credential credential = authorize();
		return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	}

	public static void main(String[] args) throws Exception {
		Drive service = getDriveService();

		// Print the names and IDs for up to 10 files.
		// FileList result =
		// service.files().list().setPageSize(10).setFields("nextPageToken,
		// files(id, name)").execute();
		FileList result = service.files().list().execute();
		List<File> files = result.getItems();// .getFiles();
		if (files == null || files.size() == 0) {
			System.out.println("No files found.");
		} else {
			System.out.println("Files:");
			for (File file : files) {
				System.out.printf("%s (%s)\n", file.getTitle(), file.getId());
				/*
				//Creating a file
				String fileName = "test.txt";
				java.io.File filePath = new java.io.File(fileName);
				try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
					bos.write("simpletext".getBytes());
					bos.write(System.lineSeparator().getBytes());
				}catch(Exception e)
				{
					
				}
				File fileMetadata = new File();
				fileMetadata.setTitle(fileName);
				//fileMetadata.setParents(Collections.singletonList(folderId));
				filePath = new java.io.File("test.txt");
				FileContent fileCont = new FileContent("text/plain", filePath);
				File uploadFile = service.files().insert(fileMetadata, fileCont).execute();
				System.out.println("File ID: " + uploadFile.getId());
				
				if(true)break;
				*/
				//downloading a file
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				service.files().get(file.getId()).executeMediaAndDownloadTo(outputStream);
				System.out.println("outputStream = " + outputStream.toString());
				
				if ("photo.jpg".equalsIgnoreCase(file.getTitle())) {
					//deleting a file if required
					File newfile = service.files().get(file.getId()).execute();
					System.out.println("photo.jpg  ---> " + newfile.getDownloadUrl() + " -- " + file.getId());
					service.files().delete(file.getId()).execute();
					/*
					//downloading a file
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					service.files().get(file.getId()).executeMediaAndDownloadTo(outputStream);
					System.out.println("outputStream = " + outputStream.toString());
					// downloadFile(service, file);
					*/
				}
			}
		}
	}

	private static InputStream downloadFile(Drive service, File file) {
		if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
			try {
				HttpResponse resp = service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl()))
						.execute();
				return resp.getContent();
			} catch (IOException e) {
				// An error occurred.
				e.printStackTrace();
				return null;
			}
		} else {
			// The file doesn't have any content stored on Drive.
			return null;
		}
	}

	private static void downloadFile(Drive drive, boolean useDirectDownload, File uploadedFile) throws Exception {
		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		// create parent directory (if necessary)
		java.io.File parentDir = new java.io.File("/home/sudhakar/Downloads");
		if (!parentDir.exists() && !parentDir.mkdirs()) {
			throw new IOException("Unable to create parent directory");
		}
		OutputStream out = new FileOutputStream(new java.io.File(parentDir, uploadedFile.getTitle()));

		MediaHttpDownloader downloader = new MediaHttpDownloader(httpTransport,
				drive.getRequestFactory().getInitializer());
		downloader.setDirectDownloadEnabled(useDirectDownload);
		// downloader.setProgressListener(new FileDownloadProgressListener());
		downloader.download(new GenericUrl(uploadedFile.getDownloadUrl()), out);
	}

}
