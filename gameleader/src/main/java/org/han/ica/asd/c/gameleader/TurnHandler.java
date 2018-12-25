package org.han.ica.asd.c.gameleader;

import org.han.ica.asd.c.interfaces.gameleader.IPersistence;
import org.han.ica.asd.c.model.domain_objects.Round;

import javax.inject.Inject;

public class TurnHandler {
    @Inject
    private IPersistence persistenceLayer;

    /**
     * Saves the incoming turn using the persistence layer and combines incoming turns with already received turns.
     * @param turnModel a turn sent by a game participant.
     */
    Round processFacilityTurn(Round turnModel, Round currentRoundData) {
        turnModel.getTurnOrder().forEach(currentRoundData.getTurnOrder()::putIfAbsent);
        turnModel.getTurnDeliver().forEach(currentRoundData.getTurnDeliver()::putIfAbsent);
        turnModel.getTurnReceived().forEach(currentRoundData.getTurnReceived()::putIfAbsent);
        turnModel.getTurnBackOrder().forEach(currentRoundData.getTurnBackOrder()::putIfAbsent);
        turnModel.getTurnStock().forEach(currentRoundData.getTurnStock()::putIfAbsent);

        persistenceLayer.saveFacilityTurn(turnModel);

        return currentRoundData;
    }
}
