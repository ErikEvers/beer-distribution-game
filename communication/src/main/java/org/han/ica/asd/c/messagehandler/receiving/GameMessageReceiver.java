package org.han.ica.asd.c.messagehandler.receiving;


import org.han.ica.asd.c.messagehandler.MessageProcessor;
import org.han.ica.asd.c.messagehandler.messagetypes.*;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.communication.IElectionObserver;
import org.han.ica.asd.c.interfaces.communication.IRoundModelObserver;
import org.han.ica.asd.c.interfaces.communication.ITurnModelObserver;
import org.han.ica.asd.c.interfaces.communication.IFacilityMessageObserver;
import org.han.ica.asd.c.messagehandler.messagetypes.ElectionMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.GameMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.ResponseMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.RoundModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
import org.han.ica.asd.c.messagehandler.sending.GameMessageClient;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameMessageReceiver {

    private static RoundModelMessage toBecommittedRound;
    private GameMessageFilterer gameMessageFilterer;

    private static final int TURN_MODEL_MESSAGE = 1;
    private static final int ROUND_MESSAGE = 2;
    private static final int ELECTION_MESSAGE = 3;
    private static final int WHO_IS_THE_LEADER_MESSAGE = 4;
    private static final int FACILITY_MESSAGE = 5;
    private static final int REQUEST_ALL_FACILITIES_MESSAGE = 6;

    private ArrayList<IConnectorObserver> gameMessageObservers;

    private MessageProcessor messageProcessor;

    private static final Logger LOGGER = Logger.getLogger(GameMessageClient.class.getName());

    public GameMessageReceiver(List<IConnectorObserver> gameMessageObservers) {
        this.gameMessageObservers = (ArrayList<IConnectorObserver>) gameMessageObservers;
        gameMessageFilterer = new GameMessageFilterer();
        messageProcessor = new MessageProcessor();
    }

    /**
     * This method handles a TurnMessage
     *
     * @param turnModelMessage
     */
    private TurnModelMessage handleTurnMessage(TurnModelMessage turnModelMessage) {
        try {
            for (IConnectorObserver observer : gameMessageObservers) {
                if (observer instanceof ITurnModelObserver) {
                    ((ITurnModelObserver) observer).turnModelReceived(turnModelMessage.getTurnModel());
                }
            }
            return TurnModelMessage.createResponseMessage(true);
        } catch (Exception e) {
            return TurnModelMessage.createResponseMessage(e);
        }
    }

    private ChooseFacilityMessage handleChooseFacilityMessage(ChooseFacilityMessage chooseFacilityMessage) {
        try {
            for (IConnectorObserver observer : gameMessageObservers) {
                if (observer instanceof IFacilityMessageObserver) {
                    ((IFacilityMessageObserver) observer).chooseFacility(chooseFacilityMessage.getFacility());
                }
            }
            return chooseFacilityMessage;
        } catch (Exception e) {
            return chooseFacilityMessage.createResponseMessage(e);
        }
    }

    /**
     * This method handles a RoundMessage
     *
     * @param roundModelMessage
     */
    private void handleRoundMessage(RoundModelMessage roundModelMessage) {
        switch (roundModelMessage.getPhase()) {
            case 0:
                //stage Commit
                toBecommittedRound = roundModelMessage;
                break;
            case 1:
                //do commit
                doCommit(roundModelMessage);
                break;
            case -1:
                //rollback
                toBecommittedRound = null;
                break;
            default:
                break;
        }
    }

    /**
     * Executes a commit
     *
     * @param roundModelMessage
     */
    private void doCommit(RoundModelMessage roundModelMessage) {
        //in theory, a bug can still occur where we receive a commit message with a different content.
        if (toBecommittedRound != null) {
            for (IConnectorObserver observer : gameMessageObservers) {
                if (observer instanceof IRoundModelObserver) {
                    ((IRoundModelObserver) observer).roundModelReceived(roundModelMessage.getRoundModel());
                }
            }
        }
    }

    /**
     * Checks if an incoming GameMessage is unique and then checks what kind of message the GameMessage is. Depending on the type of message, a method is called to further handle the GameMessage.
     *
     * @param gameMessage
     * @return ResponseMessage
     */
    public Object gameMessageReceived(GameMessage gameMessage) {

        if (gameMessageFilterer.isUnique(gameMessage)) {
            switch (gameMessage.getMessageType()) {
                case TURN_MODEL_MESSAGE:
                    TurnModelMessage turnModelMessage = (TurnModelMessage) gameMessage;
                    return handleTurnMessage(turnModelMessage);
                case ROUND_MESSAGE:
                    RoundModelMessage roundModelMessage = (RoundModelMessage) gameMessage;
                    handleRoundMessage(roundModelMessage);
                    break;
                case ELECTION_MESSAGE:
                    ElectionMessage electionMessage = (ElectionMessage) gameMessage;
                    return new ResponseMessage(handleElectionMessage(electionMessage));
                case WHO_IS_THE_LEADER_MESSAGE:
                    WhoIsTheLeaderMessage whoIsTheLeaderMessage = (WhoIsTheLeaderMessage) gameMessage;
                    return handleWhoIsTheLeaderMessage(whoIsTheLeaderMessage);
                case FACILITY_MESSAGE:
                    ChooseFacilityMessage chooseFacilityMessage = (ChooseFacilityMessage) gameMessage;
                    return handleFacilityMessage(chooseFacilityMessage);
                case REQUEST_ALL_FACILITIES_MESSAGE:
                    RequestAllFacilitiesMessage requestAllFacilitiesMessage = (RequestAllFacilitiesMessage) gameMessage;
                    return handleRequestAllFacilities(requestAllFacilitiesMessage);
                default:
                    break;
            }
        }
        // Returning null if the messageType doesn't expect a response.
        return null;
    }

    /**
     * Calls the whoIsTheLeaderMessageReceived method on the 'MessageProcessor".
     *
     * @param whoIsTheLeaderMessage The 'WhoIsTheLeaderMessage' that was received.
     * @return whoIsTheLeaderMessage with the response filled in. This is either the response that was excepted or an exception.
     * @author Oscar
     * @see WhoIsTheLeaderMessage
     * @see MessageProcessor
     */
    private WhoIsTheLeaderMessage handleWhoIsTheLeaderMessage(WhoIsTheLeaderMessage whoIsTheLeaderMessage) {
        return messageProcessor.whoIsTheLeaderMessageReceived(whoIsTheLeaderMessage);
    }

    private ChooseFacilityMessage handleFacilityMessage(ChooseFacilityMessage chooseFacilityMessage){
        try {
            for (IConnectorObserver observer : gameMessageObservers) {
                if (observer instanceof IFacilityMessageObserver) {
                    try {
                        ((IFacilityMessageObserver) observer).chooseFacility(chooseFacilityMessage.getFacility());
                        return chooseFacilityMessage.createResponseMessage();
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, e.getMessage());
                    }
                }
            }
        }catch(Exception e){
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return chooseFacilityMessage.createResponseMessage(e);
        }
        return null;
    }

    private RequestAllFacilitiesMessage handleRequestAllFacilities(RequestAllFacilitiesMessage requestAllFacilitiesMessage){
        for (IConnectorObserver observer : gameMessageObservers) {
            if (observer instanceof IFacilityMessageObserver) {
                try {
                    RequestAllFacilitiesMessage requestAllFacilitiesMessageResponse = new RequestAllFacilitiesMessage();
                    requestAllFacilitiesMessageResponse.setFacilities (((IFacilityMessageObserver) observer).getAllFacilities());
                    return requestAllFacilitiesMessageResponse;
                }catch (Exception e){
                    LOGGER.log(Level.SEVERE,e.getMessage());
                }
            }
        }
        return null;
    }

    /**
     * This method handles an Election message
     *
     * @param electionMessage
     * @return Election
     */
    private Object handleElectionMessage(ElectionMessage electionMessage) {
        for (IConnectorObserver observer : gameMessageObservers) {
            if (observer instanceof IElectionObserver) {
                return ((IElectionObserver) observer).electionReceived(electionMessage.getElection());
            }
        }
        return null;
    }
}
