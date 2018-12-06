package org.han.ica.asd.c;
import org.han.ica.asd.c.public_interfaces.ICommunication;
import org.han.ica.asd.c.stubs.ICommunicationStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;


public class GameLogicTests {
    private GameLogic gameLogic;
    private ICommunication communication;

    @BeforeEach
    public void setup() {
        communication = mock(ICommunicationStub.class);
        gameLogic = new GameLogic(communication);
    }

    @Test
    public void placeOrderSendsToICommunication() {
        gameLogic.placeOrder(4);
        Mockito.verify(communication, Mockito.times(1)).send(4);
    }

}
