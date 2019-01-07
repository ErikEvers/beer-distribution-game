package org.han.ica.asd.c.Persistence;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dao.BeergameDAO;
import org.han.ica.asd.c.dao.GameBusinessRulesInFacilityTurnDAO;
import org.han.ica.asd.c.dao.RoundDAO;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.FacilityTurn;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnDeliver;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRules;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRulesInFacilityTurn;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class PersistenceTest {
	private RoundDAO roundDAOMock;
	private BeergameDAO beerGameDAOMock;
	private GameBusinessRulesInFacilityTurnDAO gameBusinessRulesInFacilityTurnMock;
	private Persistence persistence;
	private Round round;
	private BeerGame beerGame;
	private GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn;
	private GameBusinessRules businessrules;
	private List<GameBusinessRules> businessRulesList;
	private List<FacilityTurn> facilityTurns;
	private List<FacilityTurnOrder> facilityTurnOrders;
	private List<FacilityTurnDeliver> facilityTurnDelivers;

	@BeforeEach
	void setUp() {
		facilityTurns = new ArrayList<>();
		facilityTurnOrders = new ArrayList<>();
		facilityTurnDelivers = new ArrayList<>();

		facilityTurns.add(new FacilityTurn(1,1,1,1,1,false));
		facilityTurnOrders.add(new FacilityTurnOrder(1,1,1));
		facilityTurnDelivers.add(new FacilityTurnDeliver(1,1,1,1));


		businessRulesList = new ArrayList<>();
		businessrules = new GameBusinessRules("Test","");
		businessRulesList.add(businessrules);

		beerGame = new BeerGame(UUID.randomUUID().toString(),"Beergame", LocalDateTime.now().toString(),"");
		gameBusinessRulesInFacilityTurn = new GameBusinessRulesInFacilityTurn(1,1,"Beergame",businessRulesList);


		roundDAOMock = mock(RoundDAO.class);
		Mockito.doNothing().when(roundDAOMock).createRound(anyInt());
		when((roundDAOMock).getRound(anyInt())).thenReturn(round);

		beerGameDAOMock = mock(BeergameDAO.class);
		when((beerGameDAOMock).getGameLog(anyString())).thenReturn(beerGame);

		gameBusinessRulesInFacilityTurnMock = mock(GameBusinessRulesInFacilityTurnDAO.class);
		when((gameBusinessRulesInFacilityTurnMock).readTurn(anyInt(),anyInt(),anyString())).thenReturn(gameBusinessRulesInFacilityTurn);

		round = mock(Round.class);
		when(round.getFacilityTurns()).thenReturn(facilityTurns);
		when(round.getFacilityTurnDelivers()).thenReturn(facilityTurnDelivers);
		when(round.getFacilityOrders()).thenReturn(facilityTurnOrders);



		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IDatabaseConnection.class).toInstance(DBConnectionTest.getInstance());
				bind(RoundDAO.class).toInstance(roundDAOMock);
				bind(BeergameDAO.class).toInstance(beerGameDAOMock);
				bind(GameBusinessRulesInFacilityTurnDAO.class).toInstance(gameBusinessRulesInFacilityTurnMock);
			}
		});

		persistence = injector.getInstance(Persistence.class);
	}

	@Test
	void saveRoundDataTest() {
		persistence.saveRoundData(round);
		verify((roundDAOMock), times(1)).createRound(anyInt());
	}



	@Test
	void fetchRoundDataTest() {
		persistence.fetchRoundData("BeerGame",1);
		verify((roundDAOMock), times(1)).getRound(anyInt());
	}

	@Test
	void getGameLogTest() {
		persistence.getGameLog("BeerGame");
		verify(((BeergameDAO)beerGameDAOMock), times(1)).getGameLog(anyString());
	}


	@Test
	void logUsedBusinessRuleToCreateOrderTest() {
		persistence.logUsedBusinessRuleToCreateOrder(gameBusinessRulesInFacilityTurn);
		verify((gameBusinessRulesInFacilityTurnMock), times(1)).createTurn(gameBusinessRulesInFacilityTurn);
	}



	@Test
	void saveTurnDataTest() {
		persistence.saveTurnData(round);
		verify((roundDAOMock), times(1)).createRound(anyInt());
		verify((roundDAOMock),times(1)).createFacilityOrder(anyInt(),any(FacilityTurnOrder.class));
		verify((roundDAOMock),times(1)).createFacilityDeliver(anyInt(),any(FacilityTurnDeliver.class));
		verify((roundDAOMock),times(1)).createFacilityTurn(anyInt(),any(FacilityTurn.class));
	}
}