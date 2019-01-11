package org.han.ica.asd.c.gamelogic.participants;

import org.han.ica.asd.c.agent.Agent;
import org.han.ica.asd.c.gamelogic.participants.fakes.PlayerParticipantFake;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantsPoolTest {
    private ParticipantsPool participantsPool;
    private PlayerParticipantFake fakePlayer;

    @BeforeEach
    public void setup() {
        fakePlayer = new PlayerParticipantFake();
        participantsPool = new ParticipantsPool(fakePlayer);
    }

    @Test
    public void replacePlayerWithAgentRemovesPlayer() {
        participantsPool.replacePlayerWithAgent(mock(Agent.class));
         assertFalse(participantsPool.getParticipants().contains(fakePlayer));
    }

    @Test
    public void replacePlayerWithAgentAddsAgent() {
        Agent agent = mock(Agent.class);
        participantsPool.replacePlayerWithAgent(agent);
        assertTrue(participantsPool.getParticipants().contains(agent));
    }

    @Test
    public void replaceAgentWithPlayerAddsPlayer() {
        Agent agent = mock(Agent.class);
        when(agent.getParticipant()).thenReturn(fakePlayer.getParticipant());
        participantsPool.replaceAgentWithPlayer();
        assertTrue(participantsPool.getParticipants().contains(fakePlayer));
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
}