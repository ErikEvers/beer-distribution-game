package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ComparisonValueTest {
    private ComparisonValue comparisonValue = new ComparisonValue();

    @Test
    void testComparisonValue_Equals_True() {
        comparisonValue.addChild(new Value().addValue("hello"));

        ComparisonValue equalComparisonValue = new ComparisonValue();

        equalComparisonValue.addChild(new Value().addValue("hello"));

        boolean res = comparisonValue.equals(equalComparisonValue);

        assertTrue(res);
    }

    @Test
    void testComparisonValue_Equals_True2() {
        boolean res = comparisonValue.equals(comparisonValue);
        assertTrue(res);
    }

    @Test
    void testComparisonValue_Equals_False() {

        comparisonValue.addChild(new Value().addValue("hello"));

        ComparisonValue equalComparisonValue = new ComparisonValue();

        equalComparisonValue.addChild(new Value().addValue("bye"));

        boolean res = comparisonValue.equals(equalComparisonValue);

        assertFalse(res);
    }

    @Test
    void testComparisonValue_Equals_False2() {
        comparisonValue.addChild(new Value().addValue("hello"));

        boolean res = comparisonValue.equals(null);

        assertFalse(res);
    }

    @Test
    void testComparisonValue_getChilderen_False() {
        comparisonValue.addChild(new Value().addValue("hello"));

        List<ASTNode> res = comparisonValue.getChildren();

        List<ASTNode> exp = new ArrayList<>();
        exp.add(new Value().addValue("hello"));

        assertEquals(exp, res);
    }

    @Test
    void testComparisonValue_encode_Called(){
        ComparisonValue cv = new ComparisonValue();
        StringBuilder exp = new StringBuilder();
        StringBuilder res = new StringBuilder();

        cv.encode(res);

        assertEquals(exp.toString(),res.toString());
    }

    @Test
    void testComparisonValue_hashCode_True() {
        ComparisonValue cv1 = new ComparisonValue();
        ComparisonValue cv2 = new ComparisonValue();

        assertEquals(cv1.hashCode() , cv2.hashCode());
    }
}