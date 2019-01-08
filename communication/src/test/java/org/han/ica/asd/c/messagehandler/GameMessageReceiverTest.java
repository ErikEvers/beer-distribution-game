package org.han.ica.asd.c.messagehandler;

import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.communication.IElectionObserver;
import org.han.ica.asd.c.interfaces.communication.IGameConfigurationObserver;
import org.han.ica.asd.c.interfaces.communication.IRoundModelObserver;
import org.han.ica.asd.c.interfaces.communication.ITurnModelObserver;
import org.han.ica.asd.c.messagehandler.messagetypes.ConfigurationMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.ElectionMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.RoundModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
import org.han.ica.asd.c.messagehandler.receiving.GameMessageFilterer;
import org.han.ica.asd.c.messagehandler.receiving.GameMessageReceiver;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.model.interface_models.ElectionModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class GameMessageReceiverTest {

    private GameMessageReceiver gameMessageReceiver;

    @Mock
    private IRoundModelObserver roundModelObserver;

    @Mock
    private ITurnModelObserver turnModelObserver;

    @Mock
    private IElectionObserver electionObserver;

    @Mock
    GameMessageFilterer gameMessageFilterer;

    @Mock
    private IGameConfigurationObserver gameConfigurationObserver;

    @BeforeEach
    public void setUp() {
        initMocks(this);

        ArrayList<IConnectorObserver> observers = new ArrayList<>();
        observers.add(roundModelObserver);
        observers.add(turnModelObserver);
        observers.add(electionObserver);
        observers.add(gameConfigurationObserver);

        gameMessageReceiver = new GameMessageReceiver();
        gameMessageReceiver.setObservers(observers);
        gameMessageReceiver.setGameMessageFilterer(gameMessageFilterer);
    }

    @Test
    public void electionReceived() {
        ElectionModel election = new ElectionModel();
        ElectionMessage electionMessage = new ElectionMessage(election);

        when(gameMessageFilterer.isUnique(electionMessage)).thenReturn(true);

        gameMessageReceiver.gameMessageReceived(electionMessage);

        verify(electionObserver).electionReceived(election);
    }

    @Test
    public void turnModelReceived() {
        Round turnModel = new Round();
        TurnModelMessage turnModelMessage = new TurnModelMessage(turnModel);

        when(gameMessageFilterer.isUnique(turnModelMessage)).thenReturn(true);

        gameMessageReceiver.gameMessageReceived(turnModelMessage);

        verify(turnModelObserver).turnModelReceived(turnModel);
    }

    @Test
    public void configurationReceived() {
        Configuration configuration = new Configuration();

        ConfigurationMessage configurationMessageStage = new ConfigurationMessage(configuration);
        configurationMessageStage.setPhaseToStage();

        ConfigurationMessage configurationMessageCommit = new ConfigurationMessage(configuration);
        configurationMessageCommit.setPhaseToCommit();

        gameMessageReceiver.gameMessageReceived(configurationMessageStage);
        gameMessageReceiver.gameMessageReceived(configurationMessageCommit);

        verify(gameConfigurationObserver).gameConfigurationReceived(configuration);
    }

    @Test
    public void roundModelRecieved() {
        Round roundModel = new Round();

        RoundModelMessage roundModelMessageStage = new RoundModelMessage(roundModel);
        roundModelMessageStage.setPhaseToStage();

        RoundModelMessage roundModelMessageCommit = new RoundModelMessage(roundModel);
        roundModelMessageCommit.setPhaseToCommit();

        when(gameMessageFilterer.isUnique(roundModelMessageStage)).thenReturn(true);
        gameMessageReceiver.gameMessageReceived(roundModelMessageStage);

        when(gameMessageFilterer.isUnique(roundModelMessageCommit)).thenReturn(true);
        gameMessageReceiver.gameMessageReceived(roundModelMessageCommit);

        verify(roundModelObserver).roundModelReceived(roundModel);
    }
}