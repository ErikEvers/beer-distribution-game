package org.han.ica.asd.c;
import org.han.ica.asd.c.exceptions.RoundDataNotFoundException;
import org.han.ica.asd.c.model.Facility;
import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.Round;
import org.han.ica.asd.c.public_interfaces.ICommunication;
import org.han.ica.asd.c.public_interfaces.IPersistence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class GameLogicTests {
    private GameLogic gameLogic;
    private ICommunication communication;
    private IPersistence persistence;

    @BeforeEach
    public void setup() {
        communication = mock(ICommunication.class);
        persistence = mock(IPersistence.class);
        gameLogic = new GameLogic("test", communication, persistence);
    }
}
