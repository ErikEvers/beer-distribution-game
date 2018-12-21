package org.han.ica.asd.c.messagehandler.receiving;

import org.han.ica.asd.c.messagehandler.messagetypes.*;
import org.han.ica.asd.c.observers.IConnectorObserver;
import org.han.ica.asd.c.observers.IElectionObserver;
import org.han.ica.asd.c.observers.IRoundModelObserver;
import org.han.ica.asd.c.observers.ITurnModelObserver;

import java.util.ArrayList;
import java.util.List;

public class GameMessageReceiver {

    private static RoundModelMessage toBecommittedRound;
    private GameMessageFilterer gameMessageFilterer;

    private ArrayList<IConnectorObserver> gameMessageObservers;

    public GameMessageReceiver(List<IConnectorObserver> gameMessageObservers) {
        this.gameMessageObservers = (ArrayList<IConnectorObserver>) gameMessageObservers;
        gameMessageFilterer = new GameMessageFilterer();
    }

    /**
     * This method handles a TurnMessage
     *
     * @param turnModelMessage
     */
    private void handleTurnMessage(TurnModelMessage turnModelMessage) {
        for (IConnectorObserver observer : gameMessageObservers) {
            if (observer instanceof ITurnModelObserver) {
                ((ITurnModelObserver) observer).turnModelReceived(turnModelMessage.getTurnModel());
            }
        }
    }

    /**
     * This method handles a RoundMessage
     *
     * @param roundModelMessage
     */
    private void handleRoundMessage(RoundModelMessage roundModelMessage) {
        switch (roundModelMessage.getCommitStage()) {
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
    public ResponseMessage gameMessageReceived(GameMessage gameMessage) {

        if (gameMessageFilterer.isUnique(gameMessage)) {
            switch (gameMessage.getMessageType()) {
                case 1:
                    TurnModelMessage turnModelMessage = (TurnModelMessage) gameMessage;
                    handleTurnMessage(turnModelMessage);
                    break;
                case 2:
                    RoundModelMessage roundModelMessage = (RoundModelMessage) gameMessage;
                    handleRoundMessage(roundModelMessage);
                    break;
                case 3:
                    ElectionMessage electionMessage = (ElectionMessage) gameMessage;
                    return new ResponseMessage(handleElectionMessage(electionMessage));
                default:
                    break;
            }
        }
        return new ResponseMessage(true);
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
