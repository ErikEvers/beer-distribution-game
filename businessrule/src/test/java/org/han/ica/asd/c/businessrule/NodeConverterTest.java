package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.parser.ast.NodeConverter;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NodeConverterTest {
    @Test
    void testAction_SeparateFacilityId_TwoElementString() {
        NodeConverter nodeConverter = new NodeConverter();

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
    void testAction_SeparateFacilityId_OneElementString() {
        NodeConverter nodeConverter = new NodeConverter();

        int exp = 0;
        int res = -1;

        try {
            res = Whitebox.invokeMethod(nodeConverter, "separateFacilityId", "factory");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(exp, res);
    }
}
