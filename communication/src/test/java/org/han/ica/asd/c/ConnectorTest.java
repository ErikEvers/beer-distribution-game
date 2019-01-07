package org.han.ica.asd.c;

import org.han.ica.asd.c.discovery.DiscoveryException;
import org.han.ica.asd.c.discovery.IResourceManager;
import org.han.ica.asd.c.discovery.Room;
import org.han.ica.asd.c.discovery.RoomException;
import org.han.ica.asd.c.discovery.RoomFinder;
import org.han.ica.asd.c.faultdetection.FaultDetector;
import org.han.ica.asd.c.faultdetection.exceptions.NodeCantBeReachedException;
import org.han.ica.asd.c.messagehandler.sending.GameMessageClient;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class ConnectorTest {

    Connector connector;

    @Mock
    GameMessageClient gameMessageClient;

    @Mock
    FaultDetector faultDetector;

    @Mock
    RoomFinder finder;

    @Mock
    IResourceManager service;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        connector = new Connector(faultDetector, gameMessageClient, finder);
    }

    @Test
    public void getAvailableRoomsTest() throws DiscoveryException {
        List<String> returnedRooms = new ArrayList<>();
        returnedRooms.add("Beergame 1");
        when(finder.getAvailableRooms()).thenReturn(returnedRooms);

        assertEquals(connector.getAvailableRooms(), returnedRooms);
        verify(finder).getAvailableRooms();
    }

    @Test
    public void createRoomTest() throws DiscoveryException, RoomException {
//        String roomName = "Beergame 1";
//        String leaderIP = "192.168.0.1";
//        String password = "P@SSw0rd";
//
//        Room room = new Room(roomName, leaderIP, password, service);
//
//        when(finder.createGameRoom(roomName, leaderIP, password)).thenReturn(room);
//        Room created = connector.createRoom(roomName, leaderIP, password);
//
//        verify(finder).createGameRoom(roomName, leaderIP, password);
//        assertEquals(created.getLeaderIP(), leaderIP);
//        assertEquals(created.getRoomName(), roomName);
//        assertEquals(created.getPassword(), password);
    }

    @Test
    public void createRoomTestShouldThrowErrorIfFailed() throws DiscoveryException {
//        String roomName = "Beergame 1";
//        String leaderIP = "192.168.0.1";
//        String password = "P@SSw0rd";
//
//        when(finder.createGameRoom(anyString(), anyString(), anyString())).thenThrow(DiscoveryException.class);
//
//        assertNull(connector.createRoom(roomName, leaderIP, password));
    }

    @Test
    public void joinRoomTest() throws DiscoveryException, RoomException, NodeCantBeReachedException {
        //Doet nu nog niks
        String roomName = "Beergame 1";
        String leaderIP = "192.168.0.1";
        String password = "P@SSw0rd";
        String hostIP = "192.168.0.10";

        Room room = new Room(roomName, leaderIP, password, service);
    }

    public void sendTurnTest() {
        connector.sendTurn(new Round());
        verify(gameMessageClient).sendTurnModel(anyString(), any(Round.class));
    }

    public void sendRoundToAllTest() {
        connector.updateAllPeers(new Round());
        verify(gameMessageClient).sendRoundToAllPlayers(any(String[].class), any(Round.class));
    }


}
