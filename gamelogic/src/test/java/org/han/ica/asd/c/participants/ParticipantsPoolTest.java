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

    @BeforeEach
    public void setup() {
        participantsPool = new ParticipantsPool(new PlayerParticipantFake());
    }

    @Test
    void removeParticipantRemovesParticipantFromList() {
        AgentParticipant localAgent = mock(AgentParticipant.class);
        //TODO: De agent moet een participantId hebben. Misschien dit met het team / persistance praten.
        when(localAgent.getParticipantId()).thenReturn("firstAgent");
        participantsPool.addParticipant(localAgent);
        int sizeBeforeRemoval = participantsPool.getParticipants().size();
        participantsPool.removeParticipant("firstAgent");
        assertEquals(sizeBeforeRemoval, participantsPool.getParticipants().size() + 1);
    }
}