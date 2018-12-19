package org.han.ica.asd.c.gamelogic.participants;

import org.han.ica.asd.c.model.domain_objects.Round;

/**
 * This interface is implemented by local players of the game.
 * Currently the Agent and Player classes both have a separate class for implementing this method.
 */
public interface IParticipant {
    /**
     * doOrder will notify the  participant to make an order.
     * @return A FacilityTurn with an order for the current round.
     */
    Round doOrder();

    /**
     * Returns the identifier for the ParticipantPool to compare with other participants.
     * @return The identifier of the participant.
     */
    int getParticipantId();
}
