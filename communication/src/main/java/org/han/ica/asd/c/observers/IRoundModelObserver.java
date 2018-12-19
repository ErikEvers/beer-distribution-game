package org.han.ica.asd.c.observers;

import org.han.ica.asd.c.model.domain_objects.Round;

public interface IRoundModelObserver extends IConnectorObserver {
    void roundModelReceived(Round roundModel);
}
