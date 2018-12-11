package org.han.ica.asd.c;

import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.Round;

public class TurnHandler {

    public void processFacilityTurn(FacilityTurn turnInformation) {
        validateFacilityTurn(turnInformation);
    }

    public boolean validateFacilityTurn(FacilityTurn turnInformation) {
        if(turnInformation.getOrder().getOrderAmount() <= turnInformation.getStock()
                && turnInformation.getOrder().getOrderAmount() >= 0)
            return true;
        else
            return false;
    }

    public void calculateTurnCosts(Round currentRoundData) {

    }
}
