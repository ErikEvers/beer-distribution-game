package org.han.ica.asd.c.interfaces.player;

import org.han.ica.asd.c.model.domain_objects.BeerGame;

public interface IPlayerRoundListener {
    void roundStarted();
    int getFacilityId();
    void startGame();
}
