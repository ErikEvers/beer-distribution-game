package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.businessrule.parser.ast.BooleanLiteral;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.NodeConverter;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.DivideOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.MultiplyOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.han.ica.asd.c.businessrule.parser.evaluator.Evaluator;
import org.han.ica.asd.c.businessrule.stubs.BusinessRuleStoreStub;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

import javax.inject.Provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NodeConverterTest {
    private Provider<NodeConverter> nodeConverterProvider;
    private Provider<ActionReference> actionReferenceProvider;
    private Configuration configuration;


    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IBusinessRuleStore.class).annotatedWith(Names.named("BusinessruleStore")).to(BusinessRuleStoreStub.class);
            }
        });
        nodeConverterProvider = injector.getProvider(NodeConverter.class);
        actionReferenceProvider = injector.getProvider(ActionReference.class);
    }

    @Test
    void testNodeConverter_SeparateFacilityId_TwoElementString() {
        NodeConverter nodeConverter = nodeConverterProvider.get();

        int exp = 0;
        int res = -1;

        try {
            res = Whitebox.invokeMethod(nodeConverter, "separateFacilityId", "factory 1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(exp, res);
    }

    @Test
    void testNodeConverter_SeparateFacilityId_OneElementString() {
        NodeConverter nodeConverter = nodeConverterProvider.get();

        int exp = 0;
        int res = -1;

        try {
            res = Whitebox.invokeMethod(nodeConverter, "separateFacilityId", "factory");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(exp, res);
    }

    @Test
    void testNodeConverter_getFacilityIdByAction_FactoryOrder(){
        NodeConverter nodeConverter = nodeConverterProvider.get();

        Integer exp = null;
        Integer res = nodeConverter.getFacilityIdByAction(0,actionReferenceProvider.get().addValue("order"));

        assertEquals(exp,res);
    }

    @Test
    void testNodeConverter_getFacilityIdByAction_FactoryDeliver(){
        NodeConverter nodeConverter = nodeConverterProvider.get();

        int exp = -1;
        int res = nodeConverter.getFacilityIdByAction(0,actionReferenceProvider.get().addValue("deliver"));

        assertEquals(exp,res);
    }

    @Test
    void testNodeConverter_getFacilityIdByAction_RegionalWarehouseOrder(){
        NodeConverter nodeConverter = nodeConverterProvider.get();

        int exp = -1;
        int res = nodeConverter.getFacilityIdByAction(1,actionReferenceProvider.get().addValue("order"));

        assertEquals(exp,res);
    }

    @Test
    void testNodeConverter_getFacilityIdByAction_RegionalWarehouseDeliver(){
        NodeConverter nodeConverter = nodeConverterProvider.get();

        int exp = -1;
        int res = nodeConverter.getFacilityIdByAction(1,actionReferenceProvider.get().addValue("deliver"));

        assertEquals(exp,res);
    }

    @Test
    void testNodeConverter_getFacilityIdByAction_WholesalerOrder(){
        NodeConverter nodeConverter = nodeConverterProvider.get();

        int exp = -1;
        int res = nodeConverter.getFacilityIdByAction(2,actionReferenceProvider.get().addValue("order"));

        assertEquals(exp,res);
    }

    @Test
    void testNodeConverter_getFacilityIdByAction_WholesalerDeliver(){
        NodeConverter nodeConverter = nodeConverterProvider.get();

        int exp = -1;
        int res = nodeConverter.getFacilityIdByAction(2,actionReferenceProvider.get().addValue("deliver"));

        assertEquals(exp,res);
    }

    @Test
    void testNodeConverter_getFacilityIdByAction_RetailerOrder(){
        NodeConverter nodeConverter = nodeConverterProvider.get();

        int exp = -1;
        int res = nodeConverter.getFacilityIdByAction(3,actionReferenceProvider.get().addValue("order"));

        assertEquals(exp,res);
    }

    @Test
    void testNodeConverter_getFacilityIdByAction_RetailerDeliver(){
        NodeConverter nodeConverter = nodeConverterProvider.get();

        Integer exp = null;
        Integer res = nodeConverter.getFacilityIdByAction(3,actionReferenceProvider.get().addValue("deliver"));

        assertEquals(exp,res);
    }
}
