package org.han.ica.asd.c;

import org.han.ica.asd.c.model.BeerGame;
import org.han.ica.asd.c.model.Round;
import org.junit.jupiter.api.AfterEach;
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

	@BeforeEach
	void setUp() {
		persistence = new Persistence();
		round = new Round("Beergame",1);
		beerGame = new BeerGame(UUID.randomUUID().toString(),"Beergame", LocalDateTime.now().toString(),"");
	}

	@AfterEach
	void tearDown() {
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
	}

	@Test
	void fetchTurnDataTest() {
	}

	@Test
	void saveTurnDataTest() {
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