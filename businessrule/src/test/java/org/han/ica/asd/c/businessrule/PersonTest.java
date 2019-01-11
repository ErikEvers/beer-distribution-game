package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.businessrule.parser.ast.action.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;

import static org.junit.jupiter.api.Assertions.*;


public class PersonTest {
    private Provider<Person> personProvider;

    private Person person = new Person("1");

    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
            }
        });
        personProvider = injector.getProvider(Person.class);
        person = personProvider.get().addValue("1");
    }

    @Test
    void testPerson_hashCode_True() {
        Person testPerson1 = personProvider.get().addValue("1");
        Person testPerson2 = personProvider.get().addValue("1");

        assertEquals(testPerson1.hashCode(), testPerson2.hashCode());
    }

    @Test
    void testPerson_Equal_True() {
        Person personTest = personProvider.get().addValue("1");
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
        Person personTest = personProvider.get().addValue("2");;
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
