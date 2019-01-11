package org.han.ica.asd.c.faultdetection.messagetypes;

import org.han.ica.asd.c.faultdetection.FaultDetectionClient;
import org.han.ica.asd.c.faultdetection.FaultDetectionMessageReceiver;

/**
 * The leader sends this message to all the nodes it can reach every 10 seconds.
 * This message is used in different ways. The leader uses it to check whether he is able to
 * connect with a certain ip. The player uses it to store the last received timestamp and
 * checks every x seconds whether the last received time is a long time ago.
 *
 * @author Tarik
 * @see FaultDetectionClient
 * @see FaultDetectionMessage
 * @see FaultDetectionMessageReceiver
 */
public class PingMessage extends FaultDetectionMessage {

    public PingMessage() {
        super(3);
    }
}