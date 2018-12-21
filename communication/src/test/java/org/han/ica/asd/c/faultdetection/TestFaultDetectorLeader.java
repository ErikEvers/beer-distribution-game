package org.han.ica.asd.c.faultdetection;


import org.han.ica.asd.c.faultdetection.exceptions.NodeCantBeReachedException;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessageResponse;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class TestFaultDetectorLeader {
    private FaultDetectorLeader faultDetectorLeader;

    NodeInfoList nodeInfoList;
    FaultDetectionClient faultDetectionClient;
    FaultHandlerLeader faultHandlerLeader;
    FailLog failLog;
    ArrayList<IConnectorObserver> observers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        failLog = mock(FailLog.class);
        faultHandlerLeader = mock(FaultHandlerLeader.class);
        faultDetectionClient = mock(FaultDetectionClient.class);
        nodeInfoList = mock(NodeInfoList.class);
        observers = mock(ArrayList.class);

        faultDetectorLeader = new FaultDetectorLeader();
        faultDetectorLeader.setNodeInfoList(nodeInfoList);
        faultDetectorLeader.setObservers(observers);
    }

    @Test
    @DisplayName("Tests if the Timer is used and the method scheduleAtAFixedRate has been called")
    void TestStartMethodCallsTimerMethod() {

        Timer toTest = mock(Timer.class);

        FaultDetectorLeader faultDetectorLeader = new FaultDetectorLeader() {

            public Timer createTimer(Boolean isDeamon) {
                return toTest;
            }
        };

        faultDetectorLeader.setObservers(observers);
        faultDetectorLeader.setNodeInfoList(nodeInfoList);


        faultDetectorLeader.start();
        verify(toTest, times(1)).scheduleAtFixedRate(faultDetectorLeader, 0, 10000);

    }

    @Test
    @DisplayName("Test if run is calling the right methods, and sets the status of isconnected to true if the machine is reached")
    void testRun() {

        FaultDetectionClient faultDetectionClientMock = new FaultDetectionClient() {
            public void makeConnection(String ipAddress) {
                //doNothing
                //For this test it isnt required to throw an exception so its easier to overwrite it.
            }
        };

        faultDetectorLeader = new FaultDetectorLeader() {
            @Override
            public void sendFaultMessagesToActivePlayers(List<String> ips) {
                //do nothing
            }

            @Override
            public void checkIfThisMachineIsDisconnected() {
                //do nothing
            }
        };

        faultDetectorLeader.setNodeInfoList(nodeInfoList);
        faultDetectorLeader.setObservers(observers);

        faultDetectorLeader.setFailLog(failLog);
        faultDetectorLeader.setFaultDetectionClient(faultDetectionClientMock);
        faultDetectorLeader.setFaultHandlerLeader(faultHandlerLeader);

        String testIp1 = "TestIp1";
        String testIp2 = "TestIp2";

        List<String> activeIpsMock = new ArrayList<>();
        activeIpsMock.add(testIp1);
        activeIpsMock.add(testIp2);

        when(nodeInfoList.getActiveIps()).thenReturn(activeIpsMock);

        doNothing().when(failLog).reset(any(String.class));
        doNothing().when(faultHandlerLeader).reset(any(String.class));
        doNothing().when(nodeInfoList).updateIsConnected(any(String.class), any(boolean.class));


        faultDetectorLeader.run();

        verify(failLog, times(1)).reset(testIp1);
        verify(failLog, times(1)).reset(testIp2);
        verify(faultHandlerLeader, times(1)).reset(testIp1);
        verify(faultHandlerLeader, times(1)).reset(testIp2);
        verify(nodeInfoList, times(1)).updateIsConnected(testIp1, true);
        verify(nodeInfoList, times(1)).updateIsConnected(testIp2, true);
    }

    @Test
    @DisplayName("Test if makeConnection handles the peerCantReachedException")
    void testRunThrowsException() {

        FaultDetectionClient faultDetectionClientMock = new FaultDetectionClient() {
            public void makeConnection(String ipAddress) throws NodeCantBeReachedException {
                throw new NodeCantBeReachedException();
                //doNothing
                //For this test it is required to throw an exception so its easier to overwrite it.
            }
        };

        faultDetectorLeader = new FaultDetectorLeader() {
            @Override
            public void sendFaultMessagesToActivePlayers(List<String> ips) {
                //do nothing
            }

            @Override
            public void checkIfThisMachineIsDisconnected() {
                //do nothing
            }
        };

        faultDetectorLeader.setObservers(observers);
        faultDetectorLeader.setNodeInfoList(nodeInfoList);

        faultDetectorLeader.setFailLog(failLog);
        faultDetectorLeader.setFaultDetectionClient(faultDetectionClientMock);
        faultDetectorLeader.setFaultHandlerLeader(faultHandlerLeader);

        String testIp1 = "TestIp1";
        String testIp2 = "TestIp2";

        List<String> activeIpsMock = new ArrayList<>();
        activeIpsMock.add(testIp1);
        activeIpsMock.add(testIp2);


        when(nodeInfoList.getActiveIps()).thenReturn(activeIpsMock);
        when(failLog.checkIfIpIsFailed(testIp1)).thenReturn(false);
        when(failLog.checkIfIpIsFailed(testIp2)).thenReturn(true);

        faultDetectorLeader.run();

        verify(failLog, times(1)).increment(testIp1);
        verify(failLog, times(0)).increment(testIp2);

        verify(failLog, times(0)).reset(any(String.class));
        verify(faultHandlerLeader, times(0)).reset(any(String.class));
        verify(nodeInfoList, times(0)).updateIsConnected(any(String.class), any(boolean.class));
    }

    @Test
    @DisplayName("Test if the SendfaultmessagesToActivePlayers only sends to active players, And actually calls the sendfaultmessage method")
    void TestSendFaultMessageToActivePlayersWith1ActivePlayerAnd1InactivePlayer() {

        faultDetectorLeader.setFailLog(failLog);
        faultDetectorLeader.setFaultDetectionClient(faultDetectionClient);
        faultDetectorLeader.setFaultHandlerLeader(faultHandlerLeader);

        String testIp1 = "TestIp1";
        String testIp2 = "TestIp2";

        List<String> activeIpsMock = new ArrayList<>();
        activeIpsMock.add(testIp1);
        activeIpsMock.add(testIp2);

        faultDetectorLeader.setIps(activeIpsMock);

        when(failLog.checkIfIpIsFailed(testIp1)).thenReturn(true);
        when(failLog.checkIfIpIsFailed(testIp2)).thenReturn(false);

        when(failLog.isAlive(testIp1)).thenReturn(true);
        when(failLog.isAlive(testIp2)).thenReturn(false);

        doNothing().when(faultDetectionClient).sendFaultMessage(any(FaultMessage.class), any(String.class));

        faultDetectorLeader.sendFaultMessagesToActivePlayers(activeIpsMock);

        verify(faultDetectionClient, times(1)).sendFaultMessage(any(FaultMessage.class), any(String.class));

    }

    @Test
    @DisplayName("Test if faulMessageReponseReceived doesnt call the incrementfailure method when isalive = true")
    void TestFaultMessageResponseReceiver() {

        String testIp1 = "TestIp1";

        faultDetectorLeader.setFaultHandlerLeader(faultHandlerLeader);

        FaultMessageResponse faultMessageResponse = mock(FaultMessageResponse.class);

        when(faultMessageResponse.getAlive()).thenReturn(true);
        when(faultMessageResponse.getIpOfSubject()).thenReturn(testIp1);

        faultDetectorLeader.faultMessageResponseReceived(faultMessageResponse);

        verify(faultMessageResponse, times(1)).getAlive();
        verify(faultMessageResponse, times(1)).getIpOfSubject();
        verify(faultHandlerLeader, times(0)).incrementFailure(testIp1);

    }

    @Test
    @DisplayName("Test if faulMessageReponseReceived calls the incrementfailure method when isalive = false")
    void TestFaultMessageResponseReceiverCallsIncrementFailure() {

        String testIp1 = "TestIp1";

        faultDetectorLeader.setFaultHandlerLeader(faultHandlerLeader);

        FaultMessageResponse faultMessageResponse = mock(FaultMessageResponse.class);

        when(faultMessageResponse.getAlive()).thenReturn(false);
        when(faultMessageResponse.getIpOfSubject()).thenReturn(testIp1);

        faultDetectorLeader.faultMessageResponseReceived(faultMessageResponse);

        verify(faultMessageResponse, times(1)).getAlive();
        verify(faultMessageResponse, times(1)).getIpOfSubject();
        verify(faultHandlerLeader, times(1)).incrementFailure(testIp1);

    }

    @Test
    @DisplayName("Test the checkIfThisMachineIsDisconnected method works")
    void TestCheckIfThisMachineIsDisconnected(){
        Timer timer = mock(Timer.class);

        faultDetectorLeader.setTimer(timer);
        faultDetectorLeader.setFaultHandlerLeader(faultHandlerLeader);

        when(failLog.getSuccessSize()).thenReturn(0);

        faultDetectorLeader.checkIfThisMachineIsDisconnected();

        verify(faultHandlerLeader).iAmDisconnected();
        verify(timer).cancel();
        verify(timer).purge();
    }


}



