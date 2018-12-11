package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;

class ComparisonStatementTest {
    private ComparisonStatement comparisonStatement = new ComparisonStatement();

    @Test
    void testComparisonStatement_Equals_True() {
        comparisonStatement.addChild(new Comparison()
                .addChild(new ComparisonValue().addChild(new Value().addValue("round")))
                .addChild(new ComparisonOperator("is"))
                .addChild(new ComparisonValue().addChild(new Value().addValue("11"))));

        ComparisonStatement equalComparison = new ComparisonStatement();
        equalComparison.addChild(new Comparison()
                .addChild(new ComparisonValue().addChild(new Value().addValue("round")))
                .addChild(new ComparisonOperator("is"))
                .addChild(new ComparisonValue().addChild(new Value().addValue("11"))));

        boolean res = comparisonStatement.equals(equalComparison);

        assertTrue(res);
    }

    @Test
    void testComparisonStatement_Equals_False() {
        comparisonStatement.addChild(new Comparison()
                .addChild(new ComparisonValue().addChild(new Value().addValue("round")))
                .addChild(new ComparisonOperator("is"))
                .addChild(new ComparisonValue().addChild(new Value().addValue("11"))));

        ComparisonStatement equalComparison = new ComparisonStatement();
        equalComparison.addChild(new Comparison()
                .addChild(new ComparisonValue().addChild(new Value().addValue("round")))
                .addChild(new ComparisonOperator("higher"))
                .addChild(new ComparisonValue().addChild(new Value().addValue("11"))));

        boolean res = comparisonStatement.equals(equalComparison);

        assertFalse(res);
    }

    @Test
    void testComparisonStatement_getChilderen_False() {
        comparisonStatement.addChild(new Comparison()
                .addChild(new ComparisonValue().addChild(new Value().addValue("round")))
                .addChild(new ComparisonOperator("is"))
                .addChild(new ComparisonValue().addChild(new Value().addValue("11"))));

        List<ASTNode> res = comparisonStatement.getChildren();

        List<ASTNode> exp = new ArrayList<>();
        exp.add(new Comparison()
                .addChild(new ComparisonValue().addChild(new Value().addValue("round")))
                .addChild(new ComparisonOperator("is"))
                .addChild(new ComparisonValue().addChild(new Value().addValue("11"))));

        assertEquals(exp, res);
    }
}