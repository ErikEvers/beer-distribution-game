package communicationcomponent.messagehandler;

import domainobjects.Election;
import domainobjects.RoundModel;
import domainobjects.TurnModel;
import org.han.ica.asd.c.messagehandler.messagetypes.ElectionMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.ResponseMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.RoundModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
import org.han.ica.asd.c.messagehandler.receiving.GameMessageReceiver;
import org.han.ica.asd.c.messagehandler.sending.GameMessageClient;
import org.han.ica.asd.c.observers.IConnectorObserver;
import org.han.ica.asd.c.observers.IElectionObserver;
import org.han.ica.asd.c.observers.IRoundModelObserver;
import org.han.ica.asd.c.observers.ITurnModelObserver;
import org.han.ica.asd.c.socketrpc.SocketClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class GameMessageReceiverTest {

    GameMessageReceiver gameMessageReceiver;

    @Mock
    private IRoundModelObserver roundModelObserver;

    @Mock
    private ITurnModelObserver turnModelObserver;

    @Mock
    private IElectionObserver electionObserver;

    @BeforeEach
    public void setUp() {
        initMocks(this);

        ArrayList<IConnectorObserver> observers = new ArrayList<>();
        observers.add(roundModelObserver);
        observers.add(turnModelObserver);
        observers.add(electionObserver);

        gameMessageReceiver = new GameMessageReceiver(observers);
    }

    @Test
    public void electionReceived() {
        Election election = new Election();
        ElectionMessage electionMessage = new ElectionMessage(election);

        gameMessageReceiver.gameMessageReceived(electionMessage);

        verify(electionObserver).electionReceived(election);
    }

    @Test
    public void turnModelReceived() {
        TurnModel turnModel = new TurnModel(10);
        TurnModelMessage turnModelMessage = new TurnModelMessage(turnModel);

        gameMessageReceiver.gameMessageReceived(turnModelMessage);

        verify(turnModelObserver).turnModelReceived(turnModel);
    }

    @Test
    public void roundModelRecieved() {
        RoundModel roundModel = new RoundModel();
        RoundModelMessage roundModelMessageStage = new RoundModelMessage(roundModel, 0);
        RoundModelMessage roundModelMessageCommit = new RoundModelMessage(roundModel, 1);

        gameMessageReceiver.gameMessageReceived(roundModelMessageStage);
        gameMessageReceiver.gameMessageReceived(roundModelMessageCommit);

        verify(roundModelObserver).roundModelReceived(roundModel);
    }


}
