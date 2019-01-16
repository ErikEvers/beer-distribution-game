package org.han.ica.asd.c.faultdetection;


import org.han.ica.asd.c.faultdetection.messagetypes.CanYouReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.PingMessage;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.verification.VerificationModeFactory.times;


class TestFaultDetectorPlayer {
    private FaultDetectorPlayer faultDetectorPlayer;
    private FaultHandlerPlayer faultHandlerPlayer;
    private FaultDetectionClient faultDetectionClient;
    private NodeInfoList nodeInfoList = mock(NodeInfoList.class);

    @Mock
    ArrayList<IConnectorObserver> observers;

    @BeforeEach
    void setUp() {
        initMocks(this);

        faultHandlerPlayer = mock(FaultHandlerPlayer.class);
        faultDetectionClient = mock(FaultDetectionClient.class);

        faultDetectorPlayer = new FaultDetectorPlayer();
        faultDetectorPlayer.setNodeInfoList(nodeInfoList);

        faultDetectorPlayer.setFaultDetectionClient(faultDetectionClient);
        faultDetectorPlayer.setFaultHandlerPlayer(faultHandlerPlayer);
        }

    @Test
    @DisplayName("Tests if the Timer is used and the method scheduleAtAFixedRate has been called and cancelled when stopped")
    void TestStartAndStopMethodCallsTimerMethod() {
        Timer toTest = mock(Timer.class);
        FaultDetectorPlayer faultDetectorPlayer = new FaultDetectorPlayer() {

            public Timer createTimer(Boolean isDeamon) {
                return toTest;
            }
        };

        faultDetectorPlayer.setObservers(observers);
        faultDetectorPlayer.setNodeInfoList(nodeInfoList);
        faultDetectorPlayer.setFaultHandlerPlayer(faultHandlerPlayer);

        faultDetectorPlayer.start();
        verify(toTest, times(1)).scheduleAtFixedRate(faultDetectorPlayer, 0, 10000);
        assertTrue(faultDetectorPlayer.isActive());

        faultDetectorPlayer.stop();
        assertFalse(faultDetectorPlayer.isActive());
        verify(toTest, times(1)).cancel();
    }

    @Test
    void TestRun() {
        assertEquals(nodeInfoList, faultDetectorPlayer.getNodeInfoList());

        faultDetectorPlayer.setLastReceived(0);
        faultDetectorPlayer.setLeaderIsPinging(true);
        faultDetectorPlayer.setPlayersWhoAlreadyCouldntReachLeader(new HashMap<>());

        faultDetectorPlayer.run();

        verify(faultHandlerPlayer).reset();
        verify(faultHandlerPlayer).whoIsDead();

        assertFalse(faultDetectorPlayer.getLeaderIsPinging());
    }

    @Test
    void TestPingMessageReceived() {
        faultDetectorPlayer.setLastReceived(0);
        faultDetectorPlayer.pingMessageReceived(new PingMessage());

        assertNotEquals(0, faultDetectorPlayer.getLastReceived());
    }

    @Test
    void TestLeaderIsNotPingingReturnsTrueWhenBigTimeDifference() {
        faultDetectorPlayer.setLastReceived(0);
        faultDetectorPlayer.leaderIsNotPinging();
        assertTrue(faultDetectorPlayer.leaderIsNotPinging());
    }

    @Test
    void TestLeaderIsNotPingingReturnsFalseWhenSmallTimeDifference() {
        faultDetectorPlayer.setLastReceived(System.currentTimeMillis());
        faultDetectorPlayer.leaderIsNotPinging();
        assertFalse(faultDetectorPlayer.leaderIsNotPinging());
    }

    @Test
    void TestCanYouReachLeaderMessageReceived() {
        HashMap<String, Long> mock = spy(HashMap.class);
        faultDetectorPlayer.setPlayersWhoAlreadyCouldntReachLeader(mock);

        Object result = faultDetectorPlayer.canYouReachLeaderMessageReceived(new CanYouReachLeaderMessage(), "ip");

        verify(mock).put(eq("ip"),any());
        assertTrue(mock.containsKey("ip"));
        assertTrue(result instanceof CanYouReachLeaderMessage);
    }

    @Test
    void TestStart(){
        assertNull(faultDetectorPlayer.getTimer());
        faultDetectorPlayer.start();
        assertNotNull(faultDetectorPlayer.getTimer());
    }
}
