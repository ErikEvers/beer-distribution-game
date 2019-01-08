package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValueTest {
    private Value value = new Value();

    @BeforeEach
    void before(){
        value.addValue("1");
    }

    @Test
    void testPerson_hashCode_True() {
        Value testValue1 = new Value();
        Value testValue2 = new Value();

        assertEquals(testValue1.hashCode(), testValue2.hashCode());
    }

    @Test
    void testPerson_Equal_True() {
        Value valueTest = new Value();
        valueTest.addValue("1");
        boolean match = value.equals(valueTest);
        assertTrue(match);
    }

    @Test
    void testPerson_Equal_True2() {
        boolean match = value.equals(value);
        assertTrue(match);
    }

    @Test
    void testPerson_Equal_False() {
        Value valueTest = new Value();
        valueTest.addValue("2");
        boolean match = value.equals(valueTest);
        assertFalse(match);
    }

    @Test
    void testPerson_Equal_FalseNull() {
        boolean match = value.equals(null);
        assertFalse(match);
    }

    @Test
    void testPerson_AddValue_Lowest(){
        Value testValue = new Value();
        testValue.addValue("smallest");

        String exp = "lowest";
        String cur = testValue.getValue();

        assertEquals(exp,cur);
    }

    @Test
    void testPerson_AddValue_Highest(){
        Value testValue = new Value();
        testValue.addValue("biggest");

        String exp = "highest";
        String cur = testValue.getValue();

        assertEquals(exp,cur);
    }
}
