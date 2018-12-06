package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.ast.operations.DivideOperation;
import org.han.ica.asd.c.businessrule.ast.operations.Value;
import org.han.ica.asd.c.businessrule.ast.operators.CalculationOperator;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertEquals;

class DivideOperationTest {
    private DivideOperation divideOperation = new DivideOperation();

    @Test
    void testComparisonStatement_Equals_True() {
        divideOperation.addChild(new Value("20"));
        divideOperation.addChild(new CalculationOperator("/"));
        divideOperation.addChild(new Value("4"));


        String res = divideOperation.toString();

        String exp = "Div(V(20)CalO(/)V(4))";

        assertEquals(res, exp);
    }
}
