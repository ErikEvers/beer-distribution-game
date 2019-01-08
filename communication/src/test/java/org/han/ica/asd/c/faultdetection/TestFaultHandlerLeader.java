package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TestFaultHandlerLeader {
	FaultHandlerLeader faultHandlerLeader;


	@Mock
	NodeInfoList nodeInfoList;

	ArrayList<IConnectorObserver> observers = spy(ArrayList.class);

	@BeforeEach
	void setUp(){
		initMocks(this);
		faultHandlerLeader = new FaultHandlerLeader();
		faultHandlerLeader.setNodeInfoList(nodeInfoList);
		faultHandlerLeader.setObservers(observers);
	}

	@Test
	void TestIncrementFailure() {

		when(nodeInfoList.size()).thenReturn(10);
		faultHandlerLeader.incrementFailure("1234");

		HashMap<String, Integer> amountOfFailsPerIp = faultHandlerLeader.getAmountOfFailsPerIp();

		int incrementedValue = amountOfFailsPerIp.get("1234");
		assertEquals(1,incrementedValue);

	}

	@Test
	void TestIsLeaderAliveAndIAmDisconnected() {
		assertFalse(faultHandlerLeader.isLeaderAlive());
		faultHandlerLeader.iAmDisconnected();
		assertTrue(faultHandlerLeader.isLeaderAlive());
	}

	@Test
	void TestIsPeerAlive() {

		faultHandlerLeader.isPeerAlive("");
		verify(nodeInfoList).getStatusOfOneNode("");
	}

	@Test
	void TestReset() {

		faultHandlerLeader.incrementFailure("1234");
		faultHandlerLeader.reset("1234");
		int actual = faultHandlerLeader.getAmountOfFailsPerIp().get("1234");
		assertEquals(0, actual);
	}

}
