package org.han.ica.asd.c.participants;

import org.han.ica.asd.c.model.FacilityTurn;

public interface IParticipant {
    FacilityTurn doOrder();
    String getParticipantId();
}
