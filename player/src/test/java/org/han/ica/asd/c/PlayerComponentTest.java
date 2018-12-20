package org.han.ica.asd.c;

import org.han.ica.asd.c.gamelogic.GameLogic;
import org.han.ica.asd.c.gamelogic.public_interfaces.IPlayerGameLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class PlayerComponentTest {
    private PlayerComponent playerComponent;

    @BeforeEach
    void beforeTest() {
        playerComponent = new PlayerComponent();
    }

    @Test
    void getAllGamesCallsMethodOfSameNameOnceInGameLogicTest() {
        //Arrange
        playerComponent.gameLogic = mock(GameLogic.class);

        ArrayList<String> list = new ArrayList<>();
        list.add("");

        when(playerComponent.gameLogic.getAllGames()).thenReturn(list);

        //Act
        playerComponent.getAllGames();

        //Assert
        verify(playerComponent.gameLogic, times(1)).getAllGames();
    }
}
