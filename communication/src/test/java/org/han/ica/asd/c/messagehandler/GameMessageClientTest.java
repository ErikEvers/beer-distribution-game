package org.han.ica.asd.c.messagehandler;

import org.han.ica.asd.c.exceptions.gameleader.FacilityNotAvailableException;
import org.han.ica.asd.c.messagehandler.messagetypes.ChooseFacilityMessage;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.WhoIsTheLeaderMessage;
import org.han.ica.asd.c.messagehandler.sending.GameMessageClient;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.socketrpc.SocketClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    void setUp() {
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

    @Test
    public void shouldReturnChooseFacilityMessageWithoutError() throws Exception {
        Facility facility = new Facility();
        facility.setFacilityId(123);
        ChooseFacilityMessage chooseFacilityMessage = new ChooseFacilityMessage(facility);

        when(socketClient.sendObjectWithResponseGeneric(any(String.class), any(ChooseFacilityMessage.class))).thenReturn(chooseFacilityMessage);
        ChooseFacilityMessage response = gameMessageClient.sendChooseFacilityMessage(correctIp, facility);

        assertNull(response.getException());
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

    @Test
    @DisplayName("Test If the sendWhoIsTheLeaderMessage calls the socketclient")
    void TestSendWhoIsTheLeaderMessageHappyFlow() {
        WhoIsTheLeaderMessage whoIsTheLeaderMessage = new WhoIsTheLeaderMessage();
        whoIsTheLeaderMessage.setResponse("test");
        try {
            when(socketClient.sendObjectWithResponseGeneric(any(), any())).thenReturn(whoIsTheLeaderMessage);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        String result = gameMessageClient.sendWhoIsTheLeaderMessage("TestIp");

        assertEquals("test", result);
    }

    @Test
    @DisplayName("Test If the sendWhoIsTheLeaderMessage throws catches exception")
    void TestSendWhoIsTheLeaderMessageUnHappyFlow() {
        WhoIsTheLeaderMessage whoIsTheLeaderMessage = new WhoIsTheLeaderMessage();
        whoIsTheLeaderMessage.setResponse("test");
        try {
            when(socketClient.sendObjectWithResponseGeneric(any(), any())).thenThrow(new IOException("Error"));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        String result = gameMessageClient.sendWhoIsTheLeaderMessage("TestIp");

        assertNotEquals(whoIsTheLeaderMessage.getResponse(), result);
    }

}
