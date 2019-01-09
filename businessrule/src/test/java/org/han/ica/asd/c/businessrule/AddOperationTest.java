package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.businessrule.parser.ast.operations.AddOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Operation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddOperationTest {
    private Provider<AddOperation> addOperationProvider;
    private Provider<Value> valueProvider;
    private Provider<CalculationOperator> calculationOperatorProvider;

    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
            }
        });
        addOperationProvider = injector.getProvider(AddOperation.class);
        valueProvider = injector.getProvider(Value.class);
        calculationOperatorProvider = injector.getProvider(CalculationOperator.class);
    }

    @Test
    void testComparisonStatement_Equals_True() {
        AddOperation addOperation = addOperationProvider.get();
        addOperation.addChild(valueProvider.get().addValue("20"));
        addOperation.addChild(calculationOperatorProvider.get().addValue("+"));
        addOperation.addChild(valueProvider.get().addValue("4"));

        StringBuilder stringBuilder = new StringBuilder();

        addOperation.encode(stringBuilder);

        String res = stringBuilder.toString();

        String exp = "Add(V(20)CalO(+)V(4))";

        assertEquals(res, exp);
    }

    @Test
    void testResolvingAddOperation() {
        Operation addOperation = addOperationProvider.get();
        addOperation.addChild(valueProvider.get().addValue("20"))
                .addChild(calculationOperatorProvider.get().addValue("+"))
                .addChild(valueProvider.get().addValue("4"));

        Value value = (Value) addOperation.resolveOperation();

        assertEquals("24", value.getValue().get(0));
    }

    @Test
    void testResolvingMultipleAddOperations() {
        Operation addOperation = addOperationProvider.get();
        addOperation.addChild(addOperationProvider.get()
                .addChild(valueProvider.get().addValue("20"))
                .addChild(calculationOperatorProvider.get().addValue("+"))
                .addChild(valueProvider.get().addValue("4")))
                .addChild(calculationOperatorProvider.get().addValue("+"))
                .addChild(valueProvider.get().addValue("3"));

        Value value = (Value) addOperation.resolveOperation();

        assertEquals("27", value.getValue().get(0));
    }
}
