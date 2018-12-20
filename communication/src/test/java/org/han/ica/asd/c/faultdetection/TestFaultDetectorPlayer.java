package org.han.ica.asd.c.faultdetection;


import org.han.ica.asd.c.faultdetection.messagetypes.CanYouReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.CanYouReachLeaderMessageResponse;
import org.han.ica.asd.c.faultdetection.messagetypes.PingMessage;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
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

        faultDetectorPlayer = new FaultDetectorPlayer(nodeInfoList);

        faultDetectorPlayer.setFaultDetectionClient(faultDetectionClient);
        faultDetectorPlayer.setFaultHandlerPlayer(faultHandlerPlayer);

    }

    @Test
    void TestRun() {
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

        Object result = faultDetectorPlayer.canYouReachLeaderMessageReceived(new CanYouReachLeaderMessage());

        assertTrue(result instanceof CanYouReachLeaderMessageResponse);

    }


}
