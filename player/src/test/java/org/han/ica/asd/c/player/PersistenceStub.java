package org.han.ica.asd.c.player;

import org.han.ica.asd.c.interfaces.gamelogic.IRoundStore;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;

public class PersistenceStub implements IRoundStore {
    @Override
    public void saveRoundData(Round roundData) {

    }

    @Override
    public Round fetchRoundData(int roundId) {
        return null;
    }

    @Override
    public void saveTurnData(Round turn) {

    }

    @Override
    public Player getPlayerById(String playerId) {
        return null;
    }

    @Override
    public BeerGame getCurrentBeerGame() {
        return null;
    }
}
