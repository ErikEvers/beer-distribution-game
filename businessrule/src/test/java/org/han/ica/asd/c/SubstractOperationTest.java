package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.parser.ast.operations.SubtractOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertEquals;

class SubstractOperationTest {
    private SubtractOperation subtractOperation = new SubtractOperation();

    @Test
    void testComparisonStatement_Equals_True() {
        subtractOperation.addChild(new Value().addValue("20"));
        subtractOperation.addChild(new CalculationOperator("-"));
        subtractOperation.addChild(new Value().addValue("4"));

        StringBuilder stringBuilder = new StringBuilder();

        subtractOperation.encode(stringBuilder);

        String res = stringBuilder.toString();

        String exp = "Sub(V(20)CalO(-)V(4))";

        assertEquals(res, exp);
    }
}