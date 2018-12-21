package org.han.ica.asd.c;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dao.PlayerDAO;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.exception.PlayerNotFoundException;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

public class PlayerDAOIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(PlayerDAOIntegrationTest.class.getName());
    private final String GAME_ID = "game";
    private final String PLAYER_ID = "test";
    private final Player PLAYER = new Player("test", "1", new Facility(), "test", true);
    private PlayerDAO playerDAO;
    @BeforeEach
    void setup () {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IDatabaseConnection.class).to(DBConnectionTest.class);
            }
        });
        DBConnectionTest.getInstance().createNewDatabase();
        playerDAO = injector.getInstance(PlayerDAO.class);
    }


    @Test
    void getAllPlayersTest() throws PlayerNotFoundException {

        Assert.assertNull(playerDAO.getAllPlayers());
        playerDAO.createPlayer(PLAYER);

        Player tempPlayer = playerDAO.getPlayer(GAME_ID, PLAYERID);

        Assert.assertNull(tempPlayer.getFacility());

    }
}
