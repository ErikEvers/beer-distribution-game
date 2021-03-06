package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.exceptions.PeerCantBeReachedException;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessageResponse;

class FaultResponder {
	private FaultDetectionClient faultDetectionClient;

	FaultResponder() {
		faultDetectionClient = new FaultDetectionClient();
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

		} catch (PeerCantBeReachedException e) {
			faultDetectionClient.sendFaultMessageResponse(new FaultMessageResponse(false, failingIp), senderIp);
		}
	}

	void setFaultDetectionClient(FaultDetectionClient faultDetectionClient) {
		this.faultDetectionClient = faultDetectionClient;
	}
}
