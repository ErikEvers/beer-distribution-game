package org.han.ica.asd.c.faultdetection.messagetypes;

import org.han.ica.asd.c.faultdetection.FaultDetectionClient;
import org.han.ica.asd.c.faultdetection.FaultDetectionMessageReceiver;

/**
 * This message is send by the player to respond to the FaultMessage.
 * This message is used to respond with a boolean stating whether the connection
 * was successful or not and a String with the ip of the subject.
 *
 * @author Tarik
 *
 * @see FaultDetectionClient
 * @see FaultDetectionMessage
 * @see FaultDetectionMessageReceiver
 */
public class FaultMessageResponse extends FaultDetectionMessage {
    private Boolean isAlive;
    private String ipOfSubject;

    public FaultMessageResponse(Boolean isAlive, String ipOfSubject) {
        super(2);
        this.isAlive = isAlive;
        this.ipOfSubject = ipOfSubject;
    }

    public Boolean getAlive() {
        return isAlive;
    }

    public String getIpOfSubject() {
        return ipOfSubject;
    }

}