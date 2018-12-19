package org.han.ica.asd.c.gamelogic.public_interfaces;

import org.han.ica.asd.c.model.domain_objects.Facility;

import java.util.Map;

public interface IConnectedForPlayer {
    void sendTurnData(Map<Facility, Map<Facility, Integer>> turn);
    void addObserver(IConnectorObserver observer);
}
