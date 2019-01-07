package org.han.ica.asd.c.faultdetection;


import org.han.ica.asd.c.faultdetection.messagetypes.CanYouReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultDetectionMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessageResponse;
import org.han.ica.asd.c.faultdetection.messagetypes.PingMessage;
import org.junit.jupiter.api.BeforeEach;
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
	void testMessages(){
		FaultDetectionMessage realFaultDetectionMessage = new FaultMessage("test");

		assertEquals(1, realFaultDetectionMessage.getMessageId());
		assertEquals("test", ((FaultMessage) realFaultDetectionMessage).getIp());

		realFaultDetectionMessage = new PingMessage();
		assertEquals(3, realFaultDetectionMessage.getMessageId());

		realFaultDetectionMessage = new FaultMessageResponse(true, "test");
		assertEquals(2, realFaultDetectionMessage.getMessageId());
		assertTrue(((FaultMessageResponse) realFaultDetectionMessage).getAlive());
		assertEquals("test", ((FaultMessageResponse) realFaultDetectionMessage).getIpOfSubject());

		realFaultDetectionMessage = new CanYouReachLeaderMessage();
		((CanYouReachLeaderMessage) realFaultDetectionMessage).setLeaderState(true);
		assertEquals(4, realFaultDetectionMessage.getMessageId());
		assertTrue(((CanYouReachLeaderMessage) realFaultDetectionMessage).getLeaderState());

	}

	@Test
	void TestFaultMessageReceivedCase(){
		FaultDetectionMessage faultDetectionMessage = mock(FaultMessage.class);

		doReturn(1)
				.when(faultDetectionMessage)
				.getMessageId();

		faultDetectionMessageReceiver.receiveMessage(faultDetectionMessage,"senderIp");

		verify(faultDetector).faultMessageReceived(any(),any());
	}

	@Test
	void TestFaultMessageResponseReceivedCase(){
		FaultDetectionMessage faultDetectionMessage = mock(FaultMessageResponse.class);

		doReturn(2)
				.when(faultDetectionMessage)
				.getMessageId();

		faultDetectionMessageReceiver.receiveMessage(faultDetectionMessage,"senderIp");

		verify(faultDetector).faultMessageResponseReceived(any());
	}

	@Test
	void TestPingMessageReceivedCase(){
		FaultDetectionMessage faultDetectionMessage = mock(PingMessage.class);

		doReturn(3)
				.when(faultDetectionMessage)
				.getMessageId();

		faultDetectionMessageReceiver.receiveMessage(faultDetectionMessage,"senderIp");

		verify(faultDetector).pingMessageReceived(any());
	}

	@Test
	void TestCanYouReachLeaderMessageReceivedCase(){
		FaultDetectionMessage faultDetectionMessage = mock(CanYouReachLeaderMessage.class);

		doReturn(4)
				.when(faultDetectionMessage)
				.getMessageId();

		faultDetectionMessageReceiver.receiveMessage(faultDetectionMessage,"senderIp");

		verify(faultDetector).canYouReachLeaderMessageReceived(any(), eq("senderIp"));
	}

}
