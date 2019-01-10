package org.han.ica.asd.c.messagehandler.sending;

import org.han.ica.asd.c.messagehandler.exceptions.SendGameMessageException;
import org.han.ica.asd.c.messagehandler.messagetypes.GameMessage;
import org.han.ica.asd.c.socketrpc.SocketClient;

import javax.inject.Inject;
import java.io.IOException;
import java.net.SocketTimeoutException;

public class GameMessageSender {

    private final int AMOUNT_OF_TRIES = 3;

    @Inject
    SocketClient socketClient;

    public GameMessageSender() {
        //for injection
    }

    private Object SendGameMessage(String ip, GameMessage gameMessage) throws SendGameMessageException {
        return SendGameMessage(ip, gameMessage, 0);
    }

    private Object SendGameMessage(String ip, GameMessage gameMessage, int tryCount) throws SendGameMessageException {
        if (tryCount >= AMOUNT_OF_TRIES){
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
            }catch (SendGameMessageException sgme){
                sgme.addException(e);
                throw sgme;
            }
        }
    }

    public <T> T SendGameMessageGeneric(String ip, T gameMessage) {


        return null;
    }
}
