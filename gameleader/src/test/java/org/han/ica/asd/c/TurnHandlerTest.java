package org.han.ica.asd.c;

import org.han.ica.asd.c.model.FacilityTurn;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.*;

public class TurnHandlerTest {
    private TurnHandler turnHandler;

    private FacilityTurn facilityTurnModel;

    @BeforeEach
    public void onSetUp() {
        turnHandler = new TurnHandler();
        facilityTurnModel = new FacilityTurn();
    }

    @Test
    public void testDoValidate_OrderAmountIsZero_ReturnTrue() {
        facilityTurnModel.setOrder(0);
        facilityTurnModel.setStock(10);

        assertTrue(turnHandler.validateFacilityTurn(facilityTurnModel));
    }

    @Test
    public void testDoValidate_OrderAmountIsMoreThanStock_ReturnFalse() {
        facilityTurnModel.setOrder(11);
        facilityTurnModel.setStock(10);

        assertFalse(turnHandler.validateFacilityTurn(facilityTurnModel));
    }
}
