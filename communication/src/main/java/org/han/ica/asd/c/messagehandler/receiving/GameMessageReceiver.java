package org.han.ica.asd.c.messagehandler.receiving;


import org.han.ica.asd.c.interfaces.communication.IGameConfigurationObserver;
import org.han.ica.asd.c.messagehandler.MessageProcessor;
import org.han.ica.asd.c.messagehandler.messagetypes.*;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.communication.IElectionObserver;
import org.han.ica.asd.c.interfaces.communication.IRoundModelObserver;
import org.han.ica.asd.c.interfaces.communication.ITurnModelObserver;
import org.han.ica.asd.c.messagehandler.messagetypes.ElectionMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.GameMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.ResponseMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.RoundModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;

import java.util.ArrayList;
import java.util.List;

public class GameMessageReceiver {

    private static TransactionMessage toBecommittedRound;
    private GameMessageFilterer gameMessageFilterer;

    private ArrayList<IConnectorObserver> gameMessageObservers;

    private MessageProcessor messageProcessor;

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

    /**
     * Checks if an incoming GameMessage is unique and then checks what kind of message the GameMessage is. Depending on the type of message, a method is called to further handle the GameMessage.
     *
     * @param gameMessage
     * @return ResponseMessage
     */
    public Object gameMessageReceived(GameMessage gameMessage) {
        if (gameMessageFilterer.isUnique(gameMessage)) {
            switch (gameMessage.getMessageType()) {
                case 1:
                    TurnModelMessage turnModelMessage = (TurnModelMessage) gameMessage;
                    return handleTurnMessage(turnModelMessage);
                case 2:
                    TransactionMessage roundModelMessage = (TransactionMessage) gameMessage;
                    handleTransactionMessage(roundModelMessage);
                    break;
                case 3:
                    ElectionMessage electionMessage = (ElectionMessage) gameMessage;
                    return new ResponseMessage(handleElectionMessage(electionMessage));
                case 4:
                    WhoIsTheLeaderMessage whoIsTheLeaderMessage = (WhoIsTheLeaderMessage) gameMessage;
                    return handleWhoIsTheLeaderMessage(whoIsTheLeaderMessage);
                case 5:
                    TransactionMessage configurationMessage = (TransactionMessage) gameMessage;
                    handleTransactionMessage(configurationMessage);
                    break;
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

    private void handleTransactionMessage(TransactionMessage transactionMessage) {
        switch (transactionMessage.getPhase()) {
            case 0:
                toBecommittedRound = transactionMessage;
                break;
            case 1:
                doCommit(transactionMessage);
                break;
            case -1:
                toBecommittedRound = null;
                break;
            default:
                break;
        }
    }

    /**
     * Executes a commit
     *
     * @param transactionMessage
     */
    private void doCommit(TransactionMessage transactionMessage) {
        //in theory, a bug can still occur where we receive a commit message with a different content.
        if (toBecommittedRound != null) {
            for (IConnectorObserver observer : gameMessageObservers) {
                if (observer instanceof IRoundModelObserver) {
                    if (transactionMessage.getMessageType() == 2) {
                        RoundModelMessage roundModelMessage = (RoundModelMessage) transactionMessage;
                        ((IRoundModelObserver) observer).roundModelReceived(roundModelMessage.getRoundModel());
                    }
                } else if (observer instanceof IGameConfigurationObserver) {
                    if (transactionMessage.getMessageType() == 5) {
                        ConfigurationMessage configurationMessage = (ConfigurationMessage) transactionMessage;
                        ((IGameConfigurationObserver) observer).gameConfigurationReceived(configurationMessage.getConfiguration());
                    }
                }
            }
        }
    }
}
