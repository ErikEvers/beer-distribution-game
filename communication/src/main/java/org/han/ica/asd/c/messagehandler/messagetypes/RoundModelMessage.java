package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.Round;

import java.io.Serializable;

import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.ROUND_MESSAGE;

public class RoundModelMessage extends TransactionMessage implements Serializable{
    private Round roundModel;

    public RoundModelMessage(Round roundModel) {
        super(ROUND_MESSAGE);
        this.roundModel = roundModel;
    }

    public Round getRoundModel() {
        return roundModel;
    }
}
