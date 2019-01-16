package org.han.ica.asd.c.faultdetection;


import org.han.ica.asd.c.faultdetection.messagetypes.CanYouReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultDetectionMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessageResponse;
import org.han.ica.asd.c.faultdetection.messagetypes.PingMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class TestFaultDetectionMessageReceiver {

	private FaultDetector faultDetector = mock(FaultDetector.class);
	private FaultDetectionMessageReceiver faultDetectionMessageReceiver;

	@BeforeEach
	void setUp(){
		faultDetectionMessageReceiver = new FaultDetectionMessageReceiver();
		faultDetectionMessageReceiver.setFaultDetector(faultDetector);
	}

	@Test
	@DisplayName("Test if FaultMessage getters are working correctly")
	void testFaultMessage(){
		FaultMessage faultMessage = new FaultMessage("test");

		assertEquals(1, faultMessage.getMessageId());
		assertEquals("test", faultMessage.getIp());
	}

	@Test
	@DisplayName("Test if PingMessage getters are working correctly")
	void testPingMessage(){
		PingMessage pingMessage = new PingMessage();
		assertEquals(3, pingMessage.getMessageId());
	}

	@Test
	@DisplayName("Test if FaultMessageResponse getters are working correctly")
	void testFaultMessageResponse(){
		FaultMessageResponse realFaultDetectionMessage = new FaultMessageResponse(true, "test");
		assertEquals(2, realFaultDetectionMessage.getMessageId());
		assertTrue(realFaultDetectionMessage.getAlive());
		assertEquals("test", realFaultDetectionMessage.getIpOfSubject());
	}

	@Test
	@DisplayName("Test if CanYouReachLeaderMessage getters/setters are working correctly")
	void testCanYouReachLeaderMessage(){
		CanYouReachLeaderMessage reachLeaderMessage = new CanYouReachLeaderMessage();
		reachLeaderMessage.setLeaderState(true);
		assertEquals(4, reachLeaderMessage.getMessageId());
		assertTrue(reachLeaderMessage.getLeaderState());
	}

	@Test
	@DisplayName("Test if receiveMessage delegates the FaultMessage to the correct method")
	void TestFaultMessageReceivedCase(){
		FaultDetectionMessage faultDetectionMessage = mock(FaultMessage.class);

		doReturn(1)
				.when(faultDetectionMessage)
				.getMessageId();

		faultDetectionMessageReceiver.receiveMessage(faultDetectionMessage,"senderIp");

		verify(faultDetector).faultMessageReceived(any(),any());
	}

	@Test
	@DisplayName("Test if receiveMessage delegates the FaultMessageResponse to the correct method")
	void TestFaultMessageResponseReceivedCase(){
		FaultDetectionMessage faultDetectionMessage = mock(FaultMessageResponse.class);

		doReturn(2)
				.when(faultDetectionMessage)
				.getMessageId();

		faultDetectionMessageReceiver.receiveMessage(faultDetectionMessage,"senderIp");

		verify(faultDetector).faultMessageResponseReceived(any());
	}

	@Test
	@DisplayName("Test if receiveMessage delegates the PingMessage to the correct method")
	void TestPingMessageReceivedCase(){
		FaultDetectionMessage faultDetectionMessage = mock(PingMessage.class);

		doReturn(3)
				.when(faultDetectionMessage)
				.getMessageId();

		faultDetectionMessageReceiver.receiveMessage(faultDetectionMessage,"senderIp");

		verify(faultDetector).pingMessageReceived(any());
	}

	@Test
	@DisplayName("Test if receiveMessage delegates the CanYouReachLeaderMessage to the correct method")
	void TestCanYouReachLeaderMessageReceivedCase(){
		FaultDetectionMessage faultDetectionMessage = mock(CanYouReachLeaderMessage.class);

		doReturn(4)
				.when(faultDetectionMessage)
				.getMessageId();

		faultDetectionMessageReceiver.receiveMessage(faultDetectionMessage,"senderIp");

		verify(faultDetector).canYouReachLeaderMessageReceived(any(), eq("senderIp"));
	}

}
