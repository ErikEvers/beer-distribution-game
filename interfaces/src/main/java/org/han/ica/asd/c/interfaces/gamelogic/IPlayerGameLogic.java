package org.han.ica.asd.c.interfaces.gamelogic;

import org.han.ica.asd.c.exceptions.communication.SendGameMessageException;
import org.han.ica.asd.c.interfaces.player.IPlayerRoundListener;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.Round;

public interface IPlayerGameLogic {
    /**
     * Sends and saves an turn of the player / agent.
     * @param turn
     */
    void submitTurn(Round turn) throws SendGameMessageException;

    /**
     * Returns the current state of the game.
     * @return The current state of the game.
     */
    BeerGame getBeerGame();

    /**
     * Replaces the player with the given agent.
     * @param agent Agent that will replace the player.
     */
    void letAgentTakeOverPlayer(GameAgent agent);

    /**
     * Replaces the agent with the player.
     */
    void letPlayerTakeOverAgent();

    int getRoundId();

    void setPlayer(IPlayerRoundListener player);
}
