package org.han.ica.asd.c.messagehandler.sending;

import org.han.ica.asd.c.messagehandler.messagetypes.ResponseMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TransactionMessage;
import org.han.ica.asd.c.socketrpc.SocketClient;

import javax.inject.Inject;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendInTransaction {

    private String[] ips;
    private TransactionMessage transactionMessage;
    private SocketClient socketClient;

    private int numberOfSuccesses = 0;
    private int numberFinished = 0;
    private int numberOfThreads = 0;

    @Inject
    private static Logger logger;

    public SendInTransaction(String[] ips, TransactionMessage transactionMessage, SocketClient socketClient) {
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
        numberOfSuccesses = 0;
        numberFinished = 0;
        try {
            numberOfThreads = ips.length;
            for (String ip : ips) {
                sendCanCommit(ip);
            }
        } catch (Exception ex) {
            //rollback
            logger.log(Level.INFO, "Stagecommit failed, rolling back");
            transactionMessage.setPhaseToRollback();
            send();
        }
    }

    /**
     * Sends a commit message to an ip.
     * @param ip
     */
    private void sendCanCommit(String ip) {
        Thread t = new Thread(() -> {
            try {
                ResponseMessage response = (ResponseMessage) socketClient.sendObjectWithResponse(ip, transactionMessage);
                canCommitFinished(response.getIsSuccess());

            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        });
        t.setDaemon(true);
        t.start();
    }

    /**
     * Checks if all commits are successful, if so then a commit is staged. If not, then a rollback is staged.
     * @param isSuccess
     */
    private void canCommitFinished(boolean isSuccess) {
        numberFinished++;
        if (isSuccess) {
            numberOfSuccesses++;
        }
        if (numberOfSuccesses == numberOfThreads) {
            //All succeeded, do commit.
            transactionMessage.setPhaseToCommit();
            send();
        } else if (numberFinished == numberOfThreads) {
            //Failure, rollback
            transactionMessage.setPhaseToRollback();
            send();
        }
    }

    /**
     * Send an object to every ip
     */
    private void send() {
        for (String ip : ips) {
            Thread t = new Thread(() -> {
                try {
                    ResponseMessage response = (ResponseMessage) socketClient.sendObjectWithResponse(ip, transactionMessage);

                } catch (IOException | ClassNotFoundException e) {
                    logger.log(Level.SEVERE, e.getMessage(), e);
                }
            });
            t.setDaemon(true);
            t.start();
        }
    }
}
