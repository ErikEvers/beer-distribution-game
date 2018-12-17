package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.FaultDetector;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultDetectionMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.CanYouReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessageResponse;
import org.han.ica.asd.c.faultdetection.messagetypes.ICanReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.PingMessage;


public class FaultDetectionMessageReceiver {
    private FaultDetector faultDetector;

    public FaultDetectionMessageReceiver(FaultDetector faultDetector) {
        this.faultDetector = faultDetector;
    }

    public void receiveMessage(FaultDetectionMessage faultDetectionMessage, String senderIp) {

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

                faultDetector.canYouReachLeaderMessageReceived((CanYouReachLeaderMessage) faultDetectionMessage, senderIp);

                break;

            case 5:

                faultDetector.iCanReachLeaderMessageReceived((ICanReachLeaderMessage) faultDetectionMessage);

                break;

            default:

                break;
        }

    }
}