package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.Round;

import java.io.Serializable;

public class TurnModelMessage extends GameMessage implements Serializable {
    private Round turnModel;
    private boolean isSuccess;

    public TurnModelMessage(Round roundData) {
        super(1);
        this.turnModel = roundData;
    }

    /**
     * Use this to create a serverResponse to a TurnModelMessage
     * If a TurnModelMessage is send via the sendObjectWithResponseGeneric method, it expects a response of the same object type.
     * This method is used to create that response.
     * @author Rogier
     */
    public void createResponseMessage(){
        this.isSuccess = true;
        turnModel = null;
    }

    /**
     * Use this to create a serverResponse to a TurnModelMessage
     * If a TurnModelMessage is send via the sendObjectWithResponseGeneric method, it expects a response of the same object type.
     * This method is used to create that response.
     * @author Rogier
     * @param exception
     */
    public void createResponseMessage(Exception exception){
        this.setException(exception);
        turnModel = null;
    }

    public Round getTurnModel() {
        return turnModel;
    }

    public boolean isSuccess(){
        return isSuccess;
    }
}
