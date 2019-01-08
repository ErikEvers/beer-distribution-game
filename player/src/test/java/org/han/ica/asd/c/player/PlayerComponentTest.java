package org.han.ica.asd.c.player;

import org.han.ica.asd.c.agent.Agent;
import org.han.ica.asd.c.gamelogic.GameLogic;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

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

    @Test
    void connectToGameCallsMethodOfSameNameOnceInGameLogicTest() {
        //Arrange
        playerComponent.gameLogic = mock(GameLogic.class);

        //Act
        playerComponent.connectToGame("");

        //Assert
        verify(playerComponent.gameLogic, times(1)).connectToGame("");
    }

    @Test
    void requestFacilityUsageCallsMethodOfSameNameOnceInGameLogicTest() {
        //Arrange
        playerComponent.gameLogic = mock(GameLogic.class);
        Facility facility = mock(Facility.class);

        //Act
        playerComponent.requestFacilityUsage(facility);

        //Assert
        verify(playerComponent.gameLogic, times(1)).requestFacilityUsage(facility);
    }

    @Test
    void getAllFacilitiesCallsMethodOfSameNameOnceInGameLogicTest() {
        //Arrange
        playerComponent.gameLogic = mock(GameLogic.class);

        //Act
        playerComponent.getAllFacilities();

        //Assert
        verify(playerComponent.gameLogic, times(1)).getAllFacilities();
    }

    @Test
    void getSavedAgentsCallsMethodOfSameNameOnceInGameLogicTest() {
        //Arrange
        playerComponent.gameLogic = mock(GameLogic.class);

        when(playerComponent.gameLogic.getSavedAgents()).thenReturn(null);

        //Act
        playerComponent.getSavedAgents();

        //Assert
        verify(playerComponent.gameLogic, times(1)).getSavedAgents();
    }

}
