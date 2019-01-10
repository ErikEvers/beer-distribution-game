package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.interfaces.communication.IFinder;
import org.han.ica.asd.c.discovery.IResourceManager;
import org.han.ica.asd.c.discovery.Room;
import org.han.ica.asd.c.discovery.RoomFinder;
import org.han.ica.asd.c.exceptions.communication.DiscoveryException;
import org.han.ica.asd.c.exceptions.communication.RoomException;
import org.han.ica.asd.c.faultdetection.FailLog;
import org.han.ica.asd.c.faultdetection.FaultDetectionClient;
import org.han.ica.asd.c.faultdetection.FaultDetector;
import org.han.ica.asd.c.faultdetection.FaultDetectorLeader;
import org.han.ica.asd.c.faultdetection.exceptions.NodeCantBeReachedException;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.interfaces.persistence.IGameStore;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.messagehandler.sending.GameMessageClient;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.socketrpc.IServerObserver;
import org.han.ica.asd.c.socketrpc.SocketClient;
import org.han.ica.asd.c.socketrpc.SocketServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
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
    SocketServer socketServer;

    @Mock
    NodeInfoList nodeInfoList;

    @Mock
    IResourceManager service;


    @BeforeEach
    public void setUp() {
        initMocks(this);

//        Injector injector = Guice.createInjector(new AbstractModule() {
//            @Override
//            protected void configure() {
//                //CommunicationBinds
//                bind(IServerObserver.class).annotatedWith(Names.named("MessageDirector")).to(MessageDirector.class);
//                bind(IFinder.class).to(RoomFinder.class);
//
//                //communication
//                requestStaticInjection(SocketClient.class);
//                requestStaticInjection(SocketServer.class);
//
//                //FaultDetector

//                requestStaticInjection(FailLog.class);
//                requestStaticInjection(FaultDetectorLeader.class);
//                requestStaticInjection(Connector.class);
//                requestStaticInjection(FaultDetectionClient.class);
//            }
//        });


        connector = new Connector(faultDetector, gameMessageClient, finder, socketServer);
        connector.setNodeInfoList(nodeInfoList);
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
        doNothing().when(gameMessageClient).sendRoundToAllPlayers(any(), any());
        connector.sendRoundDataToAllPlayers(new Round());
        verify(gameMessageClient).sendRoundToAllPlayers(any(String[].class), any(Round.class));
    }

    @Test
    public void sendConfigucationToAllTest() {
        connector.sendGameStart(new BeerGame());
        verify(gameMessageClient).sendStartGameToAllPlayers(any(String[].class), any(BeerGame.class));
    }
}
