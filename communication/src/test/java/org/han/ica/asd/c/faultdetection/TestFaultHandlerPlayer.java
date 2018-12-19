package org.han.ica.asd.c.faultdetection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestFaultHandlerPlayer {
	FaultHandlerPlayer faultHandlerPlayer;

	@BeforeEach
	void setUp(){
		faultHandlerPlayer = spy(FaultHandlerPlayer.class);
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

		faultHandlerPlayer.resetAmountOfConnectionsWithLeader();
		faultHandlerPlayer.setAmountOfActiveIps(5);
		faultHandlerPlayer.setAmountOfFailingIps(5);

		assertEquals("imDead",faultHandlerPlayer.whoIsDead());
	}
	@Test
	void TestShouldReturnThatLeaderIsDead() {

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

