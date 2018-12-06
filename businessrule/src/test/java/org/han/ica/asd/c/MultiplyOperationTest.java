package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.parser.ast.operations.MultiplyOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertEquals;

class MultiplyOperationTest {
    private MultiplyOperation multiplyOperation = new MultiplyOperation();

    @Test
    void testComparisonStatement_Equals_True() {
        multiplyOperation.addChild(new Value().addValue("20"));
        multiplyOperation.addChild(new CalculationOperator("*"));
        multiplyOperation.addChild(new Value().addValue("4"));

        String res = multiplyOperation.toString();

        String exp = "Mul(V(20)CalO(*)V(4))";

        assertEquals(res, exp);
    }
}