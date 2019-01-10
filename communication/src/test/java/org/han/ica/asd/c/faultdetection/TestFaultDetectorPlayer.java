package org.han.ica.asd.c.faultdetection;


import org.han.ica.asd.c.faultdetection.messagetypes.CanYouReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.PingMessage;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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


class TestFaultDetectorPlayer {
    private FaultDetectorPlayer faultDetectorPlayer;
    private FaultHandlerPlayer faultHandlerPlayer;
    private FaultDetectionClient faultDetectionClient;

    private NodeInfoList nodeInfoList = mock(NodeInfoList.class);

    @BeforeEach
    void setUp() {
        faultHandlerPlayer = mock(FaultHandlerPlayer.class);
        faultDetectionClient = mock(FaultDetectionClient.class);

        faultDetectorPlayer = new FaultDetectorPlayer();
        faultDetectorPlayer.setNodeInfoList(nodeInfoList);

        faultDetectorPlayer.setFaultDetectionClient(faultDetectionClient);
        faultDetectorPlayer.setFaultHandlerPlayer(faultHandlerPlayer);
    }

    @Test
    void TestRun() {
        assertEquals(nodeInfoList, faultDetectorPlayer.getNodeInfoList());

        faultDetectorPlayer.setLastReceived(0);
        faultDetectorPlayer.setLeaderIsPinging(true);


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
