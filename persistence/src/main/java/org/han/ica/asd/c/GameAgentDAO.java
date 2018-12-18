package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.dao_model.Facility;
import org.han.ica.asd.c.model.dao_model.GameAgent;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameAgentDAO implements IBeerDisitributionGameDAO {
    private static final String CREATE_GAMEAGENT = "INSERT INTO GameAgent VALUES (?,?,?);";
    private static final String DELETE_SPECIFIC_GAMEAGENT = "DELETE FROM GameAgent WHERE FacilityId = ? AND GameId = ? AND GameAgentName = ?;";
    private static final String DELETE_ALL_GAMEAGENTS_IN_A_BEERGAME = "DELETE FROM GameAgent WHERE GameId = ?;";
    private static final String UPDATE_GAMEAGENT = "UPDATE GameAgent SET GameAgentName = ? WHERE FacilityId = ? AND GameId = ?;";
    private static final String READ_GAMEAGENTS_FOR_A_BEERGAME = "SELECT * FROM GameAgent WHERE GameId = ?;";
    private static final Logger LOGGER = Logger.getLogger(FacilityDAO.class.getName());

    @Inject
    private IDatabaseConnection databaseConnection;

    public GameAgentDAO(){
        //A constructor for Guice.
    }

    /**
     * A method to create a new GameAgent.
     *
     * @param gameAgent The data required to create a new GameAgent.
     */
    public void createGameAgent(GameAgent gameAgent) {
        executePreparedStatement(gameAgent, CREATE_GAMEAGENT);
    }

    /**
     * A method to delete a specific GameAgent.
     *
     * @param gameAgent The data required to delete a specific GameAgent.
     */
    public void deleteSpecificGameagent(GameAgent gameAgent) {
        executePreparedStatement(gameAgent, DELETE_SPECIFIC_GAMEAGENT);
    }

    /**
     * A method to delete all GameAgent within a BeerGame.
     *
     * @param gameId The identifier of the BeerGame from whom the GameAgent has to be deleted.
     */
    public void deleteAllGameagentsInABeergame(String gameId) {
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(DELETE_ALL_GAMEAGENTS_IN_A_BEERGAME)) {
                    conn.setAutoCommit(false);

                    pstmt.setString(1, gameId);

                    pstmt.executeUpdate();
                }
                conn.commit();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    /**
     * A method to update the GameAgent from a Facility within a Beergame.
     *
     * @param gameAgent The data required to update the GameAgent.
     */
    public void updateGameagent(GameAgent gameAgent) {
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_GAMEAGENT)) {
                    conn.setAutoCommit(false);

                    pstmt.setString(1, gameAgent.getGameAgentName());
                    pstmt.setInt(2, gameAgent.getFacilityId());
                    pstmt.setString(3, gameAgent.getGameAgentName());

                    pstmt.executeUpdate();
                }
                conn.commit();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    /**
     * A method to retrieve all GameAgents from a BeerGame.
     *
     * @param gameId The identifier of the BeerGame.
     * @return A list of GameAgents from the BeerGame.
     */
    public List<GameAgent> readGameAgentsForABeerGame(String gameId) {
        Connection conn = null;
        ArrayList<GameAgent> gameAgents = new ArrayList<>();
        try {
            conn = databaseConnection.connect();
            try (PreparedStatement pstmt = conn.prepareStatement(READ_GAMEAGENTS_FOR_A_BEERGAME)) {
                conn.setAutoCommit(false);

                pstmt.setString(1, gameId);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        gameAgents.add(new GameAgent(rs.getString("GameId"),
                                rs.getString("GameAgentName"), rs.getInt("FacilityId")));
                    }
                }
                conn.commit();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return gameAgents;
    }

    /**
     * A method to execute a prepared statement to create or delete a GameAgent.
     *
     * @param gameAgent The data that is required to create or delete a GameAgent.
     * @param query The sql statement that needs to be executed.
     */
    private void executePreparedStatement(GameAgent gameAgent, String query) {
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    conn.setAutoCommit(false);

                    pstmt.setInt(1, gameAgent.getFacilityId());
                    pstmt.setString(2, gameAgent.getGameId());
                    pstmt.setString(3, gameAgent.getGameAgentName());

                    pstmt.executeUpdate();
                }
                conn.commit();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }
}
