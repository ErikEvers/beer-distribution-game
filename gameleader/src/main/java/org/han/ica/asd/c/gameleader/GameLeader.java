package org.han.ica.asd.c.gameleader;

import org.han.ica.asd.c.interfaces.gameleader.IConnectorForLeader;
import org.han.ica.asd.c.interfaces.gameleader.ILeaderGameLogic;
import org.han.ica.asd.c.interfaces.gameleader.IPersistence;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.gamelogic.participants.domain_models.AgentParticipant;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.interfaces.communication.IPlayerDisconnectedObserver;
import org.han.ica.asd.c.interfaces.communication.IPlayerReconnectedObserver;
import org.han.ica.asd.c.interfaces.communication.ITurnModelObserver;

import javax.inject.Inject;
import javax.inject.Provider;

public class GameLeader implements ITurnModelObserver, IPlayerDisconnectedObserver, IPlayerReconnectedObserver {
    @Inject private IConnectorForLeader connectorForLeader;
    @Inject private ILeaderGameLogic gameLogic;
    @Inject private IPersistence persistence;
    @Inject private TurnHandler turnHandler;

    private final Provider<BeerGame> beerGameProvider;
    private final Provider<Round> roundProvider;

    private BeerGame game;
    private Round currentRoundData;

    private int turnsExpectedPerRound;
    private int turnsReceivedInCurrentRound;

    @Inject
    public GameLeader(Provider<BeerGame> beerGameProvider, Provider<Round> roundProvider) {
        this.beerGameProvider = beerGameProvider;
        this.roundProvider = roundProvider;
    }

    public void init() {
        connectorForLeader.addObserver(this);
        this.game = beerGameProvider.get();
        this.currentRoundData = roundProvider.get();
        this.turnsExpectedPerRound = game.getConfiguration().getFacilities().size();
    }

    /**
     * This method is called when the fault detection component detects that this machine is disconnected from all other players for whatever reason.
     * When this happens, an agent is added for each player except the local player.
     */
    @Override
    public void iAmDisconnected() {
        for (Player p : game.getPlayers()) {
            if (!checkIfPlayerIsLocalPlayer(p)) {
                playerIsDisconnected(p.getPlayerId());
            }
        }
    }

    /**
     * This method is called when a player disconnects, which this class is notified of by the IPlayerDisconnected interface.
     * Using this playerId an IParticipant object is created with the facility and gameAgentName corresponding to the playerId, which is sent to the Game Logic component.
     *
     * @param playerId the Id of the player that disconnected.
     */
    public void playerIsDisconnected(String playerId) {
        for (int i = 0; i <= game.getPlayers().size(); i++) {
            if (game.getPlayers().get(i).getPlayerId().equals(playerId)) {
                IParticipant participant = new AgentParticipant(game.getPlayers().get(i).getFacility().getAgent().getGameAgentName(), game.getPlayers().get(i).getFacility());
                gameLogic.addLocalParticipant(participant);
            }
        }
    }

    /**
     * This method is called when a player reconnects, which this class is notified of by the IPlayerReconnected interface (which is going to be implemented in a next sprint)
     *
     * @param playerId the Id of the player that reconnected.
     * @return game object which the connector sends to the reconnecting player to ensure he has the correct gamestate
     */
    public BeerGame notifyPlayerReconnected(String playerId) {
        gameLogic.removeAgentByPlayerId(playerId);
        return this.game;
    }

    /**
     * This method is called when a turn is received from the ITurnModelObserver.
     * The turn is processed by the TurnHandler and the amount of turns received this round is incremented.
     * <p>
     * Once all turns have been received, allTurnDataReceived is called.
     *
     * @param turnModel an incoming turn from a facility
     */
    public void turnModelReceived(Round turnModel) {
        currentRoundData = turnHandler.processFacilityTurn(turnModel, currentRoundData);
        turnsReceivedInCurrentRound++;

        if (turnsReceivedInCurrentRound == turnsExpectedPerRound)
            allTurnDataReceived();
    }

    /**
     * Uses the ILeaderGameLogic interface to calculate the round based on the supplied Round object.
     * Afterwards it sends the Round containing the calculated Round information to all players using the IConnectorForLeader interface.
     * Then it starts a new round.
     */
    private void allTurnDataReceived() {
        this.currentRoundData = gameLogic.calculateRound(this.currentRoundData);
        persistence.saveRoundData(this.currentRoundData);
        game.getRounds().add(this.currentRoundData);
        connectorForLeader.sendRoundDataToAllPlayers(currentRoundData);
        startNextRound();
    }

    /**
     * Starts a new round of the beer game.
     * Sets the amount of received turns from players to zero.
     * Creates a new Round for the beer game.
     */
    private void startNextRound() {
        currentRoundData = roundProvider.get();
        turnsReceivedInCurrentRound = 0;
    }

    /**
     * Checks if the incoming playerId is the same as the playerId of the game leader.
     * @param p supplied player object
     * @return true if the supplied player is the local player, false otherwise
     */
    private boolean checkIfPlayerIsLocalPlayer(Player p) {
        return game.getLeader().getPlayer().getPlayerId().equals(p.getPlayerId());
    }

    public int getTurnsReceivedInCurrentRound() {
        return turnsReceivedInCurrentRound;
    }
}
