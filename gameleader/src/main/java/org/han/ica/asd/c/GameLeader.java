package org.han.ica.asd.c;

import org.han.ica.asd.c.ComponentInterfaces.IGameLeader;

public class GameLeader implements IGameLeader {
    private TurnHandler turnHandler;

    @Override
    public void receiveFacilityTurn(FacilityTurnModel turnInformation) {
        turnHandler.processFacilityTurn(turnInformation);
    }

    @Override
    public void startGame(int port) {

    }

    @Override
    public void playerLeft(String PlayerId) {

    }


}
