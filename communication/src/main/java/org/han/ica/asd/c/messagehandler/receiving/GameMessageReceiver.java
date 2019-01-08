package org.han.ica.asd.c.messagehandler.receiving;


import org.han.ica.asd.c.interfaces.communication.IGameConfigurationObserver;
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

import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.CONFIGURATION_MESSAGE;
import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.ELECTION_MESSAGE;
import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.FACILITY_MESSAGE;
import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.REQUEST_ALL_FACILITIES_MESSAGE;
import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.ROUND_MESSAGE;
import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.TURN_MODEL_MESSAGE;
import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.WHO_IS_THE_LEADER_MESSAGE;

public class GameMessageReceiver {

    private static TransactionMessage toBecommittedRound;
    private GameMessageFilterer gameMessageFilterer;



    private ArrayList<IConnectorObserver> gameMessageObservers;

    private MessageProcessor messageProcessor;

    private static final Logger LOGGER = Logger.getLogger(GameMessageClient.class.getName());

    public GameMessageReceiver(List<IConnectorObserver> gameMessageObservers) {
        this.gameMessageObservers = (ArrayList<IConnectorObserver>) gameMessageObservers;
        gameMessageFilterer = new GameMessageFilterer();
        messageProcessor = new MessageProcessor();
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
     * @param gameMessage The GameMessage that has to be handled
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
                case CONFIGURATION_MESSAGE:
                    ConfigurationMessage configurationMessage = (ConfigurationMessage) gameMessage;
                    return handleTransactionMessage(configurationMessage);
                default:
                    break;
            }
        }
        // Returning null if the messageType doesn't expect a response.
        return null;
    }

    /**
     * This method handles a TurnMessage
     *
     * @param turnModelMessage The TurnModelMessage to be handled
     */
    private TurnModelMessage handleTurnMessage(TurnModelMessage turnModelMessage) {
        try {
            for (IConnectorObserver observer : gameMessageObservers) {
                if (observer instanceof ITurnModelObserver) {
                    ((ITurnModelObserver) observer).turnModelReceived(turnModelMessage.getTurnModel());
                }
            }
            turnModelMessage.createResponseMessage();
            return turnModelMessage;
        } catch (Exception e) {
            turnModelMessage.createResponseMessage(e);
            return turnModelMessage;
        }
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
     * @param electionMessage The ElectionMessage to be handled.
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

    /**
     * Handles the TransactionMessage.
     *
     * @param transactionMessage The TransactionMessage to be handled.
     */
    private TransactionMessage handleTransactionMessage(TransactionMessage transactionMessage) {
        switch (transactionMessage.getPhase()) {
            case 0:
                toBecommittedRound = transactionMessage;
                break;
            case 1:
                return doCommit(transactionMessage);
            case -1:
                toBecommittedRound = null;
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * Executes a commit
     *
     * @param transactionMessage The TransactionMessage to be commited.
     */
    private TransactionMessage doCommit(TransactionMessage transactionMessage) {
        //in theory, a bug can still occur where we receive a commit message with a different content.
        if (toBecommittedRound != null) {
            for (IConnectorObserver observer : gameMessageObservers) {
                if (observer instanceof IRoundModelObserver && transactionMessage.getMessageType() == 2) {
                    //noinspection ConstantConditions
                    RoundModelMessage roundModelMessage = (RoundModelMessage) transactionMessage;
                    ((IRoundModelObserver) observer).roundModelReceived(roundModelMessage.getRoundModel());
                    roundModelMessage.createResponseMessage();
                    return roundModelMessage;
                } else if (observer instanceof IGameConfigurationObserver && transactionMessage.getMessageType() == 7) {
                    //noinspection ConstantConditions
                    ConfigurationMessage configurationMessage = (ConfigurationMessage) transactionMessage;
                    ((IGameConfigurationObserver) observer).gameConfigurationReceived(configurationMessage.getConfiguration());
                    configurationMessage.createResponseMessage();
                    return configurationMessage;
                }
            }
        }
        return null;
    }
}
