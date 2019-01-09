package org.han.ica.asd.c.interfaces.player;

import org.han.ica.asd.c.model.domain_objects.Facility;
import java.util.List;
import java.util.Map;

import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;
import org.han.ica.asd.c.model.domain_objects.Round;

public interface IPlayerGameLogic {
    /**
     * Sends and saves an order of the player / agent.
     * @param turn
     */
    void placeOrder(Round turn);

    /**
     * Returns the current state of the game.
     * @return The current state of the game.
     */
    Map<Facility, List<Facility>> seeOtherFacilities();

    /**
     * Replaces the agent with the player.
     */
    void letPlayerTakeOverAgent();

    List<String> getAllGames();

    void connectToGame(String game);

    void requestFacilityUsage(Facility facility);

    List<Facility> getAllFacilities();

    void selectAgent(ProgrammedAgent programmedAgent);
}
