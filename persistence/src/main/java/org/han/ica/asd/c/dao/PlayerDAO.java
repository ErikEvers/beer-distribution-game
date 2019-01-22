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
    private static final String CREATE_PLAYER_WITHOUT_FACILITY = "INSERT INTO Player (GameId, PlayerId, IpAddress, Name) VALUES (?, ?, ?, ?)";
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
        if(player.getFacility() == null) {
            createPlayerWithoutFacility(player);
        } else {
            createPlayerWithFacility(player);
        }
    }

    private void createPlayerWithFacility(Player player) {
        Connection conn = databaseConnection.connect();
        try (PreparedStatement pstmt = conn.prepareStatement(CREATE_PLAYER)) {

            conn.setAutoCommit(false);

            DaoConfig.gameIdNotSetCheck(pstmt, 1);
            pstmt.setString(2, player.getPlayerId());
            pstmt.setInt(3, player.getFacility().getFacilityId());
            pstmt.setString(4, player.getIpAddress());
            pstmt.setString(5, player.getName());

            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException | GameIdNotSetException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            databaseConnection.rollBackTransaction(conn);
        }
    }

    private void createPlayerWithoutFacility (Player player) {
        Connection conn = databaseConnection.connect();
        try (PreparedStatement pstmt = conn.prepareStatement(CREATE_PLAYER_WITHOUT_FACILITY)) {

            conn.setAutoCommit(false);

            DaoConfig.gameIdNotSetCheck(pstmt, 1);
            pstmt.setString(2, player.getPlayerId());
            pstmt.setString(3, player.getIpAddress());
            pstmt.setString(4, player.getName());

            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException | GameIdNotSetException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            databaseConnection.rollBackTransaction(conn);
        }
    }

    /**
     * A method which inserts multiple players in a database
     * @param players A list of players which needs to be inserted
     */
    public void insertPlayers(List<Player> players) {
        players.forEach(this::createPlayer);
    }

    /**
     * Can update a player's name and/or ip address
     *
     * @param player Specifies the player to update
     */
    public void updatePlayer(Player player) {
        Connection conn = conn = databaseConnection.connect();
        try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_PLAYER)) {

            conn.setAutoCommit(false);

            pstmt.setString(1, player.getIpAddress());
            pstmt.setString(2, player.getName());
            pstmt.setString(3, DaoConfig.getCurrentGameId());
            pstmt.setString(4, player.getPlayerId());

            pstmt.executeUpdate();

            conn.commit();

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
        Player player = null;
        Connection conn = databaseConnection.connect();
        try (PreparedStatement pstmt = conn.prepareStatement(GET_SPECIFIC)) {
            conn.setAutoCommit(false);

            DaoConfig.gameIdNotSetCheck(pstmt, 1);
            pstmt.setString(2, playerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                player = buildPlayer(rs);
            }
            conn.commit();
            return player;

        } catch (SQLException | GameIdNotSetException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            databaseConnection.rollBackTransaction(conn);
        }

        return player;
    }

    /**
     * This function gets all the players in the current game
     *
     * @return Returns a list of the players found in the current game
     */
    public synchronized List<Player> getAllPlayers() {
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

            conn.commit();
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
            if (!rs.isClosed()) {
                while (rs.next()) {
                    players.add(buildPlayer(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    /**
     * Creates a single player object
     * @param rs
     * Result set from which to build the player
     * @return
     * Returns the new player object
     * @throws SQLException
     * Thrown when the resultset is empty
     */
    private Player buildPlayer(ResultSet rs) throws SQLException {
        Player player = null;
        if (!rs.isClosed()) {
            player = new Player(
                    rs.getString("PlayerId"),
                    rs.getString("IpAddress"),
                    facilityDAO.readSpecificFacility(rs.getInt("FacilityId")),
                    rs.getString("Name"),
                    true);
            return player;
        }
        return player;
    }

	public void updatePlayers(List<Player> players) {
        for (Player player: players) {
            updatePlayer(player);
        }
	}
}
