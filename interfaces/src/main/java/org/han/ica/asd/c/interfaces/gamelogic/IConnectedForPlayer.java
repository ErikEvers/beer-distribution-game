package org.han.ica.asd.c.interfaces.gamelogic;

import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;

public interface IConnectedForPlayer {
    void sendTurnData(Round turn);
    void addObserver(IConnectorObserver observer);
}
