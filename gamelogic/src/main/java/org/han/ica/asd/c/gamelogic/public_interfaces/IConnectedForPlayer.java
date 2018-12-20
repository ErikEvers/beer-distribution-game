package org.han.ica.asd.c.gamelogic.public_interfaces;

import org.han.ica.asd.c.model.dao_model.FacilityTurnDB;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.observers.IConnectorObserver;

import java.util.Map;

public interface IConnectedForPlayer {
    void sendTurnData(FacilityTurnDB turn);
    void addObserver(IConnectorObserver observer);
}
