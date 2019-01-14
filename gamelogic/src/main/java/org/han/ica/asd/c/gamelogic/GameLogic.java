package org.han.ica.asd.c.gamelogic;

import org.han.ica.asd.c.gamelogic.participants.ParticipantsPool;
import org.han.ica.asd.c.interfaces.player.IPlayerGameLogic;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.communication.IGameStartObserver;
import org.han.ica.asd.c.interfaces.communication.IRoundModelObserver;
import org.han.ica.asd.c.interfaces.gameleader.ILeaderGameLogic;
import org.han.ica.asd.c.interfaces.gamelogic.IConnectedForPlayer;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.interfaces.persistence.IGameStore;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;
import org.han.ica.asd.c.model.domain_objects.Round;

import javax.inject.Inject;
import java.util.List;

/**
 * This class is responsible for game logic of the 'Beer Distribution Game'. The concept of game logic includes:
 *  - Keeping track of the current round number;
 *  - Handling player actions involving data;
 *  - Delegating the task of managing local participants to the ParticipantsPool.
 */

public class GameLogic implements IPlayerGameLogic, ILeaderGameLogic, IRoundModelObserver, IGameStartObserver {
    @Inject
    private IConnectedForPlayer communication;

    @Inject
    private IGameStore persistence;

	private ParticipantsPool participantsPool;

    private int round;
    private BeerGame beerGame;
    private IParticipant player;

    public GameLogic(){
        this.round = 0;
    }

    public void setParticipantsPool(ParticipantsPool participantsPool) {
        this.participantsPool = participantsPool;
    }

    /**
     * Sends and saves an order of the player / agent.
     * @param turn
     */
    @Override
    public void submitTurn(Round turn) {
        communication.sendTurnData(turn);
        persistence.saveRoundData(turn);
        System.out.println("=============== TURN AFGEROND =====================");
    }

    /**
     * Returns the current state of the game.
     * @return The current state of the game.
     */
    @Override
    public BeerGame seeOtherFacilities() {
        return beerGame;
    }

    /**
     * Replaces the player with the given agent.
     * @param agent Agent that will replace the player.
     */
    @Override
    public void letAgentTakeOverPlayer(IParticipant agent) {
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
        //TODO: please remove this. Quick fix for now.
        participantsPool.replaceAgentWithPlayer(player);
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

    public int getRound() {
        return round;
    }

    @Override
    public void setPlayerParticipant(IParticipant participant) {
        this.player = participant;
    }

    @Override
    public List<Facility> getAllFacilities() {
        return communication.getAllFacilities();
    }

    @Override
    public void selectAgent(ProgrammedAgent programmedAgent) {
        persistence.saveSelectedAgent(programmedAgent);
        communication.sendSelectedAgent(programmedAgent);
    }

    public void roundModelReceived(Round currentRound) {
        persistence.saveRoundData(currentRound);
        participantsPool.excecuteRound(currentRound);
        beerGame.getRounds().add(currentRound);
        round++;
    }

    @Override
    public void gameStartReceived(BeerGame beerGame) {
        this.beerGame = beerGame;
        persistence.saveGameLog(beerGame);
    }
}
