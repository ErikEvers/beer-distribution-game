package org.han.ica.asd.c.participants;

import org.han.ica.asd.c.participants.domain_models.AgentParticipant;
import org.han.ica.asd.c.participants.domain_models.PlayerParticipant;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ParticipantsPool {
    private List<IParticipant> participants;
    private IParticipant player;

    public ParticipantsPool(PlayerParticipant playerParticipant) {
        participants = new LinkedList<>();
        participants.add(playerParticipant);
        player = playerParticipant;
    }

    public void addParticipant(IParticipant participant) {
        participants.add(participant);
    }

    public List<IParticipant> getParticipants() {
        return participants;
    }

    public void replacePlayerWithAgent(AgentParticipant agent) {
        participants.remove(player);
        participants.add(agent);
    }

    public void replaceAgentWithPlayer() {
        for (IParticipant participant : participants) {
            if (participant.getParticipantId().equals(player.getParticipantId())) {
                participants.remove(participant);
                participants.add(player);
                return;
            }
        }
    }

    public void removeParticipant(String participantId) {
        for (IParticipant participant : participants) {
            //TODO: Do this with facility id.
            if (participant.getParticipantId() == participantId) {
                participants.remove(participant);
                return;
            }
        }
    }
}
