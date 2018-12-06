package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.ast.operations.SubtractOperation;
import org.han.ica.asd.c.businessrule.ast.operations.Value;
import org.han.ica.asd.c.businessrule.ast.operators.CalculationOperator;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertEquals;

class SubstractOperationTest {
    private SubtractOperation subtractOperation = new SubtractOperation();

    @Test
    void testComparisonStatement_Equals_True() {
        subtractOperation.addChild(new Value("20"));
        subtractOperation.addChild(new CalculationOperator("-"));
        subtractOperation.addChild(new Value("4"));

        String res = subtractOperation.toString();

        String exp = "Sub(V(20)CalO(-)V(4))";

        assertEquals(res, exp);
    }
}