package org.han.ica.asd.c.faultdetection;


import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class TestFaultDetector {

    FaultDetector faultDetector;

    @Mock
    NodeInfoList nodeInfoList;

    @Mock
    ArrayList<IConnectorObserver> observers;

    @Mock
    FaultResponder faultResponder;

    @Mock
    FaultDetectorLeader faultDetectorLeader;

    @Mock
    FaultDetectorPlayer faultDetectorPlayer;

    @BeforeEach
    void setUp() {
        initMocks(this);

        faultDetector = spy(new FaultDetector());
        faultDetector.setObservers(observers);
    }

    @Test
    public void TestSetLeader() {
        doReturn(faultDetectorLeader)
                .when(faultDetector)
                .makeFaultDetectorLeader(nodeInfoList, observers);

        faultDetector.setLeader(nodeInfoList);
        assertNotNull(faultDetector.getFaultDetectorLeader());
        verify(faultDetectorLeader).start();
    }

    @Test
    public void TestSetPlayer() {
        doReturn(faultResponder)
                .when(faultDetector)
                .makeFaultResponder();

        doReturn(faultDetectorPlayer)
                .when(faultDetector)
                .makeFaultDetectorPlayer(nodeInfoList);

        faultDetector.setPlayer(nodeInfoList);
        assertNotNull(faultDetector.getFaultResponder());
        assertNotNull(faultDetector.getFaultDetectorPlayer());
    }

    @Test
    void TestFaultMessageReceived() {
        doReturn(faultResponder)
                .when(faultDetector)
                .makeFaultResponder();

        doReturn(faultDetectorPlayer)
                .when(faultDetector)
                .makeFaultDetectorPlayer(nodeInfoList);

        faultDetector.setPlayer(nodeInfoList);
        faultDetector.faultMessageReceived(any(), any());
        assertNotNull(faultDetector.getFaultResponder());
        verify(faultResponder).faultMessageReceived(any(), any());
    }

    @Test
    void TestFaultMessageResponseReceived() {
        doReturn(faultDetectorLeader)
                .when(faultDetector)
                .makeFaultDetectorLeader(nodeInfoList, observers);

        faultDetector.setLeader(nodeInfoList);
        faultDetector.faultMessageResponseReceived(any());
        assertNotNull(faultDetector.getFaultDetectorLeader());
        verify(faultDetectorLeader).faultMessageResponseReceived(any());
    }

    @Test
    void TestPingMessageReceived() {
        doReturn(faultDetectorPlayer)
                .when(faultDetector)
                .makeFaultDetectorPlayer(nodeInfoList);

        faultDetector.setPlayer(nodeInfoList);
        faultDetector.pingMessageReceived(any());
        assertNotNull(faultDetector.getFaultDetectorPlayer());
        verify(faultDetectorPlayer).pingMessageReceived(any());
    }

    @Test
    void TestCanYouReachLeaderMessageReceived() {
        doReturn(faultDetectorPlayer)
                .when(faultDetector)
                .makeFaultDetectorPlayer(nodeInfoList);

        faultDetector.setPlayer(nodeInfoList);
        faultDetector.canYouReachLeaderMessageReceived(any());
        assertNotNull(faultDetector.getFaultDetectorPlayer());
        verify(faultDetectorPlayer).canYouReachLeaderMessageReceived(any());
    }


    @Test
    void TestFaultDetectionMessageReceiver() {
        assertNull(faultDetector.getFaultDetectionMessageReceiver());
    }
}
