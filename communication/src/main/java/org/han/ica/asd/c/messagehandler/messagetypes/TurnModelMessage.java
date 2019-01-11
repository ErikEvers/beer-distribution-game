package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.Round;

import java.io.Serializable;

import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.TURN_MODEL_MESSAGE;

public class TurnModelMessage extends GameMessage implements Serializable {
    private Round turnModel;

    public TurnModelMessage(Round roundData) {
        super(TURN_MODEL_MESSAGE);
        this.turnModel = roundData;
    }

    /**
     * Use this to create a serverResponse to a TurnModelMessage
     * If a TurnModelMessage is send via the sendObjectWithResponseGeneric method, it expects a response of the same object type.
     * This method is used to create that response.
     * @author Rogier
     */
    public void createResponseMessage(){
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
}
