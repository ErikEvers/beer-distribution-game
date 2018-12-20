package org.han.ica.asd.c.interfaces.gamelogic;

import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;

public interface ILeaderGameLogic {
    /**
     * Adds a local participant to the game.
     * @param participant The local participant to add to the game.
     */
    void addLocalParticipant(IParticipant participant);

    /**
     * Removes an agent with the given playerId;
     * @param playerId Identifier of the player to remove.
     */
    void removeAgentByPlayerId(String playerId);
}
