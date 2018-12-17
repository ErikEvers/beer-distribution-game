package org.han.ica.asd.c.messagehandler.sending;

import org.han.ica.asd.c.messagehandler.messagetypes.ResponseMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.RoundModelMessage;
import org.han.ica.asd.c.socketrpc.SocketClient;
import domainobjects.RoundModel;

import java.io.IOException;

public class SendInTransaction {

    String[] ips;
    RoundModel roundModel;
    SocketClient socketClient;

    SendInTransaction(String[] ips, RoundModel roundModel, SocketClient socketClient) {
        this.ips = ips;
        this.roundModel = roundModel;
        this.socketClient = socketClient;

    }

    private int numberOfSuccesses = 0;
    private int numberFinished = 0;
    private int numberOfThreads = 0;

    public void sendRoundToAllPlayers() {

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
            RoundModelMessage rollbackRoundModelMessage = new RoundModelMessage(roundModel, -1);
            send(rollbackRoundModelMessage);
        }
    }

    private void sendCanCommit(String ip, RoundModelMessage roundModelMessage) {

        Thread t = new Thread(() -> {
            try {
                ResponseMessage response = (ResponseMessage) socketClient.sendObjectWithResponse(ip, roundModelMessage);
                canCommitFinished(response.getIsSuccess());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
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

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            t.setDaemon(true);
            t.start();

        }
    }

}
