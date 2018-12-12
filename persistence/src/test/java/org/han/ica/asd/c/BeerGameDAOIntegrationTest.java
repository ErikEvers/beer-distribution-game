package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
class BeerGameDAOIntegrationTest {

	@BeforeEach
	void setUp() {
		setUpDatabase();
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void createBeergame() {
		System.out.println("SQLLite created");
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
		DBConnectionTest.createNewDatabase();
		DBConnectionTest.insertDatabase();
	}

}