package org.han.ica.asd.c.messagehandler.sending;

import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.ResponseMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.WhoIsTheLeaderMessage;
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
     * @return boolean depending on whether a connection can be made or not.
     */
    public boolean sendTurnModel(String ip, Round turn) {
        TurnModelMessage turnModelMessage = new TurnModelMessage(turn);

        int nFailedAttempts = 0;

        while (nFailedAttempts < 3) {
            try {
                TurnModelMessage response = socketClient.sendObjectWithResponseGeneric(ip, turnModelMessage);
                return response.isSuccess();
            } catch (IOException e) {
                nFailedAttempts++;
                if (nFailedAttempts == 3) {
                    LOGGER.log(Level.SEVERE, "Something went wrong when trying to connect", e);
                }
            } catch (ClassNotFoundException e) {
                nFailedAttempts++;
                if (nFailedAttempts == 3) {
                    LOGGER.log(Level.SEVERE, "Something went wrong when reading the object", e);
                }
            }
        }

        return false;
    }

    /**
     * Send the whoIsTheLeaderMessage using the socketclient.
     * @author Oscar
     * @param ip The ip to send it to.
     * @return The 'WhoIsTheLeaderMessage' with either the exception or the response filled in.
     * @see WhoIsTheLeaderMessage
     * @see SocketClient
     */
    public WhoIsTheLeaderMessage sendWhoIsTheLeaderMessage(String ip){
        WhoIsTheLeaderMessage whoIsTheLeaderMessageReturn = new WhoIsTheLeaderMessage();
        try {
            whoIsTheLeaderMessageReturn = socketClient.sendObjectWithResponseGeneric(ip, whoIsTheLeaderMessageReturn);
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE,e.getMessage());
        }
        return whoIsTheLeaderMessageReturn;
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



}
