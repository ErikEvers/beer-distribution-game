package org.han.ica.asd.c.observers;

public interface IPlayerDisconnectedObserver extends IConnectorObserver {
    void notifyPlayerDisconnected(String playerId);
}
