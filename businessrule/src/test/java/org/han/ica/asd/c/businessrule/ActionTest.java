package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.Person;
import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import javax.inject.Provider;

import static org.junit.jupiter.api.Assertions.*;

public class ActionTest {
    private Action action;

    private Provider<Action> actionProvider;
    private Provider<Person> personProvider;
    private Provider<ComparisonStatement> comparisonStatementProvider;
    private Provider<ActionReference> actionReferenceProvider;
    private Provider<Value> valueProvider;

    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
            }
        });
        actionProvider = injector.getProvider(Action.class);
        personProvider = injector.getProvider(Person.class);
        comparisonStatementProvider = injector.getProvider(ComparisonStatement.class);
        actionReferenceProvider = injector.getProvider(ActionReference.class);
        valueProvider = injector.getProvider(Value.class);

        action = actionProvider.get();
        action.addChild(actionReferenceProvider.get());
        action.addChild(valueProvider.get().addValue("1"));
    }

    @Test
    void testAction_GetFacilityId_Factory(){
        Action action = actionProvider.get();
        action.addChild(personProvider.get().addValue("factory 1"));

        int exp = 1;
        int res = action.getFacilityId();

        assertEquals(exp, res);
    }

    @Test
    void testAction_GetFacilityId_Distributor(){
        Action action = actionProvider.get();
        action.addChild(personProvider.get().addValue("distributor 1"));

        int exp = 3;
        int res = action.getFacilityId();

        assertEquals(exp, res);
    }

    @Test
    void testAction_GetFacilityId_Wholesaler(){
        Action action = actionProvider.get();
        action.addChild(personProvider.get().addValue("wholesaler 1"));

        int exp = 5;
        int res = action.getFacilityId();

        assertEquals(exp, res);
    }

    @Test
    void testAction_GetFacilityId_Retailer(){
        Action action = actionProvider.get();
        action.addChild(personProvider.get().addValue("retailer 1"));

        int exp = 6;
        int res = action.getFacilityId();

        assertEquals(exp, res);
    }

    @Test
    void testAddChild_ComparisonStatement() {
        Action action = actionProvider.get();
        ComparisonStatement comparisonStatement = comparisonStatementProvider.get();
        action.addChild(comparisonStatement);

        ASTNode exp = comparisonStatement;
        ASTNode cur = null;

        for (ASTNode child : action.getChildren()) {
            if(child instanceof ComparisonStatement){
                cur = child;
            }
        }

        assertEquals(exp,cur);
    }

    @Test
    void testAddChild_Person() {
        Action action = actionProvider.get();
        Person person = (Person) personProvider.get().addValue("1");
        action.addChild(person);

        ASTNode exp = person;
        ASTNode cur = null;

        for (ASTNode child : action.getChildren()) {
            if(child instanceof Person){
                cur = child;
            }
        }

        assertEquals(exp,cur);
    }

    @Test
    void testActionReference_hashCode_True() {
        Action testAction1 = actionProvider.get();
        Action testAction2 = actionProvider.get();

        assertEquals(testAction1.hashCode(), testAction2.hashCode());
    }

    @Test
    void testActionReference_Equal_True() {
        Action actionTest = actionProvider.get();
        actionTest.addChild(actionReferenceProvider.get());
        actionTest.addChild(valueProvider.get().addValue("1"));
        boolean match = action.equals(actionTest);
        assertTrue(match);
    }

    @Test
    void testActionReference_Equal_True2() {
        boolean match = action.equals(action);
        assertTrue(match);
    }

    @Test
    void testActionReference_Equal_False() {
        Action actionTest = actionProvider.get();
        actionTest.addChild(actionReferenceProvider.get());
        actionTest.addChild(valueProvider.get().addValue("2"));
        boolean match = action.equals(actionTest);
        assertFalse(match);
    }

    @Test
    void testActionReference_Equal_FalseNull() {
        boolean match = action.equals(null);
        assertFalse(match);
    }
}
