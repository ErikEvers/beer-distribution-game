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
     * @param currentRoundData the combined data of the entire round.
     */
    Round processFacilityTurn(Round turnModel, Round currentRoundData) {
        currentRoundData.getFacilityOrders().addAll(turnModel.getFacilityOrders());
        currentRoundData.getFacilityTurnDelivers().addAll(turnModel.getFacilityTurnDelivers());
        currentRoundData.getFacilityTurns().addAll(turnModel.getFacilityTurns());
        persistenceLayer.saveFacilityTurn(turnModel);

        return currentRoundData;
    }
}