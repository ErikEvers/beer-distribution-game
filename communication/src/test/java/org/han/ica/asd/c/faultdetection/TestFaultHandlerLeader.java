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
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class TestFaultHandlerLeader {
    FaultHandlerLeader faultHandlerLeader;

    @Mock
    NodeInfoList nodeInfoList;

    @Mock
    Logger logger;

    ArrayList<IConnectorObserver> observers = new ArrayList<>();

    @BeforeEach
    void setUp() {
        initMocks(this);

        faultHandlerLeader = new FaultHandlerLeader();
        faultHandlerLeader.setNodeInfoList(nodeInfoList);
        faultHandlerLeader.setObservers(observers);
        faultHandlerLeader.setLogger(logger);
    }

    @Test
    void TestIncrementFailureHappyFlow() {
        List<IConnectorObserver> observersMock = new ArrayList<>();

        doNothing().when(nodeInfoList).updateIsConnected("1234", false);
        faultHandlerLeader.setObservers(observersMock);

        faultHandlerLeader.incrementFailure("1234");
        verify(nodeInfoList).updateIsConnected("1234", false);

        HashMap<String, Integer> amountOfFailsPerIp = faultHandlerLeader.getAmountOfFailsPerIp();

        int incrementedValue = amountOfFailsPerIp.get("1234");
        assertEquals(1, incrementedValue);
    }

    @Test
    void TestIncrementFailureUnHappyFlow() {
        HashMap<String, Integer> listMock = new HashMap<>();
        listMock.put("Cool", -1);
        faultHandlerLeader.setAmountOfFailsPerIp(listMock);
        assertNull(faultHandlerLeader.incrementFailure("Cool"));
    }

    @Test
    void TestIsLeaderAliveAndIAmDisconnected() {
        assertFalse(faultHandlerLeader.isLeaderAlive());
        faultHandlerLeader.iAmDisconnected();
        assertTrue(faultHandlerLeader.isLeaderAlive());
    }

    @Test
    void TestReset() {
        faultHandlerLeader.incrementFailure("1234");
        faultHandlerLeader.reset("1234");
        int actual = faultHandlerLeader.getAmountOfFailsPerIp().get("1234");
        assertEquals(0, actual);
    }
}
