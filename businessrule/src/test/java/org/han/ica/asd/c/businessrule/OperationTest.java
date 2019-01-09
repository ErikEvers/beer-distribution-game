package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.operations.*;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OperationTest {
    private Operation operation;

    private Provider<Value> valueProvider;
    private Provider<CalculationOperator> calculationOperatorProvider;
    private Provider<AddOperation> addOperationProvider;
    private Provider<MultiplyOperation> multiplyOperationProvider;
    private Provider<SubtractOperation> subtractOperationProvider;

    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
            }
        });
        valueProvider = injector.getProvider(Value.class);
        calculationOperatorProvider = injector.getProvider(CalculationOperator.class);
        addOperationProvider = injector.getProvider(AddOperation.class);
        multiplyOperationProvider = injector.getProvider(MultiplyOperation.class);
        subtractOperationProvider = injector.getProvider(SubtractOperation.class);
        operation = addOperationProvider.get();
    }
    
    @Test
    void testComparisonValue_Equals_True() {
        operation.addChild(valueProvider.get().addValue("20"));
        operation.addChild(calculationOperatorProvider.get().addValue("-"));
        operation.addChild(valueProvider.get().addValue("4"));

        Operation equalsOperation = addOperationProvider.get();

        equalsOperation.addChild(valueProvider.get().addValue("20"));
        equalsOperation.addChild(calculationOperatorProvider.get().addValue("-"));
        equalsOperation.addChild(valueProvider.get().addValue("4"));

        boolean res = operation.equals(equalsOperation);

        assertTrue(res);
    }

    @Test
    void testComparisonValue_Equals_False() {
        operation.addChild(valueProvider.get().addValue("20"));
        operation.addChild(calculationOperatorProvider.get().addValue("-"));
        operation.addChild(valueProvider.get().addValue("4"));

        Operation equalsOperation = addOperationProvider.get();

        equalsOperation.addChild(valueProvider.get().addValue("20"));
        equalsOperation.addChild(calculationOperatorProvider.get().addValue("-"));
        equalsOperation.addChild(valueProvider.get().addValue("8"));

        boolean res = operation.equals(equalsOperation);

        assertFalse(res);
    }

    @Test
    void testOpertation_getChilderen_False() {
        operation.addChild(valueProvider.get().addValue("20"));
        operation.addChild(calculationOperatorProvider.get().addValue("-"));

        operation.addChild(valueProvider.get().addValue("4"));

        List<ASTNode> res = operation.getChildren();

        List<ASTNode> exp = new ArrayList<>();
        exp.add(valueProvider.get().addValue("20"));
        exp.add(calculationOperatorProvider.get().addValue("-"));
        exp.add(valueProvider.get().addValue("4"));

        assertEquals(exp, res);
    }

    @Test
    void testResolvingMixedOperations() {
        Operation subtractOperation = subtractOperationProvider.get();
        subtractOperation.addChild(multiplyOperationProvider.get()
                .addChild(valueProvider.get().addValue("10"))
                .addChild(calculationOperatorProvider.get().addValue("*"))
                .addChild(valueProvider.get().addValue("2")))
                .addChild(calculationOperatorProvider.get().addValue("-"))
                .addChild(valueProvider.get().addValue("5"));

        Value value = (Value) subtractOperation.resolveOperation();

        assertEquals("15", value.getValue().get(0));
    }
}