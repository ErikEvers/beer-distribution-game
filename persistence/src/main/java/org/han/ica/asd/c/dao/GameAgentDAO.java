package org.han.ica.asd.c.dao;

import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.exception.GameIdNotSetException;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRules;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameAgentDAO{
    private static final String CREATE_GAMEAGENT = "INSERT INTO GameAgent VALUES (?,?,?);";
    private static final String DELETE_SPECIFIC_GAMEAGENT = "DELETE FROM GameAgent WHERE FacilityId = ? AND GameId = ? AND GameAgentName = ?;";
    private static final String DELETE_ALL_GAMEAGENTS_IN_A_BEERGAME = "DELETE FROM GameAgent WHERE GameId = ?;";
    private static final String UPDATE_GAMEAGENT = "UPDATE GameAgent SET GameAgentName = ? WHERE FacilityId = ? AND GameId = ?;";
    private static final String READ_GAMEAGENTS_FOR_A_BEERGAME = "SELECT * FROM GameAgent WHERE GameId = ?;";
    private static final Logger LOGGER = Logger.getLogger(GameAgentDAO.class.getName());

    @Inject
    private IDatabaseConnection databaseConnection;

    @Inject
    private GameBusinessRulesDAO gameBusinessRulesDAO;

    @Inject
    private FacilityDAO facilityDAO;

    public GameAgentDAO() {
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
     * A method which inserts a list of GameAgents
     * @param agents A list of gameagents
     */
    public void insertGameAgents(List<GameAgent> agents) {
        for (GameAgent agent: agents) {
            createGameAgent(agent);
        }
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
     */
    public void deleteAllGameagentsInABeergame() {
        Connection conn = databaseConnection.connect();
        if (conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(DELETE_ALL_GAMEAGENTS_IN_A_BEERGAME)) {
                conn.setAutoCommit(false);

                DaoConfig.gameIdNotSetCheck(pstmt, 1);

                pstmt.executeUpdate();
                conn.commit();
            } catch (GameIdNotSetException | SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                databaseConnection.rollBackTransaction(conn);
            }
        }
    }

    /**
     * A method to update the GameAgent from a Facility within a Beergame.
     *
     * @param gameAgent The data required to update the GameAgent.
     */
    public void updateGameagent(GameAgent gameAgent) {
        Connection conn = databaseConnection.connect();
        if (conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_GAMEAGENT)) {
                conn.setAutoCommit(false);

                pstmt.setString(1, gameAgent.getGameAgentName());
                pstmt.setInt(2, gameAgent.getFacility().getFacilityId());
                DaoConfig.gameIdNotSetCheck(pstmt, 3);

                pstmt.executeUpdate();
                conn.commit();
            } catch (GameIdNotSetException | SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                databaseConnection.rollBackTransaction(conn);
            }
        }
    }

    /**
     * A method to retrieve all GameAgents from a BeerGame.
     *
     * @return A list of GameAgents from the BeerGame.
     */
    public List<GameAgent> readGameAgentsForABeerGame() {
        Connection conn = databaseConnection.connect();
        int i = 0;
        List<GameAgent> gameAgents = new ArrayList<>();
        List<GameBusinessRules> gameBusinessRulesStub = new ArrayList<>();
        if(conn == null) {
          return gameAgents;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(READ_GAMEAGENTS_FOR_A_BEERGAME)) {
            conn.setAutoCommit(false);
            DaoConfig.gameIdNotSetCheck(pstmt, 1);
            try (ResultSet rs = pstmt.executeQuery()) {
                    while (!rs.isClosed() && rs.next()) {
                        createGameAgentModel(i, gameAgents, gameBusinessRulesStub, rs);
                        i = i + 1;
                    }
            }
            conn.commit();
        } catch (GameIdNotSetException | SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            databaseConnection.rollBackTransaction(conn);
        }
        return gameAgents;
    }

    private void createGameAgentModel(int i, List<GameAgent> gameAgents, List<GameBusinessRules> gameBusinessRulesStub, ResultSet rs) throws SQLException {
        gameAgents.add(new GameAgent(rs.getString("GameAgentName"),
                facilityDAO.readSpecificFacility(rs.getInt("FacilityId")), gameBusinessRulesStub));
        List<GameBusinessRules> actualGameBusinessRules = gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame(gameAgents.get(i));
        gameAgents.get(i).setGameBusinessRules(actualGameBusinessRules);
    }

    /**
     * A method to execute a prepared statement to create or delete a GameAgent.
     *
     * @param gameAgent The data that is required to create or delete a GameAgent.
     * @param query     The sql statement that needs to be executed.
     */
    private void executePreparedStatement(GameAgent gameAgent, String query) {
        Connection conn = databaseConnection.connect();
        if (conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                conn.setAutoCommit(false);

                pstmt.setInt(1, gameAgent.getFacility().getFacilityId());
                DaoConfig.gameIdNotSetCheck(pstmt, 2);
                pstmt.setString(3, gameAgent.getGameAgentName());

                pstmt.executeUpdate();
                conn.commit();
            } catch (GameIdNotSetException | SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                databaseConnection.rollBackTransaction(conn);
            }
        }
    }

	public void updateGameagents(List<GameAgent> agents) {
        agents.forEach(this::updateGameagent);
	}
}
