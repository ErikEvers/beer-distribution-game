package org.han.ica.asd.c.participants.domain_models;

import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.Player;
import org.han.ica.asd.c.participants.IParticipant;

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

    @Override
    public FacilityTurn doOrder() {
        //TODO: Implement this method.
        return null;
    }

    @Override
    public int getParticipantId() {
        return this.getFacilityId();
    }
}
