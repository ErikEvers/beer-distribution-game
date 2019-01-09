package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
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
import org.han.ica.asd.c.businessrule.stubs.BusinessRuleStoreStub;
import org.han.ica.asd.c.gamevalue.GameValue;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.model.domain_objects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.Timeout;
import org.mockito.InOrder;
import org.mockito.Mockito;

import javax.inject.Provider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.powermock.api.mockito.PowerMockito.when;


class BusinessRuleTest {
    private BusinessRule businessRule = new BusinessRule();
    private Round round;
    private Facility facility;
    private FacilityTurn facilityTurn;
    private FacilityTurnDeliver facilityTurnDeliver;
    private FacilityTurnOrder facilityTurnOrder;
    private int facilityId = 10;

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

    @BeforeEach
    void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRuleStore.class).annotatedWith(Names.named("BusinessruleStore")).to(BusinessRuleStoreStub.class);
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

        List<FacilityTurn> facilityTurns = new ArrayList<>();
        List<FacilityTurnOrder> facilityTurnOrders = new ArrayList<>();
        List<FacilityTurnDeliver> facilityTurnDelivers = new ArrayList<>();
        round = Mockito.mock(Round.class);
        facilityTurn = Mockito.mock(FacilityTurn.class);
        facilityTurnOrder = Mockito.mock(FacilityTurnOrder.class);
        facilityTurnDeliver = Mockito.mock(FacilityTurnDeliver.class);
        facilityTurns.add(facilityTurn);
        facilityTurnOrders.add(facilityTurnOrder);
        facilityTurnDelivers.add(facilityTurnDeliver);
        when(round.getFacilityTurnDelivers()).thenReturn(facilityTurnDelivers);
        when(round.getFacilityOrders()).thenReturn(facilityTurnOrders);
        when(round.getFacilityTurns()).thenReturn(facilityTurns);

        when(facilityTurn.getFacilityId()).thenReturn(facilityId);
        when(facilityTurn.getStock()).thenReturn(facilityId);
        when(facilityTurn.getRemainingBudget()).thenReturn(21);
        when(facilityTurn.getBackorders()).thenReturn(28);

        when(facilityTurnOrder.getFacilityId()).thenReturn(facilityId);
        when(facilityTurnOrder.getOrderAmount()).thenReturn(15);
        when(facilityTurnOrder.getFacilityIdOrderTo()).thenReturn(facilityId);

        when(facilityTurnDeliver.getFacilityId()).thenReturn(facilityId);
        when(facilityTurnDeliver.getDeliverAmount()).thenReturn(facilityId);
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

        String expected = "BR(CS(CS(C(CV(V(15))ComO(==)CV(V(28))))BoolO(||)CS(C(CV(V(21))ComO(!=)CV(V(15)))))A(AR(order)Div(V(40% 10)CalO(/)V(20% 10))))";

        businessRule.substituteTheVariablesOfBusinessruleWithGameData(round, facilityId);

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
        Method getOrderMethod = BusinessRule.class.getDeclaredMethod("getOrder", Round.class, int.class);
        getOrderMethod.setAccessible(true);
        BusinessRule businessRule = new BusinessRule();
        String order = (String) getOrderMethod.invoke(businessRule, new Round(), 0);

        assertTrue(order.isEmpty());
    }

    @Test
    void testBusinessRuleGetStockEmptyReturn() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getStockMethod = BusinessRule.class.getDeclaredMethod("getStock", Round.class, int.class);
        getStockMethod.setAccessible(true);
        BusinessRule businessRule = new BusinessRule();
        String stock = (String) getStockMethod.invoke(businessRule, new Round(), 0);

        assertTrue(stock.isEmpty());
    }

    @Test
    void testBusinessRuleGetBudgetEmptyReturn() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getBudgetMethod = BusinessRule.class.getDeclaredMethod("getBudget", Round.class, int.class);
        getBudgetMethod.setAccessible(true);
        BusinessRule businessRule = new BusinessRule();
        String budget = (String) getBudgetMethod.invoke(businessRule, new Round(), 0);

        assertTrue(budget.isEmpty());
    }

    @Test
    void testBusinessRuleGetBacklogEmptyReturn() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getBacklogMethod = BusinessRule.class.getDeclaredMethod("getBacklog", Round.class, int.class);
        getBacklogMethod.setAccessible(true);
        BusinessRule businessRule = new BusinessRule();
        String backlog = (String) getBacklogMethod.invoke(businessRule, new Round(), 0);

        assertTrue(backlog.isEmpty());
    }

    @Test
    void testBusinessRuleGetIncomingOrderEmptyReturn() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getIncomingOrderMethod = BusinessRule.class.getDeclaredMethod("getIncomingOrder", Round.class, int.class);
        getIncomingOrderMethod.setAccessible(true);
        BusinessRule businessRule = new BusinessRule();
        String incomingOrder = (String) getIncomingOrderMethod.invoke(businessRule, new Round(), 0);

        assertTrue(incomingOrder.isEmpty());
    }

    @Test
    void testBusinessRuleGetOutgoingOrderEmptyReturn() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getOutgoingGoodsMethod = BusinessRule.class.getDeclaredMethod("getOutgoingGoods", Round.class, int.class);
        getOutgoingGoodsMethod.setAccessible(true);
        BusinessRule businessRule = new BusinessRule();
        String outgoingOrder = (String) getOutgoingGoodsMethod.invoke(businessRule, new Round(), 0);

        assertTrue(outgoingOrder.isEmpty());
    }
}
