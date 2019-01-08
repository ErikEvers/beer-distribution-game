package org.han.ica.asd.c.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.dbconnection.DBConnectionTest;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class PlayerDAOIntegrationTest {
    private final String GAME_ID = "game";
    private final String PLAYER_ID = "id";
    private final String PLAYER_NAME = "name";
    private final String IP_ADDRESS = "127.0.0.1";
    private PlayerDAO playerDAO;
    private Player player;

    @Mock
    private FacilityDAO facilityDAO;

    @BeforeEach
    void setup () {
        MockitoAnnotations.initMocks(this);
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IDatabaseConnection.class).to(DBConnectionTest.class);
                bind(FacilityDAO.class).toInstance(facilityDAO);
            }
        });
        when(facilityDAO.readSpecificFacility(anyInt())).thenReturn(new Facility());
        DBConnectionTest.getInstance().createNewDatabase();
        playerDAO = injector.getInstance(PlayerDAO.class);
        DaoConfig.setCurrentGameId(GAME_ID);
        player = new Player(PLAYER_ID, IP_ADDRESS, new Facility(), PLAYER_NAME, true);

    }

    @Test
    void getAllPlayersTest()  {
        Assertions.assertEquals(0, playerDAO.getAllPlayers().size());
        playerDAO.createPlayer(player);

        List<Player> players = playerDAO.getAllPlayers();

        Assertions.assertEquals(1, players.size());
        Assertions.assertEquals(PLAYER_ID, players.get(0).getPlayerId());
    }

    @Test
    void getSinglePlayerFailTest() {

        Assertions.assertNull(playerDAO.getPlayer(PLAYER_ID));

        playerDAO.createPlayer(player);
    }

    @Test
    void getSinglePlayerTest() {
        playerDAO.createPlayer(player);
        Player tempPlayer = playerDAO.getPlayer(PLAYER_ID);
        Assertions.assertEquals(PLAYER_ID, tempPlayer.getPlayerId());
    }

    @Test
    void deletePlayerTest() {
        playerDAO.createPlayer(player);
        Player tempPlayer = playerDAO.getPlayer(PLAYER_ID);
        Assertions.assertEquals(PLAYER_ID, tempPlayer.getPlayerId());
        playerDAO.deletePlayer(PLAYER_ID);
        Assertions.assertNull(playerDAO.getPlayer(PLAYER_ID));
    }

    @Test
    void updatePlayerTest() {
        String newIp = "127.0.0.2";
        Player tempPLayer = new Player(PLAYER_ID, newIp, new Facility(), PLAYER_NAME, true);
        playerDAO.createPlayer(player);
        Assertions.assertEquals(IP_ADDRESS, playerDAO.getPlayer(PLAYER_ID).getIpAddress());
        player.setIpAddress(newIp);
        playerDAO.updatePlayer(tempPLayer);
        Assertions.assertEquals(newIp, playerDAO.getPlayer(PLAYER_ID).getIpAddress());
    }
}
