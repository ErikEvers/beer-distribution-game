package org.han.ica.asd.c.observers;


import domainobjects.TurnModel;


public interface ITurnModelObserver extends IConnectorObserver {
    void turnModelReceived(TurnModel turnModel);
}
