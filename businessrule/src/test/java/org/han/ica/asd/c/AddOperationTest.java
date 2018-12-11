package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.parser.ast.operations.AddOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.*;

class AddOperationTest {
    private AddOperation addOperation = new AddOperation();

    @Test
    void testComparisonStatement_Equals_True() {
        addOperation.addChild(new Value().addValue("20"));
        addOperation.addChild(new CalculationOperator("+"));
        addOperation.addChild(new Value().addValue("4"));

        StringBuilder stringBuilder = new StringBuilder();

        addOperation.encode(stringBuilder);

        String res = stringBuilder.toString();

        String exp = "Add(V(20)CalO(+)V(4))";

        assertEquals(res, exp);
    }
}
