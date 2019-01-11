package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.Default;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class DefaultTest {
    private Default aDefault;
    private Provider<Default> defaultProvider;
    
    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
            }
        });
        defaultProvider = injector.getProvider(Default.class);
        aDefault = defaultProvider.get();
    }

    @Test
    void testDefault_Encode_True() {
        StringBuilder res = new StringBuilder();
        aDefault.encode(res);

        StringBuilder exp = new StringBuilder();
        exp.append("D()");

        assertEquals(exp.toString(), res.toString());
    }

    @Test
    void testDefault_hashCode_True() {
        Default testDefault1 = defaultProvider.get();
        Default testDefault2 = defaultProvider.get();

        assertEquals(testDefault1.hashCode(), testDefault2.hashCode());
    }

    @Test
    void testDefault_Equal_True() {
        Default testDefault = defaultProvider.get();
        boolean match = aDefault.equals(testDefault);
        assertTrue(match);
    }

}
