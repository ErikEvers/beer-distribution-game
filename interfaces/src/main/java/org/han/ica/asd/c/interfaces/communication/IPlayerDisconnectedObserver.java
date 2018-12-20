package org.han.ica.asd.c.interfaces.communication;

public interface IPlayerDisconnectedObserver extends IConnectorObserver {
	void iAmDisconnected();
	void playerIsDisconnected(String ip);
}
