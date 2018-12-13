package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.parser.ast.ActionReference;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.*;

public class ActionReferenceTest {
    private ActionReference actionReference = new ActionReference("hello");

    @Test
    void testActionReference_hashCode_True() {
        ActionReference testActionReference1 = new ActionReference("hello");
        ActionReference testActionReference2 = new ActionReference("hello");

        assertEquals(testActionReference1.hashCode(), testActionReference2.hashCode());
    }

    @Test
    void testActionReference_Equal_True() {
        ActionReference actionReferenceTest = new ActionReference("hello");
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
        ActionReference actionReferenceTest = new ActionReference("Bye");
        boolean match = actionReference.equals(actionReferenceTest);
        assertFalse(match);
    }

    @Test
    void testActionReference_Equal_FalseNull() {
        boolean match = actionReference.equals(null);
        assertFalse(match);
    }
}
