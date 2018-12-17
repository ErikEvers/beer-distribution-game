package org.han.ica.asd.c.messagehandler.receiving;

import org.han.ica.asd.c.messagehandler.messagetypes.*;
import org.han.ica.asd.c.observers.IElectionObserver;
import org.han.ica.asd.c.observers.IConnectorObserver;
import org.han.ica.asd.c.observers.IRoundModelObserver;
import org.han.ica.asd.c.observers.ITurnModelObserver;

import java.util.ArrayList;
import java.util.List;

public class GameMessageReceiver {

    static RoundModelMessage toBecommittedRound;
    private GameMessageFilterer gameMessageFilterer;

    private ArrayList<IConnectorObserver> gameMessageObservers;

    public GameMessageReceiver(List<IConnectorObserver> gameMessageObservers) {
        this.gameMessageObservers = (ArrayList<IConnectorObserver>) gameMessageObservers;
        gameMessageFilterer = new GameMessageFilterer();
    }

    private void handleTurnMessage(TurnModelMessage turnModelMessage) {
        for (IConnectorObserver observer : gameMessageObservers) {
            if (observer instanceof ITurnModelObserver) {
                ((ITurnModelObserver) observer).turnModelReceived(turnModelMessage.getTurnModel());
            }
        }
    }

    private void handleRoundMessage(RoundModelMessage roundModelMessage) {

        switch (roundModelMessage.getCommitStage()){
            case 0:
                //stage Commit
                toBecommittedRound = roundModelMessage;
                break;
            case 1:
                //do commit
                if (toBecommittedRound != null){ //in theory, a bug can still occur where we receive a commit message with a different content.
                    for (IConnectorObserver observer : gameMessageObservers) {
                        if (observer instanceof IRoundModelObserver) {
                            ((IRoundModelObserver) observer).roundModelReceived(roundModelMessage.getRoundModel());
                        }
                    }
                }
                break;
            case -1:
                //rollback
                toBecommittedRound = null;
                break;
        }
    }

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
            }
        }
        return new ResponseMessage(true);
    }

    private Object handleElectionMessage(ElectionMessage electionMessage) {
        for (IConnectorObserver observer : gameMessageObservers) {
            if (observer instanceof IElectionObserver) {
                return ((IElectionObserver) observer).electionReceived(electionMessage.getElection());
            }
        }
        return null;
    }
}
