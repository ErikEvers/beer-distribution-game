package org.han.ica.asd.c.discovery.impl;

import org.han.ica.asd.c.discovery.IResourceManager;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GoogleDrive implements IResourceManager {

    private static final String SPACE = "drive";
    private static final String APPLICATION_NAME = "Beergame";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final java.util.Collection<String> SCOPES = DriveScopes.all();
    private String credentialsFilePath;
    private static final String FIELDS = "nextPageToken, files(id, name)";
    private static final String GOOGLE_DRIVE_ERROR = "Something went wrong when trying to connect to google drive";

    private Drive service;

    private static final Logger LOGGER = Logger.getLogger(GoogleDrive.class.getName());

    public GoogleDrive(String credentialLocation) {
        try {
            credentialsFilePath = credentialLocation;
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            service = new Drive.Builder(httpTransport, JSON_FACTORY, getCredential(httpTransport))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, GOOGLE_DRIVE_ERROR, e);
        }
    }

    private Credential getCredential(final NetHttpTransport httpTransport) throws IOException {
        InputStream in = GoogleDrive.class.getResourceAsStream(credentialsFilePath);

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public Drive getService() {
        return service;
    }

    public Map<String, String> searchFiles(String name, String folderId) throws IOException {
        HashMap results = new HashMap<String, String>();
        String pageToken = null;
        do {
            FileList result = service.files().list()
                    .setQ("mimeType!=\'application/vnd.google-apps.folder' and trashed = false and name contains \'" + name + "' and parents in \'" + folderId + "'")
                    .setSpaces(SPACE)
                    .setFields(FIELDS)
                    .setPageToken(pageToken)
                    .execute();
            for (File file : result.getFiles()) {
                results.put(file.getId(), file.getName().substring(3));
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        return results;
    }

    public boolean checkIfFolderNotExists(String name) throws IOException {
        HashMap results = new HashMap<String, String>();
        String pageToken = null;
        do {
            FileList result = service.files().list()
                    .setQ("mimeType=\'application/vnd.google-apps.folder' and trashed = false and name = \'" + name + "'")
                    .setSpaces(SPACE)
                    .setFields(FIELDS)
                    .setPageToken(pageToken)
                    .execute();
            for (File file : result.getFiles()) {
                results.put(file.getId(), file.getName());
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        return results.isEmpty();
    }

    public Map<String, String> searchFolders(String name) throws IOException {
        HashMap results = new HashMap<String, String>();
        String pageToken = null;
        do {
            FileList result = service.files().list()
                    .setQ("mimeType=\'application/vnd.google-apps.folder' and trashed = false and name contains \'" + name + "'")
                    .setSpaces(SPACE)
                    .setFields(FIELDS)
                    .setPageToken(pageToken)
                    .execute();
            for (File file : result.getFiles()) {
                results.put(file.getId(), file.getName());
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        return results;
    }

    public String getFolderID(String name) throws IOException {
        String folderID = "";
        String pageToken = null;
        do {
            FileList result = service.files().list()
                    .setQ("mimeType=\'application/vnd.google-apps.folder' and trashed = false and name = \'" + name + "'")
                    .setSpaces(SPACE)
                    .setFields(FIELDS)
                    .setPageToken(pageToken)
                    .execute();
            pageToken = result.getNextPageToken();
            try {
                folderID = result.getFiles().get(0).getId();
            }catch(IndexOutOfBoundsException e){
                LOGGER.log(Level.SEVERE, "Room file can not be found", e);
            }

        } while (pageToken != null);
        return folderID;
    }

    public Map<String, String> getAllFolders() throws IOException {
        HashMap results = new HashMap<String, String>();
        String pageToken = null;
        do {
            FileList result = service.files().list()
                    .setQ("mimeType=\'application/vnd.google-apps.folder' and trashed = false")
                    .setSpaces(SPACE)
                    .setFields(FIELDS)
                    .setPageToken(pageToken)
                    .execute();
            for (File file : result.getFiles()) {
                results.put(file.getId(), file.getName());
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);

        return results;
    }

    public String createFolder(String name) throws IOException {
        try {
            if (checkIfFolderNotExists(name)) {
                File fileMetadata = new File();
                fileMetadata.setName(name);
                fileMetadata.setMimeType("application/vnd.google-apps.folder");

                File newFile = service.files().create(fileMetadata)
                        .setFields("id")
                        .execute();
                return newFile.getId();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, GOOGLE_DRIVE_ERROR, e);
            throw new IOException(GOOGLE_DRIVE_ERROR);
        }
        return "False";
    }

    public void deleteFileByNameInFolder(String name, String folderId) throws IOException {
        try {
            String pageToken = null;
            FileList result = service.files().list()
                    .setQ("name = \'" + name + "' and parents in \'" + folderId + "'")
                    .setSpaces(SPACE)
                    .setFields(FIELDS)
                    .setPageToken(pageToken)
                    .execute();
            service.files().delete(result.getFiles().get(0).getId()).execute();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, GOOGLE_DRIVE_ERROR, e);
            throw new IOException(GOOGLE_DRIVE_ERROR);
        }
    }

    public void deleteFolderByID(String id) throws IOException {
        try {
            service.files().delete(id).execute();
            service.files().emptyTrash().execute();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, GOOGLE_DRIVE_ERROR, e);
            throw new IOException(GOOGLE_DRIVE_ERROR);
        }
    }

    public void createFileInFolder(String name, String idFolder) throws IOException {
        try {
            if (!"False".equals(idFolder)) {

                File fileMetadata = new File();
                fileMetadata.setName(name);
                fileMetadata.setParents(Collections.singletonList(idFolder));

                service.files().create(fileMetadata)
                        .setFields("id, parents")
                        .execute();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, GOOGLE_DRIVE_ERROR, e);
            throw new IOException(GOOGLE_DRIVE_ERROR);
        }
    }

    public Map<String, String> listAllFilesInFolder(String idFolder) throws IOException {
        HashMap results = new HashMap<String, String>();
        String pageToken = null;
        do {
            FileList result = service.files().list()
                    .setQ("'" + idFolder + "'" + " in parents")
                    .setSpaces(SPACE)
                    .setFields(FIELDS)
                    .setPageToken(pageToken)
                    .execute();
            for (File file : result.getFiles()) {
                results.put(file.getId(), file.getName());
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        return results;
    }

    public String getPasswordFromFolder(String folderId) throws IOException {
        HashMap<String, String> tempMap = (HashMap<String, String>) searchFiles("P:", folderId);

        return (String) tempMap.values().toArray()[0];
    }

    public String getLeaderFromFolder(String folderId) throws IOException {
        HashMap<String, String> tempMap = (HashMap<String, String>) searchFiles("L:", folderId);
        return (String) tempMap.values().toArray()[0];
    }

    public List<String> getAllhostsFromFolder(String folderId) throws IOException {
        ArrayList<String> temp = new ArrayList<>();
        HashMap<String, String> results = (HashMap<String, String>) searchFiles("H:", folderId);

        for (Map.Entry<String, String> host : results.entrySet()) {
            temp.add(host.getValue());
        }
        return temp;
    }

    public void setService(Drive service) {
        this.service = service;
    }
}
