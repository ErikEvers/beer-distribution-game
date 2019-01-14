package org.han.ica.asd.c.player;

import org.han.ica.asd.c.interfaces.persistence.IGameStore;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;
import org.han.ica.asd.c.model.domain_objects.Round;

public class PersistenceStub implements IGameStore {
    @Override
    public BeerGame getGameLog() {
        return null;
    }

    @Override
    public void saveGameLog(BeerGame beerGame) {

    }

    @Override
    public void saveRoundData(Round roundData) {

    }

    @Override
    public Round fetchRoundData(int roundId) {
        return null;
    }

    @Override
    public Player getPlayerById(String playerId) {
        return null;
    }

    @Override
    public void saveSelectedAgent(ProgrammedAgent agent) {

    }
}
