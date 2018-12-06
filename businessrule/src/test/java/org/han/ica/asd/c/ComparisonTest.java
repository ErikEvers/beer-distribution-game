package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.ast.ASTNode;
import org.han.ica.asd.c.businessrule.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.ast.operations.Value;
import org.han.ica.asd.c.businessrule.ast.operators.ComparisonOperator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;

class ComparisonTest {

    private Comparison comparison = new Comparison();


    @Test
    void testComparison_Equals_True() {
        comparison.addChild(new ComparisonValue().addChild(new Value("inventory")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value("20")));

        Comparison equalComparison = new Comparison();
        equalComparison.addChild(new ComparisonValue().addChild(new Value("inventory")));
        equalComparison.addChild(new ComparisonOperator("is"));
        equalComparison.addChild(new ComparisonValue().addChild(new Value("20")));

        boolean res = comparison.equals(equalComparison);

        assertTrue(res);
    }

    @Test
    void testComparison_Equals_False() {
        System.out.println("Test");
        comparison.addChild(new ComparisonValue().addChild(new Value("inventory")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value("30")));

        Comparison equalComparison = new Comparison();
        equalComparison.addChild(new ComparisonValue().addChild(new Value("inventory")));
        equalComparison.addChild(new ComparisonOperator("is"));
        equalComparison.addChild(new ComparisonValue().addChild(new Value("20")));

        boolean res = comparison.equals(equalComparison);

        assertFalse(res);
    }

    @Test
    void testComparison_getChilderen_False() {
        comparison.addChild(new ComparisonValue().addChild(new Value("inventory")));
        comparison.addChild(new ComparisonOperator("is"));
        comparison.addChild(new ComparisonValue().addChild(new Value("30")));


        List<ASTNode> res = comparison.getChildren();

        List<ASTNode> exp = new ArrayList<>();
        exp.add(new ComparisonValue().addChild(new Value("inventory")));
        exp.add(new ComparisonOperator("is"));
        exp.add(new ComparisonValue().addChild(new Value("30")));

        assertEquals(exp, res);
    }

}
