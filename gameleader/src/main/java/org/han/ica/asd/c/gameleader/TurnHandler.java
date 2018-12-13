package org.han.ica.asd.c.gameleader;

import org.han.ica.asd.c.gameleader.componentInterfaces.IPersistence;
import org.han.ica.asd.c.model.FacilityTurn;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TurnHandler {
    private static final Logger LOGGER = Logger.getLogger(TurnHandler.class.getName() );
    @Inject
    private IPersistence persistenceLayer;

    void processFacilityTurn(FacilityTurn turnModel) {
        if(validateFacilityTurn(turnModel)) {
            persistenceLayer.savePlayerTurn(turnModel);
        } else {
            LOGGER.log(Level.WARNING, "Incoming orders can't be higher than the available stock.");
        }
    }

//Currently public for testing purposes
    public boolean validateFacilityTurn(FacilityTurn turnModel) {
        return (turnModel.getOrder() <= turnModel.getStock() && turnModel.getOrder() >= 0);
    }
}
