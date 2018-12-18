package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.BeerGame;
import org.han.ica.asd.c.model.FacilityLinkedTo;
import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.GameBusinessRulesInFacilityTurn;
import org.han.ica.asd.c.model.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class PersistenceTest {
	public static final Logger LOGGER = Logger.getLogger(PersistenceTest.class.getName());
	private IBeerDisitributionGameDAO beerDisitributionGameDAO;
	private Persistence persistence;
	private Round round;
	private BeerGame beerGame;
	private GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn;
	private FacilityTurn facilityTurn;
	private FacilityLinkedTo facilityLinkedTo;

	@BeforeEach
	void setUp() {
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IDatabaseConnection.class).to(DBConnectionTest.class);
			}
		});
		persistence = injector.getInstance(Persistence.class);
		round = new Round("Beergame",1);
		beerGame = new BeerGame(UUID.randomUUID().toString(),"Beergame", LocalDateTime.now().toString(),"");
		gameBusinessRulesInFacilityTurn = new GameBusinessRulesInFacilityTurn(1,1,1,"Beergame","Henk","Test","Test");
		facilityTurn = new FacilityTurn("Beergame",1,1,1,1,1,1,1,1);
		facilityLinkedTo = new FacilityLinkedTo("Beergame",1,1,true);
	}

	@Test
	void saveRoundDataTest() {
		beerDisitributionGameDAO = mock(RoundDAO.class);
		reflect(persistence,beerDisitributionGameDAO);
		Mockito.doNothing().when((RoundDAO)beerDisitributionGameDAO).createRound(anyString(),anyInt());
		persistence.saveRoundData(round);
		verify(((RoundDAO)beerDisitributionGameDAO), times(1)).createRound(anyString(),anyInt());
	}

	@Test
	void fetchRoundDataTest() {
		beerDisitributionGameDAO = mock(RoundDAO.class);
		reflect(persistence,beerDisitributionGameDAO);
		when(((RoundDAO)beerDisitributionGameDAO).getRound(anyString(),anyInt())).thenReturn(round);
		persistence.fetchRoundData(anyString(),anyInt());
		verify(((RoundDAO)beerDisitributionGameDAO), times(1)).getRound(anyString(),anyInt());

	}

	@Test
	void getGameLogTest() {
		beerDisitributionGameDAO = mock(BeergameDAO.class);
		reflect(persistence,beerDisitributionGameDAO);
		when(((BeergameDAO)beerDisitributionGameDAO).getGameLog(anyString())).thenReturn(beerGame);
		persistence.getGameLog(anyString());
		verify(((BeergameDAO)beerDisitributionGameDAO), times(1)).getGameLog(anyString());
	}

	@Test
	void logUsedBusinessRuleToCreateOrderTest() {
		beerDisitributionGameDAO = mock(GameBusinessRulesInFacilityTurnDAO.class);
		reflect(persistence,beerDisitributionGameDAO);
		when(((GameBusinessRulesInFacilityTurnDAO)beerDisitributionGameDAO).readTurn(anyString(),anyInt(),anyInt(),anyInt())).thenReturn(gameBusinessRulesInFacilityTurn);
		persistence.logUsedBusinessRuleToCreateOrder(gameBusinessRulesInFacilityTurn);
		verify(((GameBusinessRulesInFacilityTurnDAO)beerDisitributionGameDAO), times(1)).createTurn(gameBusinessRulesInFacilityTurn);
	}

	@Test
	void fetchTurnDataTest() {
		beerDisitributionGameDAO = mock(FacilityTurnDAO.class);
		reflect(persistence,beerDisitributionGameDAO);
		when(((FacilityTurnDAO)beerDisitributionGameDAO).fetchTurn(round,facilityLinkedTo)).thenReturn(facilityTurn);
		persistence.fetchTurnData(round,facilityLinkedTo);
		verify(((FacilityTurnDAO)beerDisitributionGameDAO), times(1)).fetchTurn(round,facilityLinkedTo);
	}

	@Test
	void saveTurnDataTest() {
		beerDisitributionGameDAO = mock(FacilityTurnDAO.class);
		reflect(persistence,beerDisitributionGameDAO);
		Mockito.doNothing().when((FacilityTurnDAO)beerDisitributionGameDAO).createTurn(facilityTurn);
		persistence.saveTurnData(facilityTurn);
		verify(((FacilityTurnDAO)beerDisitributionGameDAO), times(1)).createTurn(facilityTurn);

	}

	public void reflect(Persistence persistence, IBeerDisitributionGameDAO mock) {
		try {
			Field connField = persistence.getClass().getDeclaredField("beergameDAO");
			connField.setAccessible(true);
			connField.set(persistence, mock);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
}