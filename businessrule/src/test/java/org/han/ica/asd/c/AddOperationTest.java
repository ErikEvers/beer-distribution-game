package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.ast.operations.AddOperation;
import org.han.ica.asd.c.businessrule.ast.operations.Value;
import org.han.ica.asd.c.businessrule.ast.operators.CalculationOperator;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.*;

class AddOperationTest {
    private AddOperation addOperation = new AddOperation();

    @Test
    void testComparisonStatement_Equals_True() {
        addOperation.addChild(new Value("20"));
        addOperation.addChild(new CalculationOperator("+"));
        addOperation.addChild(new Value("4"));


        String res = addOperation.toString();

        String exp = "Add(V(20)CalO(+)V(4))";

        assertEquals(res, exp);
    }
}
