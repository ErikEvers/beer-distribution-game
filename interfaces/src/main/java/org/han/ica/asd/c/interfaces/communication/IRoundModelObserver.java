package org.han.ica.asd.c.interfaces.communication;

import org.han.ica.asd.c.exceptions.communication.SendGameMessageException;
import org.han.ica.asd.c.model.domain_objects.Round;

public interface IRoundModelObserver extends IConnectorObserver {
    void roundModelReceived(Round previousRound, Round newRound);
}
