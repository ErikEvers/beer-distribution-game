package org.han.ica.asd.c.gamelogic;

import org.han.ica.asd.c.agent.Agent;
import org.han.ica.asd.c.gamelogic.participants.ParticipantsPool;
import org.han.ica.asd.c.gamelogic.participants.domain_models.PlayerParticipant;
import org.han.ica.asd.c.gamelogic.public_interfaces.IPlayerGameLogic;
import org.han.ica.asd.c.interfaces.communication.IGameStartObserver;
import org.han.ica.asd.c.interfaces.communication.IRoundModelObserver;
import org.han.ica.asd.c.interfaces.gameleader.ILeaderGameLogic;
import org.han.ica.asd.c.interfaces.gamelogic.IConnectedForPlayer;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.interfaces.persistence.IGameStore;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameRoundAction;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;

import javax.inject.Inject;
import java.util.ArrayList;
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

    public GameLogic(){
        this.round = 0;
    }

    public void setParticipantsPool(ParticipantsPool participantsPool) {
        this.participantsPool = participantsPool;
    }

    private void executeAgents(){
        for(IParticipant participant: participantsPool.getParticipants()){
            GameRoundAction action = participant.executeTurn(new Round());
            //Action to Round
            this.submitTurn(new Round());
        }
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

    @Override
    public void requestFacilityUsage(Facility facility) {
        //Yet to be implemented
    }

    @Override
    public List<Facility> getAllFacilities() {
        //Yet to be implemented.
        return new ArrayList<>();
    }

    public int getRound() {
        return round;
    }

    /**
     * @param currentRound The current round to save.
     */
    @Override
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
