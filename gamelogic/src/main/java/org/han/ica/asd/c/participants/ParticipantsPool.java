package org.han.ica.asd.c.participants;

import org.han.ica.asd.c.participants.domain_models.AgentParticipant;
import org.han.ica.asd.c.participants.domain_models.PlayerParticipant;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ParticipantsPool {
    private List<IParticipant> participants;
    private PlayerParticipant player;

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
        replaceParticipantWithPlayer(player);
    }

    public void replaceAgentWithPlayer(PlayerParticipant playerParticipant) {
        replaceParticipantWithPlayer(playerParticipant);
    }

    private void replaceParticipantWithPlayer(PlayerParticipant playerParticipant) {
        for (IParticipant participant : participants) {
            if (participant.getParticipantId() == playerParticipant.getParticipantId()) {
                participants.remove(participant);
                participants.add(player);
                return;
            }
        }
    }

    public void removeParticipant(int participantId) {
        for (IParticipant participant : participants) {
            if (participant.getParticipantId() == participantId) {
                participants.remove(participant);
                return;
            }
        }
    }
}
