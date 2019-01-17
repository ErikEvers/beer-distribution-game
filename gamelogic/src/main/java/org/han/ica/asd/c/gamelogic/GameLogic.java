package org.han.ica.asd.c.gamelogic;

import javafx.application.Platform;
import org.han.ica.asd.c.agent.Agent;
import org.han.ica.asd.c.exceptions.communication.SendGameMessageException;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.gamelogic.participants.ParticipantsPool;
import org.han.ica.asd.c.gamelogic.roundcalculator.RoundCalculator;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.communication.IGameStartObserver;
import org.han.ica.asd.c.interfaces.communication.IRoundModelObserver;
import org.han.ica.asd.c.interfaces.gameleader.ILeaderGameLogic;
import org.han.ica.asd.c.interfaces.gamelogic.IConnectedForPlayer;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.interfaces.gamelogic.IPlayerGameLogic;
import org.han.ica.asd.c.interfaces.persistence.IGameStore;
import org.han.ica.asd.c.interfaces.player.IPlayerRoundListener;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnDeliver;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.GameRoundAction;
import org.han.ica.asd.c.model.domain_objects.Round;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
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

    @Inject
    private static ParticipantsPool participantsPool;

    @Inject
    private Provider<Agent> agentProvider;

    private static int curRoundId;
    private static IPlayerRoundListener player;

    @Inject
    @Named("SeeOtherFacilities")
    private IGUIHandler seeOtherFacilities;

    @Inject
    public GameLogic(Provider<ParticipantsPool> participantsPoolProvider, IConnectedForPlayer communication){
        if(participantsPool == null) {
            participantsPool = participantsPoolProvider.get();
        }
        this.communication = communication;
        initObserver();
    }

    public void initObserver() {
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
    public void submitTurn(Round turn) throws SendGameMessageException {
        persistence.saveRoundData(turn);
        communication.sendTurnData(turn);
    }

    /**
     * Returns the current state of the game.
     * @return The current state of the game.
     */
    @Override
    public BeerGame getBeerGame() {
        return persistence.getGameLog();
    }

    /**
     * Replaces the player with the given agent.
     * @param gameAgent Agent that will replace the player.
     */
    @Override
    public void letAgentTakeOverPlayer(GameAgent gameAgent) {
        Agent agent = this.agentProvider.get();
        agent.setConfiguration(getBeerGame().getConfiguration());
        agent.setGameAgentName(gameAgent.getGameAgentName());
        agent.setFacility(gameAgent.getFacility());
        agent.setGameBusinessRules(gameAgent.getGameBusinessRules());

        participantsPool.replacePlayerWithAgent(agent);
        sendRoundForAgent(agent);
    }

    /**
     * Replaces the agent with the player.
     */
    @Override
    public void letPlayerTakeOverAgent() {
        participantsPool.replaceAgentWithPlayer();
    }

		/**
		 * Calculates the round.
		 * @param round has the information needed to calculate the round.
		 * @return
		 */
		@Override
		public Round calculateRound(Round round, BeerGame game) {
			RoundCalculator roundCalculator = new RoundCalculator();

			return  roundCalculator.calculateRound(round, game);
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
        //TODO: please remove this. Quick fix for now.
        participantsPool.replaceAgentWithPlayer();
    }

    public void addObserver(IConnectorObserver observer) {
        communication.addObserver(observer);
    }

    public int getRoundId() {
        return curRoundId;
    }

    @Override
    public void setPlayer(IPlayerRoundListener player) {
        GameLogic.player = player;
        participantsPool.setPlayer(player);
    }

    /**
		 * @param previousRound The previous round to save.
     * @param newRound The current round to save.
     */
    @Override
    public void roundModelReceived(Round previousRound, Round newRound) {
        curRoundId = newRound.getRoundId();
        persistence.updateRound(previousRound);
        persistence.createRound(newRound);
        sendRoundActionFromAgents();
    }

    @Override
    public void gameStartReceived(BeerGame beerGame) {
            persistence.saveGameLog(beerGame, false);
        if (isBotGame()) {
            //seeOtherFacilities.setupScreen();
        } else {
            player.startGame();
        }
        curRoundId = 1;
        sendRoundActionFromAgents();
    }

    private void sendRoundActionFromAgents() {
        for (IParticipant participant : participantsPool.getParticipants()) {
           sendRoundForAgent(participant);
        }
        if (isBotGame()) {
            Platform.runLater(() -> seeOtherFacilities.setupScreen());
        } else {
            player.roundStarted();
        }
    }

    public void sendRoundForAgent(IParticipant participant){
        Round round = makeRoundFromGameRoundAction(participant.executeTurn(), participant.getParticipant().getFacilityId());
        try {
            communication.sendTurnData(round);
        } catch (SendGameMessageException e) {
            //No error should be thrown if the agent runs locally
        }
    }

    private Round makeRoundFromGameRoundAction(GameRoundAction action, int facilityId) {
        return new Round(curRoundId,
                null,
                action.targetOrderMap.entrySet().stream().map(e -> new FacilityTurnOrder(facilityId, e.getKey().getFacilityId(), e.getValue())).collect(Collectors.toList()),
                action.targetDeliverMap.entrySet().stream().map(e -> new FacilityTurnDeliver(facilityId, e.getKey().getFacilityId(), 0, e.getValue())).collect(Collectors.toList())
        );
    }

    public boolean isBotGame() {
        return getBeerGame().getPlayers().stream().filter(player1 -> player1.getPlayerId().equals("1")).findFirst().get().getFacility() == null;
    }


}
