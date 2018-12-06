package org.han.ica.asd.c;
import org.han.ica.asd.c.public_interfaces.ICommunication;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static junit.framework.TestCase.*;


public class GameLogicTests {
    GameLogic gameLogic;

    @Mock
    public ICommunication communication;

    @Before
    public void setup() {
        gameLogic = new GameLogic(communication);
    }

    @Test
    public void placeOrderSendsToICommunication() {
        gameLogic.placeOrder(4);
        Mockito.verify(communication, Mockito.times(1)).send(4);
    }

}
