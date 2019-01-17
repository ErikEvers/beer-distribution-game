package org.han.ica.asd.c.agent.stubs;

import org.han.ica.asd.c.interfaces.gamelogic.IPlayerGameLogic;
import org.han.ica.asd.c.interfaces.player.IPlayerRoundListener;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.Round;

public class GameLogicStub implements IPlayerGameLogic {
    @Override
    public void submitTurn(Round round) {

    }

    @Override
    public BeerGame getBeerGame() {
        return null;
    }

    @Override
    public void letAgentTakeOverPlayer(GameAgent agent) {

    }

    @Override
    public void letPlayerTakeOverAgent() {

    }

    @Override
    public int getRoundId() {
        return 0;
    }

    @Override
    public void setPlayer(IPlayerRoundListener player) {

    }

    @Override
    public void setLastTurn(Round lastround) {

    }
}
