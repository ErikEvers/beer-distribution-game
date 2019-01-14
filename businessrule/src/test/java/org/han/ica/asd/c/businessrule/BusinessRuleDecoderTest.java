package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.businessrule.engine.BusinessRuleDecoder;
import org.han.ica.asd.c.businessrule.engine.BusinessRuleFactory;
import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.Default;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.action.Person;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.*;
import org.han.ica.asd.c.businessrule.parser.ast.operators.BooleanOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.han.ica.asd.c.businessrule.stubs.BusinessRuleStoreStub;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Deque;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class BusinessRuleDecoderTest {
    private BusinessRuleDecoder businessRuleDecoder;
    private BusinessRuleFactory businessRuleFactory;

    @BeforeEach
    void BeforeEach() {
        Injector businessRuleFactoryInjector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRuleStore.class).annotatedWith(Names.named("BusinessruleStore")).to(BusinessRuleStoreStub.class);
        }});
        businessRuleFactory = businessRuleFactoryInjector.getInstance(BusinessRuleFactory.class);

        Injector businessRuleDecoderInjector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(BusinessRuleFactory.class).toInstance(businessRuleFactory);
                bind(IBusinessRuleStore.class).annotatedWith(Names.named("BusinessruleStore")).to(BusinessRuleStoreStub.class);
            }
        });

        businessRuleDecoder = businessRuleDecoderInjector.getInstance(BusinessRuleDecoder.class);
    }

    @Test
    void parseBusinessRuleStringWithLesserThanComparisonToBusinessRuleIsEqual() {
        String businessRuleString = "BR(CS(C(CV(V(inventory))ComO(<)CV(V(10))))A(AR(order)V(0)))";
        BusinessRule businessRuleParsed = businessRuleDecoder.decodeBusinessRule(businessRuleString);
        assertEquals(businessRuleString, businessRuleParsed.encode());
    }

    @Test
    void parseBusinessRuleStringWithGreaterThanComparisonToBusinessRuleIsEqual() {
        String businessRuleString = "BR(CS(C(CV(V(inventory))ComO(>)CV(V(20))))A(AR(order)V(20)))";
        BusinessRule businessRuleParsed = businessRuleDecoder.decodeBusinessRule(businessRuleString);
        assertEquals(businessRuleString, businessRuleParsed.encode());
    }

    @Test
    void parseBusinessRuleStringWithAndOperationComparisonToBusinessRuleIsEqual() {
        String businessRuleString = "BR(CS(C(CV(V(inventory))ComO(<)CV(V(20)))BoolO(||)CS(C(CV(V(round))ComO(>)CV(V(3)))))A(AR(order)V(20)))";
        BusinessRule businessRuleParsed = businessRuleDecoder.decodeBusinessRule(businessRuleString);
        assertEquals(businessRuleString, businessRuleParsed.encode());
    }

    @Test
    void parseBusinessRuleStringWithOrOperationComparisonToBusinessRuleIsEqual() {
        String businessRuleString = "BR(CS(C(CV(V(inventory))ComO(<)CV(V(20)))BoolO(||)CS(C(CV(V(round))ComO(>)CV(V(3)))))A(AR(order)V(20)))";
        BusinessRule businessRuleParsed = businessRuleDecoder.decodeBusinessRule(businessRuleString);
        assertEquals(businessRuleString, businessRuleParsed.encode());
    }

    @Test
    void parseBusinessRuleStringWithEqualsToAndIsNotEqualsToToBusinessRuleIsEqual() {
        String businessRuleString = "BR(CS(C(CV(V(inventory))ComO(==)CV(V(inventory)))BoolO(||)CS(C(CV(V(round))ComO(!=)CV(V(3)))))A(AR(order)V(20)))";
        BusinessRule businessRuleParsed = businessRuleDecoder.decodeBusinessRule(businessRuleString);
        assertEquals(businessRuleString, businessRuleParsed.encode());
    }

    @Test
    void parseBusinessRuleStringWithGreaterThanAndOrIsEqual() {
        String businessRuleString = "BR(CS(C(CV(V(inventory))ComO(>=)CV(V(inventory)))BoolO(||)CS(C(CV(V(round))ComO(<=)CV(V(3)))))A(AR(order)V(20)))";
        BusinessRule businessRuleParsed = businessRuleDecoder.decodeBusinessRule(businessRuleString);
        assertEquals(businessRuleString, businessRuleParsed.encode());
    }

    @Test
    void parseDefaultOrderBusinessRuleStringIsEqual() {
        String businessRuleString = "BR(D()A(AR(order)V(10)))";
        BusinessRule businessRuleParsed = businessRuleDecoder.decodeBusinessRule(businessRuleString);
        assertEquals(businessRuleString, businessRuleParsed.encode());
    }

    @Test
    void parseOrderForSpecificFacilityIsEqual() {
        String businessRuleString = "BR(D()A(AR(order)V(10)P(factory 1)))";
        BusinessRule businessRuleParsed = businessRuleDecoder.decodeBusinessRule(businessRuleString);
        assertEquals(businessRuleString, businessRuleParsed.encode());
    }

    @Test
    void parseDeliverForSpecificFacilityWithComplexBusinessRuleIsEqual() {
        String businessRuleString = "BR(CS(C(CV(V(inventory))ComO(>=)CV(V(inventory)))BoolO(||)CS(C(CV(V(round))ComO(<=)CV(V(3)))))A(AR(deliver)V(20)P(factory 2)))";
        BusinessRule businessRuleParsed = businessRuleDecoder.decodeBusinessRule(businessRuleString);
        assertEquals(businessRuleString, businessRuleParsed.encode());
    }

    @Test
    void parseBusinessRuleWithComparisonStatementInActionIsEqual() {
        String businessRuleString = "BR(CS(C(CV(V(inventory))ComO(>=)CV(V(inventory)))BoolO(||)CS(C(CV(V(round))ComO(<=)CV(V(3)))))A(AR(deliver)V(20)P(factory 2)CS(C(CV(V(inventory))ComO(<)CV(V(10))))))";
        BusinessRule businessRuleParsed = businessRuleDecoder.decodeBusinessRule(businessRuleString);
        assertEquals(businessRuleString, businessRuleParsed.encode());
    }

    @Test
    void parseBusinessRuleStringWithAddOperationBusinessRuleIsEqual() {
        BusinessRule businessRule = (BusinessRule) new BusinessRule()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("5")))
                        .addChild(new ComparisonOperator("equal"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("5"))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new AddOperation()
                                .addChild(new Value().addValue("10"))
                                .addChild(new Value().addValue("1"))));

        BusinessRule businessRuleParsed = businessRuleDecoder.decodeBusinessRule(businessRule.encode());
        assertEquals(businessRule.encode(), businessRuleParsed.encode());
    }

    @Test
    void parseBusinessRuleStringWithSubtractOperationBusinessRuleIsEqual() {
        BusinessRule businessRule = (BusinessRule) new BusinessRule()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("inventory")))
                        .addChild(new ComparisonOperator("higher"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("backlog"))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new SubtractOperation()
                                .addChild(new Value().addValue("inventory"))
                                .addChild(new Value().addValue("2"))));

        BusinessRule businessRuleParsed = businessRuleDecoder.decodeBusinessRule(businessRule.encode());
        assertEquals(businessRule.encode(), businessRuleParsed.encode());
    }

    @Test
    void parseBusinessRuleStringWithMultiplyOperationBusinessRuleIsEqual() {
        BusinessRule businessRule = (BusinessRule) new BusinessRule()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("inventory")))
                        .addChild(new ComparisonOperator("higher"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("backlog"))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new MultiplyOperation()
                                .addChild(new Value().addValue("inventory"))
                                .addChild(new Value().addValue("2"))));

        BusinessRule businessRuleParsed = businessRuleDecoder.decodeBusinessRule(businessRule.encode());
        assertEquals(businessRule.encode(), businessRuleParsed.encode());
    }

    @Test
    void parseBusinessRuleStringWithDivideOperationBusinessRuleIsEqual() {
        BusinessRule businessRule = (BusinessRule) new BusinessRule()
                .addChild(new Comparison()
                        .addChild(new ComparisonValue().addChild(new Value().addValue("inventory")))
                        .addChild(new ComparisonOperator("higher"))
                        .addChild(new ComparisonValue().addChild(new Value().addValue("backlog"))))
                .addChild(new Action()
                        .addChild(new ActionReference("order"))
                        .addChild(new DivideOperation()
                                .addChild(new Value().addValue("inventory"))
                                .addChild(new Value().addValue("2"))));

        BusinessRule businessRuleParsed = businessRuleDecoder.decodeBusinessRule(businessRule.encode());
        assertEquals(businessRule.encode(), businessRuleParsed.encode());
    }

    @Test
    void processOneIterationOfBusinessRuleScriptReturnsCorrectIdentifierAndNextIteration() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method processIterationMethod = BusinessRuleDecoder.class.getDeclaredMethod("processIteration", Deque.class, String[].class);
        processIterationMethod.setAccessible(true);

        Deque<ASTNode> astNodeDeque = new LinkedList<>();
        astNodeDeque.push(new BusinessRule());
        String[] iteration = (String[]) processIterationMethod.invoke(businessRuleDecoder, astNodeDeque, new String[] {"A", "C(A)"});

        assertEquals("C", iteration[0]);
        assertEquals("A)", iteration[1]);
    }

    @Test
    void getNextIterationOfBusinessRuleScriptBySplittingOpeningParenthesis() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method processIterationMethod = BusinessRuleDecoder.class.getDeclaredMethod("nextIteration", String.class);
        processIterationMethod.setAccessible(true);

        String[] iteration = (String[]) processIterationMethod.invoke(businessRuleDecoder, "A(A)");

        assertEquals("A", iteration[0]);
        assertEquals("A)", iteration[1]);
    }

    @Test
    void getNextIterationOfBusinessRuleScriptBySplittingClosingParenthesis() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method nextIterationMethod = BusinessRuleDecoder.class.getDeclaredMethod("nextIteration", String.class);
        nextIterationMethod.setAccessible(true);

        String[] iteration = (String[]) nextIterationMethod.invoke(businessRuleDecoder, "A)A(A)");

        assertEquals("A", iteration[0]);
        assertEquals("A(A)", iteration[1]);
    }

    @Test
    void popOrPushFindsOpeningParenthesisAndReturnsTrue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method processIterationMethod = BusinessRuleDecoder.class.getDeclaredMethod("popOrPush", String.class);
        processIterationMethod.setAccessible(true);

        boolean iteration = (boolean) processIterationMethod.invoke(businessRuleDecoder, "A(A)");

        assertTrue(iteration);
    }

    @Test
    void popOrPushFindsClosingParenthesisAndReturnsFalse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method processIterationMethod = BusinessRuleDecoder.class.getDeclaredMethod("popOrPush", String.class);
        processIterationMethod.setAccessible(true);

        boolean iteration = (boolean) processIterationMethod.invoke(businessRuleDecoder, "A)A(A)");

        assertFalse(iteration);
    }

    @Test
    void popOrPushFindsNoParenthesisReturnsFalse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method processIterationMethod = BusinessRuleDecoder.class.getDeclaredMethod("popOrPush", String.class);
        processIterationMethod.setAccessible(true);

        boolean iteration = (boolean) processIterationMethod.invoke(businessRuleDecoder, "A");

        assertFalse(iteration);
    }
}
