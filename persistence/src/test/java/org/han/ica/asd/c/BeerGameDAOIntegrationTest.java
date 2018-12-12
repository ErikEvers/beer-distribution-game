package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.model.BeerGame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
class BeerGameDAOIntegrationTest {
	private BeergameDAO beergameDAO = new BeergameDAO();
	private BeerGame beergame = new BeerGame("BEERGAME12122018","BeergameZutphen","12/12/2018","");


	@BeforeEach
	void setUp() {
		setUpDatabase();
	}

	@AfterEach
	void tearDown() {
		DBConnectionTest.cleanup();
	}
	
	@Test
	void createBeergame() {
		//beergameDAO.createBeergame("BeergameZutphen");
	}

	@Test
	void readBeergames() {
	}

	@Test
	void deleteBeergame() {
	}

	@Test
	void getGameLog() {
	}

	public void setUpDatabase(){
		DBConnectionTest.insertDatabase();
	}

}