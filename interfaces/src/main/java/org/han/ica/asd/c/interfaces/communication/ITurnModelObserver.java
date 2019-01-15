package org.han.ica.asd.c.interfaces.communication;

import org.han.ica.asd.c.exceptions.communication.TransactionException;
import org.han.ica.asd.c.model.domain_objects.Round;

public interface ITurnModelObserver extends IConnectorObserver {
    void turnModelReceived(Round turnModel) throws TransactionException;
}
