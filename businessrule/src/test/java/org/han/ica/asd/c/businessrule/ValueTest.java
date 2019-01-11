package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;

import static org.junit.jupiter.api.Assertions.*;

public class ValueTest {
    private Value value;

    private Provider<Value> valueProvider;

    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
            }
        });
        valueProvider = injector.getProvider(Value.class);
        value = valueProvider.get().addValue("1");
    }

    @Test
    void testPerson_hashCode_True() {
        Value testValue1 = valueProvider.get();
        Value testValue2 = valueProvider.get();

        assertEquals(testValue1.hashCode(), testValue2.hashCode());
    }

    @Test
    void testPerson_Equal_True() {
        Value valueTest = valueProvider.get();
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
        Value valueTest = valueProvider.get();
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
        Value testValue = valueProvider.get();
        testValue.addValue("smallest");

        String exp = "lowest";
        String cur = testValue.getValue().get(0);

        assertEquals(exp,cur);
    }

    @Test
    void testPerson_AddValue_Highest(){
        Value testValue = valueProvider.get();
        testValue.addValue("biggest");

        String exp = "highest";
        String cur = testValue.getValue().get(0);

        assertEquals(exp,cur);
    }
}
