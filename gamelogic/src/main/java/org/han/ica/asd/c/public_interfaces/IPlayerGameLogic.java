package org.han.ica.asd.c.public_interfaces;

import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.Round;
import org.han.ica.asd.c.participants.domain_models.AgentParticipant;

import java.util.List;

public interface IPlayerGameLogic {
    void placeOrder(FacilityTurn turn);

    Round seeOtherFacilities();

    void letAgentTakeOverPlayer(AgentParticipant agent);

    void letPlayerTakeOverAgent();
}
