package org.han.ica.asd.c.interfaces.persistence;

import org.han.ica.asd.c.model.domain_objects.BeerGame;

public interface IGameStore {
    BeerGame getGameLog();
    void saveGameLog(BeerGame beerGame);
}
