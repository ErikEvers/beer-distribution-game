package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.Person;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionTest {

    @Test
    void testAction_GetFacilityId_Factory(){
        Action action = new Action();
        action.addChild(new Person("factory 1"));

        int exp = 1;
        int res = action.getFacilityId();

        assertEquals(exp, res);
    }

    @Test
    void testAction_GetFacilityId_Distributor(){
        Action action = new Action();
        action.addChild(new Person("distributor 1"));

        int exp = 3;
        int res = action.getFacilityId();

        assertEquals(exp, res);
    }

    @Test
    void testAction_GetFacilityId_Wholesaler(){
        Action action = new Action();
        action.addChild(new Person("wholesaler 1"));

        int exp = 5;
        int res = action.getFacilityId();

        assertEquals(exp, res);
    }

    @Test
    void testAction_GetFacilityId_Retailer(){
        Action action = new Action();
        action.addChild(new Person("retailer 1"));

        int exp = 6;
        int res = action.getFacilityId();

        assertEquals(exp, res);
    }

    @Test
    void testAction_SeparateFacilityId_TwoElementString(){
        Action action = new Action();
        action.addChild(new Person("factory 1"));

        int exp = 0;
        int res = -1;

        try {
            res = Whitebox.invokeMethod(action, "separateFacilityId");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(exp, res);
    }

    @Test
    void testAction_SeparateFacilityId_OneElementString(){
        Action action = new Action();
        action.addChild(new Person("factory"));

        int exp = 0;
        int res = -1;

        try {
            res = Whitebox.invokeMethod(action, "separateFacilityId");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(exp, res);
    }
}
