package org.han.ica.asd.c.gamelogic;

import org.han.ica.asd.c.gamelogic.roundcalculator.RoundCalculator;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.agent.Agent;
import org.han.ica.asd.c.gamelogic.participants.ParticipantsPool;
import org.han.ica.asd.c.gamelogic.participants.domain_models.PlayerParticipant;
import org.han.ica.asd.c.gamelogic.public_interfaces.IPlayerGameLogic;
import org.han.ica.asd.c.interfaces.communication.IRoundModelObserver;
import org.han.ica.asd.c.interfaces.gameleader.ILeaderGameLogic;
import org.han.ica.asd.c.interfaces.gamelogic.IRoundStore;
import org.han.ica.asd.c.interfaces.gamelogic.IConnectedForPlayer;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for game logic of the 'Beer Distribution Game'. The concept of game logic includes:
 *  - Keeping track of the current round number;
 *  - Handling player actions involving data;
 *  - Delegating the task of managing local participants to the ParticipantsPool.
 */
public class GameLogic implements IPlayerGameLogic, ILeaderGameLogic, IRoundModelObserver {
    private IConnectedForPlayer communication;
    private IRoundStore persistence;
    private ParticipantsPool participantsPool;
    private int round;

    private Map<Facility, List<Facility>> facilityLinks;
    private final BeerGame beerGame;

    public GameLogic(IConnectedForPlayer communication, IRoundStore persistence, ParticipantsPool participantsPool, BeerGame beerGame) {
        this.communication = communication;
        this.persistence = persistence;
        this.participantsPool = participantsPool;
        this.round = 0;
        facilityLinks = new HashMap<>();
        this.beerGame = beerGame;
    }

    public void addFacilities(Facility facilityOrder, Facility facilityDeliver) {
        List<Facility> delivers;
        if(facilityLinks.containsKey(facilityOrder)) {
            delivers = facilityLinks.get(facilityOrder);
            if(!delivers.contains(facilityDeliver)) {
                delivers.add(facilityDeliver);
            }
        } else {
            delivers = new ArrayList<>();
            delivers.add(facilityDeliver);
            facilityLinks.put(facilityOrder, delivers);
        }
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
        persistence.fetchRoundData(0);
        return null;
    }

    /**
     * Replaces the player with the given agent.
     * @param agent Agent that will replace the player.
     */
    @Override
    public void letAgentTakeOverPlayer(Agent agent) {
        participantsPool.replacePlayerWithAgent(agent);
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
        //Yet to be implemented
        return new ArrayList<>();
    }

    @Override
    public void connectToGame(String game) {
        //Yet to be implemented
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
     * Removes an agent with the given playerId.
     * @param playerId Identifier of the player to remove.
     */
    @Override
    public void removeAgentByPlayerId(String playerId) {
        Player player = persistence.getPlayerById(playerId);
        participantsPool.replaceAgentWithPlayer(new PlayerParticipant(player));
    }

    /**
     * Calculates the round.
     * @param round has the information needed to calculate the round.
     * @return
     */
    @Override
    public Round calculateRound(Round round) {
        Round previousRound = beerGame.getRounds().get(round.getRoundId()- 1);
        RoundCalculator roundCalculator = new RoundCalculator();

        return  roundCalculator.calculateRound(previousRound, round, facilityLinks);
    }

    public BeerGame getBeerGame() {
        return beerGame;
    }

    @Override
    public void requestFacilityUsage(Facility facility) {
        //Yet to be implemented
    }

    @Override
    public List<Facility> getAllFacilities() {
        //Yet to be implemented.
        return new ArrayList<>();
    }

    /**
     * @param currentRound The current round to save.
     */
    @Override
    public void roundModelReceived(Round currentRound) {
        persistence.saveRoundData(currentRound);
        participantsPool.excecuteRound(currentRound);
        round++;
    }

    /**
     * Gets the current round number.
     *
     * @return The current round number
     */
    @Override
    public int getCurrentRoundNumber() {
        return round;
    }
}
