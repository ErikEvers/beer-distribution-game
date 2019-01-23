package org.han.ica.asd.c.player;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.gamelogic.GameLogic;
import org.han.ica.asd.c.interfaces.communication.IConnectorForSetup;
import org.han.ica.asd.c.interfaces.gamelogic.IConnectorForPlayer;
import org.han.ica.asd.c.interfaces.gamelogic.IPlayerGameLogic;
import org.han.ica.asd.c.interfaces.communication.IFinder;
import org.han.ica.asd.c.interfaces.persistence.IGameStore;

import org.junit.jupiter.api.BeforeEach;

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
                bind(IConnectorForPlayer.class).to(CommunicationStub.class);
                bind(IFinder.class).to(RoomFinderStub.class);
                bind(IConnectorForSetup.class).toInstance(mock(IConnectorForSetup.class));
                bind(IGUIHandler.class).annotatedWith(Names.named("PlayGame")).toInstance(mock(IGUIHandler.class));
            }
        });
        playerComponent = injector.getInstance(PlayerComponent.class);
    }
}
