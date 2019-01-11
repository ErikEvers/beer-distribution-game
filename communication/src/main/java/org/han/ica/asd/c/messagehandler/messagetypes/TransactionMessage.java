package org.han.ica.asd.c.messagehandler.messagetypes;

public abstract class TransactionMessage extends GameMessage {
    private int phase;
    private boolean isSuccess;

    public TransactionMessage(int messageType) {
        super(messageType);
    }

    /**
     * Sets the phase to 1, the commit phase during a transaction.
     */
    public void setPhaseToCommit() {
        phase = 1;
        refreshUUID();
    }

    /**
     * Sets the phase to 0, the stage phase during a transaction.
     */
    public void setPhaseToStage() {
        phase = 0;
        refreshUUID();
    }

    /**
     * Sets the phase to -1, the rollback phase during a transaction.
     */
    public void setPhaseToRollback() {
        phase = -1;
        refreshUUID();
    }

    public int getPhase() {
        return phase;
    }

    public void createResponseMessage() {
        isSuccess = true;
    }

    /**
     * Gets isSuccess.
     *
     * @return Value of isSuccess.
     */
    public boolean getIsSuccess() {
        return isSuccess;
    }
}
