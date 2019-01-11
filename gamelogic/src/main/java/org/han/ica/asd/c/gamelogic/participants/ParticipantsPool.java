package org.han.ica.asd.c.gamelogic.participants;

import org.han.ica.asd.c.agent.Agent;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.interfaces.player.IPlayerRoundListener;
import org.han.ica.asd.c.model.domain_objects.Facility;

import java.util.LinkedList;
import java.util.List;

/**
 * The ParticipantsPool manages the local participants.
 */
public class ParticipantsPool {
    private List<IParticipant> participants;
    private static IPlayerRoundListener player;

    public ParticipantsPool() {
        participants = new LinkedList<>();
    }

    /**
     * Adds a participant to the pool.
     * @param participant
     */
    public void addParticipant(IParticipant participant) {
        participants.add(participant);
    }

    /**
     * Returns the lost of participants.
     * @return
     */
    public List<IParticipant> getParticipants() {
        return participants;
    }

    /**
     * Replaces the local player with the given agent.
     * @param agent The agent to replace the local player with.
     */
    public void replacePlayerWithAgent(Agent agent) {
        participants.add(agent);
    }

    /**
     * Replaces the agent of the local player with the local player.
     */
    public void replaceAgentWithPlayer() {
        IParticipant participantToRemove = getParticipantByFacilityId(player.getFacilityId());
        if (participantToRemove != null) {
            participants.remove(participantToRemove);
        }
    }

    private IParticipant getParticipantByFacilityId(int facilityId) {
        for (IParticipant participant : participants) {
            if (participant.getParticipant().getFacilityId() == facilityId) {
                return participant;
            }
        }
        return null;
    }

    /**
     * Removes the participant with the given participantId.
     * @param participantFacility Facility of the participant to remove from the pool.
     */
    public void removeParticipant(Facility participantFacility) {
        for (IParticipant participant : participants) {
            if (participant.getParticipant() == participantFacility) {
                participants.remove(participant);
                return;
            }
        }
    }

    public void executeRound() {
        for (IParticipant participant : participants) {
            participant.executeTurn();
        }
    }

    public void setPlayer(IPlayerRoundListener iPlayer) {
        player = iPlayer;
    }
}
