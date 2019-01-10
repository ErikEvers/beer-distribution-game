package org.han.ica.asd.c.interfaces.communication;

import org.han.ica.asd.c.model.domain_objects.BeerGame;

public interface IGameStartObserver extends IConnectorObserver {
    void gameStartReceived(BeerGame beerGame);
}
