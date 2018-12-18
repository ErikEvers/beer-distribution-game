package org.han.ica.asd.c.observers;


import domainobjects.RoundModel;

public interface IRoundModelObserver extends IConnectorObserver {
    void roundModelReceived(RoundModel roundModel);
}
