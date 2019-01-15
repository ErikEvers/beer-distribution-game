package org.han.ica.asd.c.interfaces.gamelogic;

import org.han.ica.asd.c.exceptions.communication.SendGameMessageException;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;

import java.util.List;

public interface IConnectedForPlayer {
    void sendTurnData(Round turn) throws SendGameMessageException;
    void addObserver(IConnectorObserver observer);
}