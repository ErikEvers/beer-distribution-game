package org.han.ica.asd.c.messagehandler.sending;

import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
import domainobjects.RoundModel;
import domainobjects.TurnModel;
import org.han.ica.asd.c.messagehandler.messagetypes.ResponseMessage;
import org.han.ica.asd.c.socketrpc.SocketClient;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameMessageClient {

    private SocketClient socketClient = new SocketClient();

    private static final Logger LOGGER = Logger.getLogger(GameMessageClient.class.getName());

    public ResponseMessage sendTurnModel(String ip, TurnModel turn) {

        TurnModelMessage turnModelMessage = new TurnModelMessage(turn);

        int nFailedAttempts = 0;
        Exception exception = null;

        while (nFailedAttempts < 3) {
            try {
                Object response = socketClient.sendObjectWithResponse(ip, turnModelMessage);
                return (ResponseMessage) response;
            } catch (IOException e) {
                nFailedAttempts++;
                if (nFailedAttempts == 3) {
                    exception = new IOException("Something went wrong when trying to connect");
                    LOGGER.log(Level.SEVERE, "Something went wrong when trying to connect");
                }
            } catch (ClassNotFoundException e) {
                nFailedAttempts++;
                if (nFailedAttempts == 3) {
                    exception = new ClassNotFoundException("Sommething went wrong when reading the object");
                    LOGGER.log(Level.SEVERE, "Something went wrong when reading the object");
                }
            }

        }
        return new ResponseMessage(false, exception);
    }

    public void setSocketClient(SocketClient socketClient) {
        this.socketClient = socketClient;
    }


    public void sendRoundToAllPlayers(String[] ips, RoundModel roundModel) {
        new SendInTransaction(ips, roundModel, socketClient).sendRoundToAllPlayers();
    }



}
