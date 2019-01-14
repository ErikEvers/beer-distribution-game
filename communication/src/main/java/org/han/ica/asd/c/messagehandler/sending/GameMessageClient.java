package org.han.ica.asd.c.messagehandler.sending;

import org.han.ica.asd.c.exceptions.gameleader.FacilityNotAvailableException;
import org.han.ica.asd.c.exceptions.communication.TransactionException;
import org.han.ica.asd.c.exceptions.communication.SendGameMessageException;
import org.han.ica.asd.c.exceptions.communication.LeaderNotPresentException;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameMessageClient {

    @Inject
    private SocketClient socketClient;

    @Inject
    private GameMessageSender gameMessageSender;

    @Inject
    private static Logger logger;

    @Inject
    public GameMessageClient() {
        //inject
    }

    /**
     * This method sends turn data to the leader.
     *
     * @param ip   The IP to send the turn to.
     * @param turn The turn object to be send.
     * @return ResponseMessage. Can either be with an exception or without, depending whether a connection can be made or not.
     */
    public void sendTurnModel(String ip, Round turn) throws SendGameMessageException {
        TurnModelMessage turnModelMessage = new TurnModelMessage(turn);
        TurnModelMessage response = gameMessageSender.sendGameMessageGeneric(ip, turnModelMessage);
        if (response.getException() != null) {
            SendGameMessageException sgme = new SendGameMessageException("Message was arrived, but the receiver encountered an error");
            sgme.addException(response.getException());
            throw sgme;
        }
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
    public String sendWhoIsTheLeaderMessage(String ip) throws SendGameMessageException, LeaderNotPresentException {
        WhoIsTheLeaderMessage whoIsTheLeaderMessage = new WhoIsTheLeaderMessage();
        WhoIsTheLeaderMessage response = gameMessageSender.sendGameMessageGeneric(ip, whoIsTheLeaderMessage);
        if (response.getException() != null){
            throw (LeaderNotPresentException) response.getException();
        }
        return response.getResponse();
    }

    public ChooseFacilityMessage sendChooseFacilityMessage(String ip, Facility facility) throws FacilityNotAvailableException, SendGameMessageException {
        ChooseFacilityMessage chooseFacilityMessage = new ChooseFacilityMessage(facility);
        ChooseFacilityMessage response = gameMessageSender.sendGameMessageGeneric(ip, chooseFacilityMessage);
        if (response.getException() != null) {
            throw (FacilityNotAvailableException) response.getException();
        }
        return response;
    }

    public GamePlayerId sendGameDataRequestMessage(String ip) throws SendGameMessageException {
        RequestGameDataMessage requestAllFacilitiesMessage = new RequestGameDataMessage();
        RequestGameDataMessage response = gameMessageSender.sendGameMessageGeneric(ip, requestAllFacilitiesMessage);
        if (response.getException() != null) {
            //Implement is specific error en throw that, if that is need by the Setup Component.
            //If Implemented, remember to set RequestDataMessage.setException in GameMessageReceiver.
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
        new SendInTransaction(ips, roundModelMessage, gameMessageSender).sendToAllPlayers();
    }

    /**
     * This method sends the GameStart data to every peer.
     *
     * @param ips      The ips to send the GameStart data to.
     * @param beerGame The BeerGame object.
     */
    public void sendStartGameToAllPlayers(String[] ips, BeerGame beerGame) throws TransactionException {
        GameStartMessage gameStartMessage = new GameStartMessage(beerGame);
        new SendInTransaction(ips, gameStartMessage, gameMessageSender).sendToAllPlayers();
    }

    /**
     * Used for testing so set the mocked SocketClient
     *
     * @param socketClient New value of socketClient.
     */
    public void linkGameMessageSenderToSocketClient(SocketClient socketClient) {
        gameMessageSender = new GameMessageSender(socketClient);
    }
}
