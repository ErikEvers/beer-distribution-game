package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.exceptions.NodeCantBeReachedException;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessageResponse;

import javax.inject.Inject;

class FaultResponder {
	@Inject
	private FaultDetectionClient faultDetectionClient;

	FaultResponder() {
	}

	public void faultMessageReceived(FaultMessage faultMessage, String senderIp) {
		Thread t = createThread(faultMessage,senderIp);
		t.setDaemon(true);
		t.start();
	}

	public Thread createThread(FaultMessage faultMessage, String senderIp){
		return new Thread(()->sendResponse(faultMessage, senderIp));
	}

	public void sendResponse(FaultMessage faultMessage, String senderIp) {
		String failingIp = faultMessage.getIp();
		try {
			faultDetectionClient.makeConnection(failingIp);
			faultDetectionClient.sendFaultMessageResponse(new FaultMessageResponse(true, failingIp), senderIp);

		} catch (NodeCantBeReachedException e) {
			faultDetectionClient.sendFaultMessageResponse(new FaultMessageResponse(false, failingIp), senderIp);
		}
	}

	void setFaultDetectionClient(FaultDetectionClient faultDetectionClient) {
		this.faultDetectionClient = faultDetectionClient;
	}
}
