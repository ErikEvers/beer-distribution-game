package org.han.ica.asd.c.messagehandler.messagetypes;

import java.io.Serializable;
import java.util.UUID;

public abstract class GameMessage implements Serializable {
    protected int messageType;
    private UUID messageId;

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
}
