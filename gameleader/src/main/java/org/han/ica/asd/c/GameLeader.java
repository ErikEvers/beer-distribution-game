package org.han.ica.asd.c;

import org.han.ica.asd.c.ComponentInterfaces.IGameLeader;
import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.Round;

public class GameLeader implements IGameLeader {
    public Round currentRoundData;
    private TurnHandler turnHandler;


    public void turnDataReceived(FacilityTurn turnInformation) {
        turnHandler.processFacilityTurn(turnInformation);
    }

    public void playerDisconnected(String playerId) {

    }

}
