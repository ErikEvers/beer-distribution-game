package org.han.ica.asd.c.messagehandler;

import org.han.ica.asd.c.messagehandler.messagetypes.RoundModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.ResponseMessage;
import org.han.ica.asd.c.messagehandler.sending.GameMessageClient;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.socketrpc.SocketClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class GameMessageClientTest {
    private String wrongIp = "145.250.165.238";
    private GameMessageClient roundDataClient;
    private Round data;
    private String correctIp = "145.250.165.239";

    @InjectMocks
    private GameMessageClient gameMessageClient;

    @Mock
    private SocketClient socketClient = new SocketClient();

    @BeforeEach
    void setUp(){
        initMocks(this);
        roundDataClient = new GameMessageClient();
        roundDataClient.setSocketClient(socketClient);
        data = new Round();
        gameMessageClient = new GameMessageClient();
        gameMessageClient.setSocketClient(socketClient);
    }

    @Test
    void shouldReturnResponseMessageWithIOException() throws IOException, ClassNotFoundException {
        when(socketClient.sendObjectWithResponseGeneric(any(String.class), any(TurnModelMessage.class))).thenThrow(new IOException());

        boolean responseMessage = gameMessageClient.sendTurnModel(wrongIp, data);

        assertFalse(responseMessage);
    }

    @Test
    void shouldReturnResponseMessageWithClassNotFoundException() throws IOException, ClassNotFoundException {
        when(socketClient.sendObjectWithResponseGeneric(any(String.class), any(TurnModelMessage.class))).thenThrow(new ClassNotFoundException());
        boolean responseMessage = gameMessageClient.sendTurnModel(wrongIp, data);
        assertFalse(responseMessage);
    }

    @Test
    void shouldReturnResponseMessageWithNoException() throws IOException, ClassNotFoundException {
        TurnModelMessage turnModelMessage = new TurnModelMessage(new Round());
        turnModelMessage.createResponseMessage();
        when(socketClient.sendObjectWithResponseGeneric(any(String.class), any(TurnModelMessage.class))).thenReturn(turnModelMessage);
        boolean responseMessage = gameMessageClient.sendTurnModel(wrongIp, data);

        assertTrue(responseMessage);
    }

// Still unsure on how to test this cause of threading
//    @Test
//    public void sendRoundToAllPlayersSuccess() throws IOException, ClassNotFoundException {
//        RoundModel roundModel = new RoundModel();
//        String[] s = new String[]{wrongIp};
//
//        when(socketClient.sendObjectWithResponse(anyString(), any(RoundModelMessage.class))).thenReturn(new ResponseMessage(true));
//        gameMessageClient.sendToAllPlayers(s, roundModel);
//
//        verify(socketClient).sendObjectWithResponse(anyString(), any(RoundModelMessage.class));
//    }

}
