package org.han.ica.asd.c.gamelogic.public_interfaces;

import org.han.ica.asd.c.gamelogic.participants.domain_models.AgentParticipant;
import org.han.ica.asd.c.model.domain_objects.Facility;
import java.util.List;
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
    Round seeOtherFacilities();

    /**
     * Replaces the player with the given agent.
     * @param agent Agent that will replace the player.
     */
    void letAgentTakeOverPlayer(AgentParticipant agent);

    /**
     * Replaces the agent with the player.
     */
    void letPlayerTakeOverAgent();

    List<String> getAllGames();

    void connectToGame(String game);

    void requestFacilityUsage(Facility facility);

    List<Facility> getAllFacilities();
}
