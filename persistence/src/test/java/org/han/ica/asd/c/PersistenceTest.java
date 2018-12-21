//package org.han.ica.asd.c;
//
//import com.google.inject.AbstractModule;
//import com.google.inject.Guice;
//import com.google.inject.Injector;
//import org.han.ica.asd.c.dao.BeergameDAO;
//import org.han.ica.asd.c.dao.FacilityTurnDAO;
//import org.han.ica.asd.c.dao.GameBusinessRulesInFacilityTurnDAO;
//import org.han.ica.asd.c.dao.Persistence;
//import org.han.ica.asd.c.dao.RoundDAO;
//import org.han.ica.asd.c.dbconnection.DBConnectionTest;
//import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
//import org.han.ica.asd.c.model.dao_model.BeerGameDB;
//import org.han.ica.asd.c.model.dao_model.FacilityLinkedToDB;
//import org.han.ica.asd.c.model.dao_model.FacilityTurnDB;
//import org.han.ica.asd.c.model.dao_model.GameBusinessRulesInFacilityTurnDB;
//import org.han.ica.asd.c.model.domain_objects.Round;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//import java.util.logging.Logger;
//
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class PersistenceTest {
//	public static final Logger LOGGER = Logger.getLogger(PersistenceTest.class.getName());
//	private RoundDAO roundDAOMock;
//	private BeergameDAO beerGameDAOMock;
//	private GameBusinessRulesInFacilityTurnDAO gameBusinessRulesInFacilityTurnMock;
//	private FacilityTurnDAO facilityTurnDAOMock;
//	private Persistence persistence;
//	private Round round;
//	private BeerGameDB beerGame;
//	private GameBusinessRulesInFacilityTurnDB gameBusinessRulesInFacilityTurn;
//	private FacilityTurnDB facilityTurn;
//	private FacilityLinkedToDB facilityLinkedTo;
//
//	@BeforeEach
//	void setUp() {
//		round = new Round();
//		beerGame = new BeerGameDB(UUID.randomUUID().toString(),"Beergame", LocalDateTime.now().toString(),"");
//		gameBusinessRulesInFacilityTurn = new GameBusinessRulesInFacilityTurnDB(1,1,1,"Beergame","Henk","Test","Test");
//		facilityTurn = new FacilityTurnDB("Beergame",1,1,1,1,1,1,1,1);
//		facilityLinkedTo = new FacilityLinkedToDB("Beergame",1,1,true);
//
//		roundDAOMock = mock(RoundDAO.class);
//		Mockito.doNothing().when(roundDAOMock).createRound(anyInt());
//		when((roundDAOMock).getRound(anyInt())).thenReturn(round);
//
//		beerGameDAOMock = mock(BeergameDAO.class);
//		when((beerGameDAOMock).getGameLog(anyString())).thenReturn(beerGame);
//
//		gameBusinessRulesInFacilityTurnMock = mock(GameBusinessRulesInFacilityTurnDAO.class);
//		when((gameBusinessRulesInFacilityTurnMock).readTurn(anyString(),anyInt(),anyInt(),anyInt())).thenReturn(gameBusinessRulesInFacilityTurn);
//
//		facilityTurnDAOMock = mock(FacilityTurnDAO.class);
//		when((facilityTurnDAOMock).fetchTurn(round,facilityLinkedTo)).thenReturn(facilityTurn);
//		Mockito.doNothing().when(facilityTurnDAOMock).createTurn(facilityTurn);
//
//
//		Injector injector = Guice.createInjector(new AbstractModule() {
//			@Override
//			protected void configure() {
//				bind(IDatabaseConnection.class).toInstance(DBConnectionTest.getInstance());
//				bind(RoundDAO.class).toInstance(roundDAOMock);
//				bind(BeergameDAO.class).toInstance(beerGameDAOMock);
//				bind(GameBusinessRulesInFacilityTurnDAO.class).toInstance(gameBusinessRulesInFacilityTurnMock);
//				bind(FacilityTurnDAO.class).toInstance(facilityTurnDAOMock);
//			}
//		});
//
//		persistence = injector.getInstance(Persistence.class);
//	}
//
//	@Test
//	void saveRoundDataTest() {
//		persistence.saveRoundData(round);
//		//verify(((RoundDAO)roundDAOMock), times(1)).createRound(anyString(),anyInt());
//	}
//
//
//
////	@Test
////	void fetchRoundDataTest() {
////		persistence.fetchRoundData(anyString(),anyInt());
////		//verify(((RoundDAO)roundDAOMock), times(1)).getRound(anyString(),anyInt());
////	}
//
//	@Test
//	void getGameLogTest() {
////		persistence.getGameLog(anyString());
////		verify(((BeergameDAO)beerGameDAOMock), times(1)).getGameLog(anyString());
//	}
//
//	@Test
//	void logUsedBusinessRuleToCreateOrderTest() {
//		persistence.logUsedBusinessRuleToCreateOrder(gameBusinessRulesInFacilityTurn);
////		verify(((GameBusinessRulesInFacilityTurnDAO)gameBusinessRulesInFacilityTurnMock), times(1)).createTurn(gameBusinessRulesInFacilityTurn);
//	}
//
//	@Test
//	void fetchTurnDataTest() {
//		persistence.fetchTurnData(round,facilityLinkedTo);
////		verify(((FacilityTurnDAO)facilityTurnDAOMock), times(1)).fetchTurn(round,facilityLinkedTo);
//	}
//
//	@Test
//	void saveTurnDataTest() {
//		persistence.saveTurnData(facilityTurn);
////		verify(((FacilityTurnDAO)facilityTurnDAOMock), times(1)).createTurn(facilityTurn);
//	}
//}