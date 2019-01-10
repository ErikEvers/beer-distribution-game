package org.han.ica.asd.c.socketrpc;

import org.han.ica.asd.c.messagehandler.receiving.GameMessageReceiver;

public interface IServerObserver {
    Object serverObjectReceived(Object receivedObject, String senderIp);

    void setGameMessageReceiver(GameMessageReceiver gameMessageReceiver);
}
