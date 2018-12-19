package org.han.ica.asd.c.observers;

import org.han.ica.asd.c.model.domain_objects.Round;

public interface ITurnModelObserver extends IConnectorObserver {
    void turnModelReceived(Round turnModel);
}
