package org.han.ica.asd.c.faultdetection.messagetypes;

import org.han.ica.asd.c.faultdetection.FaultDetectionMessageReceiver;

import java.io.Serializable;

/**
 * <h2> This is the super class of all the FaultDetectionMessages. It is used to make sure every message has a
 * messageId attribute</h2>
 * <p>Every FaultDetectionMessage extends this class to make sure they have a messageId attribute.<br>
 * This 'messageId' is then used at the 'FaultDetectionMessageReceiver' to determine which type of message was
 * received.</p>
 *
 * @author Rogier
 * @see org.han.ica.asd.c.faultdetection.messagetypes.FaultDetectionMessage
 * @see FaultDetectionMessageReceiver
 */

public abstract class FaultDetectionMessage implements Serializable {
    protected int messageId;

    protected FaultDetectionMessage(int messageId) {
        this.messageId = messageId;
    }

    public int getMessageId() {
        return messageId;
    }
}