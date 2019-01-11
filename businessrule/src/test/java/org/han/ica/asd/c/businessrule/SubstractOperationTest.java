package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Operation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.SubtractOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;

import static junit.framework.TestCase.assertEquals;

class SubstractOperationTest {
    private Provider<Value> valueProvider;
    private Provider<CalculationOperator> calculationOperatorProvider;
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
        subtractOperationProvider = injector.getProvider(SubtractOperation.class);
    }
    
    @Test
    void testComparisonStatement_Equals_True() {
        SubtractOperation subtractOperation = subtractOperationProvider.get();
        subtractOperation.addChild(valueProvider.get().addValue("20"));
        subtractOperation.addChild(calculationOperatorProvider.get().addValue("-"));
        subtractOperation.addChild(valueProvider.get().addValue("4"));

        StringBuilder stringBuilder = new StringBuilder();

        subtractOperation.encode(stringBuilder);

        String res = stringBuilder.toString();

        String exp = "Sub(V(20)CalO(-)V(4))";

        assertEquals(res, exp);
    }

    @Test
    void testResolvingSubtractOperation() {
        Operation subtractOperation = subtractOperationProvider.get();
        subtractOperation.addChild(valueProvider.get().addValue("20"))
                .addChild(calculationOperatorProvider.get().addValue("-"))
                .addChild(valueProvider.get().addValue("4"));

        Value value = (Value) subtractOperation.resolveOperation();

        Assertions.assertEquals("16", value.getValue().get(0));
    }

    @Test
    void testResolvingMultipleSubtractOperations() {
        Operation subtractOperation = subtractOperationProvider.get();
        subtractOperation.addChild(subtractOperationProvider.get()
                .addChild(valueProvider.get().addValue("20"))
                .addChild(calculationOperatorProvider.get().addValue("-"))
                .addChild(valueProvider.get().addValue("2")))
                .addChild(calculationOperatorProvider.get().addValue("-"))
                .addChild(valueProvider.get().addValue("2"));

        Value value = (Value) subtractOperation.resolveOperation();

        Assertions.assertEquals("16", value.getValue().get(0));
    }
}