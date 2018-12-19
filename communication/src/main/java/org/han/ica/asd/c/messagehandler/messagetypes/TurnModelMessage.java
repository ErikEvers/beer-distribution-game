package org.han.ica.asd.c.messagehandler.messagetypes;

import domainobjects.TurnModel;

import java.io.Serializable;

public class TurnModelMessage extends GameMessage implements Serializable {

    public TurnModel getTurnModel() {
        return turnModel;
    }

    private TurnModel turnModel;

    public TurnModelMessage(TurnModel roundData) {
        super(1);
        this.turnModel = roundData;
    }
}
