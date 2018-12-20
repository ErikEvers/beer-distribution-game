package org.han.ica.asd.c.messagehandler.messagetypes;

import java.io.Serializable;
import java.util.UUID;

public abstract class GameMessage implements Serializable {
    protected int messageType;
    private UUID messageId;
    private Object response;

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
     * Gets response.
     *
     * @return Value of response.
     */
    public Object getResponse() {
        return response;
    }

    /**
     * Sets new response.
     *
     * @param response New value of response.
     */
    public void setResponse(Object response) {
        this.response = response;
    }
}
