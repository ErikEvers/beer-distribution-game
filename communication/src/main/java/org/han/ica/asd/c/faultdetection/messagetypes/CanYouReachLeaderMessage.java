package org.han.ica.asd.c.faultdetection.messagetypes;

import org.han.ica.asd.c.faultdetection.FaultDetectionMessageReceiver;

/**
 * Is used for asking a different node if they have received a ping from the Leader within a certain interval.
 * It inherits the messageId from the super class 'FaultDetectionMessage'.<br>
 * This 'messageId' is then used at the 'FaultDetectionMessageReceiver' to determine which type of message was received.
 *
 * @author Tarik
 * @see org.han.ica.asd.c.faultdetection.messagetypes.FaultDetectionMessage
 * @see FaultDetectionMessageReceiver
 */
public class CanYouReachLeaderMessage extends FaultDetectionMessage {

    public CanYouReachLeaderMessage() {
        super(4);
    }
}

