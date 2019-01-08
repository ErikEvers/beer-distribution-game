package org.han.ica.asd.c.messagehandler.sending;

import org.han.ica.asd.c.messagehandler.messagetypes.TransactionMessage;
import org.han.ica.asd.c.socketrpc.SocketClient;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendInTransaction {

    private String[] ips;
    private TransactionMessage transactionMessage;
    private SocketClient socketClient;

    private static final Logger LOGGER = Logger.getLogger(SendInTransaction.class.getName());

    SendInTransaction(String[] ips, TransactionMessage transactionMessage, SocketClient socketClient) {
        this.ips = ips;
        this.transactionMessage = transactionMessage;
        this.socketClient = socketClient;
    }

    /**
     * The beginning of the transaction.
     */
    public void sendToAllPlayers() {
        transactionMessage.setPhaseToStage();
        try {
            handleStagePhase(socketClient.sendToAll(ips, transactionMessage));
        } catch (Exception e) {
            transactionMessage.setPhaseToStage();
        }
    }

    private void handleStagePhase(Map<String, Object> map) throws Exception {
        boolean allSuccessful = true;
        Exception e = null;
        for(Object value : map.values()) {
            if(value instanceof Exception) {
                allSuccessful = false;
                e = (Exception) value;
            }
        }

        if(allSuccessful) {
            transactionMessage.setPhaseToCommit();
            socketClient.sendToAll(ips, transactionMessage);
        }
        else {
            transactionMessage.setPhaseToRollback();
            socketClient.sendToAll(ips, transactionMessage);
            throw e;
        }
    }
}
