package org.han.ica.asd.c.messagehandler.receiving;


import org.han.ica.asd.c.interfaces.communication.IGameConfigurationObserver;
import org.han.ica.asd.c.messagehandler.MessageProcessor;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.communication.IElectionObserver;
import org.han.ica.asd.c.interfaces.communication.IRoundModelObserver;
import org.han.ica.asd.c.interfaces.communication.ITurnModelObserver;
import org.han.ica.asd.c.interfaces.communication.IFacilityMessageObserver;
import org.han.ica.asd.c.messagehandler.messagetypes.ChooseFacilityMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.ConfigurationMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.ElectionMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.GameMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.RequestGameDataMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.ResponseMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.RoundModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TransactionMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;

import org.han.ica.asd.c.messagehandler.messagetypes.WhoIsTheLeaderMessage;

import javax.inject.Inject;
import java.util.ArrayList;

import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.CONFIGURATION_MESSAGE;
import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.ELECTION_MESSAGE;
import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.FACILITY_MESSAGE;
import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.REQUEST_GAME_DATA_MESSAGE;
import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.ROUND_MESSAGE;
import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.TURN_MODEL_MESSAGE;
import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.WHO_IS_THE_LEADER_MESSAGE;

public class GameMessageReceiver {

    @Inject
    private GameMessageFilterer gameMessageFilterer;

    @Inject
    private MessageProcessor messageProcessor;

    private ArrayList<IConnectorObserver> gameMessageObservers;

    private TransactionMessage toBecommittedRound;

    public GameMessageReceiver() {
        //inject purposes
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
    public Object gameMessageReceived(GameMessage gameMessage, String senderIp) {
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
                case REQUEST_GAME_DATA_MESSAGE:
                    return handleRequestGameData(senderIp);
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
                    ((IFacilityMessageObserver) observer).chooseFacility(chooseFacilityMessage.getFacility());
                    return chooseFacilityMessage.createResponseMessage();
                }
            }
        }catch(Exception e){
            return chooseFacilityMessage.createResponseMessage(e);
        }
        return null;
    }

    private RequestGameDataMessage handleRequestGameData(String playerIp){
        for (IConnectorObserver observer : gameMessageObservers) {
            if (observer instanceof IFacilityMessageObserver) {
                    RequestGameDataMessage requestAllFacilitiesMessageResponse = new RequestGameDataMessage();
                    requestAllFacilitiesMessageResponse.setGameData(((IFacilityMessageObserver) observer).getGameData(playerIp));
                    return requestAllFacilitiesMessageResponse;
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

    public void setObservers(ArrayList<IConnectorObserver> observers) {
        this.gameMessageObservers = observers;
    }

    public void setGameMessageFilterer(GameMessageFilterer gameMessageFilterer) {
        this.gameMessageFilterer = gameMessageFilterer;
    }
}
