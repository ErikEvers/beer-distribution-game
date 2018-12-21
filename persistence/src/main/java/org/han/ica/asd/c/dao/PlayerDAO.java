package org.han.ica.asd.c.dao;

import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Player;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerDAO {
    private static final Logger LOGGER = Logger.getLogger(PlayerDAO.class.getName());
    private static final String GET_ALL = "SELECT * FROM Player WHERE GameId = ?";
    private static final String GET_SPECIFIC = "SELECT * FROM Player WHERE GameId = ? AND PlayerId = ?";
    private static final String CREATE_PLAYER = "INSERT INTO Player VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_PLAYER = "DELETE FROM Player WHERE GameId = ? AND PlayerId = ?";
    private static final String UPDATE_PLAYER = "UPDATE Player"

    @Inject
    private IDatabaseConnection databaseConnection;

    public PlayerDAO () {
        // empty for Guice
    }

    /**
     * A method to create a new player.
     * @param player
     * A domain object which contains the necessary data to create a player
     */
    public void createPlayer(Player player) {
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(CREATE_PLAYER)) {
                    conn.setAutoCommit(false);

                    pstmt.setString(1, DaoConfig.getCurrentGameId());
                    pstmt.setString(2, player.getPlayerId());
                    pstmt.setInt(3, player.getFacility().getFacilityId());
                    pstmt.setString(4, player.getIpAddress());
                    pstmt.setString(5, player.getName());

                    pstmt.executeUpdate();
                }
                conn.commit();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void updatePlayer(Player player) {


    }

    public void getPlayer(String playerId) {


    }

    public void deletePlayer(String playerId) {

    }

}
