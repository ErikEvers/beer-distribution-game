package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ast.operations.MultiplyOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Operation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertEquals;

class MultiplyOperationTest {

    @Test
    void testComparisonStatement_Equals_True() {
        MultiplyOperation multiplyOperation = new MultiplyOperation();
        multiplyOperation.addChild(new Value().addValue("20"));
        multiplyOperation.addChild(new CalculationOperator("*"));
        multiplyOperation.addChild(new Value().addValue("4"));

        StringBuilder stringBuilder = new StringBuilder();

        multiplyOperation.encode(stringBuilder);

        String res = stringBuilder.toString();

        String exp = "Mul(V(20)CalO(*)V(4))";

        assertEquals(res, exp);
    }


    @Test
    void testResolvingMultiplyOperation() {
        Operation multiplyOperation = new MultiplyOperation();
        multiplyOperation.addChild(new Value().addValue("20"))
                .addChild(new CalculationOperator("*"))
                .addChild(new Value().addValue("4"));

        Value value = (Value) multiplyOperation.resolveOperation();

        Assertions.assertEquals("80", value.getValue());
    }

    @Test
    void testResolvingMultipleMultiplyOperations() {
        Operation multiplyOperation = new MultiplyOperation();
        multiplyOperation.addChild(new MultiplyOperation()
                .addChild(new Value().addValue("20"))
                .addChild(new CalculationOperator("*"))
                .addChild(new Value().addValue("2")))
                .addChild(new CalculationOperator("*"))
                .addChild(new Value().addValue("2"));

        Value value = (Value) multiplyOperation.resolveOperation();

        Assertions.assertEquals("80", value.getValue());
    }
}