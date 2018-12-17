package org.han.ica.asd.c.discovery.impl;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.han.ica.asd.c.discovery.impl.GoogleDrive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GoogleDriveTest {

    @InjectMocks
    GoogleDrive googleDrive;

    @Mock
    Drive service;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        googleDrive = mock(GoogleDrive.class);
        service = mock(Drive.class, RETURNS_DEEP_STUBS);
        googleDrive.setService(service);

//        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
//        fileMetadata.setName("test");
//        fileMetadata.setMimeType("application/vnd.google-apps.folder");
    }

    @Test
    public void createFolderShouldReturnTest() throws IOException {
        when(googleDrive.createFolder("test")).thenReturn("test");
        String test = googleDrive.createFolder("test");

        assertEquals(test, googleDrive.createFolder("test"));
    }

    @Test(expected = IOException.class)
    public void createFolderShouldThrowIOException() throws IOException {
        when(googleDrive.createFolder("test")).thenThrow(IOException.class);
        googleDrive.createFolder("test");
    }

    @Test
    public void checkIfSearchFilesIsCalled() throws IOException {
        String roomName = "test";
        String folderID = "12345";

        when(service.files().list()
                .setQ(anyString())
                .setSpaces(anyString())
                .setFields(anyString())
                .setPageToken(anyString())
                .execute()).thenReturn(new FileList());

        googleDrive.searchFiles(roomName, folderID);

        //verify(service).files().list().setQ(anyString()).setSpaces(anyString()).setFields(anyString()).setPageToken(anyString()).execute();

        verify(googleDrive).searchFiles(roomName, folderID);
    }

    @Test
    public void checkIfMethodIsCalled2() throws IOException {
        String roomName = "test";
        String folderID = "12345";
        FileList fileList = new FileList();

        File file = new File();

        when(service.files().list()
                .setQ(anyString())
                .setSpaces(anyString())
                .setFields(anyString())
                .setPageToken(anyString())
                .execute()).thenReturn(new FileList());

        googleDrive.checkIfFolderNotExists(roomName);
    }

    @Test
    public void test3(){
        // Map<String, String> searchFolders(String name)throws IOException
    }
    @Test
    public void test4(){
        //String getFolderID(String name)throws IOException
    }

    @Test
    public void test5(){
        // Map<String, String> getAllFolders()throws IOException
    }
    @Test
    public void test6(){
        //String createFolder(String name) throws IOException
    }
    @Test
    public void test7(){
        //void deleteFileByNameInFolder(String name, String folderId) throws IOException
    }

    @Test
    public void test8(){
        //void deleteFolderByID(String id) throws IOException
    }

    @Test
    public void test9(){
        //void createFileInFolder(String name, String idFolder) throws IOException
    }

    @Test
    public void test10(){
        //Map<String, String> listAllFilesInFolder(String idFolder)throws IOException
    }

    @Test
    public void test11(){
        //String getPasswordFromFolder(String folderId)throws IOException
    }

    @Test
    public void test12(){
        //String getLeaderFromFolder(String folderId)throws IOException
    }

    @Test
    public void test13(){
        //List<String> getAllhostsFromFolder(String folderId)throws IOException
    }

    @Test
    public void test14(){
        //String getPasswordFromFolder(String folderId)throws IOException
    }

    @Test
    public void test15(){
        //String getPasswordFromFolder(String folderId)throws IOException
    }
}
