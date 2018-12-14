package org.han.ica.asd.c;

import org.han.ica.asd.c.model.Round;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class PersistenceTest {
	public static final Logger LOGGER = Logger.getLogger(PersistenceTest.class.getName());
	private IBeerDisitributionGameDAO beerDisitributionGameDAO;
	private Persistence persistence;
	private Round round;

	@BeforeEach
	void setUp() {
		persistence = new Persistence();
		round = new Round("Beergame",1);
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
	}

	@Test
	void getGameLogTest() {
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