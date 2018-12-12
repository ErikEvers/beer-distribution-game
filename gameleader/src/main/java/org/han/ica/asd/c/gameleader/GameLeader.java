package org.han.ica.asd.c.gameleader;

import org.han.ica.asd.c.gameleader.componentInterfaces.IConnectorForLeader;
import org.han.ica.asd.c.model.BeerGame;
import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.Round;
import org.han.ica.asd.c.observers.ITurnModelObserver;

import javax.inject.Inject;

public class GameLeader implements ITurnModelObserver {
    @Inject
    private IConnectorForLeader connectorForLeader;

    private TurnHandler turnHandler;
    private Round currentRoundData;

    private int turnsExpected;
    private int turnsReceived;

    public GameLeader() {
        connectorForLeader.addObserver(this);
        this.turnHandler = new TurnHandler();
    }

    public void turnModelReceived(FacilityTurn turnModel) {
        turnHandler.processFacilityTurn(turnModel);
        currentRoundData.addTurn(turnModel);
        turnsReceived++;

        if(turnsReceived == turnsExpected) allTurnsReceived(currentRoundData);
    }
}
