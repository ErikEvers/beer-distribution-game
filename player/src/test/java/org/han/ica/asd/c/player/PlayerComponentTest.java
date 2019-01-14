package org.han.ica.asd.c.player;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.gamelogic.GameLogic;
import org.han.ica.asd.c.interfaces.communication.IFinder;
import org.han.ica.asd.c.interfaces.persistence.IGameStore;

import org.han.ica.asd.c.interfaces.gamelogic.IConnectedForPlayer;

import org.han.ica.asd.c.interfaces.player.IPlayerGameLogic;
import org.han.ica.asd.c.model.domain_objects.Facility;

import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.mockito.Mockito.*;

public class PlayerComponentTest {
    private PlayerComponent playerComponent;
    private IPlayerGameLogic logicMock;

    @BeforeEach
    void beforeTest() {
        logicMock = mock(GameLogic.class);

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IPlayerGameLogic.class).toInstance(logicMock);
                bind(IGameStore.class).to(PersistenceStub.class);
                bind(IConnectedForPlayer.class).to(CommunicationStub.class);
                bind(IFinder.class).to(RoomFinderStub.class);
            }
        });
        playerComponent = injector.getInstance(PlayerComponent.class);
    }

    @Test
    void getAllGamesCallsMethodOfSameNameOnceInGameLogicTest() {

        //Act
        playerComponent.getAllGames();

        //Assert
        verify(logicMock, times(1)).getAllGames();
    }

    @Test
    void connectToGameCallsMethodOfSameNameOnceInGameLogicTest() {
        //Act
        playerComponent.connectToGame("");

        //Assert
        verify(logicMock, times(1)).connectToGame("");
    }

    @Test
    void requestFacilityUsageCallsMethodOfSameNameOnceInGameLogicTest() {
        //Act
        playerComponent.requestFacilityUsage(mock(Facility.class));

        //Assert
        verify(logicMock, times(1)).requestFacilityUsage(any(Facility.class));
    }

    @Test
    void getAllFacilitiesCallsMethodOfSameNameOnceInGameLogicTest() {
        //Act
        playerComponent.getAllFacilities();

        //Assert
        verify(logicMock, times(1)).getAllFacilities();
    }

    @Test
    void selectAgentCallsMethodOfSameNameOnceInGameLogicTest() {
        //Arrange
        playerComponent.gameLogic = mock(GameLogic.class);
        ProgrammedAgent programmedAgent = mock(ProgrammedAgent.class);

        //Act
        playerComponent.selectAgent(programmedAgent);

        //Assert
        verify(playerComponent.gameLogic, times(1)).selectAgent(programmedAgent);
    }
}
