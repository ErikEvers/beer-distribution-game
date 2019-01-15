package org.han.ica.asd.c.interfaces.persistence;

import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;

public interface IGameStore {
    BeerGame getGameLog();
    void saveGameLog(BeerGame beerGame, boolean isStarted);
    void saveRoundData(Round roundData);
    Round fetchRoundData(int roundId);
    Player getPlayerById(String playerId);
    void updateEndGame();
}
