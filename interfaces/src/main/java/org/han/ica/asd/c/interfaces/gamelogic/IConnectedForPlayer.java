package org.han.ica.asd.c.interfaces.gamelogic;

import org.han.ica.asd.c.exceptions.communication.SendGameMessageException;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;

public interface IConnectedForPlayer {
    void sendTurn(Round turn) throws SendGameMessageException;
    void addObserver(IConnectorObserver observer);

    void startFaultDetector();
}