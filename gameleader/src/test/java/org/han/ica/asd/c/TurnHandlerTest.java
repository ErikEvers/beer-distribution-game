package org.han.ica.asd.c;

import org.han.ica.asd.c.model.FacilityTurn;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.*;

public class TurnHandlerTest {
    private TurnHandler turnHandler;

    private FacilityTurn facilityTurnModel;

    /*@Before
    public void setUp() {
        turnHandler = new TurnHandler();
        facilityTurnModel = new FacilityTurnModel();
        facilityTurnModel.setOrder(new Order());

        facilityTurnModel.getOrder().setOrderAmount(0);
    }*/

    @Test
    public void testDoesValidateFacilityTurnReturnTrue() {
        turnHandler = new TurnHandler();
        facilityTurnModel = new FacilityTurn();
        facilityTurnModel.setOrder(new Order());

        facilityTurnModel.getOrder().setOrderAmount(0);
        facilityTurnModel.setStock(10);
        //turnHandler.validateFacilityTurn(facilityTurnModel);
        assertTrue(turnHandler.validateFacilityTurn(facilityTurnModel));
    }
}
