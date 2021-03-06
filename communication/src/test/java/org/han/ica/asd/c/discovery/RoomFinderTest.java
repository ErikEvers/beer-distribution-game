package org.han.ica.asd.c.discovery;

import org.han.ica.asd.c.discovery.DiscoveryException;
import org.han.ica.asd.c.discovery.IResourceManager;
import org.han.ica.asd.c.discovery.Room;
import org.han.ica.asd.c.discovery.RoomFinder;
import org.junit.Before;
import org.junit.Test;

import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoomFinderTest {

    @InjectMocks
    private RoomFinder roomFinder;

    @Mock
    private IResourceManager service;

    @Before
    public void setup(){
        RoomFinder.setService(service);
    }

    @Test
    public void shouldReturnAvailableRooms() throws IOException, DiscoveryException {
        Map<String, String> host = new HashMap<>();
        String hostIP = "192.168.100.1";
        host.put("1", hostIP);
        when(service.getAllFolders()).thenReturn(host);

        roomFinder = new RoomFinder();

        assertEquals(hostIP, roomFinder.getAvailableRooms().get(0));
    }

    @Test
    public void shouldCreateRoomWithGivenValues() throws DiscoveryException, IOException {
        String roomName = "Beergame";
        String leaderIP = "192.168.1.1";
        String password = "P@SSw0rd";
        when(service.checkIfFolderNotExists(roomName)).thenReturn(true);
        roomFinder = new RoomFinder();
        Room room = roomFinder.createGameRoom(roomName, leaderIP, password);

        assertEquals(roomName, room.getRoomName());
        assertEquals(leaderIP, room.getLeaderIP());
        assertEquals(password, room.getPassword());
    }

    @Test
    public void shouldJoinRoomWithCorrectCredentials() throws DiscoveryException, IOException {
        String roomName = "Beergame";
        String leaderIP = "192.168.1.1";
        String hostIP = "192.168.1.100";
        String password = "P@SSw0rd";
        String roomID = "12345";
        when(service.checkIfFolderNotExists(roomName)).thenReturn(false);
        when(service.getFolderID(roomName)).thenReturn(roomID);
        when(service.getLeaderFromFolder(roomID)).thenReturn(leaderIP);
        when(service.getAllhostsFromFolder(roomID)).thenReturn(new ArrayList<String>());
        when(service.getPasswordFromFolder(roomID)).thenReturn(password);

        roomFinder = new RoomFinder();
        Room room = roomFinder.joinGameRoom(roomName, hostIP, password);

        assertEquals(leaderIP, room.getLeaderIP());
        assertEquals(roomName, room.getRoomName());
        assertEquals(hostIP, room.getHosts().get(0));
        assertEquals(password, room.getPassword());
    }

    @Test(expected = DiscoveryException.class)
    public void shouldThrowErrorJoiningRoomWhenJoiningWithWrongIP() throws IOException, DiscoveryException {
        String roomName = "Beergame";
        String leaderIP = "192.168.1.1";
        String hostIP = "192.168.1.1900";
        String password = "P@SSw0rd";
        String roomID = "12345";
        when(service.checkIfFolderNotExists(roomName)).thenReturn(false);
        when(service.getFolderID(roomName)).thenReturn(roomID);
        when(service.getLeaderFromFolder(roomID)).thenReturn(leaderIP);
        when(service.getAllhostsFromFolder(roomID)).thenReturn(new ArrayList<String>());
        when(service.getPasswordFromFolder(roomID)).thenReturn(password);

        roomFinder = new RoomFinder();
        roomFinder.joinGameRoom(roomName, hostIP, password);
    }

    @Test
    public void shouldReturnNullWhenJoiningRoomThatDoesNotExist() throws IOException, DiscoveryException {
        String roomName = "Beergame";
        String leaderIP = "192.168.1.1";
        String hostIP = "192.168.1.1900";
        String password = "P@SSw0rd";
        String roomID = "12345";
        when(service.checkIfFolderNotExists(roomName)).thenReturn(true);
        when(service.getFolderID(roomName)).thenReturn(roomID);
        when(service.getLeaderFromFolder(roomID)).thenReturn(leaderIP);
        when(service.getAllhostsFromFolder(roomID)).thenReturn(new ArrayList<String>());
        when(service.getPasswordFromFolder(roomID)).thenReturn(password);

        roomFinder = new RoomFinder();
        Room room = roomFinder.joinGameRoom(roomName, hostIP, password);
        assertNull(room);
    }

    @Test(expected = DiscoveryException.class)
    public void shouldThrowErrorIfCreatingRoomThatExists() throws IOException, DiscoveryException {
        String roomName = "Beergame";
        String hostIP = "192.168.1.100";
        String password = "P@SSw0rd";
        when(service.checkIfFolderNotExists(roomName)).thenReturn(false);

        roomFinder = new RoomFinder();
        roomFinder.createGameRoom(roomName, hostIP, password);
    }

    @Test(expected = DiscoveryException.class)
    public void shouldThrowErrorIfCreatingRoomWithNoConnection() throws IOException, DiscoveryException {
        String roomName = "Beergame";
        String hostIP = "192.168.1.100";
        String password = "P@SSw0rd";
        when(service.checkIfFolderNotExists(roomName)).thenReturn(true);
        when(service.createFolder(roomName)).thenThrow(IOException.class);

        roomFinder = new RoomFinder();
        roomFinder.createGameRoom(roomName, hostIP, password);
    }

    @Test(expected = DiscoveryException.class)
    public void shouldThrowErrorFindingRoomsWithNoConnection() throws IOException, DiscoveryException {
        when(service.getAllFolders()).thenThrow(IOException.class);
        roomFinder = new RoomFinder();
        roomFinder.getAvailableRooms();
    }
}
