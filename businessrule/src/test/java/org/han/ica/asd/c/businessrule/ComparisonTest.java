package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;

class ComparisonTest {
    private Comparison comparison;

    private Provider<Value> valueProvider;
    private Provider<Comparison> comparisonProvider;
    private Provider<ComparisonValue> comparisonValueProvider;
    private Provider<ComparisonOperator> comparisonOperatorProvider;


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
        comparison = comparisonProvider.get();
    }
    
    @Test
    void testComparison_Equals_True() {
        comparison.addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("inventory")));
        comparison.addChild(comparisonOperatorProvider.get().addValue("is"));
        comparison.addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20")));

        Comparison equalComparison = comparisonProvider.get();
        equalComparison.addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("inventory")));
        equalComparison.addChild(comparisonOperatorProvider.get().addValue("is"));
        equalComparison.addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20")));

        boolean res = comparison.equals(equalComparison);

        assertTrue(res);
    }

    @Test
    void testComparison_Equals_False() {
        comparison.addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("inventory")));
        comparison.addChild(comparisonOperatorProvider.get().addValue("is"));
        comparison.addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("30")));

        Comparison equalComparison = comparisonProvider.get();
        equalComparison.addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("inventory")));
        equalComparison.addChild(comparisonOperatorProvider.get().addValue("is"));
        equalComparison.addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("20")));

        boolean res = comparison.equals(equalComparison);

        assertFalse(res);
    }

    @Test
    void testComparison_getChilderen_False() {
        comparison.addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("inventory")));
        comparison.addChild(comparisonOperatorProvider.get().addValue("is"));
        comparison.addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("30")));

        List<ASTNode> res = comparison.getChildren();

        List<ASTNode> exp = new ArrayList<>();
        exp.add(comparisonValueProvider.get().addChild(valueProvider.get().addValue("inventory")));
        exp.add(comparisonOperatorProvider.get().addValue("is"));
        exp.add(comparisonValueProvider.get().addChild(valueProvider.get().addValue("30")));

        assertEquals(exp, res);
    }
}