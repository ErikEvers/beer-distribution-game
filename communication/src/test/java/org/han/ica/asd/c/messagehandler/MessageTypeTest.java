package org.han.ica.asd.c.messagehandler;

import org.apache.ibatis.transaction.TransactionException;
import org.han.ica.asd.c.messagehandler.messagetypes.ChooseFacilityMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.ElectionMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.GameMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.GameStartMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.MessageIds;
import org.han.ica.asd.c.messagehandler.messagetypes.RequestGameDataMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.ResponseMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.RoundModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TransactionMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.WhoIsTheLeaderMessage;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GamePlayerId;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.model.interface_models.ElectionModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class MessageTypeTest {

    private GameMessage gameMessage;

    @BeforeEach
    void setup() {

    }

    @Test
    void TestGameMessage() {
        ElectionModel electionModel = mock(ElectionModel.class);
        gameMessage = new ElectionMessage(electionModel);

        UUID expected = new UUID(0, 1);
        gameMessage.setMessageId(expected);
        Exception exception = mock(TransactionException.class);
        gameMessage.setException(exception);

        assertEquals(expected, gameMessage.getMessageId());
        assertEquals(exception, gameMessage.getException());
    }

    @Test
    void TestTransactionMessage(){
        BeerGame beerGameMock = mock(BeerGame.class);
        TransactionMessage transactionMessage = new GameStartMessage(beerGameMock);

        UUID id1 = transactionMessage.getMessageId();
        transactionMessage.setPhaseToCommit();
        assertNotEquals(id1, transactionMessage.getMessageId());
        assertEquals(1, transactionMessage.getPhase());

        id1 = transactionMessage.getMessageId();
        transactionMessage.setPhaseToStage();
        assertNotEquals(id1, transactionMessage.getMessageId());
        assertEquals(0, transactionMessage.getPhase());

        id1 = transactionMessage.getMessageId();
        transactionMessage.setPhaseToRollback();
        assertNotEquals(id1, transactionMessage.getMessageId());
        assertEquals(-1, transactionMessage.getPhase());

        assertFalse(transactionMessage.getIsSuccess());
        transactionMessage.createResponseMessage();
        assertTrue(transactionMessage.getIsSuccess());
    }

    @Test
    void TestElectionMessage() {
        ElectionModel electionModel = mock(ElectionModel.class);
        gameMessage = new ElectionMessage(electionModel);

        assertEquals(electionModel, ((ElectionMessage) gameMessage).getElection());
        assertEquals(gameMessage.getMessageType(), MessageIds.ELECTION_MESSAGE);
    }

    @Test
    void TestChooseFacilityMessageWithFirstConstructor() {
        Facility facilityMock = mock(Facility.class);

        gameMessage = new ChooseFacilityMessage(facilityMock);

        assertEquals(gameMessage.getMessageType(), MessageIds.CHOOSE_FACILITY_MESSAGE);
        assertEquals(facilityMock, ((ChooseFacilityMessage) gameMessage).getFacility());

        Exception exceptionMock = mock(TransactionException.class);
        ChooseFacilityMessage result = ((ChooseFacilityMessage) gameMessage).createResponseMessage(exceptionMock);

        assertEquals(exceptionMock, result.getException());

        ChooseFacilityMessage result2 = ((ChooseFacilityMessage) gameMessage).createResponseMessage();

        assertNull(result2.getException());
    }

    @Test
    void TestChooseFacilityMessageWithSecondConstructor() {
        Facility facilityMock = mock(Facility.class);


        gameMessage = new ChooseFacilityMessage(facilityMock);

        assertEquals(facilityMock, ((ChooseFacilityMessage) gameMessage).getFacility());
    }

    @Test
    void TestGameStartMessage() {
        BeerGame beerGame = mock(BeerGame.class);
        gameMessage = new GameStartMessage(beerGame);

        assertEquals(beerGame, ((GameStartMessage) gameMessage).getBeerGame());
        assertEquals(MessageIds.GAME_START_MESSAGE, gameMessage.getMessageType());
    }

    @Test
    void TestRequestGameDataMessage() {
        gameMessage = new RequestGameDataMessage();
        GamePlayerId gamePlayerIdMock = mock(GamePlayerId.class);

        ((RequestGameDataMessage) gameMessage).setGameData(gamePlayerIdMock);

        assertEquals(MessageIds.REQUEST_GAME_DATA_MESSAGE, gameMessage.getMessageType());
        assertEquals(gamePlayerIdMock, ((RequestGameDataMessage) gameMessage).getGameData());
    }

    @Test
    void TestResponseMessageWithFirstConstructor() {
        Exception exceptionMock = mock(TransactionException.class);
        ResponseMessage responseMessage = new ResponseMessage(true, exceptionMock);

        assertTrue(responseMessage.getIsSuccess());
        assertEquals(exceptionMock, responseMessage.getException());
        assertNull(responseMessage.getResponseObject());
    }

    @Test
    void TestResponseMessageWithSecondConstructor() {
        ResponseMessage responseMessage = new ResponseMessage(true);

        assertTrue(responseMessage.getIsSuccess());
        assertNull(responseMessage.getException());
        assertNull(responseMessage.getResponseObject());
    }

    @Test
    void TestResponseMessageWithThirdConstructor() {
        Exception exceptionMock = mock(TransactionException.class);
        ResponseMessage responseMessage = new ResponseMessage(exceptionMock);

        assertEquals(exceptionMock, responseMessage.getResponseObject());
        assertTrue(responseMessage.getIsSuccess());
        assertNull(responseMessage.getException());
    }

    @Test
    void TestRoundModelMessage() {
        Round roundMock = mock(Round.class);
        gameMessage = new RoundModelMessage(roundMock);
        assertEquals(MessageIds.ROUND_MESSAGE, gameMessage.getMessageType());
        assertEquals(roundMock, ((RoundModelMessage) gameMessage).getRoundModel());
    }

    @Test
    void TestTurnModelMessage() {
        Exception exceptionMock = mock(TransactionException.class);
        Round roundMock = mock(Round.class);
        gameMessage = new TurnModelMessage(roundMock);

        assertEquals(MessageIds.TURN_MODEL_MESSAGE, gameMessage.getMessageType());

        assertEquals(roundMock, ((TurnModelMessage) gameMessage).getTurnModel());
        assertNull(gameMessage.getException());

        ((TurnModelMessage) gameMessage).createResponseMessage();
        assertNull(gameMessage.getException());
        assertNull(((TurnModelMessage) gameMessage).getTurnModel());

        ((TurnModelMessage) gameMessage).createResponseMessage(exceptionMock);
        assertEquals(exceptionMock, gameMessage.getException());
        assertNull(((TurnModelMessage) gameMessage).getTurnModel());
    }

    @Test
    void TestWhoIsTheLeaderMessage() {
        gameMessage = new WhoIsTheLeaderMessage();
        ((WhoIsTheLeaderMessage) gameMessage).setResponse("Response");

        assertEquals(MessageIds.WHO_IS_THE_LEADER_MESSAGE, gameMessage.getMessageType());
        assertEquals("Response", ((WhoIsTheLeaderMessage) gameMessage).getResponse());
    }

}
