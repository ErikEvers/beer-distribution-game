package org.han.ica.asd.c.messagehandler.receiving;


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
import org.han.ica.asd.c.model.domain_objects.Facility;

import java.util.ArrayList;
import java.util.List;

public class GameMessageReceiver {

    private static RoundModelMessage toBecommittedRound;
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
                case 1:
                    TurnModelMessage turnModelMessage = (TurnModelMessage) gameMessage;
                    return handleTurnMessage(turnModelMessage);
                case 2:
                    RoundModelMessage roundModelMessage = (RoundModelMessage) gameMessage;
                    handleRoundMessage(roundModelMessage);
                    break;
                case 3:
                    ElectionMessage electionMessage = (ElectionMessage) gameMessage;
                    return new ResponseMessage(handleElectionMessage(electionMessage));
                case 4:
                    WhoIsTheLeaderMessage whoIsTheLeaderMessage = (WhoIsTheLeaderMessage) gameMessage;
                    return handleWhoIsTheLeaderMessage(whoIsTheLeaderMessage);
                case 5:
                    FacilityMessage facilityMessage = (FacilityMessage) gameMessage;
                    return null;
                default:
                    break;
            }
        }
        return new ResponseMessage(true);
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
}
