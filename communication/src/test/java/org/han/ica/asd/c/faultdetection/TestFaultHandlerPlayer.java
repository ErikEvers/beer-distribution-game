package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TestFaultHandlerPlayer {
	private FaultHandlerPlayer faultHandlerPlayer;
	private NodeInfoList nodeInfoList = mock(NodeInfoList.class);

	@Mock
	ArrayList<IConnectorObserver> observers;

	@Mock
	Logger logger;

	@BeforeEach
	void setUp() {
		initMocks(this);

		faultHandlerPlayer = spy(FaultHandlerPlayer.class);
		faultHandlerPlayer.setObservers(new ArrayList<>());
		faultHandlerPlayer.setLogger(logger);
	}

	@Test
	void TestWhoIsDead(){
		faultHandlerPlayer.setAmountOfFailingIps(10);
		faultHandlerPlayer.setAmountOfActiveIps(4);
		faultHandlerPlayer.setFilteredAmount(2);

		faultHandlerPlayer.incrementAmountOfConnectionsWithLeader();

		String result = faultHandlerPlayer.whoIsDead();

		assertEquals("leaderIsNotCompletelyDead", result);
	}

	@Test
	void TestReset(){
		faultHandlerPlayer.reset();
		verify(faultHandlerPlayer).resetAmountOfFailingIps();
		verify(faultHandlerPlayer).resetAmountOfConnectionsWithLeader();
	}

	@Test
	void TestResetAmountOfConnectionsWithLeader(){

		faultHandlerPlayer.resetAmountOfConnectionsWithLeader();
		assertEquals(0, faultHandlerPlayer.getAmountOfConnectionsWithLeader());
	}

	@Test
	void TestIncrementAmountOfConnectionsWithLeader(){

		faultHandlerPlayer.resetAmountOfConnectionsWithLeader();
		faultHandlerPlayer.incrementAmountOfConnectionsWithLeader();
		assertEquals(1, faultHandlerPlayer.getAmountOfConnectionsWithLeader());
	}

	@Test
	void TestResetAmountOfFailingIps(){

		faultHandlerPlayer.resetAmountOfFailingIps();
		assertEquals(0, faultHandlerPlayer.getAmountOfFailingIps());
	}

	@Test
	void TestIncrementAmountOfFailingIps() {

		faultHandlerPlayer.resetAmountOfFailingIps();
		faultHandlerPlayer.incrementAmountOfFailingIps();
		assertEquals(1, faultHandlerPlayer.getAmountOfFailingIps());
	}

	@Test
	void TestShouldReturnThatImDead() {
		faultHandlerPlayer.setFilteredAmount(0);
		faultHandlerPlayer.resetAmountOfConnectionsWithLeader();
		faultHandlerPlayer.setAmountOfActiveIps(5);
		faultHandlerPlayer.setAmountOfFailingIps(5);

        assertEquals("imDead",faultHandlerPlayer.whoIsDead());
	}

	@Test
	void TestShouldReturnThatLeaderIsDead() {
		faultHandlerPlayer.setFilteredAmount(0);
		faultHandlerPlayer.resetAmountOfConnectionsWithLeader();
		faultHandlerPlayer.setAmountOfActiveIps(5);
		faultHandlerPlayer.resetAmountOfFailingIps();

		assertEquals("leaderIsDead",faultHandlerPlayer.whoIsDead());
	}

	@Test
	void TestSetAmountOfActiveIps() {
		faultHandlerPlayer.setAmountOfActiveIps(10);

		assertEquals(10, faultHandlerPlayer.getAmountOfActiveIps());
	}
}

