package org.han.ica.asd.c;

import org.han.ica.asd.c.gamevalue.GameValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameValueTest {

    @Test
    public void checkIfFacilityFalse(){
        assertFalse(GameValue.checkIfFacility("pinda"));
    }

    @Test
    public void checkIfFacilityTrue(){
        assertTrue(GameValue.checkIfFacility("retailer"));
    }
}
