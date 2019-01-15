package org.han.ica.asd.c.interfaces.player;

public interface IPlayerRoundListener {
    void roundStarted();
    int getFacilityId();
    void startGame();
    void endGame();
}
