package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TestFaultResponder {
    FaultResponder faultResponder;


    @BeforeEach
    void setUp() {
        faultResponder = spy(FaultResponder.class);
    }


    @Test
    void TestSendResponseToPingableIp() {
        FaultDetectionClient faultDetectionClient = mock(FaultDetectionClient.class);

        faultResponder.setFaultDetectionClient(faultDetectionClient);

        faultResponder.sendResponse(new FaultMessage("127.0.0.1"), "");

        verify(faultDetectionClient).sendFaultMessageResponse(any(), eq(""));

    }

    @Test
    void TestSendResponseToUnpingableIp() {
        FaultDetectionClient faultDetectionClient = spy(FaultDetectionClient.class);

        faultResponder.setFaultDetectionClient(faultDetectionClient);

        faultResponder.sendResponse(new FaultMessage("192.187.23.21"), "");

        verify(faultDetectionClient).sendFaultMessageResponse(any(), eq(""));

    }

    @Test
    @DisplayName("Test if the faultMessageReceived starts a thread")
    void TestFaultmessageReceived(){
        FaultMessage faultMessage = mock(FaultMessage.class);
        String test = "test";

        Thread t = mock(Thread.class);

        when(faultResponder.createThread(faultMessage, test)).thenReturn(t);

        faultResponder.faultMessageReceived(faultMessage, test);

        verify(faultResponder , times(1)).createThread(faultMessage,test);
        verify(t).start();
    }

}
