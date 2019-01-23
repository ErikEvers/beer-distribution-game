package org.han.ica.asd.c.messagehandler.receiving;


import org.han.ica.asd.c.Connector;
import org.han.ica.asd.c.exceptions.gameleader.FacilityNotAvailableException;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.communication.IElectionObserver;
import org.han.ica.asd.c.interfaces.communication.IFacilityMessageObserver;
import org.han.ica.asd.c.interfaces.communication.IGameStartObserver;
import org.han.ica.asd.c.interfaces.communication.IRoundModelObserver;
import org.han.ica.asd.c.interfaces.communication.ITurnModelObserver;
import org.han.ica.asd.c.messagehandler.MessageProcessor;
import org.han.ica.asd.c.messagehandler.messagetypes.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.*;

public class GameMessageReceiver {

    @Inject
    private GameMessageFilterer gameMessageFilterer;

    @Inject
    private static MessageProcessor messageProcessor;

    private static ArrayList<IConnectorObserver> gameMessageObservers;

    private TransactionMessage toBecommittedRound;

    public GameMessageReceiver() {
        //inject purposes
    }

    public static void setConnector(Connector connector) {
        MessageProcessor.setConnector(connector);
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
                case GAME_START_MESSAGE:
                case GAME_END_MESSAGE:
                case NEXT_ROUND_MESSAGE:
                    TransactionMessage roundModelMessage = (TransactionMessage) gameMessage;
                    return handleTransactionMessage(roundModelMessage);
                case ELECTION_MESSAGE:
                    ElectionMessage electionMessage = (ElectionMessage) gameMessage;
                    return new ResponseMessage(handleElectionMessage(electionMessage));
                case WHO_IS_THE_LEADER_MESSAGE:
                    WhoIsTheLeaderMessage whoIsTheLeaderMessage = (WhoIsTheLeaderMessage) gameMessage;
                    return handleWhoIsTheLeaderMessage(whoIsTheLeaderMessage);
                case CHOOSE_FACILITY_MESSAGE:
                    ChooseFacilityMessage chooseFacilityMessage = (ChooseFacilityMessage) gameMessage;
                    return handleFacilityMessage(chooseFacilityMessage);
                case REQUEST_GAME_DATA_MESSAGE:
                    return handleRequestGameData(senderIp, ((RequestGameDataMessage) gameMessage).getUserName());
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

    private ChooseFacilityMessage handleFacilityMessage(ChooseFacilityMessage chooseFacilityMessage) {
        try {
            for (IConnectorObserver observer : gameMessageObservers) {
                if (observer instanceof IFacilityMessageObserver) {
                    ((IFacilityMessageObserver) observer).chooseFacility(chooseFacilityMessage.getFacility(), chooseFacilityMessage.getPlayerId());
                    return chooseFacilityMessage.createResponseMessage();
                }
            }

        } catch (FacilityNotAvailableException e) {
            return chooseFacilityMessage.createResponseMessage(e);
        }
        return null;
    }

    private RequestGameDataMessage handleRequestGameData(String playerIp, String userName) {
        for (IConnectorObserver observer : gameMessageObservers) {
            if (observer instanceof IFacilityMessageObserver) {
                RequestGameDataMessage requestGameDataMessageResponse = new RequestGameDataMessage();
                requestGameDataMessageResponse.setGameData(((IFacilityMessageObserver) observer).getGameData(playerIp, userName));
                return requestGameDataMessageResponse;
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
                transactionMessage.createResponseMessage();
                return transactionMessage;
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
                if (observer instanceof IRoundModelObserver && transactionMessage.getMessageType() == ROUND_MESSAGE) {
                    //noinspection ConstantConditions
                    RoundModelMessage roundModelMessage = (RoundModelMessage) transactionMessage;
                    ((IRoundModelObserver) observer).roundModelReceived(roundModelMessage.getPreviousRound(), roundModelMessage.getNewRound());
                    roundModelMessage.createResponseMessage();
                    return roundModelMessage;
                } else if (observer instanceof IGameStartObserver && transactionMessage.getMessageType() == GAME_START_MESSAGE) {
                    //noinspection ConstantConditions
                    GameStartMessage gameStartMessage = (GameStartMessage) transactionMessage;
                    ((IGameStartObserver) observer).gameStartReceived(gameStartMessage.getBeerGame());
                    gameStartMessage.createResponseMessage();
                    return gameStartMessage;
                } else if (observer instanceof IRoundModelObserver && transactionMessage.getMessageType() == GAME_END_MESSAGE){
                    GameEndMessage gameEndMessage = (GameEndMessage) transactionMessage;
                    ((IRoundModelObserver) observer).roundEndReceived(gameEndMessage.getPreviousRound());
                    gameEndMessage.createResponseMessage();
                    return gameEndMessage;
                } else if (observer instanceof IRoundModelObserver && transactionMessage.getMessageType() == NEXT_ROUND_MESSAGE){
                    NextRoundMessage nextRoundMessage = (NextRoundMessage) transactionMessage;
                    ((IRoundModelObserver) observer).nextRoundStarted();
                    return nextRoundMessage;
                }
            }
        }
        return null;
    }

    public static void setObservers(List<IConnectorObserver> observers) {
        gameMessageObservers = (ArrayList<IConnectorObserver>) observers;
    }

    public void setGameMessageFilterer(GameMessageFilterer gameMessageFilterer) {
        this.gameMessageFilterer = gameMessageFilterer;
    }
}