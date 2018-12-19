package org.han.ica.asd.c.gameleader;

import org.han.ica.asd.c.gameleader.componentInterfaces.IConnectorForLeader;
import org.han.ica.asd.c.gameleader.componentInterfaces.ILeaderGameLogic;
import org.han.ica.asd.c.gamelogic.participants.IParticipant;
import org.han.ica.asd.c.gamelogic.participants.domain_models.AgentParticipant;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.observers.IPlayerDisconnectedObserver;
import org.han.ica.asd.c.observers.ITurnModelObserver;

import javax.inject.Inject;
import javax.inject.Provider;

public class GameLeader implements ITurnModelObserver, IPlayerDisconnectedObserver {
    @Inject
    private IConnectorForLeader connectorForLeader;
    @Inject
    private ILeaderGameLogic gameLogic;
    @Inject
    private TurnHandler turnHandler;

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
     * Checks if the incoming playerId is the same as the playerId of the game leader.
     * @param p supplied player object
     * @return true if the supplied player is the local player, false otherwise
     */
    private boolean checkIfPlayerIsLocalPlayer(Player p) {
        return game.getLeader().getPlayer().getPlayerId().equals(p.getPlayerId());
    }

    /**
     * This method is called when a player disconnects, which this class is notified of by the IPlayerDisconnected interface.
     * Using this playerId an IParticipant object is created with the facilityId corresponding to the playerId, which is sent to the Game Logic component.
     *
     * @param playerId the Id of the player that disconnected.
     */
    public void playerIsDisconnected(String playerId) {
        for (int i = 0; i <= game.getConfiguration().getFacilities().size(); i++) {
            if (game.getConfiguration().getFacilities().get(i).getPlayer().getPlayerId().equals(playerId)) {
                IParticipant participant = new AgentParticipant(game.getConfiguration().getFacilities().get(i).getAgent().getGameAgentName(), game.getConfiguration().getFacilities().get(i).getFacilityId());
                gameLogic.addLocalParticipant(participant);
            }
        }
    }

    /**
     * This method is called when a player reconnects, which this class is notified of by the IPlayerReconnected interface (which is going to be implemented in a next sprint)
     *
     * @param playerId the Id of the player that reconnected.
     */
    public void notifyPlayerReconnected(String playerId) {
        gameLogic.removeAgentByPlayerId(playerId);
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

}
