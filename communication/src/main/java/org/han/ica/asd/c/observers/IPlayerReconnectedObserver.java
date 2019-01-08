package org.han.ica.asd.c.observers;

import org.han.ica.asd.c.model.domain_objects.BeerGame;

public interface IPlayerReconnectedObserver {
    BeerGame notifyPlayerReconnected(String playerId);
}
