package org.han.ica.asd.c.participants;

import org.han.ica.asd.c.participants.domain_models.AgentParticipant;
import org.han.ica.asd.c.participants.domain_models.PlayerParticipant;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ParticipantsPool {
    private List<IParticipant> participants;

    public ParticipantsPool(PlayerParticipant playerParticipant) {
        participants = new LinkedList<>();
        participants.add(playerParticipant);
    }

    public void removeParticipant(String participantId) {
        for (IParticipant participant : participants) {
            if (participant.getParticipantId() == participantId) {
                participants.remove(participant);
                return;
            }
        }
    }

    public void addParticipant(IParticipant participant) {
        participants.add(participant);
    }

    public List<IParticipant> getParticipants() {
        return participants;
    }
}
