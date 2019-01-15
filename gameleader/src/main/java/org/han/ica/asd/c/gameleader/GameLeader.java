package org.han.ica.asd.c.gameleader;

import com.sun.org.apache.xpath.internal.operations.Equals;
import org.han.ica.asd.c.agent.Agent;
import org.han.ica.asd.c.exceptions.gameleader.BeerGameException;
import org.han.ica.asd.c.exceptions.communication.TransactionException;
import org.han.ica.asd.c.exceptions.gameleader.FacilityNotAvailableException;
import org.han.ica.asd.c.interfaces.communication.IFacilityMessageObserver;
import org.han.ica.asd.c.interfaces.gameleader.IConnectorForLeader;
import org.han.ica.asd.c.interfaces.gameleader.IGameLeader;
import org.han.ica.asd.c.interfaces.gameleader.ILeaderGameLogic;
import org.han.ica.asd.c.interfaces.gameleader.IPersistence;
import org.han.ica.asd.c.interfaces.communication.IPlayerDisconnectedObserver;
import org.han.ica.asd.c.interfaces.communication.IPlayerReconnectedObserver;
import org.han.ica.asd.c.interfaces.communication.ITurnModelObserver;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.GamePlayerId;
import org.han.ica.asd.c.model.domain_objects.Leader;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.RoomModel;
import org.han.ica.asd.c.model.domain_objects.Round;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameLeader implements IGameLeader, ITurnModelObserver, IPlayerDisconnectedObserver, IPlayerReconnectedObserver, IFacilityMessageObserver {
    @Inject
    private IConnectorForLeader connectorForLeader;
    @Inject
    private ILeaderGameLogic gameLogic;
    @Inject
    private IPersistence persistence;
    @Inject
    private TurnHandler turnHandler;
    @Inject
    @Named("PlayerComponent")
    private IPlayerComponent playerComponent;
    @Inject
    private static Logger logger; //NOSONAR

    private final Provider<BeerGame> beerGameProvider;
    private final Provider<Round> roundProvider;
    private final Provider<Player> playerProvider;

    private static RoomModel roomModel;
    private static BeerGame game;

    private Round previousRoundData;
    private Round currentRoundData;

    private int highestPlayerId = 0;
    private int turnsExpectedPerRound;
    private int turnsReceivedInCurrentRound = 0;
    private int roundId = 1;

    @Inject
    public GameLeader(Provider<BeerGame> beerGameProvider, Provider<Round> roundProvider, Provider<Player> playerProvider) {
        this.beerGameProvider = beerGameProvider;
        this.roundProvider = roundProvider;
        this.playerProvider = playerProvider;
    }

    /**
     * Sets up initial variables of this class and adds the instance as an observer for incoming messages.
     */
    public void init(String leaderIp, RoomModel roomModel, BeerGame beerGame) {
        connectorForLeader.addObserver(this);

        game = beerGame;
        GameLeader.roomModel = roomModel;

        Player player = new Player("0", leaderIp, null, "Yarno", true);
        game.getPlayers().add(player);
        game.setLeader(new Leader(player));
        playerComponent.setPlayer(player);

        this.currentRoundData = game.getRounds().get(0);
        this.currentRoundData.setRoundId(roundId);
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
        for (int i = 0; i < game.getPlayers().size(); i++) {
            if (game.getPlayers().get(i).getPlayerId().equals(playerId)) {
                Agent agent = getAgentByFacility(game.getPlayers().get(i).getFacility().getFacilityId());
                if (agent != null) {
                    gameLogic.addLocalParticipant(agent);
                    return;
                }
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
        return game;
    }

    @Override
    public void chooseFacility(Facility facility, String playerId) throws FacilityNotAvailableException {
        Optional<Player> connectingPlayerO = game.getPlayers().stream().filter(player -> player.getPlayerId().equals(playerId)).findFirst();
        Player actualPlayer;
        if (connectingPlayerO.isPresent()) {
            actualPlayer = connectingPlayerO.get();
            Optional<Player> facilityTaken = game.getPlayers().stream().filter(player -> player.getFacility() != null && player.getFacility().getFacilityId() == facility.getFacilityId()).findFirst();
            if (!facilityTaken.isPresent()) {
                game.removePlayerById(actualPlayer.getPlayerId());
                actualPlayer.setFacility(facility);
                game.getPlayers().add(actualPlayer);
                return;
            }
        }
        throw new FacilityNotAvailableException();
    }

    public GamePlayerId getGameData(String playerIp, String userName) {
        Optional<Player> connectingPlayerO = game.getPlayers().stream().filter(player -> player.getIpAddress().equals(playerIp)).findFirst();
        Player actualPlayer;
        if (!connectingPlayerO.isPresent()) {
            actualPlayer = playerProvider.get();
            actualPlayer.setPlayerId(Integer.toString(highestPlayerId + 1));
            actualPlayer.setIpAddress(playerIp);
            actualPlayer.setName(userName);
            if (highestPlayerId < Integer.parseInt(actualPlayer.getPlayerId())) {
                highestPlayerId = Integer.parseInt(actualPlayer.getPlayerId());
            }
            game.getPlayers().add(actualPlayer);
        } else {
            actualPlayer = connectingPlayerO.get();
        }

        return new GamePlayerId(game, actualPlayer.getPlayerId());
    }

    /**
     * This method is called when a turn is received from the ITurnModelObserver.
     * The turn is processed by the TurnHandler and the amount of turns received this round is incremented.
     * <p>
     * Once all turns have been received, allTurnDataReceived is called.
     *
     * @param turnModel an incoming turn from a facility
     */
    public void turnModelReceived(Round turnModel) throws TransactionException {
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
    private void allTurnDataReceived() throws TransactionException {
        this.previousRoundData = this.currentRoundData;
        this.currentRoundData = gameLogic.calculateRound(this.currentRoundData, game);
        persistence.saveRoundData(this.currentRoundData);
        game.getRounds().add(this.currentRoundData);

        startNextRound();
    }

    public void startGame() throws BeerGameException, TransactionException {
        persistence.saveGameLog(game, false);
        for (Player player : game.getPlayers()) {
            if (player.getFacility() == null) {
                throw new BeerGameException("Every player needs to control a facility");
            }
        }
        connectorForLeader.startRoom(roomModel);
        connectorForLeader.sendGameStart(game);
    }


    /**
     * Starts a new round of the beer game.
     * Sets the amount of received turns from players to zero.
     * Creates a new Round for the beer game.
     */
    private void startNextRound() throws TransactionException {
        //TODO: check if game is done? (round count exceeds config max)
        roundId++;
        currentRoundData.setRoundId(roundId);
        turnsReceivedInCurrentRound = 0;

        try {
            connectorForLeader.sendRoundDataToAllPlayers(previousRoundData, currentRoundData, game);
        } catch (TransactionException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        if (currentRoundData.getRoundId() == getBeerGame().getConfiguration().getAmountOfRounds()) {
            connectorForLeader.sendGameEnd(game);
        }
    }

    /**
     * Checks if the incoming playerId is the same as the playerId of the game leader.
     *
     * @param p supplied player object
     * @return true if the supplied player is the local player, false otherwise
     */
    private boolean checkIfPlayerIsLocalPlayer(Player p) {
        return game.getLeader().getPlayer().getPlayerId().equals(p.getPlayerId());
    }

    /**
     * @return the amount of turns received from players in the current round. Since each player can send one turn, this also indicates the amount of players who have sent their turn to the game leader.
     */
    public int getTurnsReceivedInCurrentRound() {
        return turnsReceivedInCurrentRound;
    }

    /**
     * Retrieves the agent which belongs to the supplied FacilityId.
     *
     * @param facilityId the id of the facility that will be used to find the correct Facility instance.
     * @return Agent object belonging to the facility.
     */
    Agent getAgentByFacility(int facilityId) {
        for (GameAgent agent : game.getAgents()) {
            if (agent.getFacility().getFacilityId() == facilityId) {
                return new Agent(game.getConfiguration(), agent.getGameAgentName(), agent.getFacility(), agent.getGameBusinessRules());
            }
        }
        return null;
    }


    @Override
    public BeerGame getBeerGame() {
        return game;
    }

    @Override
    public RoomModel getRoomModel() {
        return roomModel;
    }
}
