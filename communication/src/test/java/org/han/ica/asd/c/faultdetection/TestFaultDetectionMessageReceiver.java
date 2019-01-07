package org.han.ica.asd.c.faultdetection;


import org.han.ica.asd.c.faultdetection.messagetypes.CanYouReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultDetectionMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessageResponse;
import org.han.ica.asd.c.faultdetection.messagetypes.PingMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
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
