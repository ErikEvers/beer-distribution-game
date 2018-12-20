package org.han.ica.asd.c.gamelogic.public_interfaces;

import org.han.ica.asd.c.gamelogic.participants.domain_models.AgentParticipant;
import org.han.ica.asd.c.model.dao_model.FacilityTurnDB;
import org.han.ica.asd.c.model.dao_model.RoundDB;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.util.Map;

public interface IPlayerGameLogic {
    /**
     * Sends and saves an order of the player / agent.
     * @param turn
     */
    void placeOrder(FacilityTurnDB turn);

    /**
     * Returns the current state of the game.
     * @return The current state of the game.
     */
    RoundDB seeOtherFacilities();

    /**
     * Replaces the player with the given agent.
     * @param agent Agent that will replace the player.
     */
    void letAgentTakeOverPlayer(AgentParticipant agent);

    /**
     * Replaces the agent with the player.
     */
    void letPlayerTakeOverAgent();
}
