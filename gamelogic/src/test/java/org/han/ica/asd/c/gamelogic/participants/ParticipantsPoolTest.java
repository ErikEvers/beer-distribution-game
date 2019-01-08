package org.han.ica.asd.c.gamelogic.participants;

import org.han.ica.asd.c.gamelogic.participants.domain_models.AgentParticipant;
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
        participantsPool.replacePlayerWithAgent(mock(AgentParticipant.class));
         assertFalse(participantsPool.getParticipants().contains(fakePlayer));
    }

    @Test
    public void replacePlayerWithAgentAddsAgent() {
        AgentParticipant agent = mock(AgentParticipant.class);
        participantsPool.replacePlayerWithAgent(agent);
        assertTrue(participantsPool.getParticipants().contains(agent));
    }

    @Test
    public void replaceAgentWithPlayerAddsPlayer() {
        AgentParticipant agent = mock(AgentParticipant.class);
        when(agent.getParticipant()).thenReturn(fakePlayer.getParticipant());
        participantsPool.replaceAgentWithPlayer();
        assertTrue(participantsPool.getParticipants().contains(fakePlayer));
    }

    @Test
    public void replaceAgentWithPlayerRemovesAgent() {
        AgentParticipant agent = mock(AgentParticipant.class);
        when(agent.getParticipant()).thenReturn(new Facility());
        participantsPool.replaceAgentWithPlayer();
        assertFalse(participantsPool.getParticipants().contains(agent));
    }

    @Test
    public void removeParticipantRemovesParticipantFromList() {
        AgentParticipant localAgent = mock(AgentParticipant.class);
        Facility facility = new Facility();
        when(localAgent.getParticipant()).thenReturn(facility);
        participantsPool.addParticipant(localAgent);
        int sizeBeforeRemoval = participantsPool.getParticipants().size();
        participantsPool.removeParticipant(facility);
        assertEquals(sizeBeforeRemoval - 1, participantsPool.getParticipants().size());
    }
}