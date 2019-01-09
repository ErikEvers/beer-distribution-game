package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.exceptions.NodeCantBeReachedException;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessageResponse;

import javax.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FaultResponder {
	@Inject private static Logger logger;

	@Inject
	private FaultDetectionClient faultDetectionClient;

	public FaultResponder() {
		//For inject purposes
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
			logger.log(Level.INFO, e.getMessage(), e);
			faultDetectionClient.sendFaultMessageResponse(new FaultMessageResponse(false, failingIp), senderIp);
		}
	}

	void setFaultDetectionClient(FaultDetectionClient faultDetectionClient) {
		this.faultDetectionClient = faultDetectionClient;
	}
}
