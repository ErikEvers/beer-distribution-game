package org.han.ica.asd.c.socketrpc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SocketSettingsTest {

    @Test
    void TestSocketSettings(){
        assertEquals(4445, SocketSettings.PORT);
    }
}
