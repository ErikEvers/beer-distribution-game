package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.observers.IConnectorObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestFaultHandlerLeader {
	FaultHandlerLeader faultHandlerLeader;


	@Mock
	NodeInfoList nodeInfoList = mock(NodeInfoList.class);
	ArrayList<IConnectorObserver> observers = spy(ArrayList.class);

	@BeforeEach
	void setUp(){
		faultHandlerLeader = new FaultHandlerLeader(nodeInfoList, observers);
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
	void TestShouldReturnDeadIpWhenIncrementedTooMuch() {

		when(nodeInfoList.size()).thenReturn(10);

		for (int i = 1; i < 10/2; i++) {
			assertNull(faultHandlerLeader.incrementFailure("1234"));

		}

		assertEquals("1234",faultHandlerLeader.incrementFailure("1234"));

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
