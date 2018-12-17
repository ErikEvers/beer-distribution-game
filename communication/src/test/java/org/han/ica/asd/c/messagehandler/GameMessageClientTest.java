package org.han.ica.asd.c.messagehandler;

import domainobjects.TurnModel;
import org.han.ica.asd.c.messagehandler.messagetypes.RoundModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.ResponseMessage;
import org.han.ica.asd.c.messagehandler.sending.GameMessageClient;
import org.han.ica.asd.c.socketrpc.SocketClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class GameMessageClientTest {
    private String wrongIp = "145.250.165.238";
    private String correctIp = "145.250.165.239";

    @InjectMocks
    private GameMessageClient gameMessageClient;

    @Mock
    private SocketClient socketClient = new SocketClient();

    @BeforeEach
    public void setUp(){
        initMocks(this);
        gameMessageClient = new GameMessageClient();
        gameMessageClient.setSocketClient(socketClient);
    }

    @Test
    public void shouldReturnResponseMessageWithIOException() throws IOException, ClassNotFoundException {
        TurnModel data = new TurnModel(10);

        String expected = new IOException("Something went wrong when trying to connect").getMessage();

        when(socketClient.sendObjectWithResponse(any(String.class), any(TurnModelMessage.class))).thenThrow(new IOException());


        ResponseMessage responseMessage = gameMessageClient.sendTurnModel(wrongIp, data);

        assertEquals(responseMessage.getException().getMessage(), expected);
        assertNotNull(responseMessage.getException());
    }

    @Test
    public void shouldReturnResponseMessageWithClassNotFoundException() throws IOException, ClassNotFoundException {
        TurnModel data = new TurnModel(10);

        String expected = new ClassNotFoundException("Sommething went wrong when reading the object").getMessage();

        when(socketClient.sendObjectWithResponse(any(String.class), any(TurnModelMessage.class))).thenThrow(new ClassNotFoundException());

        ResponseMessage responseMessage = gameMessageClient.sendTurnModel(wrongIp, data);

        assertEquals(responseMessage.getException().getMessage(), expected);
        assertNotNull(responseMessage.getException());
    }

    @Test
    public void shouldReturnResponseMessageWithNoException() throws IOException, ClassNotFoundException {
        TurnModel data = new TurnModel(10);

        when(socketClient.sendObjectWithResponse(any(String.class), any(TurnModelMessage.class))).thenReturn(new ResponseMessage(true, null));
        ResponseMessage responseMessage = gameMessageClient.sendTurnModel(wrongIp, data);

        assertNull(responseMessage.getException());
    }

// Still unsure on how to test this cause of threading
//    @Test
//    public void sendRoundToAllPlayersSuccess() throws IOException, ClassNotFoundException {
//        RoundModel roundModel = new RoundModel();
//        String[] s = new String[]{wrongIp};
//
//        when(socketClient.sendObjectWithResponse(anyString(), any(RoundModelMessage.class))).thenReturn(new ResponseMessage(true));
//        gameMessageClient.sendRoundToAllPlayers(s, roundModel);
//
//        verify(socketClient).sendObjectWithResponse(anyString(), any(RoundModelMessage.class));
//    }

}
