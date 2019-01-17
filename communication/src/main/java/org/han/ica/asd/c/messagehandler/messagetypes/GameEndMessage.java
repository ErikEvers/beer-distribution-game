package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Round;

import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.GAME_END_MESSAGE;

public class GameEndMessage extends TransactionMessage {
    private BeerGame beerGame;
    private Round previousRound;

    public GameEndMessage(Round previousRound) {
        super(GAME_END_MESSAGE);
       this.previousRound = previousRound;
    }

    public Round getPreviousRound() {
        return previousRound;
    }

    @Override
    public void createResponseMessage(){

    }
}