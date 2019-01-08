package org.han.ica.asd.c.messagehandler.sending;

import org.han.ica.asd.c.messagehandler.messagetypes.ChooseFacilityMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.RequestAllFacilitiesMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.RoundModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.WhoIsTheLeaderMessage;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.socketrpc.SocketClient;

import java.io.IOException;
import java.util.List;
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
    public boolean sendTurnModel(String ip, Round turn) {
        TurnModelMessage turnModelMessage = new TurnModelMessage(turn);
        int nFailedAttempts = 0;

        while (nFailedAttempts < 3) {
            try {
                TurnModelMessage response = socketClient.sendObjectWithResponseGeneric(ip, turnModelMessage);
                if (response.getException() != null){
                    LOGGER.log(Level.INFO, response.getException().getMessage(), response.getException());
                }
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

    public ChooseFacilityMessage sendChooseFacilityMessage(String ip, Facility facility){
        ChooseFacilityMessage chooseFacilityMessageReturn = new ChooseFacilityMessage(facility);
        try {
            ChooseFacilityMessage response = null;
            chooseFacilityMessageReturn = socketClient.sendObjectWithResponseGeneric(ip, chooseFacilityMessageReturn);
            if (response.getException() != null){
                LOGGER.log(Level.INFO, response.getException().getMessage(), response.getException());
            }
            return response;
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE,e.getMessage());
        }
        return chooseFacilityMessageReturn;
    }

    public List<Facility> sendAllFacilitiesRequestMessage(String ip){
        RequestAllFacilitiesMessage requestAllFacilitiesMessage = new RequestAllFacilitiesMessage();
        try {
            RequestAllFacilitiesMessage response = null;
            response = socketClient.sendObjectWithResponseGeneric(ip, requestAllFacilitiesMessage);
            if (response.getException() != null){
                LOGGER.log(Level.INFO, response.getException().getMessage(), response.getException());
            }
            return response.getFacilities();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE,e.getMessage());
        }
        return null;
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