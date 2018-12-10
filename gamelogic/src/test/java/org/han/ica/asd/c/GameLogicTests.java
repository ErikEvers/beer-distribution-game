package org.han.ica.asd.c;
import org.han.ica.asd.c.domain.Facility;
import org.han.ica.asd.c.domain.Order;
import org.han.ica.asd.c.domain.PlayerRoundData;
import org.han.ica.asd.c.public_interfaces.ICommunication;
import org.han.ica.asd.c.public_interfaces.IPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
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
        ArgumentCaptor<Order> givenOrder = ArgumentCaptor.forClass(Order.class);
        gameLogic.placeOrder(4);
        verify(persistence, times(1)).saveOrder(givenOrder.capture());
        assertEquals(4, givenOrder.getValue().getAmount());
    }

    @Test
    public void getRoundDataFromFacilityCallsPersistence() {
        Facility facility = new Facility(1, "");
        gameLogic.getRoundDataFromFacility(facility);
        ArgumentCaptor<Facility> givenFacility = ArgumentCaptor.forClass(Facility.class);
        verify(persistence, times(1)).getRoundData(givenFacility.capture());
        assertEquals(facility, givenFacility.getValue());
    }
}
