package org.han.ica.asd.c.public_interfaces;

import org.han.ica.asd.c.model.FacilityTurn;

public interface ICommunication {
    void sendTurnData(FacilityTurn turn);
}
