package org.han.ica.asd.c.discovery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoomTest {
    @Mock
    IResourceManager service;

    @Test(expected = RoomException.class)
    public void shouldThrowErrorWhenRoomIDCouldntBeRetrieved() throws IOException, RoomException {
        when(service.getFolderID(any(String.class))).thenThrow(new IOException());
        String roomName = "Beergame 1";
        new Room(roomName, service);
    }
    @Test(expected = RoomException.class)
    public void shouldThrowErrorWhenIPisNotValid() throws RoomException, IOException {
        String roomName = "Beergame 1";
        String wrongIP = "265.22.33.55";
        String password = "P@SSw0rd";
        String roomID = "12345";
        when(service.getFolderID(any(String.class))).thenReturn(roomID);
        when(service.getLeaderFromFolder(roomID)).thenReturn("192.168.1.1");
        when(service.getAllhostsFromFolder(roomID)).thenReturn(new ArrayList<String>());
        when(service.getPasswordFromFolder(roomID)).thenReturn(password);

        Room room = new Room("Beergame 1", service);

        room.addHost(wrongIP, password);
    }

    @Test(expected = RoomException.class)
    public void shouldThrowErrorWhenPasswordIsNotCorrect() throws RoomException, IOException {
        String roomName = "Beergame 1";
        String wrongIP = "265.22.33.55";
        String password = "P@SSw0rd";
        String wrongPassword = "Error";
        String roomID = "12345";
        when(service.getFolderID(any(String.class))).thenReturn(roomID);
        when(service.getLeaderFromFolder(roomID)).thenReturn("192.168.1.1");
        when(service.getAllhostsFromFolder(roomID)).thenReturn(new ArrayList<String>());
        when(service.getPasswordFromFolder(roomID)).thenReturn(password);

        Room room = new Room("Beergame 1", service);

        room.addHost(wrongIP, wrongPassword);
    }

    @Test(expected = RoomException.class)
    public void shouldThrowErrorWhenAddingHostWithNoConnection() throws RoomException, IOException {
        String roomName = "Beergame 1";
        String ip = "192.22.33.55";
        String password = "P@SSw0rd";
        String roomID = "12345";
        when(service.getFolderID(any(String.class))).thenReturn(roomID);
        when(service.getLeaderFromFolder(roomID)).thenReturn("192.168.1.1");
        when(service.getAllhostsFromFolder(roomID)).thenReturn(new ArrayList<String>());
        when(service.getPasswordFromFolder(roomID)).thenReturn(password);

        doThrow(IOException.class).when(service).createFileInFolder("H: " + ip, roomID);

        Room room = new Room("Beergame 1", service);

        room.addHost(ip, password);
        assertEquals(room.getHosts().get(0), ip);
    }

    @Test(expected = RoomException.class)
    public void shouldThrowErrorWhenCreatingRoomThatExists() throws IOException, RoomException {
        when(service.getFolderID(any(String.class))).thenThrow(new IOException());
        String roomName = "Beergame 1";
        new Room(roomName, service);
    }

    @Test(expected = RoomException.class)
    public void shouldThrowErrorWhenCreatingRoomWithInvalidIP() throws IOException, RoomException {
        String roomID = "12345";
        String password = "P@SSw0rd";
        String roomName = "Beergame";
        String leader = "266.168.1.1";

        Room room = new Room(roomName, leader, password, service);
    }

    @Test(expected = RoomException.class)
    public void shouldThrowErrorWhenCreatingRoomWithNoConnection() throws IOException, RoomException {
        String roomID = "12345";
        String password = "P@SSw0rd";
        String roomName = "Beergame";
        String leader = "192.168.1.1";
        when(service.createFolder(roomName)).thenThrow(IOException.class);

        Room room = new Room(roomName, leader, password, service);
    }

    @Test
    public void shouldCreateRoomWithGivenValuesWithoutError() throws RoomException, IOException {
        String roomID = "12345";
        String roomName = "Beergame";
        String password = "P@SSw0rd";
        String leader = "192.168.1.1";

        when(service.createFolder(roomName)).thenReturn(roomID);

        Room room = new Room(roomName, leader, password, service);

        assertEquals(roomName, room.getRoomName());
        assertEquals(password, room.getPassword());
        assertEquals(leader, room.getLeaderIP());
        assertEquals(roomID, room.getRoomID());
    }

    @Test
    public void shouldRemoveHost() throws RoomException, IOException {
        String roomName = "Beergame";
        String newIP = "192.168.100.1";
        String password = "";
        String roomID = "12345";

        when(service.getFolderID(any(String.class))).thenReturn(roomID);
        when(service.getLeaderFromFolder(roomID)).thenReturn("192.168.1.1");
        when(service.getAllhostsFromFolder(roomID)).thenReturn(new ArrayList<String>());
        when(service.getPasswordFromFolder(roomID)).thenReturn(password);
        doNothing().when(service).deleteFileByNameInFolder("H: " + newIP, roomID);

        Room room = new Room(roomName, service);
        room.addHost(newIP, password);
        room.removeHost(newIP);

        assertFalse(room.getHosts().contains(newIP));
    }

    @Test
    public void shouldThrowErrorWhenRemovingHostWithNoConnection() throws IOException, RoomException {
        String roomName = "Beergame";
        String newIP = "192.168.100.1";
        String password = "";
        String roomID = "12345";

        when(service.getFolderID(any(String.class))).thenReturn(roomID);
        when(service.getLeaderFromFolder(roomID)).thenReturn("192.168.1.1");
        when(service.getAllhostsFromFolder(roomID)).thenReturn(new ArrayList<String>());
        when(service.getPasswordFromFolder(roomID)).thenReturn(password);
        doThrow(IOException.class).when(service).deleteFileByNameInFolder("H: " + newIP, roomID);

        Room room = new Room(roomName, service);
        room.addHost(newIP, password);
        room.removeHost(newIP);
    }

    @Test
    public void shouldReturnTrueWhenRoomOpen() throws RoomException, IOException {
        String roomID = "12345";
        String roomName = "Beergame";
        String password = "P@SSw0rd";
        String leader = "192.168.1.1";

        when(service.createFolder(roomName)).thenReturn(roomID);
        when(service.getFolderID(roomName)).thenReturn(roomID);

        Room room = new Room(roomName, leader, password, service);
        assertFalse(room.isGameClosedForDiscovery());
        assertEquals(room.getGameStarted(), false);
    }

    @Test
    public void shouldReturnFalseWhenRoomClosed() throws RoomException, IOException {
        String roomID = "12345";
        String roomName = "Beergame";
        String password = "P@SSw0rd";
        String leader = "192.168.1.1";

        when(service.createFolder(roomName)).thenReturn(roomID);
        when(service.getFolderID(roomName)).thenReturn("");

        Room room = new Room(roomName, leader, password, service);
        assertTrue(room.isGameClosedForDiscovery());
        assertEquals(room.getGameStarted(), true);
    }
    @Test
    public void shouldThrowErrorWithNoConnection() throws RoomException, IOException {
        String roomID = "12345";
        String roomName = "Beergame";
        String password = "P@SSw0rd";
        String leader = "192.168.1.1";

        when(service.createFolder(roomName)).thenReturn(roomID);
        doThrow(IOException.class).when(service).getFolderID(roomName);

        Room room = new Room(roomName, leader, password, service);
        assertFalse(room.isGameClosedForDiscovery());
        assertEquals(room.getGameStarted(), false);
    }

    @Test
    public void shouldJoinRoomWithGivenValuesWithoutError() throws RoomException, IOException {
        String roomName = "Beergame";
        String newIP = "192.168.100.1";
        String password = "";
        String roomID = "12345";

        when(service.getFolderID(any(String.class))).thenReturn(roomID);
        when(service.getLeaderFromFolder(roomID)).thenReturn("192.168.1.1");
        when(service.getAllhostsFromFolder(roomID)).thenReturn(new ArrayList<String>());
        when(service.getPasswordFromFolder(roomID)).thenReturn(password);

        Room room = new Room(roomName, service);
        room.addHost(newIP, password);

        assertTrue(room.getHosts().contains(newIP));
    }

    @Test(expected = RoomException.class)
    public void shouldThrowErrorWhenJoiningRoomWithInvalidIP() throws IOException, RoomException {
        String roomName = "Beergame";
        String newInvladidIP = "400.23.12.3";
        String password = "P@SSw0rd";
        String roomID = "12345";

        when(service.getFolderID(any(String.class))).thenReturn(roomID);
        when(service.getLeaderFromFolder(roomID)).thenReturn("192.168.1.1");
        when(service.getAllhostsFromFolder(roomID)).thenReturn(new ArrayList<String>());
        when(service.getPasswordFromFolder(roomID)).thenReturn(password);

        Room room = new Room(roomName, service);
        room.addHost(newInvladidIP, password);
    }

    @Test
    public void shouldReturnUpdatedHostsWhenUpdating() throws RoomException, IOException {
        String roomName = "Beergame";
        String leader = "192.168.1.1";
        String host = "192.168.2.1";
        String password = "P@SSw0rd";
        String roomID = "12345";
        ArrayList<String> updatedHosts = new ArrayList<>();
        updatedHosts.add(host);

        when(service.getFolderID(any(String.class))).thenReturn(roomID);
        when(service.getLeaderFromFolder(roomID)).thenReturn(leader);
        when(service.getAllhostsFromFolder(roomID)).thenReturn(updatedHosts);
        when(service.getPasswordFromFolder(roomID)).thenReturn(password);

        Room room = new Room(roomName, service);

        room.updateRoom();
        assertTrue(room.getHosts().contains(host));
    }
    @Test(expected = RoomException.class)
    public void shouldThrowErrorWhenupdatingHostWithNoConnection() throws IOException, RoomException {
        String roomName = "Beergame";
        String leader = "192.168.1.1";
        String host = "192.168.2.1";
        String password = "P@SSw0rd";
        String roomID = "12345";
        ArrayList<String> updatedHosts = new ArrayList<>();
        updatedHosts.add(host);

        when(service.getFolderID(any(String.class))).thenReturn(roomID);
        when(service.getLeaderFromFolder(roomID)).thenReturn(leader);
        when(service.getAllhostsFromFolder(roomID)).thenReturn(updatedHosts);
        when(service.getPasswordFromFolder(roomID)).thenReturn(password);

        Room room = new Room(roomName, service);

        when(service.getAllhostsFromFolder(roomID)).thenThrow(IOException.class);
        room.updateRoom();
    }

    @Test
    public void closeGameAndStartGameShouldReturnTrue() throws RoomException {
        String roomID = "12345";
        String roomName = "Beergame";
        String password = "P@SSw0rd";
        String leader = "192.168.1.1";

        Room room = new Room(roomName, leader, password, service);

        room.closeGameAndStartGame();
        assertTrue(room.getGameStarted());
    }

    @Test(expected = RoomException.class)
    public void closeGameAndStartGameWithNoConnectionShouldThrowError() throws IOException, RoomException {
        String roomID = "12345";
        String roomName = "Beergame";
        String password = "P@SSw0rd";
        String leader = "192.168.1.1";

        when(service.getFolderID(any(String.class))).thenReturn(roomID);
        when(service.getLeaderFromFolder(roomID)).thenReturn(leader);
        when(service.getAllhostsFromFolder(roomID)).thenReturn(new ArrayList<String>());
        when(service.getPasswordFromFolder(roomID)).thenReturn(password);

        Room room = new Room(roomName, service);
        doThrow(IOException.class).when(service).deleteFolderByID(anyString());

        room.closeGameAndStartGame();
    }

}
