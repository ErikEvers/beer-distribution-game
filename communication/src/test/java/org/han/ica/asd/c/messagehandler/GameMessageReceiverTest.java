//package org.han.ica.asd.c.messagehandler;
//
//import com.google.inject.AbstractModule;
//import com.google.inject.Guice;
//import com.google.inject.Injector;
//import com.google.inject.name.Names;
//import org.han.ica.asd.c.Connector;
//import org.han.ica.asd.c.MessageDirector;
//import org.han.ica.asd.c.interfaces.communication.IFinder;
//import org.han.ica.asd.c.discovery.RoomFinder;
//import org.han.ica.asd.c.faultdetection.FailLog;
//import org.han.ica.asd.c.faultdetection.FaultDetectionClient;
//import org.han.ica.asd.c.faultdetection.FaultDetectorLeader;
//import org.han.ica.asd.c.faultdetection.FaultResponder;
//import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
//import org.han.ica.asd.c.interfaces.communication.IElectionObserver;
//import org.han.ica.asd.c.interfaces.communication.IFacilityMessageObserver;
//import org.han.ica.asd.c.interfaces.communication.IGameStartObserver;
//import org.han.ica.asd.c.interfaces.communication.IRoundModelObserver;
//import org.han.ica.asd.c.interfaces.communication.ITurnModelObserver;
//import org.han.ica.asd.c.messagehandler.messagetypes.ChooseFacilityMessage;
//import org.han.ica.asd.c.messagehandler.messagetypes.GameStartMessage;
//import org.han.ica.asd.c.messagehandler.messagetypes.ElectionMessage;
//import org.han.ica.asd.c.messagehandler.messagetypes.RequestGameDataMessage;
//import org.han.ica.asd.c.messagehandler.messagetypes.RoundModelMessage;
//import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
//import org.han.ica.asd.c.messagehandler.receiving.GameMessageFilterer;
//import org.han.ica.asd.c.messagehandler.receiving.GameMessageReceiver;
//import org.han.ica.asd.c.messagehandler.sending.GameMessageClient;
//import org.han.ica.asd.c.model.domain_objects.BeerGame;
//import org.han.ica.asd.c.model.domain_objects.Facility;
//import org.han.ica.asd.c.model.domain_objects.Round;
//import org.han.ica.asd.c.model.interface_models.ElectionModel;
//import org.han.ica.asd.c.socketrpc.IServerObserver;
//import org.han.ica.asd.c.socketrpc.SocketClient;
//import org.han.ica.asd.c.socketrpc.SocketServer;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.platform.runner.JUnitPlatform;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.mockito.MockitoAnnotations.initMocks;
//
//@ExtendWith(MockitoExtension.class)
//@RunWith(JUnitPlatform.class)
//public class GameMessageReceiverTest {
//
//    private GameMessageReceiver gameMessageReceiver;
//
//    @Mock
//    private IRoundModelObserver roundModelObserver;
//
//    @Mock
//    private ITurnModelObserver turnModelObserver;
//
//    @Mock
//    private IElectionObserver electionObserver;
//
//    @Mock
//    private GameMessageFilterer gameMessageFilterer;
//
//    @Mock
//    private IGameStartObserver gameStartObserver;
//
//    @Mock
//    private IFacilityMessageObserver facilityMessageObserver;
//
//    @BeforeEach
//    public void setUp() {
//        initMocks(this);
//
//        Injector inject = Guice.createInjector(new AbstractModule() {
//            @Override
//            protected void configure() {
//                requestStaticInjection(SocketClient.class);
//                requestStaticInjection(SocketServer.class);
//                requestStaticInjection(FailLog.class);
//                requestStaticInjection(FaultDetectionClient.class);
//                requestStaticInjection(FaultDetectorLeader.class);
//                requestStaticInjection(FaultResponder.class);
//                requestStaticInjection(Connector.class);
//                requestStaticInjection(GameMessageClient.class);
//                bind(IServerObserver.class).annotatedWith(Names.named("MessageDirector")).to(MessageDirector.class);
//                bind(IFinder.class).to(RoomFinder.class);
//            }
//        });
//        gameMessageReceiver = inject.getInstance(GameMessageReceiver.class);
//
//        ArrayList<IConnectorObserver> observers = new ArrayList<>();
//        observers.add(roundModelObserver);
//        observers.add(turnModelObserver);
//        observers.add(electionObserver);
//        observers.add(gameStartObserver);
//        observers.add(facilityMessageObserver);
//
//        gameMessageReceiver.setObservers(observers);
//        gameMessageReceiver.setGameMessageFilterer(gameMessageFilterer);
//    }
//
//    @Test
//    public void electionReceived() {
//        ElectionModel election = new ElectionModel();
//        ElectionMessage electionMessage = new ElectionMessage(election);
//
//        when(gameMessageFilterer.isUnique(electionMessage)).thenReturn(true);
//
//        gameMessageReceiver.gameMessageReceived(electionMessage, anyString());
//
//        verify(electionObserver).electionReceived(election);
//    }
//
//    @Test
//    public void turnModelReceived() {
//        Round turnModel = new Round();
//        TurnModelMessage turnModelMessage = new TurnModelMessage(turnModel);
//
//        when(gameMessageFilterer.isUnique(turnModelMessage)).thenReturn(true);
//
//        gameMessageReceiver.gameMessageReceived(turnModelMessage, anyString());
//
//        verify(turnModelObserver).turnModelReceived(turnModel);
//    }
//
//    @Test
//    public void gameStartReceived() {
//        BeerGame beerGame = new BeerGame();
//
//        GameStartMessage gameStartMessageStage = new GameStartMessage(beerGame);
//        gameStartMessageStage.setPhaseToStage();
//
//        GameStartMessage gameStartMessageCommit = new GameStartMessage(beerGame);
//        gameStartMessageCommit.setPhaseToCommit();
//
//        when(gameMessageFilterer.isUnique(gameStartMessageStage)).thenReturn(true);
//        gameMessageReceiver.gameMessageReceived(gameStartMessageStage, "");
//
//        when(gameMessageFilterer.isUnique(gameStartMessageCommit)).thenReturn(true);
//        gameMessageReceiver.gameMessageReceived(gameStartMessageCommit, "");
//
//        verify(gameStartObserver).gameStartReceived(beerGame);
//    }
//
//    @Test
//    public void roundModelReceived() {
//        Round roundModel = new Round();
//
//        RoundModelMessage roundModelMessageStage = new RoundModelMessage(roundModel);
//        roundModelMessageStage.setPhaseToStage();
//
//        RoundModelMessage roundModelMessageCommit = new RoundModelMessage(roundModel);
//        roundModelMessageCommit.setPhaseToCommit();
//
//        when(gameMessageFilterer.isUnique(roundModelMessageStage)).thenReturn(true);
//        gameMessageReceiver.gameMessageReceived(roundModelMessageStage, anyString());
//
//        when(gameMessageFilterer.isUnique(roundModelMessageCommit)).thenReturn(true);
//        gameMessageReceiver.gameMessageReceived(roundModelMessageCommit, anyString());
//
//        verify(roundModelObserver).roundModelReceived(roundModel);
//    }
//
//    @Test
//    public void chooseFacilityMessageReceived() throws Exception {
//        Facility facility = new Facility();
//        facility.setFacilityId(123);
//        when(gameMessageFilterer.isUnique(any())).thenReturn(true);
//        ChooseFacilityMessage chooseFacilityMessage = new ChooseFacilityMessage(facility);
//        gameMessageReceiver.gameMessageReceived(chooseFacilityMessage, anyString());
//        verify(facilityMessageObserver).chooseFacility(facility);
//    }
//
//    @Test
//    public void shouldReturnExceptionResponseWhenErrorIsThrown() throws Exception {
//        Facility facility = new Facility();
//        facility.setFacilityId(123);
//
//        ChooseFacilityMessage chooseFacilityMessage = new ChooseFacilityMessage(facility);
//
//        when(gameMessageFilterer.isUnique(any())).thenReturn(true);
//        doThrow(Exception.class).when(facilityMessageObserver).chooseFacility(any(Facility.class));
//
//        chooseFacilityMessage = (ChooseFacilityMessage) gameMessageReceiver.gameMessageReceived(chooseFacilityMessage, anyString());
//
//        assertEquals(Exception.class, chooseFacilityMessage.getException().getClass());
//    }
//
//    @Test
//    public void requestAllFacilitiesMessageReceived(){
//        when(gameMessageFilterer.isUnique(any())).thenReturn(true);
//        RequestGameDataMessage requestGameDataMessage = new RequestGameDataMessage();
//        gameMessageReceiver.gameMessageReceived(requestGameDataMessage, anyString());
//        verify(facilityMessageObserver).getGameData(anyString());
//    }
//}