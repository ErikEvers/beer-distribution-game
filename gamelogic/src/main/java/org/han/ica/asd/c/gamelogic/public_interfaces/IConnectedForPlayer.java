package org.han.ica.asd.c.gamelogic.public_interfaces;

import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.observers.IConnectorObserver;

public interface IConnectedForPlayer {
    void sendTurnData(Round turn);
    void addObserver(IConnectorObserver observer);
}