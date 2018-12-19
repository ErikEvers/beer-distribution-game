package org.han.ica.asd.c.gamelogic.participants.domain_models;

import org.han.ica.asd.c.gamelogic.participants.IParticipant;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;

/**
 * Wrapper for the player domain class. This wrapper implements the IParticipant interface.
 */
public class PlayerParticipant extends Player implements IParticipant {
    public PlayerParticipant(String gameId, String playerId, String ipAddress, int facilityId, String name, boolean isConnected) {
        super(gameId, playerId, ipAddress, facilityId, name, isConnected);
    }

    public PlayerParticipant(Player player) {
        super(
                player.getGameId(),
                player.getPlayerId(),
                player.getIpAddress(),
                player.getFacilityId(),
                player.getName(),
                player.isConnected()
        );
    }

    /**
     * doOrder will notify the  participant to make an order.
     * @return A FacilityTurn with an order for the current round.
     */
    @Override
    public Round doOrder() {
        //TODO: Implement this method.
        return null;
    }

    /**
     * Returns the identifier for the ParticipantPool to compare with other participants.
     * @return The identifier of the participant.
     */
    @Override
    public int getParticipantId() {
        return this.getFacilityId();
    }
}
