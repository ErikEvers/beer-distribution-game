package org.han.ica.asd.c.gamelogic;

import org.han.ica.asd.c.gamelogic.public_interfaces.*;
import org.han.ica.asd.c.model.*;
import org.han.ica.asd.c.gamelogic.participants.IParticipant;
import org.han.ica.asd.c.gamelogic.participants.ParticipantsPool;
import org.han.ica.asd.c.gamelogic.participants.domain_models.AgentParticipant;
import org.han.ica.asd.c.gamelogic.participants.domain_models.PlayerParticipant;

import java.util.ArrayList;
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

    private ArrayList<FacilityLinkedTo> facilitityLinks;

    public GameLogic(String gameId, IConnectedForPlayer communication, IPersistence persistence, ParticipantsPool participantsPool) {
        this.gameId = gameId;
        this.communication = communication;
        this.persistence = persistence;
        this.participantsPool = participantsPool;
        this.round = 0;
        facilitityLinks = new ArrayList<>();
    }

    public void addfacilities(FacilityLinkedTo facilityLink) {
        facilitityLinks.add(facilityLink);
    }

    /**
     * Sends and saves an order of the player / agent.
     * @param turn
     */
    @Override
    public void placeOrder(Map<Facility, Map<Facility, Integer>> turn) {
        persistence.saveTurnData(turn);
        communication.sendTurnData(turn);
    }

    /**
     * Returns the current state of the game.
     * @return The current state of the game.
     */
    @Override
    public Round seeOtherFacilities() {
        return persistence.fetchRoundData(gameId, round);
    }

    /**
     * Replaces the player with the given agent.
     * @param agent Agent that will replace the player.
     */
    @Override
    public void letAgentTakeOverPlayer(AgentParticipant agent) {
        participantsPool.replacePlayerWithAgent(agent);
    }

    /**
     * Replaces the agent with the player.
     */
    @Override
    public void letPlayerTakeOverAgent() {
        participantsPool.replaceAgentWithPlayer();
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

    /**
     * Calculates the round.
     * @param round has the information needed to calculate the round
     * @return
     */
    @Override
    public Round calculateRound(Round round) {

        for (FacilityLinkedTo f : facilitityLinks) {
            Facility facilityOrder = f.getFacilityOrder();
            Facility facilityDeliver = f.getFacilityDeliver();

            int ordered = round.getTurnOrderByFacility(facilityOrder, facilityDeliver);
            int backOrders = 0;

            if (!facilityOrder.equals(facilityDeliver)) {
                int facilityStockDeliver = round.getStockByFacility(facilityDeliver);
                int newFacilityStockDeliver = facilityStockDeliver - ordered;

                if (newFacilityStockDeliver < 0) {
                    ordered = newFacilityStockDeliver + ordered;
                    backOrders = -newFacilityStockDeliver;
                    newFacilityStockDeliver = 0;
                    round.addTurnBackOrder(facilityOrder, facilityDeliver, backOrders);
                }

                round.updateStock(facilityDeliver, newFacilityStockDeliver);
            }

            int facilityStockOrder = round.getStockByFacility(facilityOrder);
            int newFacilityStockOrder = facilityStockOrder + ordered;

            round.addTurnReceived(facilityOrder, facilityDeliver, ordered);
            round.updateStock(facilityOrder, newFacilityStockOrder);
            round.addTurnDeliver(facilityOrder, facilityDeliver, ordered);
        }

        calculateNewRemainingBudget(round);
        return round;
    }

    private void calculateNewRemainingBudget(Round round) {
        for (FacilityLinkedTo f : facilitityLinks) {
            Facility facilityOrder = f.getFacilityOrder();
            if (round.isremainingBudgetExisting(facilityOrder)) {
                //The stock is calculated with the FacilityOrder variable
                int remainingBudget = round.getRemainingBudget(facilityOrder);
                int stock = round.getStockByFacility(facilityOrder);
                FacilityType facilityType = facilityOrder.getFacilityType();
                int stockCosts = stock * facilityType.getStockHoldingCosts();
                remainingBudget -= stockCosts;
                round.updateRemainingBudget(remainingBudget, facilityOrder);

                //The backlog gets calculated with the FacilityDeliver
                if (round.isTurnBackLogfilledByFacility(facilityOrder)) {
                    int backOrders = round.getTurnBacklogByFacility(facilityOrder, f.getFacilityDeliver());
                    int backlogcosts = backOrders * facilityType.getOpenOrderCosts();
                    Facility facilityDeliver = f.getFacilityDeliver();
                    int remainingBudgetFacilityDeliver = round.getRemainingBudget(facilityDeliver);
                    remainingBudgetFacilityDeliver -= backlogcosts;
                    round.updateRemainingBudget(remainingBudgetFacilityDeliver, f.getFacilityDeliver());
                }
            }
        }
    }
}
