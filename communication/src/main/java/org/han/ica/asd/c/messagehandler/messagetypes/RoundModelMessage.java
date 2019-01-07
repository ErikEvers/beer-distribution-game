package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.Round;

import java.io.Serializable;

public class RoundModelMessage extends TransactionMessage implements Serializable{
    private Round roundModel;

    public RoundModelMessage(Round roundModel) {
        super(2);
        this.roundModel = roundModel;
    }

    public Round getRoundModel() {
        return roundModel;
    }

}
