package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.Round;

import java.io.Serializable;

public class TurnModelMessage extends GameMessage implements Serializable {

    public Round getTurnModel() {
        return turnModel;
    }

    private Round turnModel;
    private boolean isSuccess;
    public TurnModelMessage(Round roundData) {
        super(1);
        this.turnModel = roundData;
    }

    public void setIsSuccess(boolean value){
        isSuccess = value;
    }

    public boolean isSuccess(){
        return isSuccess;
    }
}
