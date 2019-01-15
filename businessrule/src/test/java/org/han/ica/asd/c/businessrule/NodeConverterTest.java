package org.han.ica.asd.c.businessrule;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.businessrule.parser.ast.NodeConverter;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.stubs.BusinessRuleStoreStub;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

import javax.inject.Provider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NodeConverterTest {
    private Provider<NodeConverter> nodeConverterProvider;
    private Provider<ActionReference> actionReferenceProvider;

    @BeforeEach
    void setup() {
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

        int exp = 1;
        int res = nodeConverter.getFacilityIdByAction(1,actionReferenceProvider.get().addValue("order"));

        assertEquals(exp,res);
    }

    @Test
    void testNodeConverter_getFacilityIdByAction_FactoryDeliver(){
        NodeConverter nodeConverter = nodeConverterProvider.get();

        int exp = -1;
        int res = nodeConverter.getFacilityIdByAction(1,actionReferenceProvider.get().addValue("deliver"));

        assertEquals(exp,res);
    }

    @Test
    void testNodeConverter_getFacilityIdByAction_RegionalWarehouseOrder(){
        NodeConverter nodeConverter = nodeConverterProvider.get();

        int exp = -1;
        int res = nodeConverter.getFacilityIdByAction(4,actionReferenceProvider.get().addValue("order"));

        assertEquals(exp,res);
    }

    @Test
    void testNodeConverter_getFacilityIdByAction_RegionalWarehouseDeliver(){
        NodeConverter nodeConverter = nodeConverterProvider.get();

        int exp = -1;
        int res = nodeConverter.getFacilityIdByAction(4,actionReferenceProvider.get().addValue("deliver"));

        assertEquals(exp,res);
    }

    @Test
    void testNodeConverter_getFacilityIdByAction_WholesalerOrder(){
        NodeConverter nodeConverter = nodeConverterProvider.get();

        int exp = -1;
        int res = nodeConverter.getFacilityIdByAction(6,actionReferenceProvider.get().addValue("order"));

        assertEquals(exp,res);
    }

    @Test
    void testNodeConverter_getFacilityIdByAction_WholesalerDeliver(){
        NodeConverter nodeConverter = nodeConverterProvider.get();

        int exp = -1;
        int res = nodeConverter.getFacilityIdByAction(6,actionReferenceProvider.get().addValue("deliver"));

        assertEquals(exp,res);
    }

    @Test
    void testNodeConverter_getFacilityIdByAction_RetailerOrder(){
        NodeConverter nodeConverter = nodeConverterProvider.get();

        int exp = -1;
        int res = nodeConverter.getFacilityIdByAction(10,actionReferenceProvider.get().addValue("order"));

        assertEquals(exp,res);
    }

    @Test
    void testNodeConverter_getFacilityIdByAction_RetailerDeliver(){
        NodeConverter nodeConverter = nodeConverterProvider.get();

        int exp = 10;
        int res = nodeConverter.getFacilityIdByAction(10,actionReferenceProvider.get().addValue("deliver"));

        assertEquals(exp,res);
    }

    @Test
    void testNodeConverterSortFacilities() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method sortFacilitiesMethod = NodeConverter.class.getDeclaredMethod("sortFacilities", List.class);
        sortFacilitiesMethod.setAccessible(true);
        NodeConverter nodeConverter = nodeConverterProvider.get();

        List<List<String>> expectedSortedFacilityList = Arrays.asList(
                Lists.newArrayList("1", "2", "3"),
                Lists.newArrayList("4", "5"),
                Lists.newArrayList("6", "7", "8", "9"),
                Lists.newArrayList("10", "11", "12", "13", "14", "15")
        );

        List<List<String>> resultSortedFacilityList = (List<List<String>>) sortFacilitiesMethod.invoke(nodeConverter, Arrays.asList(
                Lists.newArrayList("3", "2", "1"),
                Lists.newArrayList("4", "5"),
                Lists.newArrayList("7", "6", "9", "8"),
                Lists.newArrayList("15", "10", "12", "14", "13", "11")
        ));

        assertEquals(expectedSortedFacilityList, resultSortedFacilityList);
    }
}
