package org.han.asd.c;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestMain {

    @Test
    public void testMain(){
        Main.main(new String[0]);
        assertTrue(true);
    }
    @Test
    public void testGetValueReturnsFalse(){
        Main main = new Main();
        assertFalse(main.getValue());
    }
}