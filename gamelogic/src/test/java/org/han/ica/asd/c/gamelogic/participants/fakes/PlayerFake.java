package org.han.ica.asd.c.gamelogic.participants.fakes;

import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.interfaces.player.IPlayerRoundListener;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;

public class PlayerFake implements IPlayerRoundListener {

    @Override
    public void roundStarted() {

    }

    @Override
    public int getFacilityId() {
        return 0;
    }

    @Override
    public void startGame() {

    }

    @Override
    public void endGame(Round lastround) {

    }

    @Override
    public Player getPlayer() {
        return null;
    }

}
