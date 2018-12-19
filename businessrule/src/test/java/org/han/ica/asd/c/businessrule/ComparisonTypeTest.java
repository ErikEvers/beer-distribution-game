package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ComparisonTypeTest {
    @Test
    void testGetComparisonTypeFromEqualsComparisonSymbolReturnsCorrectComparisonType() {
        ComparisonType comparisonType = ComparisonType.getComparisonTypeFromComparisonSymbol("==");

        assertEquals(ComparisonType.EQUAL, comparisonType);
    }

    @Test
    void testGetComparisonTypeFromNotEqualsComparisonSymbolReturnsCorrectComparisonType() {
        ComparisonType comparisonType = ComparisonType.getComparisonTypeFromComparisonSymbol("!=");

        assertEquals(ComparisonType.NOT, comparisonType);
    }

    @Test
    void testGetComparisonTypeFromGreaterComparisonSymbolReturnsCorrectComparisonType() {
        ComparisonType comparisonType = ComparisonType.getComparisonTypeFromComparisonSymbol(">");

        assertEquals(ComparisonType.GREATER, comparisonType);
    }

    @Test
    void testGetComparisonTypeFromLessComparisonSymbolReturnsCorrectComparisonType() {
        ComparisonType comparisonType = ComparisonType.getComparisonTypeFromComparisonSymbol("<");

        assertEquals(ComparisonType.LESS, comparisonType);
    }

    @Test
    void testGetComparisonTypeFromNonExistingComparisonSymbolReturnsNull() {
        ComparisonType comparisonType = ComparisonType.getComparisonTypeFromComparisonSymbol("$$");

        assertNull(comparisonType);
    }
}
