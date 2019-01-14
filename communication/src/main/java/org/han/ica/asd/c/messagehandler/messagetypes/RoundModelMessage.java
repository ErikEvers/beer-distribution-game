package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.Round;

import java.io.Serializable;

import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.ROUND_MESSAGE;

public class RoundModelMessage extends TransactionMessage implements Serializable{
    private Round previousRound;
    private Round newRound;

    public RoundModelMessage(Round previousRound, Round newRound) {
        super(ROUND_MESSAGE);
        this.previousRound = previousRound;
        this.newRound = newRound;
    }

    public Round getNewRound() {
        return newRound;
    }
    public Round getPreviousRound() {
        return previousRound;
    }
}
