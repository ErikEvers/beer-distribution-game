package org.han.ica.asd.c.gameleader;

import org.han.ica.asd.c.agent.Agent;
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
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityTurn;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.GamePlayerId;
import org.han.ica.asd.c.model.domain_objects.Leader;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.RoomModel;
import org.han.ica.asd.c.model.domain_objects.Round;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.UUID.randomUUID;

public class GameLeader implements IGameLeader, ITurnModelObserver, IPlayerDisconnectedObserver, IPlayerReconnectedObserver, IFacilityMessageObserver {
    @Inject private IConnectorForLeader connectorForLeader;
    @Inject private ILeaderGameLogic gameLogic;
    @Inject private IPersistence persistence;
    @Inject private TurnHandler turnHandler;
		@Inject @Named("PlayerComponent") private IPlayerComponent playerComponent;

    private final Provider<BeerGame> beerGameProvider;
    private final Provider<Round> roundProvider;
    private final Provider<Player> playerProvider;

    private static RoomModel roomModel;
    private static BeerGame game;
    
    private Round currentRoundData;

    private int highestPlayerId = 0;
    private int turnsExpectedPerRound;
    private int turnsReceivedInCurrentRound;
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
    public void init(String leaderIp, RoomModel roomModel) {
        connectorForLeader.addObserver(this);
        game = beerGameProvider.get();

        Configuration configuration = new Configuration();

        Facility retailer = new Facility(new FacilityType("Retailer", 0, 0,0,0,0,0, 0), 0);
        Facility wholesale = new Facility(new FacilityType("Wholesaler", 0, 0,0,0,0,0, 0), 1);
        Facility warehouse = new Facility(new FacilityType("Regional Warehouse", 0, 0,0,0,0,0, 0), 2);
        Facility factory = new Facility(new FacilityType("Factory", 0, 0,0,0,0,0, 0), 3);

        List<Facility> facilityList = new ArrayList<>();
        facilityList.add(retailer);
        facilityList.add(wholesale);
        facilityList.add(warehouse);
        facilityList.add(factory);

        configuration.setFacilities(facilityList);

        Map<Facility, List<Facility>> links = new HashMap<>();
        List<Facility> list = new ArrayList<>();
        list.add(wholesale);
        links.put(retailer, list);

        list = new ArrayList<>();
        list.add(warehouse);
        links.put(wholesale, list);

        list = new ArrayList<>();
        list.add(factory);
        links.put(warehouse, list);

        configuration.setFacilitiesLinkedTo(links);

        configuration.setAmountOfWarehouses(1);
        configuration.setAmountOfFactories(1);
        configuration.setAmountOfWholesalers(1);
        configuration.setAmountOfRetailers(1);

        configuration.setAmountOfRounds(20);

        configuration.setContinuePlayingWhenBankrupt(false);

        configuration.setInsightFacilities(true);

        configuration.setMaximumOrderRetail(99);
        configuration.setMinimalOrderRetail(5);

				GameLeader.roomModel = roomModel;
        game.setConfiguration(configuration);
        game.setGameId(randomUUID().toString());
        game.setGameName(roomModel.getRoomName());
        game.setGameDate("2019-01-01 0:00:00");
        Player henk = new Player("0", leaderIp, retailer, "Yarno", true);
        game.getPlayers().add(henk);
        game.setLeader(new Leader(henk));

        Round firstRound = roundProvider.get();
        firstRound.setRoundId(roundId);

        List<FacilityTurn> turns = new ArrayList<>();
				for(Facility facility: game.getConfiguration().getFacilities()) {
					turns.add(
							new FacilityTurn(
								facility.getFacilityId(),
								roundId,
								facility.getFacilityType().getStartingStock(),
								0,
								facility.getFacilityType().getStartingBudget(),
								false));
				}

        firstRound.setFacilityTurns(turns);
        game.getRounds().add(firstRound);

				playerComponent.setPlayer(henk);

        this.currentRoundData = firstRound;
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
        for (int i = 0; i <= game.getPlayers().size(); i++) {
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
			if(connectingPlayerO.isPresent()) {
				actualPlayer = connectingPlayerO.get();
				Optional<Player> facilityTaken = game.getPlayers().stream().filter(player -> player.getFacility() != null && player.getFacility().getFacilityId() == facility.getFacilityId()).findFirst();
				if(!facilityTaken.isPresent()) {
					game.removePlayerById(actualPlayer.getPlayerId());
					actualPlayer.setFacility(facility);
					game.getPlayers().add(actualPlayer);
					return;
				}
			}
			throw new FacilityNotAvailableException();
    }

    public GamePlayerId getGameData(String playerIp) {
        Optional<Player> connectingPlayerO = game.getPlayers().stream().filter(player -> player.getIpAddress().equals(playerIp)).findFirst();
        Player actualPlayer;
        if(!connectingPlayerO.isPresent()) {
            actualPlayer = playerProvider.get();
            actualPlayer.setPlayerId(Integer.toString(highestPlayerId + 1));
            actualPlayer.setIpAddress(playerIp);
            actualPlayer.setName("Piet paaltjes");
            if(highestPlayerId < Integer.parseInt(actualPlayer.getPlayerId())) {
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

    public void startGame() {
			//persistence.saveGameLog(game);
			connectorForLeader.startRoom(roomModel);
			connectorForLeader.sendGameStart(game);
		}

    /**
     * Starts a new round of the beer game.
     * Sets the amount of received turns from players to zero.
     * Creates a new Round for the beer game.
     */
    private void startNextRound() {
        currentRoundData = roundProvider.get();
        //TODO: check if game is done? (round count exceeds config max)
        roundId++;
        currentRoundData.setRoundId(roundId);
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

    /**
     *
     * @return the amount of turns received from players in the current round. Since each player can send one turn, this also indicates the amount of players who have sent their turn to the game leader.
     */
    public int getTurnsReceivedInCurrentRound() {
        return turnsReceivedInCurrentRound;
    }

    /**
     * Retrieves the agent which belongs to the supplied FacilityId.
     * @param facilityId the id of the facility that will be used to find the correct Facility instance.
     * @return Agent object belonging to the facility.
     */
    Agent getAgentByFacility (int facilityId) {
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
