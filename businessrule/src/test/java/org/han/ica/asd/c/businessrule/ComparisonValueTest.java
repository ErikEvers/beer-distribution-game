package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;

class ComparisonValueTest {
    private ComparisonValue comparisonValue;

    private Provider<Value> valueProvider;
    private Provider<ComparisonValue> comparisonValueProvider;


    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
            }
        });
        valueProvider = injector.getProvider(Value.class);
        comparisonValueProvider = injector.getProvider(ComparisonValue.class);
        comparisonValue = comparisonValueProvider.get();
    }
    
    @Test
    void testComparisonValue_Equals_True() {
        comparisonValue.addChild(valueProvider.get().addValue("hello"));

        ComparisonValue equalComparisonValue = comparisonValueProvider.get();

        equalComparisonValue.addChild(valueProvider.get().addValue("hello"));

        boolean res = comparisonValue.equals(equalComparisonValue);

        assertTrue(res);
    }

    @Test
    void testComparisonValue_Equals_True2() {
        boolean res = comparisonValue.equals(comparisonValue);
        assertTrue(res);
    }

    @Test
    void testComparisonValue_Equals_False() {

        comparisonValue.addChild(valueProvider.get().addValue("hello"));

        ComparisonValue equalComparisonValue = comparisonValueProvider.get();

        equalComparisonValue.addChild(valueProvider.get().addValue("bye"));

        boolean res = comparisonValue.equals(equalComparisonValue);

        assertFalse(res);
    }

    @Test
    void testComparisonValue_Equals_False2() {
        comparisonValue.addChild(valueProvider.get().addValue("hello"));

        boolean res = comparisonValue.equals(null);

        assertFalse(res);
    }

    @Test
    void testComparisonValue_getChilderen_False() {
        comparisonValue.addChild(valueProvider.get().addValue("hello"));

        List<ASTNode> res = comparisonValue.getChildren();

        List<ASTNode> exp = new ArrayList<>();
        exp.add(valueProvider.get().addValue("hello"));

        assertEquals(exp, res);
    }

    @Test
    void testComparisonValue_encode_Called() {
        ComparisonValue cv = comparisonValueProvider.get();
        StringBuilder exp = new StringBuilder().append("CV()");
        StringBuilder res = new StringBuilder();

        cv.encode(res);

        assertEquals(exp.toString(), res.toString());
    }

    @Test
    void testComparisonValue_hashCode_True() {
        ComparisonValue cv1 = comparisonValueProvider.get();
        ComparisonValue cv2 = comparisonValueProvider.get();

        assertEquals(cv1.hashCode(), cv2.hashCode());
    }
}