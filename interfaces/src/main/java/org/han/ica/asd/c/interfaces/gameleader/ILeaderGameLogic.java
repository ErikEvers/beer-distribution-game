package org.han.ica.asd.c.interfaces.gameleader;

import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Round;

/**
 * interface for the Game Logic component
 * this interface is used to calculate a round of the beergame and enable or disable an agent for disconnected players.
 * */
public interface ILeaderGameLogic {
    /**
     * Performs all calculations of a round of the beergame.
     * @param round a Round object containing the turns of all facilities in a beer game instance.
     * @return a Round object containing updated game values which will be distributed to all players using the communication component.
     */
    Round calculateRound(Round previousRound, Round round, BeerGame beerGame);

    /**
     * Adds a local participant to the game when the connection to a facility is interrupted.
     * @param participant the participant to add to the game, either the local player or an agent which is executed locally.
     */
    void addLocalParticipant(IParticipant participant);

    /**
     * removes a local participant from the game when the player reconnects.
     * @param playerId the Id of the player that has reconnected.
     */
    void removeAgentByPlayerId(String playerId);
}
