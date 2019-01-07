package org.han.ica.asd.c.messagehandler.messagetypes;

public abstract class TransactionMessage extends GameMessage {
    private int phase;

    public TransactionMessage(int messageType) {
        super(messageType);
    }

    public void setPhaseToCommit() {
        phase = 1;
        refreshUUID();
    }

    public void setPhaseToStage() {
        phase = 0;
        refreshUUID();
    }

    public void setPhaseToRollback() {
        phase = -1;
        refreshUUID();
    }

    public int getPhase() {
        return phase;
    }
}
