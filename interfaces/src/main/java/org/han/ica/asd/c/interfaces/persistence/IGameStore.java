package org.han.ica.asd.c.interfaces.agent;

import org.han.ica.asd.c.model.domain_objects.BeerGame;

public interface IGameStore {
    BeerGame getGameLog(String gameId);
}
