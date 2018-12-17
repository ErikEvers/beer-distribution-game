package org.han.ica.asd.c.gameleader;

import com.google.common.annotations.VisibleForTesting;
import org.han.ica.asd.c.gameleader.componentInterfaces.IConnectorForLeader;
import org.han.ica.asd.c.gameleader.componentInterfaces.ILeaderGameLogic;
import org.han.ica.asd.c.model.BeerGame;
import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.Round;
import org.han.ica.asd.c.observers.ITurnModelObserver;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class GameLeader implements ITurnModelObserver {

    private IConnectorForLeader connectorForLeader;
    @Inject
    private ILeaderGameLogic gameLogic;

    private BeerGame game;
    private TurnHandler turnHandler;
    private Round currentRoundData;

    private int turnsExpected;
    private int turnsReceived;

    @Inject
    public GameLeader(BeerGame game, IConnectorForLeader connectorForLeader) {
        this.connectorForLeader = connectorForLeader;
        this.connectorForLeader.addObserver(this);
        this.turnHandler = new TurnHandler();
        this.currentRoundData = new Round(game.getGameId(), game.getRounds().size());
        this.turnsExpected = game.getConfiguration().getFacilities().size();
        this.turnsReceived = 0;
    }

    public void turnModelReceived(FacilityTurn turnModel) {
        turnHandler.processFacilityTurn(turnModel);
        currentRoundData.addTurn(turnModel);
        turnsReceived++;

        if(turnsReceived == turnsExpected)
            allTurnDataReceived();
    }

    private void allTurnDataReceived() {
        this.currentRoundData = gameLogic.calculateRound(this.currentRoundData);
        game.addRound(currentRoundData);
        connectorForLeader.sendRoundDataToAllPlayers(currentRoundData);
        startNextRound();
    }

    private void startNextRound() {
        currentRoundData = new Round(game.getGameId(), game.getRounds().size() + 1);
        turnsReceived = 0;
    }

}
