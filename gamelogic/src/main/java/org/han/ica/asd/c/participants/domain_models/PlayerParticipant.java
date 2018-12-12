package org.han.ica.asd.c.participants.domain_models;

import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.Player;
import org.han.ica.asd.c.participants.IParticipant;

public class PlayerParticipant extends Player implements IParticipant {
    public PlayerParticipant(String gameId, String playerId, String ipAddress, int facilityId, String name, boolean isConnected) {
        super(gameId, playerId, ipAddress, facilityId, name, isConnected);
    }

    @Override
    public FacilityTurn doOrder() {
        return null;
    }
}
