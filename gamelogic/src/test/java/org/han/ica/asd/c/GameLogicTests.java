package org.han.ica.asd.c;
import org.han.ica.asd.c.exceptions.RoundDataNotFoundException;
import org.han.ica.asd.c.model.*;
import org.han.ica.asd.c.participants.IParticipant;
import org.han.ica.asd.c.participants.ParticipantsPool;
import org.han.ica.asd.c.participants.domain_models.AgentParticipant;
import org.han.ica.asd.c.public_interfaces.ICommunication;
import org.han.ica.asd.c.public_interfaces.IPersistence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;


public class GameLogicTests {
    private GameLogic gameLogic;
    private ParticipantsPool participantsPool;
    private ICommunication communication;
    private IPersistence persistence;

    @BeforeEach
    public void setup() {
        communication = mock(ICommunication.class);
        persistence = mock(IPersistence.class);
        participantsPool = mock(ParticipantsPool.class);
        gameLogic = new GameLogic("test", communication, persistence, participantsPool);
    }

    @Test
    public void placeOrderCallsPersistence() {
        FacilityTurn facilityTurn = mock(FacilityTurn.class);
        gameLogic.placeOrder(facilityTurn);
        verify(persistence, times(1)).saveTurnData(facilityTurn);
    }

    @Test
    public void placeOrderCallsCommunication() {
        FacilityTurn facilityTurn = mock(FacilityTurn.class);
        gameLogic.placeOrder(facilityTurn);
        verify(communication, times(1)).sendTurnData(facilityTurn);
    }

    @Test
    public void seeOtherFacilitiesCallsPersistence() {
        gameLogic.seeOtherFacilities();
        verify(persistence, times(1)).fetchRoundData(anyString(), anyInt());
    }

    @Test
    public void letAgentTakeOverPlayerReplacesPlayer() {
        gameLogic.letAgentTakeOverPlayer(mock(AgentParticipant.class));
        verify(participantsPool, times(1)).replacePlayerWithAgent(any());
    }

    @Test
    public void letPlayerTakeOverAgentReplacesAgent() {
        gameLogic.letPlayerTakeOverAgent();
        verify(participantsPool, times(1)).replaceAgentWithPlayer();
    }
}
