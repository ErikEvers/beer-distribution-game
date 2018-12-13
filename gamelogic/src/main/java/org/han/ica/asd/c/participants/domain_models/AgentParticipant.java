package org.han.ica.asd.c.participants.domain_models;

import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.GameAgent;
import org.han.ica.asd.c.participants.IParticipant;

public class AgentParticipant extends GameAgent implements IParticipant {
    public AgentParticipant(String gameId, String gameAgentName, int facilityId) {
        super(gameId, gameAgentName, facilityId);
    }

    @Override
    public FacilityTurn doOrder() {
        //TODO: Implement this method.
        return null;
    }

    @Override
    public int getParticipantId() {
        return this.getFacilityId();
    }
}
