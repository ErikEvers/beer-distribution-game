package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.businessrule.parser.ast.BooleanLiteral;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.DivideOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.MultiplyOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Operation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.han.ica.asd.c.businessrule.parser.evaluator.Evaluator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;

import static junit.framework.TestCase.assertEquals;

class MultiplyOperationTest {

    private Provider<Value> valueProvider;
    private Provider<CalculationOperator> calculationOperatorProvider;
    private Provider<MultiplyOperation> multiplyOperationProvider;

    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
            }
        });
        valueProvider = injector.getProvider(Value.class);
        calculationOperatorProvider = injector.getProvider(CalculationOperator.class);
        multiplyOperationProvider = injector.getProvider(MultiplyOperation.class);
    }
    
    @Test
    void testComparisonStatement_Equals_True() {
        MultiplyOperation multiplyOperation = multiplyOperationProvider.get();
        multiplyOperation.addChild(valueProvider.get().addValue("20"));
        multiplyOperation.addChild(calculationOperatorProvider.get().addValue("*"));
        multiplyOperation.addChild(valueProvider.get().addValue("4"));

        StringBuilder stringBuilder = new StringBuilder();

        multiplyOperation.encode(stringBuilder);

        String res = stringBuilder.toString();

        String exp = "Mul(V(20)CalO(*)V(4))";

        assertEquals(res, exp);
    }


    @Test
    void testResolvingMultiplyOperation() {
        Operation multiplyOperation = multiplyOperationProvider.get();
        multiplyOperation.addChild(valueProvider.get().addValue("20"))
                .addChild(calculationOperatorProvider.get().addValue("*"))
                .addChild(valueProvider.get().addValue("4"));

        Value value = (Value) multiplyOperation.resolveOperation();

        Assertions.assertEquals("80", value.getValue().get(0));
    }

    @Test
    void testResolvingMultipleMultiplyOperations() {
        Operation multiplyOperation = multiplyOperationProvider.get();
        multiplyOperation.addChild(multiplyOperationProvider.get()
                .addChild(valueProvider.get().addValue("20"))
                .addChild(calculationOperatorProvider.get().addValue("*"))
                .addChild(valueProvider.get().addValue("2")))
                .addChild(calculationOperatorProvider.get().addValue("*"))
                .addChild(valueProvider.get().addValue("2"));

        Value value = (Value) multiplyOperation.resolveOperation();

        Assertions.assertEquals("80", value.getValue().get(0));
    }
}