package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.Round;

import java.io.Serializable;

public class RoundModelMessage extends TransactionMessage implements Serializable{
    private Round roundModel;

    private static final int ROUND_MESSAGE = 2;

    public RoundModelMessage(Round roundModel) {
        super(ROUND_MESSAGE);
        this.roundModel = roundModel;
    }

    public Round getRoundModel() {
        return roundModel;
    }

}
