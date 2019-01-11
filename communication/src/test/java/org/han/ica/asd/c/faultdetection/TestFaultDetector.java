package org.han.ica.asd.c.faultdetection;


import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
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

        faultDetector.startFaultDetectorLeader(nodeInfoList);
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
                .makeFaultDetectorPlayer(nodeInfoList, observers);

        faultDetector.startFaultDetectorPlayer(nodeInfoList);
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
                .makeFaultDetectorPlayer(nodeInfoList, observers);

        faultDetector.startFaultDetectorPlayer(nodeInfoList);
        faultDetector.faultMessageReceived(any(), any());
        assertNotNull(faultDetector.getFaultResponder());
        verify(faultResponder).faultMessageReceived(any(), any());
    }

    @Test
    void TestFaultMessageResponseReceived() {
        doReturn(faultDetectorLeader)
                .when(faultDetector)
                .makeFaultDetectorLeader(nodeInfoList, observers);

        faultDetector.startFaultDetectorLeader(nodeInfoList);
        faultDetector.faultMessageResponseReceived(any());
        assertNotNull(faultDetector.getFaultDetectorLeader());
        verify(faultDetectorLeader).faultMessageResponseReceived(any());
    }

    @Test
    void TestPingMessageReceived() {
        doReturn(faultDetectorPlayer)
                .when(faultDetector)
                .makeFaultDetectorPlayer(nodeInfoList, observers);

        faultDetector.startFaultDetectorPlayer(nodeInfoList);
        faultDetector.pingMessageReceived(any());
        assertNotNull(faultDetector.getFaultDetectorPlayer());
        verify(faultDetectorPlayer).pingMessageReceived(any());
    }

    @Test
    void TestCanYouReachLeaderMessageReceived() {
        doReturn(faultDetectorPlayer)
                .when(faultDetector)
                .makeFaultDetectorPlayer(nodeInfoList, observers);

        faultDetector.startFaultDetectorPlayer(nodeInfoList);
        faultDetector.canYouReachLeaderMessageReceived(any(), any());
        assertNotNull(faultDetector.getFaultDetectorPlayer());
        verify(faultDetectorPlayer).canYouReachLeaderMessageReceived(any(), any());
    }

    @Test
    void TestFaultDetectionMessageReceiver() {
        assertNull(faultDetector.getFaultDetectionMessageReceiver());
    }

    @Test
    void TestMakeFaultDetectorLeader(){
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                requestStaticInjection(FailLog.class);
                requestStaticInjection(FaultDetectorLeader.class);
            }
        });
        faultDetector = injector.getInstance(FaultDetector.class);
        faultDetectorLeader = injector.getInstance(FaultDetectorLeader.class);

        faultDetector.setFaultDetectorLeader(faultDetectorLeader);

        FaultDetectorLeader result = faultDetector.makeFaultDetectorLeader(nodeInfoList, observers);

        assertEquals(observers, result.getObservers());
        assertEquals(faultDetectorLeader, result);
    }

    @Test
    void testMakeFaultDetectorPlayer(){

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                requestStaticInjection(FailLog.class);
                requestStaticInjection(FaultDetectorPlayer.class);
            }
        });
        faultDetector = injector.getInstance(FaultDetector.class);
        faultDetectorPlayer = injector.getInstance(FaultDetectorPlayer.class);

        FaultDetectorPlayer result = faultDetector.makeFaultDetectorPlayer(nodeInfoList, observers);

        assertEquals(nodeInfoList, result.getNodeInfoList());
    }
}
