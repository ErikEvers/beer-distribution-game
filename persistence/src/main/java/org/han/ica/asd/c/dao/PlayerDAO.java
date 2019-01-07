package org.han.ica.asd.c.dao;

import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.exception.GameIdNotSetException;
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

    @Inject
    FacilityDAO facilityDAO;

    public PlayerDAO() {
        // empty for Guice
    }

    /**
     * A method to create a new player.
     *
     * @param player A domain object which contains the necessary data to create a player
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

    /**
     * Can update a player's name and/or ip address
     *
     * @param player Specifies the player to update
     */
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

    /**
     * Gets the player
     *
     * @param playerId Primary identifier for a player
     * @return Returns the specified player
     */
    public Player getPlayer(String playerId) {
        return null;
    }

    /**
     * This function gets all the players in the current game
     *
     * @return Returns a list of the players found in the current game
     */
    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        Connection conn = databaseConnection.connect();
        try (PreparedStatement pstmt = conn.prepareStatement(GET_ALL)) {

            conn.setAutoCommit(false);

            DaoConfig.gameIdNotSetCheck(pstmt, 1);
            try (ResultSet rs = pstmt.executeQuery()) {
                buildPlayerArray(rs, players);
            }
            conn.commit();

        } catch (SQLException | GameIdNotSetException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            databaseConnection.rollBackTransaction(conn);
        }
        return players;
    }

    /**
     * Function to delete player from current game
     *
     * @param playerId The primary identifier of the player to be removed
     */
    public void deletePlayer(String playerId) {
        Connection conn = databaseConnection.connect();
        try (PreparedStatement pstmt = conn.prepareStatement(DELETE_PLAYER)) {

            conn.setAutoCommit(false);

            DaoConfig.gameIdNotSetCheck(pstmt, 1);
            pstmt.setString(2, playerId);

            pstmt.executeUpdate();
        } catch (SQLException | GameIdNotSetException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            databaseConnection.rollBackTransaction(conn);
        }
    }

    /**
     * Create list of players using provided result set
     *
     * @param rs      Result set containing the player data
     * @param players List to add the created player objects into
     */
    private void buildPlayerArray(ResultSet rs, List<Player> players) {
        try {
            if (!rs.next()) {
                do {
                    players.add(createPlayer(rs));
                } while (rs.next());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    private Player createPlayer(ResultSet rs) throws SQLException {
        return new Player(
                rs.getString("PlayerId"),
                rs.getString("IpAddress"),
                facilityDAO.readSpecificFacility(rs.getInt("")),
                rs.getString("Name"),
                true);
    }

}
