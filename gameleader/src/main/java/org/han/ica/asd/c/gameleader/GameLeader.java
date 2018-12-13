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

    /**
     * creates a new Game Leader instance for a beer game.
     * @param game
     */
    public GameLeader(BeerGame game) {
        connectorForLeader.addObserver(this);
        this.turnHandler = new TurnHandler();
        this.currentRoundData = new Round(game.getGameId(), game.getRounds().size());
        this.turnsExpected = game.getConfiguration().getFacilities().size();
        this.turnsReceived = 0;
    }

    /**
     * This method is called when a player disconnects, which this class is notified of by the IPlayerDisconnected interface.
     * Using this playerId an IParticipant object is created with the facilityId corresponding to the playerId, which is sent to the Game Logic component.
     * @param playerId the Id of the player that disconnected.
     */
    public void notifyPlayerDisconnected(String playerId) {
        IParticipant participant = new AgentParticipant(Integer.parseInt(playerId));
        gameLogic.addLocalParticipant(participant);
    }

    /**
     * This method is called when a player reconnects, which this class is notified of by the IPlayerReconnected interface (which is going to be implemented in a next sprint)
     * @param playerId the Id of the player that reconnected.
     */
    public void notifyPlayerReconnected(String playerId) {
        gameLogic.removeAgentByPlayerId(playerId);
    }

    /**
     * This method is called when a turn is received from the ITurnModelObserver.
     * The turn is processed by the TurnHandler and the amount of turns received this round is incremented.
     *
     * Once all turns have been received, allTurnDataReceived is called.
     * @param turnModel an incoming turn from a facility
     */
    public void turnModelReceived(FacilityTurn turnModel) {
        turnHandler.processFacilityTurn(turnModel);
        currentRoundData.addTurn(turnModel);
        turnsReceived++;

        if(turnsReceived == turnsExpected)
            allTurnDataReceived();
    }

    /**
     * Uses the ILeaderGameLogic interface to calculate the round based on the supplied Round object.
     * Afterwards it sends the Round containing the calculated Round information to all players using the IConnectorForLeader interface.
     * Then it starts a new round.
     */
    private void allTurnDataReceived() {
        this.currentRoundData = gameLogic.calculateRound(this.currentRoundData);
        game.addRound(currentRoundData);
        connectorForLeader.sendRoundDataToAllPlayers(currentRoundData);
        startNextRound();
    }

    /**
     * Starts a new round of the beer game.
     * Sets the amount of received turns from players to zero.
     * Creates a new Round for the beer game, setting the roundId the last roundId plus one.
     */
    private void startNextRound() {
        currentRoundData = new Round(game.getGameId(), game.getRounds().size() + 1);
        turnsReceived = 0;
    }

}
