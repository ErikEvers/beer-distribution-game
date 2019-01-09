package org.han.ica.asd.c.messagehandler;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.Connector;
import org.han.ica.asd.c.MessageDirector;
import org.han.ica.asd.c.faultdetection.FailLog;
import org.han.ica.asd.c.faultdetection.FaultDetectionClient;
import org.han.ica.asd.c.faultdetection.FaultDetectorLeader;
import org.han.ica.asd.c.faultdetection.FaultResponder;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.communication.IElectionObserver;
import org.han.ica.asd.c.interfaces.communication.IFacilityMessageObserver;
import org.han.ica.asd.c.interfaces.communication.IGameConfigurationObserver;
import org.han.ica.asd.c.interfaces.communication.IRoundModelObserver;
import org.han.ica.asd.c.interfaces.communication.ITurnModelObserver;
import org.han.ica.asd.c.messagehandler.messagetypes.ChooseFacilityMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.ConfigurationMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.ElectionMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.RequestAllFacilitiesMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.RoundModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
import org.han.ica.asd.c.messagehandler.receiving.GameMessageFilterer;
import org.han.ica.asd.c.messagehandler.receiving.GameMessageReceiver;
import org.han.ica.asd.c.messagehandler.sending.GameMessageClient;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.model.interface_models.ElectionModel;
import org.han.ica.asd.c.socketrpc.IServerObserver;
import org.han.ica.asd.c.socketrpc.SocketClient;
import org.han.ica.asd.c.socketrpc.SocketServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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

    @Mock
    private IFacilityMessageObserver facilityMessageObserver;

    @BeforeEach
    public void setUp() {
        initMocks(this);

        Injector inject = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                requestStaticInjection(SocketClient.class);
                requestStaticInjection(SocketServer.class);
                requestStaticInjection(FailLog.class);
                requestStaticInjection(FaultDetectionClient.class);
                requestStaticInjection(FaultDetectorLeader.class);
                requestStaticInjection(FaultResponder.class);
                requestStaticInjection(Connector.class);
                requestStaticInjection(GameMessageClient.class);
                bind(IServerObserver.class).annotatedWith(Names.named("MessageDirector")).to(MessageDirector.class);

            }
        });
        gameMessageReceiver = inject.getInstance(GameMessageReceiver.class);

        ArrayList<IConnectorObserver> observers = new ArrayList<>();
        observers.add(roundModelObserver);
        observers.add(turnModelObserver);
        observers.add(electionObserver);
        observers.add(gameConfigurationObserver);
        observers.add(facilityMessageObserver);
        
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

        when(gameMessageFilterer.isUnique(configurationMessageStage)).thenReturn(true);
        gameMessageReceiver.gameMessageReceived(configurationMessageStage);

        when(gameMessageFilterer.isUnique(configurationMessageCommit)).thenReturn(true);
        gameMessageReceiver.gameMessageReceived(configurationMessageCommit);

        verify(gameConfigurationObserver).gameConfigurationReceived(configuration);
    }

    @Test
    public void roundModelReceived() {
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

    @Test
    public void chooseFacilityMessageReceived() throws Exception {
        Facility facility = new Facility();
        facility.setFacilityId(123);
        when(gameMessageFilterer.isUnique(any())).thenReturn(true);
        ChooseFacilityMessage chooseFacilityMessage = new ChooseFacilityMessage(facility);
        gameMessageReceiver.gameMessageReceived(chooseFacilityMessage);
        verify(facilityMessageObserver).chooseFacility(facility);
    }

    @Test
    public void shouldReturnExceptionResponseWhenErrorIsThrown() throws Exception {
        Facility facility = new Facility();
        facility.setFacilityId(123);

        ChooseFacilityMessage chooseFacilityMessage = new ChooseFacilityMessage(facility);

        when(gameMessageFilterer.isUnique(any())).thenReturn(true);
        doThrow(Exception.class).when(facilityMessageObserver).chooseFacility(any(Facility.class));

        chooseFacilityMessage = (ChooseFacilityMessage) gameMessageReceiver.gameMessageReceived(chooseFacilityMessage);

        assertEquals(Exception.class, chooseFacilityMessage.getException().getClass());
    }

    @Test
    public void requestAllFacilitiesMessageReceived(){
        List<Facility> facilities = new ArrayList<>();
        when(gameMessageFilterer.isUnique(any())).thenReturn(true);
        RequestAllFacilitiesMessage requestAllFacilitiesMessage = new RequestAllFacilitiesMessage();
        gameMessageReceiver.gameMessageReceived(requestAllFacilitiesMessage);
        verify(facilityMessageObserver).getAllFacilities();
    }
}