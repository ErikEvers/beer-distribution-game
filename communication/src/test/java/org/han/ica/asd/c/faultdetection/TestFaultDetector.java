package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.observers.IConnectorObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TestFaultDetector {
	FaultDetector faultDetector;
	@Mock
	NodeInfoList nodeInfoList = mock(NodeInfoList.class);
	ArrayList<IConnectorObserver> observers = mock(ArrayList.class);
	FaultMessage faultMessage = mock(FaultMessage.class);
	FaultResponder faultResponder = mock(FaultResponder.class);
	FaultDetectorLeader faultDetectorLeader = mock(FaultDetectorLeader.class);
	FaultDetectorPlayer faultDetectorPlayer = mock(FaultDetectorPlayer.class);

	@BeforeEach
	void setUp() {
		faultDetector = spy(new FaultDetector(observers));
	}

	@Test
	public void TestSetLeader() {
		doReturn( faultDetectorLeader )
				.when( faultDetector )
				.makeFaultDetectorLeader(nodeInfoList, observers);

		faultDetector.setLeader(nodeInfoList);
		assertNotNull(faultDetector.getFaultDetectorLeader());
		verify(faultDetectorLeader).start();

	}

	@Test
	public void TestSetPlayer() {
		faultDetector.setPlayer(nodeInfoList);
		assertNotNull(faultDetector.getFaultResponder());
		assertNotNull(faultDetector.getFaultDetectorPlayer());
	}

	@Test
	void TestFaultMessageReceived() {

		doReturn( faultResponder )
				.when( faultDetector )
				.makeFaultResponder();

		faultDetector.setPlayer(nodeInfoList);
		faultDetector.faultMessageReceived(any(), any());
		assertNotNull(faultDetector.getFaultResponder());
		verify(faultResponder).faultMessageReceived(any(),any());

	}

	@Test
	void TestFaultMessageResponseReceived() {

		doReturn( faultDetectorLeader )
				.when( faultDetector )
				.makeFaultDetectorLeader(nodeInfoList, observers);

		faultDetector.setLeader(nodeInfoList);
		faultDetector.faultMessageResponseReceived(any());
		assertNotNull(faultDetector.getFaultDetectorLeader());
		verify(faultDetectorLeader).faultMessageResponseReceived(any());

	}

	@Test
	void TestPingMessageReceived() {

		doReturn( faultDetectorPlayer )
				.when( faultDetector )
				.makeFaultDetectorPlayer(nodeInfoList);

		faultDetector.setPlayer(nodeInfoList);
		faultDetector.pingMessageReceived(any());
		assertNotNull(faultDetector.getFaultDetectorPlayer());
		verify(faultDetectorPlayer).pingMessageReceived(any());

	}

	@Test
	void TestCanYouReachLeaderMessageReceived() {

		doReturn( faultDetectorPlayer )
				.when( faultDetector )
				.makeFaultDetectorPlayer(nodeInfoList);

		faultDetector.setPlayer(nodeInfoList);
		faultDetector.canYouReachLeaderMessageReceived(any());
		assertNotNull(faultDetector.getFaultDetectorPlayer());
		verify(faultDetectorPlayer).canYouReachLeaderMessageReceived(any());

	}

	@Test
	void TestFaultDetectionMessageReceiver() {
		assertNotNull(faultDetector.getFaultDetectionMessageReceiver());
	}

}
