package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;

class ComparisonValueTest {
    private ComparisonValue comparisonValue = new ComparisonValue();

    @Test
    void testComparisonValue_Equals_True() {
        comparisonValue.addChild(new Value("hello"));

        ComparisonValue equalComparisonValue = new ComparisonValue();

        equalComparisonValue.addChild(new Value("hello"));

        boolean res = comparisonValue.equals(equalComparisonValue);

        assertTrue(res);
    }

    @Test
    void testComparisonValue_Equals_False() {
        comparisonValue.addChild(new Value("hello"));

        ComparisonValue equalComparisonValue = new ComparisonValue();

        equalComparisonValue.addChild(new Value("bye"));

        boolean res = comparisonValue.equals(equalComparisonValue);

        assertFalse(res);
    }

    @Test
    void testComparisonValue_getChilderen_False() {
        comparisonValue.addChild(new Value("hello"));

        List<ASTNode> res = comparisonValue.getChildren();

        List<ASTNode> exp = new ArrayList<>();
        exp.add(new Value("hello"));

        assertEquals(exp, res);
    }
}