package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ast.operations.Operation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.SubtractOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertEquals;

class SubstractOperationTest {

    @Test
    void testComparisonStatement_Equals_True() {
        SubtractOperation subtractOperation = new SubtractOperation();
        subtractOperation.addChild(new Value().addValue("20"));
        subtractOperation.addChild(new CalculationOperator("-"));
        subtractOperation.addChild(new Value().addValue("4"));

        StringBuilder stringBuilder = new StringBuilder();

        subtractOperation.encode(stringBuilder);

        String res = stringBuilder.toString();

        String exp = "Sub(V(20)CalO(-)V(4))";

        assertEquals(res, exp);
    }

    @Test
    void testResolvingSubtractOperation() {
        Operation subtractOperation = new SubtractOperation();
        subtractOperation.addChild(new Value().addValue("20"))
                .addChild(new CalculationOperator("-"))
                .addChild(new Value().addValue("4"));

        Value value = (Value) subtractOperation.resolveOperation();

        Assertions.assertEquals("16", value.getValue().get(0));
    }

    @Test
    void testResolvingMultipleSubtractOperations() {
        Operation subtractOperation = new SubtractOperation();
        subtractOperation.addChild(new SubtractOperation()
                .addChild(new Value().addValue("20"))
                .addChild(new CalculationOperator("-"))
                .addChild(new Value().addValue("2")))
                .addChild(new CalculationOperator("-"))
                .addChild(new Value().addValue("2"));

        Value value = (Value) subtractOperation.resolveOperation();

        Assertions.assertEquals("16", value.getValue().get(0));
    }
}