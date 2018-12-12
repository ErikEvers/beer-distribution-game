package org.han.ica.asd.c.participants;

import org.han.ica.asd.c.participants.domain_models.AgentParticipant;
import org.han.ica.asd.c.participants.domain_models.PlayerParticipant;
import org.han.ica.asd.c.participants.fakes.PlayerParticipantFake;
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
        when(agent.getParticipantId()).thenReturn(fakePlayer.getParticipantId());
        participantsPool.replaceAgentWithPlayer();
        assertTrue(participantsPool.getParticipants().contains(fakePlayer));
    }

    @Test
    public void replaceAgentWithPlayerRemovesAgent() {
        AgentParticipant agent = mock(AgentParticipant.class);
        when(agent.getParticipantId()).thenReturn("WrongOne");
        participantsPool.replaceAgentWithPlayer();
        assertFalse(participantsPool.getParticipants().contains(agent));
    }

    @Test
    public void removeParticipantRemovesParticipantFromList() {
        AgentParticipant localAgent = mock(AgentParticipant.class);
        //TODO: De agent moet een participantId hebben. Misschien dit met het team / persistance praten.
        when(localAgent.getParticipantId()).thenReturn("firstAgent");
        participantsPool.addParticipant(localAgent);
        int sizeBeforeRemoval = participantsPool.getParticipants().size();
        participantsPool.removeParticipant("firstAgent");
        assertEquals(sizeBeforeRemoval - 1, participantsPool.getParticipants().size());
    }
}