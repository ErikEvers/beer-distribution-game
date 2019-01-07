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

    public static TurnModelMessage createResponseMessage(boolean isSuccess){
        TurnModelMessage turnModelMessage = new TurnModelMessage(null);
        turnModelMessage.isSuccess = isSuccess;
        return turnModelMessage;
    }

    public static TurnModelMessage createResponseMessage(Exception exception){
        TurnModelMessage turnModelMessage = new TurnModelMessage(null);
        turnModelMessage.setException(exception);
        return turnModelMessage;
    }

    public boolean isSuccess(){
        return isSuccess;
    }
}
