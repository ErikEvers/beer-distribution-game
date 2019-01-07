package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.discovery.DiscoveryException;
import org.han.ica.asd.c.discovery.Room;
import org.han.ica.asd.c.discovery.RoomFinder;
import org.han.ica.asd.c.faultdetection.FailLog;
import org.han.ica.asd.c.faultdetection.FaultDetectionClient;
import org.han.ica.asd.c.faultdetection.FaultDetector;
import org.han.ica.asd.c.faultdetection.FaultDetectorLeader;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfo;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.messagehandler.sending.GameMessageClient;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.socketrpc.IServerObserver;
import org.han.ica.asd.c.socketrpc.SocketClient;
import org.han.ica.asd.c.socketrpc.SocketServer;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
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
        SocketServer socketServer;

        @Mock
        NodeInfoList nodeInfoList;

        @BeforeEach
        public void setUp() {
            initMocks(this);

            Injector injector = Guice.createInjector(new AbstractModule() {
                @Override
                protected void configure() {
                    //CommunicationBinds
                    bind(IServerObserver.class).annotatedWith(Names.named("MessageDirector")).to(MessageDirector.class);

                    //communication
                    requestStaticInjection(SocketClient.class);
                    requestStaticInjection(SocketServer.class);

                    //FaultDetector
                    requestStaticInjection(FailLog.class);
                    requestStaticInjection(FaultDetectionClient.class);
                    requestStaticInjection(FaultDetectorLeader.class);
                    requestStaticInjection(Connector.class);
                }
            });

            connector = new Connector(faultDetector, gameMessageClient, finder, socketServer);
            connector.setNodeInfoList(nodeInfoList);

        }


        @Test
        public void getAvailableRoomsTest() throws DiscoveryException {
            connector.getAvailableRooms();
            verify(finder).getAvailableRooms();
        }

        @Test
        public void createRoomTest() throws DiscoveryException {
            connector.createRoom("name","123.123.123.123","pw");
            verify(finder).createGameRoom("name","123.123.123.123","pw");
        }

        @Test
        public void joinRoomTest() throws DiscoveryException {
            Room room = mock(Room.class);

            when(finder.joinGameRoom("name","123.123.123.123","pw")).thenReturn(room);
            when(room.getLeaderIP()).thenReturn("testIP");

            connector.joinRoom("name","123.123.123.123","pw");
            verify(finder).joinGameRoom("name","123.123.123.123","pw");

            verify(nodeInfoList, times(1)).add(any(NodeInfo.class));
        }

        @Test
        public void sendTurnTest(){
            connector.sendTurn(new Round());
            verify(gameMessageClient).sendTurnModel(anyString(),any(Round.class));
        }

        @Test
        public void sendRoundToAllTest(){
            connector.updateAllPeers(new Round());
            verify(gameMessageClient).sendRoundToAllPlayers(any(String[].class),any(Round.class));
        }
}
