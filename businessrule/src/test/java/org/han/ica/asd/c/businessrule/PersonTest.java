package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ast.action.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class PersonTest {
    private Person person = new Person("1");

    @Test
    void testPerson_hashCode_True() {
        Person testPerson1 = new Person("1");
        Person testPerson2 = new Person("1");

        assertEquals(testPerson1.hashCode(), testPerson2.hashCode());
    }

    @Test
    void testPerson_Equal_True() {
        Person personTest = new Person("1");
        boolean match = person.equals(personTest);
        assertTrue(match);
    }

    @Test
    void testPerson_Equal_True2() {
        boolean match = person.equals(person);
        assertTrue(match);
    }

    @Test
    void testPerson_Equal_False() {
        Person personTest = new Person("2");
        boolean match = person.equals(personTest);
        assertFalse(match);
    }

    @Test
    void testPerson_Equal_FalseNull() {
        boolean match = person.equals(null);
        assertFalse(match);
    }

    @Test
    void testPerson_Encode(){
        StringBuilder stringBuilder = new StringBuilder();
        person.encode(stringBuilder);

        String exp = "P(1)";
        String cur = stringBuilder.toString();

        assertEquals(exp,cur);
    }

    @Test
    void testPerson_GetPerson(){
        String exp = "1";
        String cur = person.getPerson();

        assertEquals(exp,cur);
    }
}
