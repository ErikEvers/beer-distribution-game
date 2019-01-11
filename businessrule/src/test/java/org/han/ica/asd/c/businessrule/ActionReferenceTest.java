package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.action.Person;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;

import static junit.framework.TestCase.*;

public class ActionReferenceTest {
    private Provider<ActionReference> actionReferenceProvider;
    private ActionReference actionReference;

    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
            }
        });
        actionReferenceProvider = injector.getProvider(ActionReference.class);
        actionReference = actionReferenceProvider.get().addValue("hello");
    }

    @Test
    void testActionReference_hashCode_True() {
        ActionReference testActionReference1 = actionReferenceProvider.get().addValue("hello");
        ActionReference testActionReference2 = actionReferenceProvider.get().addValue("hello");

        assertEquals(testActionReference1.hashCode(), testActionReference2.hashCode());
    }

    @Test
    void testActionReference_Equal_True() {
        ActionReference actionReferenceTest = actionReferenceProvider.get().addValue("hello");
        boolean match = actionReference.equals(actionReferenceTest);
        assertTrue(match);
    }

    @Test
    void testActionReference_Equal_True2() {
        boolean match = actionReference.equals(actionReference);
        assertTrue(match);
    }

    @Test
    void testActionReference_Equal_False() {
        ActionReference actionReferenceTest = actionReferenceProvider.get().addValue("bye");
        boolean match = actionReference.equals(actionReferenceTest);
        assertFalse(match);
    }

    @Test
    void testActionReference_Equal_FalseNull() {
        boolean match = actionReference.equals(null);
        assertFalse(match);
    }
}
