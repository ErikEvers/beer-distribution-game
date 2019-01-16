package org.han.ica.asd.c.faultdetection;


import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.model.domain_objects.Leader;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class TestFaultDetector {
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

    FaultDetector faultDetector;

    @BeforeEach
    void setUp() {
        initMocks(this);
        faultDetector = spy(new FaultDetector(mock(FaultDetectionMessageReceiver.class)));
        faultDetector.setObservers(observers);
    }

    @Test
    @DisplayName("Test if the start and stop methods are called correctly on FaultDetectorLeader")
    public void TestStartAndStopFaultDetectorLeader() {
        doReturn(faultDetectorLeader)
                .when(faultDetector)
                .makeFaultDetectorLeader(nodeInfoList, observers);

        faultDetector.startFaultDetectorLeader(nodeInfoList);
        verify(faultDetectorLeader).start();

        faultDetector.stopFaultDetectorLeader();
        verify(faultDetectorLeader).stop();
    }

    @Test
    @DisplayName("Test if the start and stop methods are called correctly on FaultDetectorPlayer")
    public void TestStartAndStopFaultDetectorPlayer() {
        doReturn(faultResponder)
                .when(faultDetector)
                .makeFaultResponder();

        doReturn(faultDetectorPlayer)
                .when(faultDetector)
                .makeFaultDetectorPlayer(nodeInfoList, observers);

        faultDetector.startFaultDetectorPlayer(nodeInfoList);
        verify(faultDetectorPlayer).start();
        verify(faultResponder).start();

        faultDetector.stopFaultDetectorPlayer();
        verify(faultDetectorPlayer).stop();
        verify(faultResponder).stop();
    }

    @Test
    @DisplayName("Test if faultMessageReceived is delegated correctly")
    void TestFaultMessageReceived() {
        doReturn(faultResponder)
                .when(faultDetector)
                .makeFaultResponder();

        doReturn(faultDetectorPlayer)
                .when(faultDetector)
                .makeFaultDetectorPlayer(nodeInfoList, observers);

        doReturn(true)
                .when(faultResponder)
                .isActive();

        faultDetector.startFaultDetectorPlayer(nodeInfoList);
        faultDetector.faultMessageReceived(any(), any());
        verify(faultResponder).faultMessageReceived(any(), any());
    }

    @Test
    @DisplayName("Test if faultMessageResponseReceived is delegated correctly")
    void TestFaultMessageResponseReceived() {
        doReturn(faultDetectorLeader)
                .when(faultDetector)
                .makeFaultDetectorLeader(nodeInfoList, observers);

        doReturn(true)
                .when(faultDetectorLeader)
                .isActive();

        faultDetector.startFaultDetectorLeader(nodeInfoList);
        faultDetector.faultMessageResponseReceived(any());
        verify(faultDetectorLeader).faultMessageResponseReceived(any());
    }

    @Test
    @DisplayName("Test if pingMessageReceived is delegated correctly")
    void TestPingMessageReceived() {
        doReturn(faultDetectorPlayer)
                .when(faultDetector)
                .makeFaultDetectorPlayer(nodeInfoList, observers);

        doReturn(faultResponder)
                .when(faultDetector)
                .makeFaultResponder();

        doReturn(true)
                .when(faultDetectorPlayer)
                .isActive();

        faultDetector.startFaultDetectorPlayer(nodeInfoList);
        faultDetector.pingMessageReceived(any());
        verify(faultDetectorPlayer).pingMessageReceived(any());
    }

    @Test
    @DisplayName("Test if canYouReachLeaderMessageReceived is delegated correctly")
    void TestCanYouReachLeaderMessageReceived() {
        doReturn(faultDetectorPlayer)
                .when(faultDetector)
                .makeFaultDetectorPlayer(nodeInfoList, observers);

        doReturn(faultResponder)
                .when(faultDetector)
                .makeFaultResponder();

        doReturn(true)
                .when(faultDetectorPlayer)
                .isActive();

        faultDetector.startFaultDetectorPlayer(nodeInfoList);
        faultDetector.canYouReachLeaderMessageReceived(any(), any());
        verify(faultDetectorPlayer).canYouReachLeaderMessageReceived(any(), any());
    }

    @Test
    @DisplayName("Test if makeFaultDetectorLeader sets the variables correctly")
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
    @DisplayName("Test if makeFaultDetectorPlayer sets the variables correctly")
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
