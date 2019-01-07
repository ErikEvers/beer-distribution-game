package org.han.ica.asd.c.messagehandler.sending;

import org.han.ica.asd.c.messagehandler.messagetypes.RoundModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.ResponseMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.WhoIsTheLeaderMessage;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.socketrpc.SocketClient;

import javax.inject.Inject;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameMessageClient {

    @Inject
    private SocketClient socketClient;

    @Inject
    private static  Logger logger;

    public GameMessageClient(){
        //inject purposes
    }

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
<<<<<<< HEAD
                    logger.log(Level.SEVERE, "Something went wrong when trying to connect");
=======
                    LOGGER.log(Level.SEVERE, "Something went wrong when trying to connect", e);
>>>>>>> 47eef9a33686c870fd90ef6d56078ed51c2331d1
                }
            } catch (ClassNotFoundException e) {
                nFailedAttempts++;
                if (nFailedAttempts == 3) {
                    exception = new ClassNotFoundException("Sommething went wrong when reading the object");
<<<<<<< HEAD
                    logger.log(Level.SEVERE, "Something went wrong when reading the object");
=======
                    LOGGER.log(Level.SEVERE, "Something went wrong when reading the object", e);
>>>>>>> 47eef9a33686c870fd90ef6d56078ed51c2331d1
                }
            }
        }
        return new ResponseMessage(false, exception);
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
            logger.log(Level.SEVERE,e.getMessage());
        }
        return whoIsTheLeaderMessageReturn;
    }

    /**
     * This method sends the handled round data back to every peer.
     * @param ips
     * @param roundModel
     */
    public void sendRoundToAllPlayers(String[] ips, Round roundModel) {
        RoundModelMessage roundModelMessage = new RoundModelMessage(roundModel);
        new SendInTransaction(ips, roundModelMessage, socketClient).sendToAllPlayers();
    }

    public void setSocketClient(SocketClient socketClient) {
        this.socketClient = socketClient;
    }
}