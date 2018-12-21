package org.han.ica.asd.c.gamelogic.participants.domain_models;

import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.GameRoundAction;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.model.domain_objects.Facility;

/**
 * Wrapper for the agent domain class. This wrapper implements the IParticipant interface.
 */
@Deprecated
public class AgentParticipant extends GameAgent implements IParticipant {
    public AgentParticipant(String gameAgentName, Facility facility) {
        super(gameAgentName, facility, null);
    }

    /**
     * executeTurn will notify the  participant to make an order and/ or deliver.
     * @return A FacilityTurn with an order for the current round.
     */
    @Override
    public GameRoundAction executeTurn(Round round) {
        return null;
    }

    /**
     * Returns the identifier for the ParticipantPool to compare with other participants.
     * @return The identifier of the participant.
     */
    @Override
    public Facility getParticipant() {
        return this.getFacility();
    }
}
