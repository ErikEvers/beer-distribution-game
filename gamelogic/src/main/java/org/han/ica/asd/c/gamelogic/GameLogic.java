package org.han.ica.asd.c.gamelogic;

import org.han.ica.asd.c.agent.Agent;
import org.han.ica.asd.c.gamelogic.participants.ParticipantsPool;
import org.han.ica.asd.c.interfaces.gamelogic.IPlayerGameLogic;
import org.han.ica.asd.c.interfaces.communication.IGameStartObserver;
import org.han.ica.asd.c.interfaces.communication.IRoundModelObserver;
import org.han.ica.asd.c.interfaces.gameleader.ILeaderGameLogic;
import org.han.ica.asd.c.interfaces.gamelogic.IConnectedForPlayer;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.interfaces.persistence.IGameStore;
import org.han.ica.asd.c.interfaces.player.IPlayerRoundListener;
import org.han.ica.asd.c.model.domain_objects.*;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for game logic of the 'Beer Distribution Game'. The concept of game logic includes:
 *  - Keeping track of the current round number;
 *  - Handling player actions involving data;
 *  - Delegating the task of managing local participants to the ParticipantsPool.
 */
public class GameLogic implements IPlayerGameLogic, ILeaderGameLogic, IRoundModelObserver, IGameStartObserver {
    private IConnectedForPlayer communication;

    @Inject
    private IGameStore persistence;

		private static ParticipantsPool participantsPool;

    private static int curRoundId;
    private static BeerGame beerGame;
    private static IPlayerRoundListener player;

    @Inject
    public GameLogic(Provider<ParticipantsPool> participantsPoolProvider, IConnectedForPlayer communication){
        if(participantsPool == null) {
					participantsPool = participantsPoolProvider.get();
				}
        this.communication = communication;
        this.communication.addObserver(this);
    }

    public void setParticipantsPool(ParticipantsPool participantsPool) {
        GameLogic.participantsPool = participantsPool;
    }

    /**
     * Sends and saves an order of the player / agent.
     * @param turn
     */
    @Override
    public boolean submitTurn(Round turn) {
				//persistence.saveRoundData(turn);
        return communication.sendTurnData(turn);
    }

    /**
     * Returns the current state of the game.
     * @return The current state of the game.
     */
    @Override
    public BeerGame getBeerGame() {
        return beerGame;
    }

    /**
     * Replaces the player with the given agent.
     * @param agent Agent that will replace the player.
     */
    @Override
    public void letAgentTakeOverPlayer(IParticipant agent) {
        participantsPool.addParticipant(agent);
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

    public Round calculateRound(Round round, BeerGame game) {
    		Round outcome = new Round();

				outcome.getFacilityOrders().addAll(round.getFacilityOrders());
				outcome.getFacilityTurnDelivers().addAll(round.getFacilityTurnDelivers());
				outcome.setRoundId(round.getRoundId());

				for(Facility facility : game.getConfiguration().getFacilities()) {
					FacilityTurn curFacility = round.getFacilityTurns().stream().filter(facilityTurn -> facilityTurn.getFacilityId() == facility.getFacilityId()).findFirst().get();

					int stock = curFacility.getStock();
					for(FacilityTurnDeliver facilityTurnDeliver : round.getFacilityTurnDelivers()) {
						if(facilityTurnDeliver.getFacilityIdDeliverTo() == facility.getFacilityId()) {
							stock += facilityTurnDeliver.getDeliverAmount();
						}
					}

					int backorders = curFacility.getBackorders();
					for(FacilityTurnDeliver facilityTurnDeliver : round.getFacilityTurnDelivers()) {
						if(facilityTurnDeliver.getFacilityId() == facility.getFacilityId()) {
							backorders -= facilityTurnDeliver.getDeliverAmount();
							stock -= facilityTurnDeliver.getDeliverAmount();
						}
					}
					for(FacilityTurnOrder facilityTurnOrder : round.getFacilityOrders()) {
						if(facilityTurnOrder.getFacilityIdOrderTo() == facility.getFacilityId()) {
							backorders += facilityTurnOrder.getOrderAmount();
						}
					}

					int remainingBudget = curFacility.getRemainingBudget() + 3;

					boolean bankrupt = remainingBudget > 0;

					outcome.getFacilityTurns().add(new FacilityTurn(
							facility.getFacilityId(),
							round.getRoundId() +1,
							stock,
							backorders,
							remainingBudget,
							bankrupt
					));
				}
        return outcome;
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
        //TODO: please remove this. Quick fix for now.
        participantsPool.replaceAgentWithPlayer();
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

    public int getRoundId() {
        return curRoundId;
    }

    @Override
    public void setPlayer(IPlayerRoundListener player) {
        this.player = player;
        participantsPool.setPlayer(this.player);
    }

    /**
     * @param currentRound The current round to save.
     */
    @Override
    public void roundModelReceived(Round currentRound) {
        beerGame.getRounds().removeIf(round -> round.getRoundId() == currentRound.getRoundId());
        beerGame.getRounds().add(currentRound);
        sendRoundActionFromAgents();
    }

    @Override
    public void gameStartReceived(BeerGame beerGame) {
        GameLogic.beerGame = beerGame;
        //persistence.saveGameLog(beerGame);
        player.startGame();
        sendRoundActionFromAgents();
    }

    private void sendRoundActionFromAgents() {
        for (IParticipant participant : participantsPool.getParticipants()) {
            Round round = makeRoundFromGameRoundAction(participant.executeTurn(), participant.getParticipant().getFacilityId());
            communication.sendTurnData(round);
        }
        player.roundStarted();
    }

    private Round makeRoundFromGameRoundAction(GameRoundAction action, int facilityId) {
        return new Round(curRoundId,
                null,
                action.targetOrderMap.entrySet().stream().map(e -> new FacilityTurnOrder(facilityId, e.getKey().getFacilityId(), e.getValue())).collect(Collectors.toList()),
                action.targetDeliverMap.entrySet().stream().map(e -> new FacilityTurnDeliver(facilityId, e.getKey().getFacilityId(), 0, e.getValue())).collect(Collectors.toList())
        );
    }
}
