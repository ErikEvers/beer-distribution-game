package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.parser.ast.Default;
import org.han.ica.asd.c.businessrule.parser.ast.operations.AddOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

public class DefaultTest {
    private Default aDefault = new Default();

    @Test
    void testDefault_Encode_True() {
        StringBuilder res = new StringBuilder();
        aDefault.encode(res);

        StringBuilder exp = new StringBuilder();
        exp.append("D()");

        assertEquals(exp.toString(),res.toString());
    }

    @Test
    void testDefault_hashCode_True() {
        Default testDefault1 = new Default();
        Default testDefault2 = new Default();

        assertEquals(testDefault1.hashCode() , testDefault2.hashCode());
    }

    @Test
    void testDefault_Equal_True(){
        Default testDefault = new Default();
        boolean match = aDefault.equals(testDefault);
        assertTrue(match);
    }

}
