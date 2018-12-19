package org.han.ica.asd.c.gamelogic.participants.domain_models;

import org.han.ica.asd.c.gamelogic.participants.IParticipant;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.Round;

/**
 * Wrapper for the agent domain class. This wrapper implements the IParticipant interface.
 */
public class AgentParticipant extends GameAgent implements IParticipant {
    public AgentParticipant(String gameAgentName, int facilityId) {
        super(gameAgentName, facilityId);
    }

    /**
     * doOrder will notify the  participant to make an order.
     * @return A FacilityTurn with an order for the current round.
     */
    @Override
    public Round doOrder() {
        //TODO: Implement this method.
        return null;
    }

    /**
     * Returns the identifier for the ParticipantPool to compare with other participants.
     * @return The identifier of the participant.
     */
    @Override
    public int getParticipantId() {
        return this.getFacilityId();
    }
}
