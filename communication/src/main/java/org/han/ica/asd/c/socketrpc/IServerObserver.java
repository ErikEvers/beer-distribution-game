package org.han.ica.asd.c.socketrpc;

public interface IServerObserver {
    Object serverObjectReceived(Object receivedObject, String senderIp);
}
