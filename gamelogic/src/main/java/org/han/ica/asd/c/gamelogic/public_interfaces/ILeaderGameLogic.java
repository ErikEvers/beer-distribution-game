package org.han.ica.asd.c.gamelogic.public_interfaces;

import org.han.ica.asd.c.gamelogic.participants.IParticipant;
import org.han.ica.asd.c.model.Round;

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

    Round calculateRound(Round round);
}
