package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.ast.ASTNode;
import org.han.ica.asd.c.businessrule.ast.operations.AddOperation;
import org.han.ica.asd.c.businessrule.ast.operations.Operation;
import org.han.ica.asd.c.businessrule.ast.operations.Value;
import org.han.ica.asd.c.businessrule.ast.operators.CalculationOperator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;

class OperationTest {
    private Operation operation = new AddOperation();

    @Test
    void testComparisonValue_Equals_True() {
        operation.addChild(new Value("20"));
        operation.addChild(new CalculationOperator("-"));
        operation.addChild(new Value("4"));

        Operation equalsOperation = new AddOperation();

        equalsOperation.addChild(new Value("20"));
        equalsOperation.addChild(new CalculationOperator("-"));
        equalsOperation.addChild(new Value("4"));

        boolean res = operation.equals(equalsOperation);

        assertTrue(res);
    }

    @Test
    void testComparisonValue_Equals_False() {
        operation.addChild(new Value("20"));
        operation.addChild(new CalculationOperator("-"));
        operation.addChild(new Value("4"));

        Operation equalsOperation = new AddOperation();

        equalsOperation.addChild(new Value("20"));
        equalsOperation.addChild(new CalculationOperator("-"));
        equalsOperation.addChild(new Value("8"));

        boolean res = operation.equals(equalsOperation);

        assertFalse(res);
    }

    @Test
    void testOpertation_getChilderen_False() {
        operation.addChild(new Value("20"));
        operation.addChild(new CalculationOperator("-"));
        operation.addChild(new Value("4"));


        List<ASTNode> res = operation.getChildren();

        List<ASTNode> exp = new ArrayList<>();
        exp.add(new Value("20"));
        exp.add(new CalculationOperator("-"));
        exp.add(new Value("4"));

        assertEquals(exp, res);
    }
}
