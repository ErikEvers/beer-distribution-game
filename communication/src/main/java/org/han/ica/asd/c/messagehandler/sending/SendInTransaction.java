package org.han.ica.asd.c.messagehandler.sending;

import org.han.ica.asd.c.messagehandler.messagetypes.ResponseMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.RoundModelMessage;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.socketrpc.SocketClient;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendInTransaction {

    private String[] ips;
    private Round roundModel;
    private SocketClient socketClient;

    private static final Logger LOGGER = Logger.getLogger(SendInTransaction.class.getName());

    SendInTransaction(String[] ips, Round roundModel, SocketClient socketClient) {
        this.ips = ips;
        this.roundModel = roundModel;
        this.socketClient = socketClient;

    }

    private int numberOfSuccesses = 0;
    private int numberFinished = 0;
    private int numberOfThreads = 0;

    public void sendRoundToAllPlayers() {

        //TODO implement with this: https://stackoverflow.com/questions/9148899/returning-value-from-thread

        RoundModelMessage roundModelMessage = new RoundModelMessage(roundModel, 0);

        numberOfSuccesses = 0;
        numberFinished = 0;
        try {
            numberOfThreads = ips.length;
            for (String ip : ips) {
                sendCanCommit(ip, roundModelMessage);
            }
        } catch (Exception ex) {
            //rollback
            LOGGER.log(Level.INFO, "Stagecommit failed, rolling back");
            RoundModelMessage rollbackRoundModelMessage = new RoundModelMessage(roundModel, -1);
            send(rollbackRoundModelMessage);
        }
    }

    private void sendCanCommit(String ip, RoundModelMessage roundModelMessage) {

        Thread t = new Thread(() -> {
            try {
                ResponseMessage response = (ResponseMessage) socketClient.sendObjectWithResponse(ip, roundModelMessage);
                canCommitFinished(response.getIsSuccess());

            } catch (IOException | ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private void canCommitFinished(boolean isSuccess) {
        numberFinished++;
        if (isSuccess) {
            numberOfSuccesses++;
        }
        if (numberOfSuccesses == numberOfThreads) {
            //All succeeded, do commit.
            RoundModelMessage roundModelMessage = new RoundModelMessage(roundModel, 1);
            send(roundModelMessage);

        } else if (numberFinished == numberOfThreads) {
            //Failure, rollback
            RoundModelMessage roundModelMessage = new RoundModelMessage(roundModel, -1);
            send(roundModelMessage);
        }
    }

    private void send(RoundModelMessage roundModelMessage) {
        for (String ip : ips) {

            Thread t = new Thread(() -> {
                try {
                    ResponseMessage response = (ResponseMessage) socketClient.sendObjectWithResponse(ip, roundModelMessage);

                } catch (IOException | ClassNotFoundException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage());
                }
            });
            t.setDaemon(true);
            t.start();

        }
    }

}
