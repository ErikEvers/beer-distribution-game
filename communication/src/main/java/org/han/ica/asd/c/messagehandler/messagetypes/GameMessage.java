package org.han.ica.asd.c.messagehandler.messagetypes;

import java.io.Serializable;
import java.util.UUID;

public abstract class GameMessage implements Serializable {
    protected int messageType;
    private UUID messageId;
    private Exception exception;

    protected GameMessage(int messageType) {
        this.messageId = UUID.randomUUID();
        this.messageType = messageType;
    }

    public int getMessageType() {
        return messageType;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    /**
     * Sets the messageId variable to a new random UUID.
     */
    protected void refreshUUID() {
        messageId = UUID.randomUUID();
    }

    /**
     * Gets exception.
     *
     * @return Value of exception.
     */
    public Exception getException() {
        return exception;
    }

    /**
     * Sets new exception.
     *
     * @param exception New value of exception.
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }
}
