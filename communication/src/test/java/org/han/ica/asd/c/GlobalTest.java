package org.han.ica.asd.c;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalTest {

    @Test
    void TestGlobalValue(){
        assertEquals(10000, Global.FAULT_DETECTION_INTERVAL );
    }
}
