package org.han.ica.asd.c.gamelogic.public_interfaces;

import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import java.util.List;

import org.han.ica.asd.c.agent.Agent;
import org.han.ica.asd.c.model.domain_objects.Round;

public interface IPlayerGameLogic {
    /**
     * Sends and saves an turn of the player / agent.
     * @param turn
     */
    void submitTurn(Round turn);

    /**
     * Returns the current state of the game.
     * @return The current state of the game.
     */
    BeerGame seeOtherFacilities();

    /**
     * Replaces the player with the given agent.
     * @param agent Agent that will replace the player.
     */
    void letAgentTakeOverPlayer(Agent agent);

    /**
     * Replaces the agent with the player.
     */
    void letPlayerTakeOverAgent();

    List<String> getAllGames();

    void connectToGame(String game);

    void requestFacilityUsage(Facility facility);

    List<Facility> getAllFacilities();

    int getRound();
}
