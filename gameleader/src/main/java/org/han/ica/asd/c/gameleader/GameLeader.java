package org.han.ica.asd.c.gameleader;

import com.google.common.annotations.VisibleForTesting;
import org.han.ica.asd.c.gameleader.componentInterfaces.IConnectorForLeader;
import org.han.ica.asd.c.gameleader.componentInterfaces.ILeaderGameLogic;
import org.han.ica.asd.c.model.BeerGame;
import org.han.ica.asd.c.model.Facility;
import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.Round;
import org.han.ica.asd.c.observers.ITurnModelObserver;
import org.han.ica.asd.c.gameleader.componentInterfaces.IConnectorForLeader;
import org.han.ica.asd.c.observers.IPlayerDisconnectedObserver;

import javax.inject.Inject;

public class GameLeader implements ITurnModelObserver, IPlayerDisconnectedObserver {
    @Inject
    private IConnectorForLeader connectorForLeader;
    @Inject
    private ILeaderGameLogic gameLogic;

    private BeerGame game;
    private TurnHandler turnHandler;
    private Round currentRoundData;

    private int turnsExpected;
    private int turnsReceived;

    public GameLeader(BeerGame game) {
        connectorForLeader.addObserver(this);
        this.turnHandler = new TurnHandler();
        this.currentRoundData = new Round(game.getGameId(), game.getRounds().size());
        this.turnsExpected = game.getConfiguration().getFacilities().size();
        this.turnsReceived = 0;
    }

    public void notifyPlayerDisconnected(String playerId) {
        IParticipant participant = new AgentParticipant(Integer.parseInt(playerId));
        gameLogic.addLocalParticipant(participant);
    }

    public void notifyPlayerReconnected(String playerId) {
        gameLogic.removeAgentByPlayerId(playerId);
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
