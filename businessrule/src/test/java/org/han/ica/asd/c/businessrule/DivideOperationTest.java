package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ast.operations.DivideOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Operation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertEquals;

class DivideOperationTest {
    @Test
    void testComparisonStatement_Equals_True() {
        DivideOperation divideOperation = new DivideOperation();
        divideOperation.addChild(new Value().addValue("20"));
        divideOperation.addChild(new CalculationOperator("/"));
        divideOperation.addChild(new Value().addValue("4"));

        StringBuilder stringBuilder = new StringBuilder();

        divideOperation.encode(stringBuilder);

        String res = stringBuilder.toString();

        String exp = "Div(V(20)CalO(/)V(4))";

        assertEquals(res, exp);
    }

    @Test
    void testResolvingDivideOperation() {
        Operation divideOperation = new DivideOperation();
        divideOperation.addChild(new Value().addValue("20"))
                .addChild(new CalculationOperator("/"))
                .addChild(new Value().addValue("4"));

        Value value = (Value) divideOperation.resolveOperation();

        Assertions.assertEquals("5", value.getValue().get(0));
    }

    @Test
    void testResolvingMultipleDivideOperations() {
        Operation divideOperation = new DivideOperation();
        divideOperation.addChild(new DivideOperation()
                .addChild(new Value().addValue("20"))
                .addChild(new CalculationOperator("/"))
                .addChild(new Value().addValue("2")))
                .addChild(new CalculationOperator("/"))
                .addChild(new Value().addValue("2"));

        Value value = (Value) divideOperation.resolveOperation();

        Assertions.assertEquals("5", value.getValue().get(0));
    }

    @Test
    void testResolvingDivideOperationWithRoundedResult() {
        Operation divideOperation = new DivideOperation();
        divideOperation.addChild(new Value().addValue("17"))
                .addChild(new CalculationOperator("/"))
                .addChild(new Value().addValue("2"));

        Value value = (Value) divideOperation.resolveOperation();

        Assertions.assertEquals("8", value.getValue().get(0));
    }

}