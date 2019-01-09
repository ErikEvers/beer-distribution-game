package org.han.ica.asd.c.model.domain_objects;

public class GamePlayerId {
    private String playerId;
    private BeerGame beerGame;

    public GamePlayerId(BeerGame beerGame, String playerId) {
        this.beerGame = beerGame;
        this.playerId = playerId;
    }

    public BeerGame getBeerGame() {
        return beerGame;
    }

    public String getPlayerId() {
        return playerId;
    }
}
