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
        //TODO implement with this: https://stackoverflow.com/questions/9148899/returning-value-from-thread

        transactionMessage.setPhaseToStage();
        handleStagePhase(socketClient.sendToAll(ips, transactionMessage));
    }

    private void handleStagePhase(Map<String, Object> map) {
        boolean allSuccessful = true;
        for(Object value : map.values()) {
            if(value instanceof Exception)
                allSuccessful = false;
        }

        if(allSuccessful) {
            transactionMessage.setPhaseToCommit();
            socketClient.sendToAll(ips, transactionMessage);
        }
        else {
            transactionMessage.setPhaseToRollback();
            socketClient.sendToAll(ips, transactionMessage);
        }
    }
}
