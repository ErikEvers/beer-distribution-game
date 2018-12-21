package org.han.ica.asd.c;

import org.han.ica.asd.c.discovery.DiscoveryException;
import org.han.ica.asd.c.discovery.RoomFinder;
import org.han.ica.asd.c.faultdetection.FaultDetector;
import org.han.ica.asd.c.messagehandler.sending.GameMessageClient;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
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

        @BeforeEach
        public void setUp() {
            initMocks(this);
            connector = new Connector(faultDetector, gameMessageClient, finder);
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
            connector.joinRoom("name","123.123.123.123","pw");
            verify(finder).joinGameRoom("name","123.123.123.123","pw");
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

        @Test
        public void sendConfigurationTest() {
            connector.sendConfiguration(new Configuration());
            verify(gameMessageClient).sendConfigurationToAllPlayers();
        }
}
