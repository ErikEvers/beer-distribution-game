package org.han.ica.asd.c;
import org.han.ica.asd.c.public_interfaces.ICommunication;
import org.han.ica.asd.c.public_interfaces.IPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class GameLogicTests {
    private GameLogic gameLogic;
    private ICommunication communication;
    private IPersistence persistence;

    @BeforeEach
    public void setup() {
        communication = mock(ICommunication.class);
        persistence = mock(IPersistence.class);
        gameLogic = new GameLogic(communication, persistence);
    }

    @Test
    public void placeOrderSendsToICommunication() {
        gameLogic.placeOrder(4);
        verify(communication, times(1)).send(4);
    }

    @Test
    public void placeOrderSavesToDatabase() {
        gameLogic.placeOrder(4);
        verify(persistence, times(1)).saveOrder(Mockito.any());
    }
}
