package org.han.ica.asd.c.messagehandler;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                requestStaticInjection(GameMessageClient.class);
            }
        });

        gameMessageClient = injector.getInstance(GameMessageClient.class);
        gameMessageClient.setSocketClient(socketClient);
    }

    @Test
    void shouldReturnResponseMessageWithIOException() throws IOException, ClassNotFoundException {
        String expected = new IOException("Something went wrong when trying to connect").getMessage();

        when(socketClient.sendObjectWithResponseGeneric(any(String.class), any(TurnModelMessage.class))).thenThrow(new IOException());

        boolean responseMessage = gameMessageClient.sendTurnModel(wrongIp, data);
        assertFalse(responseMessage);
        //assertEquals(responseMessage.getException().getMessage(), expected);
        //assertNotNull(responseMessage.getException());
    }

    @Test
    void shouldReturnResponseMessageWithClassNotFoundException() throws IOException, ClassNotFoundException {
        String expected = new ClassNotFoundException("Sommething went wrong when reading the object").getMessage();

        when(socketClient.sendObjectWithResponseGeneric(any(String.class), any(TurnModelMessage.class))).thenThrow(new ClassNotFoundException());

        boolean responseMessage = gameMessageClient.sendTurnModel(wrongIp, data);

        assertFalse(responseMessage);
    }

    @Test
    void shouldReturnResponseMessageWithNoException() throws IOException, ClassNotFoundException {
        when(socketClient.sendObjectWithResponseGeneric(any(String.class), any(TurnModelMessage.class))).thenReturn(TurnModelMessage.createResponseMessage(true));
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
