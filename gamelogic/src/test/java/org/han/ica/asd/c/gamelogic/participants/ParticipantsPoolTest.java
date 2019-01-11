package org.han.ica.asd.c.gamelogic.participants;

import org.han.ica.asd.c.agent.Agent;
import org.han.ica.asd.c.gamelogic.participants.fakes.PlayerFake;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.interfaces.player.IPlayerRoundListener;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantsPoolTest {
    private ParticipantsPool participantsPool;
    private IPlayerRoundListener fakePlayer;

    @BeforeEach
    public void setup() {
        fakePlayer = mock(PlayerFake.class);
        participantsPool = new ParticipantsPool();
        participantsPool.setPlayer(fakePlayer);
    }
    @Test
    public void replaceAgentWithPlayerRemovesAgent() {
        Agent agent = mock(Agent.class);
        when(agent.getParticipant()).thenReturn(new Facility());
        participantsPool.replaceAgentWithPlayer();
        assertFalse(participantsPool.getParticipants().contains(agent));
    }

    @Test
    public void removeParticipantRemovesParticipantFromList() {
        Agent localAgent = mock(Agent.class);
        Facility facility = new Facility();
        when(localAgent.getParticipant()).thenReturn(facility);
        participantsPool.addParticipant(localAgent);
        int sizeBeforeRemoval = participantsPool.getParticipants().size();
        participantsPool.removeParticipant(facility);
        assertEquals(sizeBeforeRemoval - 1, participantsPool.getParticipants().size());
    }

    @Test
    public void excecuteRoundCallsParticipantsToAction() {
        IParticipant playerMock = mock(IParticipant.class);
        Agent agentMock = mock(Agent.class);
        participantsPool.addParticipant(playerMock);
        participantsPool.addParticipant(agentMock);
        participantsPool.executeRound();
        verify(playerMock, times(1)).executeTurn();
        verify(agentMock, times(1)).executeTurn();
    }
}