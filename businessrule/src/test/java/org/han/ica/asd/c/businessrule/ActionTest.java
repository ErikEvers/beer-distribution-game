package org.han.ica.asd.c.businessrule;


import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.action.Person;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ActionTest {
    private Action action = new Action();

    @BeforeEach
    void before(){
        action.addChild(new ActionReference());
        action.addChild(new Value().addValue("1"));
    }

    @Test
    void testAddChild_ComparisonStatement() {
        Action action = new Action();
        ComparisonStatement comparisonStatement = new ComparisonStatement();
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
        Action action = new Action();
        Person person = new Person("1");
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
        Action testAction1 = new Action();
        Action testAction2 = new Action();

        assertEquals(testAction1.hashCode(), testAction2.hashCode());
    }

    @Test
    void testActionReference_Equal_True() {
        Action actionTest = new Action();
        actionTest.addChild(new ActionReference());
        actionTest.addChild(new Value().addValue("1"));
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
        Action actionTest = new Action();
        actionTest.addChild(new ActionReference());
        actionTest.addChild(new Value().addValue("2"));
        boolean match = action.equals(actionTest);
        assertFalse(match);
    }

    @Test
    void testActionReference_Equal_FalseNull() {
        boolean match = action.equals(null);
        assertFalse(match);
    }
}
