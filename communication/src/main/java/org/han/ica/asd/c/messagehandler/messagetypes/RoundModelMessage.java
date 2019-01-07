package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.Round;

import java.io.Serializable;

public class RoundModelMessage extends TransactionMessage implements Serializable{
    private Round roundModel;
    private boolean isSuccess;

    public RoundModelMessage(Round roundModel) {
        super(2);
        this.roundModel = roundModel;
    }

    public Round getRoundModel() {
        return roundModel;
    }

    public boolean IsSuccess() { return isSuccess; }

    public void setIsSuccess(boolean isSuccess){
        this.isSuccess = isSuccess;
    }
}
