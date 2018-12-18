package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ast.operations.AddOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Operation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddOperationTest {
    @Test
    void testComparisonStatement_Equals_True() {
        AddOperation addOperation = new AddOperation();
        addOperation.addChild(new Value().addValue("20"));
        addOperation.addChild(new CalculationOperator("+"));
        addOperation.addChild(new Value().addValue("4"));

        StringBuilder stringBuilder = new StringBuilder();

        addOperation.encode(stringBuilder);

        String res = stringBuilder.toString();

        String exp = "Add(V(20)CalO(+)V(4))";

        assertEquals(res, exp);
    }

    @Test
    void testResolvingAddOperation() {
        Operation addOperation = new AddOperation();
        addOperation.addChild(new Value().addValue("20"))
                .addChild(new CalculationOperator("+"))
                .addChild(new Value().addValue("4"));

        Value value = (Value) addOperation.resolveOperation();

        assertEquals("24", value.getValue());
    }

    @Test
    void testResolvingMultipleAddOperations() {
        Operation addOperation = new AddOperation();
        addOperation.addChild(new AddOperation()
                .addChild(new Value().addValue("20"))
                .addChild(new CalculationOperator("+"))
                .addChild(new Value().addValue("4")))
                .addChild(new CalculationOperator("+"))
                .addChild(new Value().addValue("3"));

        Value value = (Value) addOperation.resolveOperation();

        assertEquals("27", value.getValue());
    }
}
