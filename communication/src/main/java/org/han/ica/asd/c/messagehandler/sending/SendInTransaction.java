package org.han.ica.asd.c.messagehandler.sending;

import org.han.ica.asd.c.exceptions.communication.TransactionException;
import org.han.ica.asd.c.messagehandler.messagetypes.TransactionMessage;
import org.han.ica.asd.c.socketrpc.SocketClient;
import java.util.Map;

/**
 * Helper class for transactional sending of data.
 *
 * @author Mathijs Bouwmeister, Rogier Grobbee
 */
public class SendInTransaction {

    private String[] ips;
    private TransactionMessage transactionMessage;
    private GameMessageSender gameMessageSender;

    public SendInTransaction(String[] ips, TransactionMessage transactionMessage, GameMessageSender gameMessageSender) {
        this.ips = ips;
        this.transactionMessage = transactionMessage;
        this.gameMessageSender = gameMessageSender;
    }

    /**
     * The beginning of the transaction.
     *
     * @throws TransactionException Throws an exception when something went wrong with transactional sending.
     */
    public void sendToAllPlayers() throws TransactionException {
        transactionMessage.setPhaseToStage();
        handleStagePhase(gameMessageSender.sendToAll(ips, transactionMessage));
    }

    /**
     * Handles the stage phase. Throws an exception when the map contains one.
     * @param map Contains the respond objects.
     * @throws TransactionException Throws an exception when the respond object is an exception.
     */
    private void handleStagePhase(Map<String, Object> map) throws TransactionException {
        boolean allSuccessful = true;

        for(Object value : map.values()) {
            if(value instanceof Exception) {
                allSuccessful = false;
            }
        }

        if(allSuccessful) {
            transactionMessage.setPhaseToCommit();
            gameMessageSender.sendToAll(ips, transactionMessage);
        }

        else {
            transactionMessage.setPhaseToRollback();
            gameMessageSender.sendToAll(ips, transactionMessage);

            throw new TransactionException("Something went wrong with transactional sending data", map);
        }
    }
}
