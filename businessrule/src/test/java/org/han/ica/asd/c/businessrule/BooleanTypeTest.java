package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ast.operators.BooleanType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BooleanTypeTest {
    @Test
    void testGetBooleanTypeFromAndBooleanSymbolReturnsCorrectBooleanType() {
        BooleanType booleanType = BooleanType.getBooleanTypeFromBooleanSymbol("&&");

        assertEquals(BooleanType.AND, booleanType);
    }

    @Test
    void testGetBooleanTypeFromOrBooleanSymbolReturnsCorrectBooleanType() {
        BooleanType booleanType = BooleanType.getBooleanTypeFromBooleanSymbol("||");

        assertEquals(BooleanType.OR, booleanType);
    }

    @Test
    void testGetBooleanTypeFromNonExistingBooleanSymbolReturnsNull() {
        BooleanType booleanType = BooleanType.getBooleanTypeFromBooleanSymbol("$$");

        assertNull(booleanType);
    }
}
