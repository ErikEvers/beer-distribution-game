package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.messagetypes.FaultDetectionMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.CanYouReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessageResponse;
import org.han.ica.asd.c.faultdetection.messagetypes.PingMessage;

import javax.inject.Inject;

/**
 * This class is used to determine which 'FaultDetectionMessage' is received and then which method is supposed to be
 * called with said message. This class is used by the 'SocketServer' and then
 *
 * @author Oscar, Tarik
 */
public class FaultDetectionMessageReceiver {
    @Inject private FaultDetector faultDetector;

    public FaultDetectionMessageReceiver() {
        //For inject purposes
    }

    /**
     * This method is called when a message is received. It uses the messageId from the FaultDetectionMessage classes
     * to determine which message is received.
     * This method is called by the SocketServer. And calls methods at the 'FaultDetector'.
     *
     * @param faultDetectionMessage   is the message object that is received.
     * @param senderIp This is the ipAddress of the node that sent the message.
     * @author Oscar Tarik
     * @see org.han.ica.asd.c.socketrpc.SocketServer
     * @see FaultDetector
     */
    public Object receiveMessage(FaultDetectionMessage faultDetectionMessage, String senderIp) {
        switch (faultDetectionMessage.getMessageId()) {
            case 1:
                faultDetector.faultMessageReceived((FaultMessage) faultDetectionMessage, senderIp);
                break;
            case 2:
                faultDetector.faultMessageResponseReceived((FaultMessageResponse) faultDetectionMessage);
                break;
            case 3:
                faultDetector.pingMessageReceived((PingMessage) faultDetectionMessage);
                break;
            case 4:
                return faultDetector.canYouReachLeaderMessageReceived((CanYouReachLeaderMessage) faultDetectionMessage, senderIp);
            default:
                break;
        }
        return null;
    }

    /**
     * Sets new faultDetector.
     *
     * @param faultDetector New value of faultDetector.
     */
    public void setFaultDetector(FaultDetector faultDetector) {
        this.faultDetector = faultDetector;
    }
}