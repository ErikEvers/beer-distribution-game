package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.dao.*;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.dao_model.BeerGameDB;
import org.han.ica.asd.c.model.dao_model.FacilityLinkedToDB;
import org.han.ica.asd.c.model.dao_model.FacilityTurnDB;
import org.han.ica.asd.c.model.dao_model.GameBusinessRulesInFacilityTurnDB;
import org.han.ica.asd.c.model.dao_model.RoundDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class PersistenceTest {
	public static final Logger LOGGER = Logger.getLogger(PersistenceTest.class.getName());
	private IBeerDisitributionGameDAO roundDAOMock;
	private IBeerDisitributionGameDAO beerGameDAOMock;
	private IBeerDisitributionGameDAO gameBusinessRulesInFacilityTurnMock;
	private IBeerDisitributionGameDAO facilityTurnDAOMock;
	private Persistence persistence;
	private RoundDB round;
	private BeerGameDB beerGame;
	private GameBusinessRulesInFacilityTurnDB gameBusinessRulesInFacilityTurn;
	private FacilityTurnDB facilityTurn;
	private FacilityLinkedToDB facilityLinkedTo;

	@BeforeEach
	void setUp() {
		round = new RoundDB("Beergame",1);
		beerGame = new BeerGameDB(UUID.randomUUID().toString(),"Beergame", LocalDateTime.now().toString(),"");
		gameBusinessRulesInFacilityTurn = new GameBusinessRulesInFacilityTurnDB(1,1,1,"Beergame","Henk","Test","Test");
		facilityTurn = new FacilityTurnDB("Beergame",1,1,1,1,1,1,1,1);
		facilityLinkedTo = new FacilityLinkedToDB("Beergame",1,1,true);

		roundDAOMock = mock(RoundDAO.class);
		Mockito.doNothing().when((RoundDAO)roundDAOMock).createRound(anyString(),anyInt());
		when(((RoundDAO) roundDAOMock).getRound(anyString(),anyInt())).thenReturn(round);

		beerGameDAOMock = mock(BeergameDAO.class);
		when(((BeergameDAO)beerGameDAOMock).getGameLog(anyString())).thenReturn(beerGame);

		gameBusinessRulesInFacilityTurnMock = mock(GameBusinessRulesInFacilityTurnDAO.class);
		when(((GameBusinessRulesInFacilityTurnDAO)gameBusinessRulesInFacilityTurnMock).readTurn(anyString(),anyInt(),anyInt(),anyInt())).thenReturn(gameBusinessRulesInFacilityTurn);

		facilityTurnDAOMock = mock(FacilityTurnDAO.class);
		when(((FacilityTurnDAO)facilityTurnDAOMock).fetchTurn(round,facilityLinkedTo)).thenReturn(facilityTurn);
		Mockito.doNothing().when((FacilityTurnDAO)facilityTurnDAOMock).createTurn(facilityTurn);


		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IDatabaseConnection.class).toInstance(DBConnectionTest.getInstance());
				bind(IBeerDisitributionGameDAO.class).annotatedWith(Names.named("RoundDAO")).toInstance(roundDAOMock);
				bind(IBeerDisitributionGameDAO.class).annotatedWith(Names.named("BeergameDAO")).toInstance(beerGameDAOMock);
				bind(IBeerDisitributionGameDAO.class).annotatedWith(Names.named("GameBusinessRulesRulesInFacilityTurnDAO")).toInstance(gameBusinessRulesInFacilityTurnMock);
				bind(IBeerDisitributionGameDAO.class).annotatedWith(Names.named("FacilityTurnDAO")).toInstance(facilityTurnDAOMock);
			}
		});

		persistence = injector.getInstance(Persistence.class);
	}

	@Test
	void saveRoundDataTest() {
		persistence.saveRoundData(round);
		//verify(((RoundDAO)roundDAOMock), times(1)).createRound(anyString(),anyInt());
	}



//	@Test
//	void fetchRoundDataTest() {
//		persistence.fetchRoundData(anyString(),anyInt());
//		//verify(((RoundDAO)roundDAOMock), times(1)).getRound(anyString(),anyInt());
//	}

	@Test
	void getGameLogTest() {
//		persistence.getGameLog(anyString());
//		verify(((BeergameDAO)beerGameDAOMock), times(1)).getGameLog(anyString());
	}

	@Test
	void logUsedBusinessRuleToCreateOrderTest() {
		persistence.logUsedBusinessRuleToCreateOrder(gameBusinessRulesInFacilityTurn);
//		verify(((GameBusinessRulesInFacilityTurnDAO)gameBusinessRulesInFacilityTurnMock), times(1)).createTurn(gameBusinessRulesInFacilityTurn);
	}

	@Test
	void fetchTurnDataTest() {
		persistence.fetchTurnData(round,facilityLinkedTo);
//		verify(((FacilityTurnDAO)facilityTurnDAOMock), times(1)).fetchTurn(round,facilityLinkedTo);
	}

	@Test
	void saveTurnDataTest() {
		persistence.saveTurnData(facilityTurn);
//		verify(((FacilityTurnDAO)facilityTurnDAOMock), times(1)).createTurn(facilityTurn);
	}
}