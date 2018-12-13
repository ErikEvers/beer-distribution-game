package org.han.ica.asd.c;

import org.han.ica.asd.c.gameleader.TurnHandler;
import org.han.ica.asd.c.gameleader.componentInterfaces.IPersistence;
import org.han.ica.asd.c.model.FacilityTurn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static junit.framework.TestCase.*;

class TurnHandlerTest {
    private FacilityTurn facilityTurnModel;

    private TurnHandler turnHandler;


//    @BeforeEach
//    void onSetUp() {
//        turnHandler = new TurnHandler();
//        facilityTurnModel = new FacilityTurn();
//    }
//
//    @Test
//    void testDoValidate_OrderAmountIsZero_ReturnTrue() {
//        facilityTurnModel.setOrder(0);
//        facilityTurnModel.setStock(10);
//
//        assertTrue(turnHandler.validateFacilityTurn(facilityTurnModel));
//    }
//
//    @Test
//    void testDoValidate_OrderAmountIsLessThanZero_ReturnFalse() {
//        facilityTurnModel.setOrder(-10);
//        facilityTurnModel.setStock(10);
//
//        assertFalse(turnHandler.validateFacilityTurn(facilityTurnModel));
//    }
//
//    @Test
//    void testDoValidate_OrderAmountIsMoreThanStock_ReturnFalse() {
//        facilityTurnModel.setOrder(11);
//        facilityTurnModel.setStock(10);
//
//        assertFalse(turnHandler.validateFacilityTurn(facilityTurnModel));
//    }
//
//    @Test
//    void testDoValidate_OrderAmountEqualToStock_ReturnTrue() {
//        facilityTurnModel.setOrder(10);
//        facilityTurnModel.setStock(10);
//
//        assertTrue(turnHandler.validateFacilityTurn(facilityTurnModel));
//    }

}
