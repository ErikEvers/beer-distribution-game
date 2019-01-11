package org.han.ica.asd.c.gamelogic.participants;

import org.han.ica.asd.c.agent.Agent;
import org.han.ica.asd.c.gamelogic.participants.domain_models.PlayerParticipant;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameRoundAction;
import org.han.ica.asd.c.model.domain_objects.Round;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

/**
 * The ParticipantsPool manages the local participants.
 */
public class ParticipantsPool {
    private List<IParticipant> participants;
    private PlayerParticipant player;

    @Inject
    public ParticipantsPool(PlayerParticipant playerParticipant) {
        participants = new LinkedList<>();
        participants.add(playerParticipant);
        player = playerParticipant;
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
        participants.remove(player);
        participants.add(agent);
    }

    /**
     * Replaces the agent of the local player with the local player.
     */
    public void replaceAgentWithPlayer() {
        replaceParticipantWithPlayer(player);
    }

    public void replaceAgentWithPlayer(PlayerParticipant playerParticipant) {
        replaceParticipantWithPlayer(playerParticipant);
    }

    /**
     * Replaces the agent that has the same facilityId as the given player.
     * This method is used when a player gets back in to the game.
     * @param playerParticipant The player that got back into the game.
     */
    private void replaceParticipantWithPlayer(PlayerParticipant playerParticipant) {
        for (IParticipant participant : participants) {
            if (participant.getParticipant() == playerParticipant.getParticipant()) {
                participants.remove(participant);
                participants.add(player);
                return;
            }
        }
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

    public void excecuteRound(Round round) {
        //Start round for players
        for (IParticipant participant : participants) {
            participant.executeTurn(round);
        }
    }

}
