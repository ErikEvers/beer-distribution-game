package org.han.ica.asd.c.interfaces.player;

import org.han.ica.asd.c.model.domain_objects.Round;

public interface IPlayerRoundListener {
    void roundStarted();
    int getFacilityId();
    void startGame();
    void endGame(Round lastround);
}
