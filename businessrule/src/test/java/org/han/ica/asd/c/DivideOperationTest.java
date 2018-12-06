package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.parser.ast.operations.DivideOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertEquals;

class DivideOperationTest {
    private DivideOperation divideOperation = new DivideOperation();

    @Test
    void testComparisonStatement_Equals_True() {
        divideOperation.addChild(new Value().addValue("20"));
        divideOperation.addChild(new CalculationOperator("/"));
        divideOperation.addChild(new Value().addValue("4"));

        String res = divideOperation.toString();

        String exp = "Div(V(20)CalO(/)V(4))";

        assertEquals(res, exp);
    }
}