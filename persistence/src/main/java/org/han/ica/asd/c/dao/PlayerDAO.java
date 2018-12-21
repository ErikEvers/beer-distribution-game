package org.han.ica.asd.c.dao;

import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.exception.GameIdNotSetException;
import org.han.ica.asd.c.exception.PlayerNotFoundException;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Player;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerDAO {
    private static final Logger LOGGER = Logger.getLogger(PlayerDAO.class.getName());
    private static final String GET_ALL = "SELECT * FROM Player WHERE GameId = ?";
    private static final String GET_SPECIFIC = "SELECT * FROM Player WHERE GameId = ? AND PlayerId = ?";
    private static final String CREATE_PLAYER = "INSERT INTO Player VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_PLAYER = "DELETE FROM Player WHERE GameId = ? AND PlayerId = ?";
    private static final String UPDATE_PLAYER = "UPDATE Player SET IpAddress = ?, Name = ? WHERE GameId = ? AND PlayerId = ?";

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

                    DaoConfig.gameIdNotSetCheck(pstmt, 1);
                    pstmt.setString(2, player.getPlayerId());
                    pstmt.setInt(3, player.getFacility().getFacilityId());
                    pstmt.setString(4, player.getIpAddress());
                    pstmt.setString(5, player.getName());

                    pstmt.executeUpdate();
                } catch (GameIdNotSetException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }
                conn.commit();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void updatePlayer(Player player) {
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_PLAYER)) {
                    conn.setAutoCommit(false);

                    pstmt.setString(1, player.getIpAddress());
                    pstmt.setString(2, player.getName());
                    pstmt.setString(3, DaoConfig.getCurrentGameId());
                    pstmt.setString(4, player.getPlayerId());

                    pstmt.executeUpdate();
                }
                conn.commit();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    public Player getPlayer(String playerId) {
        return null;
    }

    public List<Player> getAllPlayers() throws PlayerNotFoundException {
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(GET_ALL)) {
                    conn.setAutoCommit(false);

                    DaoConfig.gameIdNotSetCheck(pstmt, 1);

                    rs  = pstmt.executeQuery();
                } catch (GameIdNotSetException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }
                conn.commit();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        throw new PlayerNotFoundException("No player was found in the current game");
    }

    private List<Player> buildPlayerArray (ResultSet rs) {
        List<Player> players = new ArrayList<>();
        try {
            if (!rs.next()) {
                return players;
            } else {
                do {

                } while (rs.next());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return players;
    }

    private Player assemblePlayerObject () {
        return null;
    }

    public void deletePlayer(String playerId) {

    }

}
