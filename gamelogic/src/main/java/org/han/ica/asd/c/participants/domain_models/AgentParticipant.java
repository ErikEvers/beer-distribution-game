package org.han.ica.asd.c.participants.domain_models;

import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.GameAgent;
import org.han.ica.asd.c.participants.IParticipant;

/**
 * Wrapper for the agent domain class. This wrapper implements the IParticipant interface.
 */
public class AgentParticipant extends GameAgent implements IParticipant {
    public AgentParticipant(String gameId, String gameAgentName, int facilityId) {
        super(gameId, gameAgentName, facilityId);
    }

    /**
     * doOrder will notify the  participant to make an order.
     * @return A FacilityTurn with an order for the current round.
     */
    @Override
    public FacilityTurn doOrder() {
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
