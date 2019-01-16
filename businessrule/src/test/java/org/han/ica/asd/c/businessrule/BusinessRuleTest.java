package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.DivideOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Operation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.BooleanOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.han.ica.asd.c.businessrule.parser.replacer.Replacer;
import org.han.ica.asd.c.businessrule.parser.walker.ASTListener;
import org.han.ica.asd.c.businessrule.stubs.BusinessRuleStoreStub;
import org.han.ica.asd.c.gamevalue.GameValue;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.model.domain_objects.FacilityTurn;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnDeliver;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import javax.inject.Provider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.powermock.api.mockito.PowerMockito.when;


class BusinessRuleTest {
    private BusinessRule businessRule = new BusinessRule();
    private Round round;

    private int facilityIdReplace = 0;

    private Provider<BusinessRule> businessRuleProvider;
    private Provider<Comparison> comparisonProvider;
    private Provider<ComparisonStatement> comparisonStatementProvider;
    private Provider<ComparisonValue> comparisonValueProvider;
    private Provider<Value> valueProvider;
    private Provider<BooleanOperator> booleanOperatorProvider;
    private Provider<ComparisonOperator> comparisonOperatorProvider;
    private Provider<DivideOperation> divideOperationProvider;
    private Provider<Action> actionProvider;
    private Provider<ActionReference> actionReferenceProvider;

    private com.google.inject.Provider<ASTListener> astListenerProvider;

    @BeforeEach
    void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRuleStore.class).to(BusinessRuleStoreStub.class);
            }
        });

        actionProvider = injector.getProvider(Action.class);
        comparisonStatementProvider = injector.getProvider(ComparisonStatement.class);
        actionReferenceProvider = injector.getProvider(ActionReference.class);
        valueProvider = injector.getProvider(Value.class);
        businessRuleProvider = injector.getProvider(BusinessRule.class);
        comparisonProvider = injector.getProvider(Comparison.class);
        comparisonValueProvider = injector.getProvider(ComparisonValue.class);
        booleanOperatorProvider = injector.getProvider(BooleanOperator.class);
        comparisonOperatorProvider = injector.getProvider(ComparisonOperator.class);
        divideOperationProvider = injector.getProvider(DivideOperation.class);
        tmp = injector.getProvider(Fixtures.class);
        astListenerProvider = injector.getProvider(ASTListener.class);

        List<FacilityTurn> facilityTurns = new ArrayList<>();
        List<FacilityTurnOrder> facilityTurnOrders = new ArrayList<>();
        List<FacilityTurnDeliver> facilityTurnDelivers = new ArrayList<>();
        round = Mockito.mock(Round.class);

        for (int i = 0; i < 20; i++) {
            createTurnDeliver(i, facilityTurnDelivers);
            createTurnOrder(i, facilityTurnOrders);
            createTurn(i, facilityTurns);
        }
        when(round.getFacilityTurnDelivers()).thenReturn(facilityTurnDelivers);
        when(round.getFacilityOrders()).thenReturn(facilityTurnOrders);
        when(round.getFacilityTurns()).thenReturn(facilityTurns);
        when(round.getRoundId()).thenReturn(4);
    }




    void createTurnDeliver(int id, List<FacilityTurnDeliver> facilityTurnDelivers) {
        FacilityTurnDeliver facilityTurnDeliver = Mockito.mock(FacilityTurnDeliver.class);
        when(facilityTurnDeliver.getFacilityId()).thenReturn(id);
        when(facilityTurnDeliver.getDeliverAmount()).thenReturn(id);
        facilityTurnDelivers.add(facilityTurnDeliver);
    }

    void createTurnOrder(int id, List<FacilityTurnOrder> facilityTurnOrders) {
        FacilityTurnOrder facilityTurnOrder = Mockito.mock(FacilityTurnOrder.class);
        when(facilityTurnOrder.getFacilityId()).thenReturn(id);
        when(facilityTurnOrder.getOrderAmount()).thenReturn(id);
        when(facilityTurnOrder.getFacilityIdOrderTo()).thenReturn(id);
        facilityTurnOrders.add(facilityTurnOrder);
    }

    void createTurn(int id, List<FacilityTurn> facilityTurns) {
        FacilityTurn facilityTurn = Mockito.mock(FacilityTurn.class);

        when(facilityTurn.getFacilityId()).thenReturn(id);
        when(facilityTurn.getStock()).thenReturn(id);
        when(facilityTurn.getRemainingBudget()).thenReturn(id);
        when(facilityTurn.getBackorders()).thenReturn(id);
        facilityTurns.add(facilityTurn);
    }

    @Test
    void testBusinessRule_getChilderen_True() {
        List<ASTNode> exp = new ArrayList<>();
        exp.add(null);
        exp.add(null);

        assertEquals(exp, businessRule.getChildren());
    }

    @Test
    void testBusinessRule_encode() {
        String res = businessRule.encode();
        String exp = "BR()";

        assertEquals(exp, res);
    }

    @Test
    void testBusinessRule_Equals_True() {
        boolean res = businessRule.equals(businessRule);
        assertTrue(res);
    }

    @Test
    void testBusinessRule_Equals_FalseNull() {
        boolean res = businessRule.equals(null);
        assertFalse(res);
    }

    @Test
    void testTransformChildWithNonOperation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ComparisonValue comparisonValue = mock(ComparisonValue.class);
        Value value = mock(Value.class);

        when(comparisonValue.getOperationValue()).thenReturn(value);

        BusinessRule businessRule = new BusinessRule();

        Method method = businessRule.getClass().getDeclaredMethod("transformChild", ASTNode.class);
        method.setAccessible(true);
        method.invoke(businessRule, comparisonValue);

        verifyZeroInteractions(value);

        InOrder inOrder = Mockito.inOrder(comparisonValue);

        inOrder.verify(comparisonValue).getOperationValue();
        inOrder.verify(comparisonValue).getChildren();
    }

    @Test
    void testTransformChildWithOperation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ComparisonValue comparisonValue = mock(ComparisonValue.class);
        Operation operation = mock(Operation.class);

        when(comparisonValue.getOperationValue()).thenReturn(operation);
        when(operation.resolveOperation()).thenReturn(new Value().addValue("20"));
        BusinessRule businessRule = new BusinessRule();

        Method method = businessRule.getClass().getDeclaredMethod("transformChild", ASTNode.class);
        method.setAccessible(true);
        method.invoke(businessRule, comparisonValue);

        InOrder inOrder = Mockito.inOrder(comparisonValue, operation);
        inOrder.verify(comparisonValue).getOperationValue();
        inOrder.verify(operation).resolveOperation();
        inOrder.verify(comparisonValue).getChildren();
    }

    @Test
    void testBusinessruleGetReplacementValueEquals10() {
        BusinessRule businessRule = (BusinessRule) businessRuleProvider.get()
                .addChild(comparisonStatementProvider.get()
                        .addChild(comparisonStatementProvider.get()
                                .addChild(comparisonProvider.get()
                                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("incoming order").addValue("factory 1")))
                                        .addChild(comparisonOperatorProvider.get().addValue("equal"))
                                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("back orders").addValue("retailer 1")))))
                        .addChild(booleanOperatorProvider.get().addValue("||"))
                        .addChild(comparisonStatementProvider.get()
                                .addChild(comparisonProvider.get()
                                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("budget")))
                                        .addChild(comparisonOperatorProvider.get().addValue("not equal"))
                                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("ordered"))))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(divideOperationProvider.get()
                                .addChild(valueProvider.get().addValue("40%").addValue("inventory"))
                                .addChild(valueProvider.get().addValue("20%").addValue("outgoing goods"))));

        String expected = "BR(CS(CS(C(CV(V(0))ComO(==)CV(V(0))))BoolO(||)CS(C(CV(V(0))ComO(!=)CV(V(0)))))A(AR(order)Div(V(40% 0)CalO(/)V(20% 0))))";

        businessRule.substituteTheVariablesOfBusinessruleWithGameData(round, facilityIdReplace);

        String result = businessRule.encode();
        assertEquals(expected, result);
    }

    Provider<Fixtures> tmp;

    @Test
    void replaceFactoryWithHighest() {
        String input = "if inventory is 20 then order 20 from factory where inventory is highest";
        String expected = "BR(CS(C(CV(V(0))ComO(==)CV(V(20))))A(AR(order)V(20)P(3)))";
        BusinessRule businessRule = parseString(input);
        businessRule.substituteTheVariablesOfBusinessruleWithGameData(round, facilityIdReplace);
        assertEquals(expected, businessRule.encode());
    }

    @Test
    void replaceRetailerWithLowest() {
        String input = "if inventory is 20 then order 20 from retailer where ordered is lowest";
        String expected = "BR(CS(C(CV(V(0))ComO(==)CV(V(20))))A(AR(order)V(20)P(10)))";
        BusinessRule businessRule = parseString(input);
        businessRule.substituteTheVariablesOfBusinessruleWithGameData(round, facilityIdReplace);
        assertEquals(expected, businessRule.encode());
    }


    @Test
    void replaceWholesalerWithHighest() {
        String input = "if inventory is 20 then order 20 from regional warehouse where outgoing goods is highest";
        String expected = "BR(CS(C(CV(V(0))ComO(==)CV(V(20))))A(AR(order)V(20)P(9)))";
        BusinessRule businessRule = parseString(input);
        businessRule.substituteTheVariablesOfBusinessruleWithGameData(round, facilityIdReplace);
        assertEquals(expected, businessRule.encode());
    }

    @Test
    void replaceRegionalWarehouseWithLowest() {
        String input = "if inventory is 20 then order 20 from retailer where back orders is lowest";
        String expected = "BR(CS(C(CV(V(0))ComO(==)CV(V(20))))A(AR(order)V(20)P(10)))";
        BusinessRule businessRule = parseString(input);
        businessRule.substituteTheVariablesOfBusinessruleWithGameData(round, facilityIdReplace);
        assertEquals(expected, businessRule.encode());
    }

    @Test
    void replaceRegionalWarehouseWithLowestIncomingOrder() {
        String input = "if inventory is 20 then order 20 from retailer where incoming order is lowest";
        String expected = "BR(CS(C(CV(V(0))ComO(==)CV(V(20))))A(AR(order)V(20)P(10)))";
        BusinessRule businessRule = parseString(input);
        businessRule.substituteTheVariablesOfBusinessruleWithGameData(round, facilityIdReplace);
        assertEquals(expected, businessRule.encode());
    }
    @Test
    void replaceRegionalWarehouseWithLowestOutGoingGoodsOrder() {
        String input = "if inventory is 20 then order 20 from wholesaler where outgoing goods is lowest";
        String expected = "BR(CS(C(CV(V(0))ComO(==)CV(V(20))))A(AR(order)V(20)P(4)))";
        BusinessRule businessRule = parseString(input);
        businessRule.substituteTheVariablesOfBusinessruleWithGameData(round, facilityIdReplace);
        assertEquals(expected, businessRule.encode());
    }

    @Test
    void replaceRegionalWarehouseWithLowestBudget() {
        String input = "if inventory is 20 then order 20 from retailer where budget is lowest";
        String expected = "BR(CS(C(CV(V(0))ComO(==)CV(V(20))))A(AR(order)V(20)P(10)))";
        BusinessRule businessRule = parseString(input);
        businessRule.substituteTheVariablesOfBusinessruleWithGameData(round, facilityIdReplace);
        assertEquals(expected, businessRule.encode());
    }


    BusinessRule parseString(String input) {
        CharStream inputStream = CharStreams.fromString(input);
        BusinessRuleLexer lexer = new BusinessRuleLexer(inputStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        BusinessRuleParser parser = new BusinessRuleParser(tokens);
        parser.setErrorHandler(new BailErrorStrategy());

        //Setup collection of the parse error messages
        BaseErrorListener errorListener = new BaseErrorListener() {
            private String message;

            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                message = msg;
            }

            public String toString() {
                return message;
            }
        };
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        //Parse & extract AST
        ASTListener listener = astListenerProvider.get();
        try {
            ParseTree parseTree = parser.businessrule();
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(listener, parseTree);
        } catch (ParseCancellationException e) {
            fail(errorListener.toString());
        }

        return listener.getBusinessRules().get(0);
    }

    @Test
    void testBusinessruleGetReplacementRound() {
        BusinessRule businessRule = (BusinessRule) businessRuleProvider.get()
                .addChild(comparisonStatementProvider.get()
                        .addChild(comparisonStatementProvider.get()
                                .addChild(comparisonProvider.get()
                                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("round")))
                                        .addChild(comparisonOperatorProvider.get().addValue("equal"))
                                        .addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("5"))))))
                .addChild(actionProvider.get()
                        .addChild(actionReferenceProvider.get().addValue("order"))
                        .addChild(valueProvider.get().addValue("20")));

        String expected = "BR(CS(CS(C(CV(V(5))ComO(==)CV(V(5)))))A(AR(order)V(20)))";

        businessRule.substituteTheVariablesOfBusinessruleWithGameData(round, facilityIdReplace);

        String result = businessRule.encode();
        assertEquals(expected, result);
    }

    @Test
    void testGameValueORDEREDContainsOrdered() {
        assertTrue(GameValue.ORDERED.contains("ordered"));
    }

    @Test
    void testGameORDEREDGetValueEqualsOrdered() {
        String[] actual = GameValue.ORDERED.getValue();
        String[] expected = {"ordered"};
        assertEquals(expected[0], actual[0]);
    }

    @Test
    void testBusinessRuleGetOrderEmptyReturn() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getOrderMethod = Replacer.class.getDeclaredMethod("getOrder", Round.class, int.class);
        getOrderMethod.setAccessible(true);
        Replacer replacer = new Replacer();
        String order = (String) getOrderMethod.invoke(replacer, new Round(), 0);

        assertTrue(order.isEmpty());
    }

    @Test
    void testBusinessRuleGetStockEmptyReturn() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getStockMethod = Replacer.class.getDeclaredMethod("getStock", Round.class, int.class);
        getStockMethod.setAccessible(true);
        Replacer replacer = new Replacer();
        String stock = (String) getStockMethod.invoke(replacer, new Round(), 0);

        assertTrue(stock.isEmpty());
    }

    @Test
    void testBusinessRuleGetBudgetEmptyReturn() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getBudgetMethod = Replacer.class.getDeclaredMethod("getBudget", Round.class, int.class);
        getBudgetMethod.setAccessible(true);
        Replacer replacer = new Replacer();
        String budget = (String) getBudgetMethod.invoke(replacer, new Round(), 0);

        assertTrue(budget.isEmpty());
    }

    @Test
    void testBusinessRuleGetBacklogEmptyReturn() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getBacklogMethod = Replacer.class.getDeclaredMethod("getBacklog", Round.class, int.class);
        getBacklogMethod.setAccessible(true);
        Replacer replacer = new Replacer();
        String backlog = (String) getBacklogMethod.invoke(replacer, new Round(), 0);

        assertTrue(backlog.isEmpty());
    }

    @Test
    void testBusinessRuleGetIncomingOrderEmptyReturn() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getIncomingOrderMethod = Replacer.class.getDeclaredMethod("getIncomingOrder", Round.class, int.class);
        getIncomingOrderMethod.setAccessible(true);
        Replacer replacer = new Replacer();
        String incomingOrder = (String) getIncomingOrderMethod.invoke(replacer, new Round(), 0);

        assertTrue(incomingOrder.isEmpty());
    }

    @Test
    void testBusinessRuleGetOutgoingOrderEmptyReturn() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getOutgoingGoodsMethod = Replacer.class.getDeclaredMethod("getOutgoingGoods", Round.class, int.class);
        getOutgoingGoodsMethod.setAccessible(true);
        Replacer replacer = new Replacer();
        String outgoingOrder = (String) getOutgoingGoodsMethod.invoke(replacer, new Round(), 0);

        assertTrue(outgoingOrder.isEmpty());
    }
}
