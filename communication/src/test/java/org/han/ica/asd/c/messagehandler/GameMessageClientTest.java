package org.han.ica.asd.c.messagehandler;

import org.han.ica.asd.c.exceptions.communication.LeaderNotPresentException;
import org.han.ica.asd.c.exceptions.communication.SendGameMessageException;
import org.han.ica.asd.c.exceptions.gameleader.FacilityNotAvailableException;
import org.han.ica.asd.c.messagehandler.messagetypes.ChooseFacilityMessage;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.messagehandler.messagetypes.RequestGameDataMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.WhoIsTheLeaderMessage;
import org.han.ica.asd.c.messagehandler.sending.GameMessageClient;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GamePlayerId;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private SocketClient socketClient;

    @BeforeEach
    void setUp() {
        initMocks(this);
        roundDataClient = new GameMessageClient();
        roundDataClient.linkGameMessageSenderToSocketClient(socketClient);
        data = new Round();

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                requestStaticInjection(GameMessageClient.class);
            }
        });

        gameMessageClient = injector.getInstance(GameMessageClient.class);
        gameMessageClient.linkGameMessageSenderToSocketClient(socketClient);

    }

    @Test
    void shouldReturnResponseMessageWithIOException() throws IOException, ClassNotFoundException {
        when(socketClient.sendObjectWithResponse(any(String.class), any(TurnModelMessage.class))).thenThrow(new IOException());
        assertThrows(SendGameMessageException.class, () -> gameMessageClient.sendTurnModel(wrongIp, data));
    }

    @Test
    void shouldReturnResponseMessageWithNoException() throws IOException, ClassNotFoundException, SendGameMessageException {
        TurnModelMessage turnModelMessage = new TurnModelMessage(new Round());
        turnModelMessage.createResponseMessage();
        when(socketClient.sendObjectWithResponse(any(String.class), any(TurnModelMessage.class))).thenReturn(turnModelMessage);
        gameMessageClient.sendTurnModel(wrongIp, data);
    }

    @Test
    public void shouldReturnChooseFacilityMessageWithoutError() throws Exception {
        Facility facility = new Facility();
        facility.setFacilityId(123);
        ChooseFacilityMessage chooseFacilityMessage = new ChooseFacilityMessage(facility);

        when(socketClient.sendObjectWithResponse(any(String.class), any(ChooseFacilityMessage.class))).thenReturn(chooseFacilityMessage);

        ChooseFacilityMessage response = gameMessageClient.sendChooseFacilityMessage(correctIp, facility, null);

        assertNull(response.getException());
    }

    @Test
    public void shouldThrowErrorWhenChooseFacillity() throws IOException, ClassNotFoundException {
        Facility facility = new Facility();
        facility.setFacilityId(123);
        ChooseFacilityMessage chooseFacilityMessage = new ChooseFacilityMessage(facility);
        chooseFacilityMessage.setException(new FacilityNotAvailableException());

        when(socketClient.sendObjectWithResponse(any(String.class), any(ChooseFacilityMessage.class))).thenReturn(chooseFacilityMessage);

        assertThrows(FacilityNotAvailableException.class,()-> gameMessageClient.sendChooseFacilityMessage(correctIp, facility, null));
    }

    @Test
    public void shouldReturnSameChooseFacilityMessage() throws IOException, ClassNotFoundException, FacilityNotAvailableException, SendGameMessageException {
        Facility facility = new Facility();
        facility.setFacilityId(123);
        ChooseFacilityMessage chooseFacilityMessage = new ChooseFacilityMessage(facility);

        when(socketClient.sendObjectWithResponse(any(String.class), any(ChooseFacilityMessage.class))).thenReturn(chooseFacilityMessage);
        assertEquals(chooseFacilityMessage.getClass(), gameMessageClient.sendChooseFacilityMessage(correctIp, facility, null).getClass());
    }

    @Test
    public void shouldReturnGamePlayerIDWithoutError() throws Exception {
        RequestGameDataMessage response = new RequestGameDataMessage();
        response.setGameData(new GamePlayerId(new BeerGame(), "1"));

        when(socketClient.sendObjectWithResponse(any(), any(RequestGameDataMessage.class))).thenReturn(response);
        GamePlayerId responseRequest = gameMessageClient.sendGameDataRequestMessage(correctIp, null);

        assertNotNull(responseRequest);
    }

    @Test
    public void shouldReturnGamePlayerIDWithError() throws SendGameMessageException, IOException, ClassNotFoundException {
        RequestGameDataMessage response = new RequestGameDataMessage();
        response.setException(new Exception());

        when(socketClient.sendObjectWithResponse(any(), any(RequestGameDataMessage.class))).thenReturn(response);
        GamePlayerId responseRequest = gameMessageClient.sendGameDataRequestMessage(correctIp, null);

        assertNotNull(response.getException());
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
    void TestSendWhoIsTheLeaderMessageHappyFlow() throws SendGameMessageException, LeaderNotPresentException, IOException, ClassNotFoundException {
        WhoIsTheLeaderMessage whoIsTheLeaderMessage = new WhoIsTheLeaderMessage();
        whoIsTheLeaderMessage.setResponse("test");
        when(socketClient.sendObjectWithResponse(any(), any())).thenReturn(whoIsTheLeaderMessage);

        String result = gameMessageClient.sendWhoIsTheLeaderMessage("TestIp");

        assertEquals("test", result);
    }

    @Test
    @DisplayName("Test If the sendWhoIsTheLeaderMessage throws catches exception")
    void TestSendWhoIsTheLeaderMessageUnHappyFlow() throws SendGameMessageException, LeaderNotPresentException {
        WhoIsTheLeaderMessage whoIsTheLeaderMessage = new WhoIsTheLeaderMessage();
        whoIsTheLeaderMessage.setResponse("test");
        try {
            when(socketClient.sendObjectWithResponse(any(), any())).thenThrow(new IOException("Error"));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(SendGameMessageException.class, () -> gameMessageClient.sendWhoIsTheLeaderMessage("TestIp"));
    }
}
