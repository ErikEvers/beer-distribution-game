package org.han.ica.asd.c.messagehandler.sending;

import org.han.ica.asd.c.exceptions.gameleader.FacilityNotAvailableException;
import org.han.ica.asd.c.exceptions.communication.TransactionException;
import org.han.ica.asd.c.messagehandler.messagetypes.ChooseFacilityMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.RequestGameDataMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.RoundModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.GameStartMessage;

import org.han.ica.asd.c.messagehandler.messagetypes.WhoIsTheLeaderMessage;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GamePlayerId;
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
    private GameMessageSender gameMessageSender;

    @Inject
    private static Logger logger;

    public GameMessageClient() {
        gameMessageSender = new GameMessageSender(socketClient);
    }

    /**
     * This method sends turn data to the leader.
     *
     * @param ip   The IP to send the turn to.
     * @param turn The turn object to be send.
     * @return ResponseMessage. Can either be with an exception or without, depending whether a connection can be made or not.
     */
    public boolean sendTurnModel(String ip, Round turn) {
        TurnModelMessage turnModelMessage = new TurnModelMessage(turn);
        gameMessageSender.SendGameMessageGeneric()
        TurnModelMessage response = socketClient.sendObjectWithResponseGeneric(ip, turnModelMessage);

        return false;
    }

    /**
     * Send the whoIsTheLeaderMessage using the socketclient.
     *
     * @param ip The ip to send it to.
     * @return The 'WhoIsTheLeaderMessage' with either the exception or the response filled in.
     * @author Oscar
     * @see WhoIsTheLeaderMessage
     * @see SocketClient
     */
    public String sendWhoIsTheLeaderMessage(String ip) {
        WhoIsTheLeaderMessage whoIsTheLeaderMessageReturn = new WhoIsTheLeaderMessage();
        try {
            whoIsTheLeaderMessageReturn = socketClient.sendObjectWithResponseGeneric(ip, whoIsTheLeaderMessageReturn);
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return whoIsTheLeaderMessageReturn.getResponse();
    }

    public ChooseFacilityMessage sendChooseFacilityMessage(String ip, Facility facility) throws FacilityNotAvailableException {
        ChooseFacilityMessage chooseFacilityMessageReturn = new ChooseFacilityMessage(facility);
        try {
            ChooseFacilityMessage response = socketClient.sendObjectWithResponseGeneric(ip, chooseFacilityMessageReturn);
            if (response.getException() != null) {
                throw (FacilityNotAvailableException) response.getException();
            }
            return response;
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return chooseFacilityMessageReturn;
    }

    public GamePlayerId sendGameDataRequestMessage(String ip) throws IOException, ClassNotFoundException {
        RequestGameDataMessage requestAllFacilitiesMessage = new RequestGameDataMessage();
        RequestGameDataMessage response = socketClient.sendObjectWithResponseGeneric(ip, requestAllFacilitiesMessage);
        if (response.getException() != null) {
            logger.log(Level.INFO, response.getException().getMessage(), response.getException());
        }
        return response.getGameData();
    }

    /**
     * This method sends the handled round data back to every peer.
     *
     * @param ips        The ips to send the round to.
     * @param roundModel The round object.
     */
    public void sendRoundToAllPlayers(String[] ips, Round roundModel) throws TransactionException {
        RoundModelMessage roundModelMessage = new RoundModelMessage(roundModel);
        new SendInTransaction(ips, roundModelMessage, socketClient).sendToAllPlayers();
    }

    /**
     * This method sends the GameStart data to every peer.
     *
     * @param ips      The ips to send the GameStart data to.
     * @param beerGame The BeerGame object.
     */
    public void sendStartGameToAllPlayers(String[] ips, BeerGame beerGame) throws TransactionException {
        GameStartMessage gameStartMessage = new GameStartMessage(beerGame);
        new SendInTransaction(ips, gameStartMessage, socketClient).sendToAllPlayers();
    }

    /**
     * Sets new socketClient.
     *
     * @param socketClient New value of socketClient.
     */
    public void setSocketClient(SocketClient socketClient) {
        this.socketClient = socketClient;
    }
}
