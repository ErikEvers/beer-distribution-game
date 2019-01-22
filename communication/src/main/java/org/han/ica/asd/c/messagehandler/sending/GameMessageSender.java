package org.han.ica.asd.c.messagehandler.sending;

import org.han.ica.asd.c.exceptions.communication.SendGameMessageException;
import org.han.ica.asd.c.messagehandler.messagetypes.GameMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TransactionMessage;
import org.han.ica.asd.c.socketrpc.SocketClient;

import javax.inject.Inject;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class GameMessageSender {

    @Inject
    private SocketClient socketClient;

    @Inject
    public GameMessageSender(){
        //inject
    }

    /**
     * Used for testing
     * constultor for SendInTransaction tests
     * @param socketClient the socket client mock
     */
    public GameMessageSender(SocketClient socketClient){
        this.socketClient = socketClient;
    }

    <T extends GameMessage> T sendGameMessageGeneric(String ip, T gameMessage) throws SendGameMessageException {
        Object response = sendGameMessage(ip, gameMessage);
        T result = null;
        if (response != null) {
            result = (T) response;
        }
        return result;
    }

    Map<String, Object> sendToAll(String[] ips, TransactionMessage transactionMessage) {
        return socketClient.sendToAll(ips,transactionMessage);
    }

    private Object sendGameMessage(String ip, GameMessage gameMessage) throws SendGameMessageException {
        return sendGameMessage(ip, gameMessage, 0);
    }

    private Object sendGameMessage(String ip, GameMessage gameMessage, int tryCount) throws SendGameMessageException {
        int amountOfTries = 3;
        if (tryCount >= amountOfTries) {
            throw new SendGameMessageException("Errors occurred during gamemessage sending");
        }

        try {
            return socketClient.sendObjectWithResponse(ip, gameMessage);
        } catch (SocketTimeoutException e) {
            SendGameMessageException sgme = new SendGameMessageException("Connection failed");
            sgme.addException(e);
            throw sgme;
        } catch (IOException | ClassNotFoundException e) {
            try {
                return sendGameMessage(ip, gameMessage, tryCount + 1);
            } catch (SendGameMessageException sgme) {
                sgme.addException(e);
                throw sgme;
            }
        }
    }
}
