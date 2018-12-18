package org.han.ica.asd.c.discovery.impl;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;



import java.io.IOException;

import java.util.Map;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.spy;


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
        //service = mock(Drive.class, RETURNS_DEEP_STUBS);
        System.out.println(service);
        googleDrive.setService(service);

        service = mock(Drive.class);
        googleDrive.setService(service);
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

    public void checkIfSearchFilesIsCalled() throws IOException {
        String roomName = "test";
        String folderID = "12345";

        googleDrive.setService(service);
        System.out.println(service == googleDrive.getService());

        googleDrive.searchFiles(roomName, folderID);

        verify(service).files().list().setQ(anyString()).setSpaces(anyString()).setFields(anyString()).setPageToken(anyString()).execute();

        verify(googleDrive).searchFiles(roomName, folderID);
    }

    @Test
    public void test3() {
        // Map<String, String> searchFolders(String name)throws IOException
    }

    @Test
    public void test4() {
        //String getFolderID(String name)throws IOException
    }

    @Test
    public void test5() {
        // Map<String, String> getAllFolders()throws IOException
    }

    @Test
    public void test6() {
        //String createFolder(String name) throws IOException
    }

    @Test
    public void test7() {
        //void deleteFileByNameInFolder(String name, String folderId) throws IOException
    }

    @Test
    public void test8() {
        //void deleteFolderByID(String id) throws IOException
    }

    @Test
    public void test9() {
        //void createFileInFolder(String name, String idFolder) throws IOException
    }

    @Test
    public void test10() {
        //Map<String, String> listAllFilesInFolder(String idFolder)throws IOException
    }

    @Test
    public void test11() {
        //String getPasswordFromFolder(String folderId)throws IOException
    }

    @Test
    public void test12() {
        //String getLeaderFromFolder(String folderId)throws IOException
    }

    @Test
    public void test13() {
        //List<String> getAllhostsFromFolder(String folderId)throws IOException
    }

    @Test
    public void test14() {
        //String getPasswordFromFolder(String folderId)throws IOException
    }

    @Test
    public void test15() {
        //String getPasswordFromFolder(String folderId)throws IOException
    }
//        googleDrive.setService(service);

}
