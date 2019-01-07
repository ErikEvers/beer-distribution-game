package org.han.ica.asd.c.messagehandler;

import org.han.ica.asd.c.messagehandler.messagetypes.ResponseMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TransactionMessage;
import org.han.ica.asd.c.messagehandler.sending.SendInTransaction;
import org.han.ica.asd.c.socketrpc.SocketClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContextException;
import org.mockito.Mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SendInTransactionTest {

    private SendInTransaction sendInTransaction;

    @Mock
    TransactionMessage transactionMessage;

    @Mock
    SocketClient socketClient;

    @Mock
    ResponseMessage responseMessage;


    @BeforeEach
    void setup() {
        initMocks(this);

        List<String> ips = new ArrayList<>();
        ips.add("Test");

        sendInTransaction = new SendInTransaction(ips.toArray(new String[0]), transactionMessage, socketClient);
    }

    @Test
    @DisplayName("Test sendToAllPlayers calls the correct method on the socketclient and checks if the phase gets " +
            "updated according to the value of IsSucces with isSucces set to true")
    void TestSendToAllPlayers1() {

        responseMessage = new ResponseMessage(true);
        try {
            when(socketClient.sendObjectWithResponse("Test", transactionMessage)).thenReturn(responseMessage);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        sendInTransaction.sendToAllPlayers();
        verify(transactionMessage).setPhaseToStage();
        verify(transactionMessage).setPhaseToCommit();

    }
}
