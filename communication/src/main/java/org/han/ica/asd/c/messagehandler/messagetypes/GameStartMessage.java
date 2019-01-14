package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.BeerGame;

import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.GAME_START_MESSAGE;

public class GameStartMessage extends TransactionMessage {
    private BeerGame beerGame;

    public GameStartMessage(BeerGame beerGame) {
        super(GAME_START_MESSAGE);
        this.beerGame = beerGame;
    }

    public BeerGame getBeerGame() {
        return beerGame;
    }
}
