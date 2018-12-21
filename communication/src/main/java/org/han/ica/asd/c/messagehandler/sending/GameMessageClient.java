package org.han.ica.asd.c.messagehandler.sending;

import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.ResponseMessage;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.socketrpc.SocketClient;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameMessageClient {

    private SocketClient socketClient = new SocketClient();

    private static final Logger LOGGER = Logger.getLogger(GameMessageClient.class.getName());

    /**
     * This method sends turn data to the leader.
     * @param ip
     * @param turn
     * @return ResponseMessage. Can either be with an exception or without, depending whether a connection can be made or not.
     */
    public ResponseMessage sendTurnModel(String ip, Round turn) {
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

    /**
     * This method sends the handled round data back to every peer.
     * @param ips
     * @param roundModel
     */
    public void sendRoundToAllPlayers(String[] ips, Round roundModel) {
        new SendInTransaction(ips, roundModel, socketClient).sendRoundToAllPlayers();
    }

    public void sendConfigurationToAllPlayers(Configuration configuration) {
        new SendInTransaction(ips, configuration, socketClient).sendRoundToAllPlayers();
    }

}
