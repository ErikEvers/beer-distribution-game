package org.han.ica.asd.c.discovery;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IResourceManager {
    Map<String, String> searchFiles(String name, String folderId)throws IOException;

    boolean checkIfFolderNotExists(String name)throws IOException;

    Map<String, String> searchFolders(String name)throws IOException;

    String createFolder(String name) throws IOException;

    void deleteFolderByID(String id) throws IOException;

    void createFileInFolder(String name, String idFolder) throws IOException;

    Map<String, String> listAllFilesInFolder(String idFolder)throws IOException;

    String getPasswordFromFolder(String folderId)throws IOException;

    String getLeaderFromFolder(String folderId)throws IOException;

    List<String> getAllhostsFromFolder(String folderId)throws IOException;

    void deleteFileByNameInFolder(String name, String folderId) throws IOException;

    Map<String, String> getAllFolders() throws IOException;

    String getFolderID(String roomName)throws IOException;
}
