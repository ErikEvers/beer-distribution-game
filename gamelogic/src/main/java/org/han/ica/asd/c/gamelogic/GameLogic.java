package org.han.ica.asd.c.gamelogic;

import org.han.ica.asd.c.gamelogic.participants.ParticipantsPool;
import org.han.ica.asd.c.gamelogic.participants.domain_models.PlayerParticipant;
import org.han.ica.asd.c.interfaces.player.IPlayerGameLogic;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.gameleader.ILeaderGameLogic;
import org.han.ica.asd.c.interfaces.gamelogic.IPersistence;
import org.han.ica.asd.c.interfaces.gamelogic.IConnectedForPlayer;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.util.List;
import java.util.Map;

/**
 * This class is responsible for game logic of the 'Beer Distribution Game'. The concept of game logic includes:
 *  - Keeping track of the current round number;
 *  - Handling player actions involving data;
 *  - Delegating the task of managing local participants to the ParticipantsPool.
 */
public class GameLogic implements IPlayerGameLogic, ILeaderGameLogic {
    String gameId;
    private IConnectedForPlayer communication;
    private IPersistence persistence;
    private ParticipantsPool participantsPool;
    private int round;

    public GameLogic(String gameId, IConnectedForPlayer communication, IPersistence persistence, ParticipantsPool participantsPool) {
        this.gameId = gameId;
        this.communication = communication;
        this.persistence = persistence;
        this.participantsPool = participantsPool;
        this.round = 0;
    }

    /**
     * Sends and saves an order of the player / agent.
     * @param turn
     */
    @Override
    public void placeOrder(Round turn) {
        persistence.saveTurnData(turn);
        communication.sendTurnData(turn);
    }

    /**
     * Returns the current state of the game.
     * @return The current state of the game.
     */
    @Override
    public Map<Facility, List<Facility>> seeOtherFacilities() {
        //Yet to be implemented.
        persistence.fetchRoundData("", 0);
        return null;
    }

    /**
     * Replaces the agent with the player.
     */
    @Override
    public void letPlayerTakeOverAgent() {
        participantsPool.replaceAgentWithPlayer();
    }

    @Override
    public List<String> getAllGames() {
        return communication.getAllGames();
    }

    @Override
    public void connectToGame(String game) {
        communication.connectToGame(game);
    }

    public Round calculateRound(Round round) {
        return null;
    }

    /**
     * Adds a local participant to the game.
     * @param participant The local participant to add to the game.
     */
    @Override
    public void addLocalParticipant(IParticipant participant) {
        participantsPool.addParticipant(participant);
    }

    /**
     * Removes an agent with the given playerId;
     * @param playerId Identifier of the player to remove.
     */
    @Override
    public void removeAgentByPlayerId(String playerId) {
        Player player = persistence.getPlayerById(playerId);
        participantsPool.replaceAgentWithPlayer(new PlayerParticipant(player));
    }

    public void sendTurnData(Round turn) {
        communication.sendTurnData(turn);
    }

    public void addObserver(IConnectorObserver observer) {
        communication.addObserver(observer);
    }

    @Override
    public void requestFacilityUsage(Facility facility) {
        communication.requestFacilityUsage(facility);
    }

    @Override
    public List<Facility> getAllFacilities() {
        return communication.getAllFacilities();
    }
}
