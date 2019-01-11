package org.han.ica.asd.c.messagehandler.sending;

import org.han.ica.asd.c.exceptions.communication.SendGameMessageException;
import org.han.ica.asd.c.messagehandler.messagetypes.GameMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TransactionMessage;
import org.han.ica.asd.c.socketrpc.SocketClient;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;

public class GameMessageSender {

    private final int AMOUNT_OF_TRIES = 3;

    private SocketClient socketClient;

    public GameMessageSender(){
        //inject
    }
    public GameMessageSender(SocketClient socketClient) {
        this.socketClient = socketClient;
    }

    private Object SendGameMessage(String ip, GameMessage gameMessage) throws SendGameMessageException {
        return SendGameMessage(ip, gameMessage, 0);
    }

    private Object SendGameMessage(String ip, GameMessage gameMessage, int tryCount) throws SendGameMessageException {
        if (tryCount >= AMOUNT_OF_TRIES) {
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
                return SendGameMessage(ip, gameMessage, tryCount + 1);
            } catch (SendGameMessageException sgme) {
                sgme.addException(e);
                throw sgme;
            }
        }
    }

    public <T extends GameMessage> T SendGameMessageGeneric(String ip, T gameMessage) throws SendGameMessageException {
        Object response = SendGameMessage(ip, gameMessage);
        T result = null;
        if (response != null) {
            result = (T) response;
        }
        return result;
    }

    public Map<String, Object> sendToAll(String[] ips, TransactionMessage transactionMessage) {
        CountDownLatch cdl = new CountDownLatch(ips.length);
        Map<String, Object> map = new HashMap<>();

        for (String ip : ips) {
            Thread t = new Thread(() -> {
                try {
                    Object response = SendGameMessage(ip, transactionMessage);
                    map.put(ip, response);
                } catch (SendGameMessageException e) {
                    map.put(ip, e);
                }
                cdl.countDown();
            });
            t.setDaemon(false);
            t.start();
        }

        try {
            cdl.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return map;

    }
}
