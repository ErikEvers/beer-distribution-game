package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.evaluator.Counter;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.*;

class CounterTest {
    private Counter counter = new Counter();
    @Test
    void testCounter_hashCode_True() {
        Counter testCounter1 = new Counter();
        Counter testCounter2 = new Counter();

        assertEquals(testCounter1.hashCode() , testCounter2.hashCode());
    }

    @Test
    void testCounter_Equal_True(){
        boolean match = counter.equals(counter);
        assertTrue(match);
    }

    @Test
    void testCounter_Equal_False(){
        Counter testCounter = new Counter();
        boolean match = counter.equals(testCounter);
        assertFalse(match);
    }

    @Test
    void testCounter_Equal_FalseNull(){
        boolean match = counter.equals(null);
        assertFalse(match);
    }

    @Test
    void testCounter_AddOne_True(){
        counter.addOne();
        assertEquals(counter.getCountedValue(),1);
    }
}
