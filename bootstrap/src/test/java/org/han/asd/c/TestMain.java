package org.han.asd.c;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestMain {
    @Test
    public void testGetValueReturnsFalse(){
        Main main = new Main();
        assertFalse(main.getValue());
    }
}