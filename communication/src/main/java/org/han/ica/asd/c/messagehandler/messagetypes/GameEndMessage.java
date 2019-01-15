package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.BeerGame;

import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.GAME_END_MESSAGE;

public class GameEndMessage extends TransactionMessage {
    private BeerGame beerGame;

    public GameEndMessage(BeerGame beerGame) {
        super(GAME_END_MESSAGE);
        this.beerGame = beerGame;
    }

    public BeerGame getBeerGame() {
        return beerGame;
    }

    @Override
    public void createResponseMessage(){

    }
}