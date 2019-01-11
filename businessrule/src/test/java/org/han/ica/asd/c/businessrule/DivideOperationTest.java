package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.businessrule.parser.ast.operations.DivideOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Operation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;

import static junit.framework.TestCase.assertEquals;

class DivideOperationTest {
    private Provider<Value> valueProvider;
    private Provider<DivideOperation> divideOperationProvider;
    private Provider<CalculationOperator> calculationOperatorProvider;

    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
            }
        });
        valueProvider = injector.getProvider(Value.class);
        divideOperationProvider = injector.getProvider(DivideOperation.class);
        calculationOperatorProvider = injector.getProvider(CalculationOperator.class);
    }
    
    @Test
    void testComparisonStatement_Equals_True() {
        DivideOperation divideOperation = divideOperationProvider.get();
        divideOperation.addChild(valueProvider.get().addValue("20"));
        divideOperation.addChild(calculationOperatorProvider.get().addValue("/"));
        divideOperation.addChild(valueProvider.get().addValue("4"));

        StringBuilder stringBuilder = new StringBuilder();

        divideOperation.encode(stringBuilder);

        String res = stringBuilder.toString();

        String exp = "Div(V(20)CalO(/)V(4))";

        assertEquals(res, exp);
    }

    @Test
    void testResolvingDivideOperation() {
        Operation divideOperation = divideOperationProvider.get();
        divideOperation.addChild(valueProvider.get().addValue("20"))
                .addChild(calculationOperatorProvider.get().addValue("/"))
                .addChild(valueProvider.get().addValue("4"));

        Value value = (Value) divideOperation.resolveOperation();

        Assertions.assertEquals("5", value.getValue().get(0));
    }

    @Test
    void testResolvingMultipleDivideOperations() {
        Operation divideOperation = divideOperationProvider.get();
        divideOperation.addChild(divideOperationProvider.get()
                .addChild(valueProvider.get().addValue("20"))
                .addChild(calculationOperatorProvider.get().addValue("/"))
                .addChild(valueProvider.get().addValue("2")))
                .addChild(calculationOperatorProvider.get().addValue("/"))
                .addChild(valueProvider.get().addValue("2"));

        Value value = (Value) divideOperation.resolveOperation();

        Assertions.assertEquals("5", value.getValue().get(0));
    }

    @Test
    void testResolvingDivideOperationWithRoundedResult() {
        Operation divideOperation = divideOperationProvider.get();
        divideOperation.addChild(valueProvider.get().addValue("17"))
                .addChild(calculationOperatorProvider.get().addValue("/"))
                .addChild(valueProvider.get().addValue("2"));

        Value value = (Value) divideOperation.resolveOperation();

        Assertions.assertEquals("8", value.getValue().get(0));
    }

}