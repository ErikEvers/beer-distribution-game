package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.alternatives.AlternativeFinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlternativeFinderTest {
    AlternativeFinder alternativeFinder;
    @BeforeEach
    void beforeEach(){
        this.alternativeFinder = new AlternativeFinder();
    }

    @Test
    void testFindAlternativeForInvendory(){
        String alternative = this.alternativeFinder.findAlternative("invendory");

        Assertions.assertEquals("inventory", alternative);
    }

    @Test
    void testFindAlternativeForBacorder(){
        String alternative = this.alternativeFinder.findAlternative("bacorder");

        Assertions.assertEquals("back orders, incoming order", alternative);
    }
}
