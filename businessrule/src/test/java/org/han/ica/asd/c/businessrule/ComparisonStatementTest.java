package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;

class ComparisonStatementTest {
    private ComparisonStatement comparisonStatement;

    private Provider<Value> valueProvider;
    private Provider<Comparison> comparisonProvider;
    private Provider<ComparisonValue> comparisonValueProvider;
    private Provider<ComparisonOperator> comparisonOperatorProvider;
    private Provider<ComparisonStatement> comparisonStatementProvider;


    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
            }
        });
        valueProvider = injector.getProvider(Value.class);
        comparisonOperatorProvider = injector.getProvider(ComparisonOperator.class);
        comparisonProvider = injector.getProvider(Comparison.class);
        comparisonValueProvider = injector.getProvider(ComparisonValue.class);
        comparisonStatementProvider = injector.getProvider(ComparisonStatement.class);
        comparisonStatement = comparisonStatementProvider.get();
    }

    @Test
    void testComparisonStatement_Equals_True() {
        comparisonStatement.addChild(comparisonProvider.get()
                .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("round")))
                .addChild(comparisonOperatorProvider.get().addValue("is"))
                .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("11"))));

        ComparisonStatement equalComparison = comparisonStatementProvider.get();
        equalComparison.addChild(comparisonProvider.get()
                .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("round")))
                .addChild(comparisonOperatorProvider.get().addValue("is"))
                .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("11"))));

        boolean res = comparisonStatement.equals(equalComparison);

        assertTrue(res);
    }

    @Test
    void testComparisonStatement_Equals_False() {
        comparisonStatement.addChild(comparisonProvider.get()
                .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("round")))
                .addChild(comparisonOperatorProvider.get().addValue("is"))
                .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("11"))));

        ComparisonStatement equalComparison = comparisonStatementProvider.get();
        equalComparison.addChild(comparisonProvider.get()
                .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("round")))
                .addChild(comparisonOperatorProvider.get().addValue("higher"))
                .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("11"))));

        boolean res = comparisonStatement.equals(equalComparison);

        assertFalse(res);
    }

    @Test
    void testComparisonStatement_getChilderen_False() {
        comparisonStatement.addChild(comparisonProvider.get()
                .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("round")))
                .addChild(comparisonOperatorProvider.get().addValue("is"))
                .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("11"))));

        List<ASTNode> res = comparisonStatement.getChildren();

        List<ASTNode> exp = new ArrayList<>();
        exp.add(comparisonProvider.get()
                .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("round")))
                .addChild(comparisonOperatorProvider.get().addValue("is"))
                .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("11"))));

        assertEquals(exp, res);
    }
}