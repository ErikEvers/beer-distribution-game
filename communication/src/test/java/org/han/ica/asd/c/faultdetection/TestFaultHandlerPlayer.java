package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class TestFaultHandlerPlayer {
	FaultHandlerPlayer faultHandlerPlayer;
	private NodeInfoList nodeInfoList = mock(NodeInfoList.class);

	@Mock
	ArrayList<IConnectorObserver> observers;

	@BeforeEach
	void setUp()
	{
		initMocks(this);

		faultHandlerPlayer = spy(FaultHandlerPlayer.class);
		faultHandlerPlayer.setObservers(new ArrayList<>());
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

