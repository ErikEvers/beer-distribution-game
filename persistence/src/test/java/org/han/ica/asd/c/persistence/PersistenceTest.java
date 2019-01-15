package org.han.ica.asd.c.persistence;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dao.BeergameDAO;
import org.han.ica.asd.c.dao.FacilityDAO;
import org.han.ica.asd.c.dao.GameBusinessRulesInFacilityTurnDAO;
import org.han.ica.asd.c.dao.PlayerDAO;
import org.han.ica.asd.c.dao.RoundDAO;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityTurn;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnDeliver;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRules;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRulesInFacilityTurn;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.junit.Assert;
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
	private PlayerDAO playerDAOMock;
	private GameBusinessRulesInFacilityTurnDAO gameBusinessRulesInFacilityTurnMock;
	private FacilityDAO facilityDAOMock;
	private Persistence persistence;
	private Round round;
	private Player player;
	private BeerGame beerGame;
	private GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn;
	private GameBusinessRules businessrules;
	private List<GameBusinessRules> businessRulesList;
	private List<FacilityTurn> facilityTurns;
	private List<FacilityTurnOrder> facilityTurnOrders;
	private List<FacilityTurnDeliver> facilityTurnDelivers;
	private List<Facility> facilities;
	private static final Facility FACILITY = new Facility(null, 1);
	private static final Facility FACILITY2 = new Facility(null, 2);

	@BeforeEach
	void setUp() {
		facilityTurns = new ArrayList<>();
		facilityTurnOrders = new ArrayList<>();
		facilityTurnDelivers = new ArrayList<>();
		facilities = new ArrayList<>();

		facilityTurns.add(new FacilityTurn(1,1,1,1,1,false));
		facilityTurnOrders.add(new FacilityTurnOrder(1,1,1));
		facilityTurnDelivers.add(new FacilityTurnDeliver(1,1,1,1));
		facilities.add(FACILITY);
		facilities.add(FACILITY2);

		player = new Player("1", "234", null, "Henk", true);
		businessRulesList = new ArrayList<>();
		businessrules = new GameBusinessRules("Test","");
		businessRulesList.add(businessrules);

		beerGame = new BeerGame(UUID.randomUUID().toString(),"Beergame", LocalDateTime.now().toString(),"");
		gameBusinessRulesInFacilityTurn = new GameBusinessRulesInFacilityTurn(1,1,"Beergame",businessRulesList);


		roundDAOMock = mock(RoundDAO.class);
		Mockito.doNothing().when(roundDAOMock).createRound(anyInt());
		when((roundDAOMock).getRound(anyInt())).thenReturn(round);

		beerGameDAOMock = mock(BeergameDAO.class);
		when((beerGameDAOMock).getGameLog()).thenReturn(beerGame);

		gameBusinessRulesInFacilityTurnMock = mock(GameBusinessRulesInFacilityTurnDAO.class);
		when((gameBusinessRulesInFacilityTurnMock).readTurn(anyInt(),anyInt(),anyString())).thenReturn(gameBusinessRulesInFacilityTurn);

		playerDAOMock = mock(PlayerDAO.class);
		when(playerDAOMock.getPlayer(anyString())).thenReturn(player);

		round = mock(Round.class);
		when(round.getFacilityTurns()).thenReturn(facilityTurns);
		when(round.getFacilityTurnDelivers()).thenReturn(facilityTurnDelivers);
		when(round.getFacilityOrders()).thenReturn(facilityTurnOrders);

		facilityDAOMock = mock(FacilityDAO.class);
		when((facilityDAOMock).readAllFacilitiesInGame()).thenReturn();

		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IDatabaseConnection.class).toInstance(DBConnectionTest.getInstance());
				bind(RoundDAO.class).toInstance(roundDAOMock);
				bind(BeergameDAO.class).toInstance(beerGameDAOMock);
				bind(GameBusinessRulesInFacilityTurnDAO.class).toInstance(gameBusinessRulesInFacilityTurnMock);
				bind(PlayerDAO.class).toInstance(playerDAOMock);
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
		persistence.fetchFacilityTurn(1);
		verify((roundDAOMock), times(1)).getRound(anyInt());
	}

	@Test
	void getGameLogTest() {
		persistence.getGameLog();
		verify((beerGameDAOMock), times(1)).getGameLog();
	}


	@Test
	void logUsedBusinessRuleToCreateOrderTest() {
		persistence.logUsedBusinessRuleToCreateOrder(gameBusinessRulesInFacilityTurn);
		verify((gameBusinessRulesInFacilityTurnMock), times(1)).createTurn(gameBusinessRulesInFacilityTurn);
	}



	@Test
	void saveTurnDataTest() {
		persistence.saveFacilityTurn(round);
		verify((roundDAOMock), times(1)).createRound(anyInt());
		verify((roundDAOMock),times(1)).createFacilityOrder(anyInt(),any(FacilityTurnOrder.class));
		verify((roundDAOMock),times(1)).createFacilityDeliver(anyInt(),any(FacilityTurnDeliver.class));
		verify((roundDAOMock),times(1)).createFacilityTurn(anyInt(),any(FacilityTurn.class));
	}

	@Test
	void getPlayerByIdTest(){
		persistence.getPlayerById("Henk");
		verify((playerDAOMock),times(1)).getPlayer(anyString());
	}

	@Test
	public void saveGameLogTest(){
		persistence.saveGameLog(beerGame);
		verify((beerGameDAOMock),times(1)).createBeergame(beerGame);
	}

	@Test
	void getAllFacilities(){
		List<Facility> facilities = persistence.getAllFacilities();
		verify((facilityDAOMock), times(1)).readAllFacilitiesInGame();
		Assert.assertEquals(2, facilities.size());
	}

	@Test
	void getBeerGame(){
		persistence.getGameLog();
		verify((beerGameDAOMock), times(1)).getGameLog();
	}

	@Test
	void getAllBeerGames(){
		persistence.getAllBeerGames();
		verify((beerGameDAOMock), times(1)).readBeergames();

	}
}