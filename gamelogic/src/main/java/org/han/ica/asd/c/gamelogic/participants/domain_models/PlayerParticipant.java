package org.han.ica.asd.c.gamelogic.participants.domain_models;

import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.model.domain_objects.Facility;

/**
 * Wrapper for the player domain class. This wrapper implements the IParticipant interface.
 */
public class PlayerParticipant extends Player implements IParticipant {
    public PlayerParticipant(String playerId, String ipAddress, Facility facility, String name, boolean isConnected) {
        super(playerId, ipAddress, facility, name, isConnected);
    }

    public PlayerParticipant(Player player) {
        super(
                player.getPlayerId(),
                player.getIpAddress(),
                player.getFacility(),
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
     * Returns the facility for the ParticipantPool to compare with other participants.
     * @return The facility of the participant.
     */
    @Override
    public Facility getParticipant() {
        return this.getFacility();
    }
}
