package org.han.ica.asd.c.observers;

public interface IPlayerDisconnectedObserver extends IConnectorObserver {
	void iAmDisconnected();
	void playerIsDisconnected(String ip);
}
