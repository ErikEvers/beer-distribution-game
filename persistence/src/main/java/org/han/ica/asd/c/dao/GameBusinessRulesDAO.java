package org.han.ica.asd.c.dao;

import com.google.inject.Inject;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.exception.GameIdNotSetException;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRules;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameBusinessRulesDAO implements IBeerDisitributionGameDAO {
    private static final String CREATE_GAMEBUSINESSRULE = "INSERT INTO GameBusinessRules VALUES (?,?,?,?,?);";
    private static final String DELETE_SPECIFIC_GAMEBUSINESSRULE = "DELETE FROM GameBusinessRules WHERE FacilityId = ? AND GameId = ? AND GameAgentName = ? AND GameBusinessRule = ? AND GameAST = ?;";
    private static final String DELETE_ALL_GAMEBUSINESSRULES_FOR_GAMEAGENT_IN_A_GAME = "DELETE FROM GameBusinessRules WHERE GameId = ? AND GameAgentName = ?;";
    private static final String READ_ALL_GAMEBUSINESSRULES_FOR_GAMEAGENT_IN_A_GAME = "SELECT * FROM GameBusinessRules WHERE GameId = ? AND GameAgentName = ?";
    private static final Logger LOGGER = Logger.getLogger(GameBusinessRulesDAO.class.getName());

    @Inject
    private IDatabaseConnection databaseConnection;

    public GameBusinessRulesDAO() {
        //A constructor for Guice
    }

    /**
     * A method to create a new GameBusinessRule.
     *
     * @param gameAgent         The data required to insert the GameAgentName and FacilityId.
     * @param gameBusinessRules The data required to insert the GameBusinessRule and GameAST.
     */
    public void createGameBusinessRule(GameAgent gameAgent, GameBusinessRules gameBusinessRules) {
        executePreparedStatement(gameAgent, gameBusinessRules, CREATE_GAMEBUSINESSRULE);
    }

    /**
     * A method to delete a specific GameBusinessRule.
     *
     * @param gameAgent         The data required to delete the GameAgentName and FacilityId.
     * @param gameBusinessRules The data required to delete the GameBusinessRule and GameAST.
     */
    public void deleteSpecificGamebusinessrule(GameAgent gameAgent, GameBusinessRules gameBusinessRules) {
        executePreparedStatement(gameAgent, gameBusinessRules, DELETE_SPECIFIC_GAMEBUSINESSRULE);
    }

    /**
     * A method to delete all GameBusinessRules from a specific GameAgent within a Game.
     *
     * @param gameAgent Contains the identifier of tje GameAgent from which the GameBusinessRules have to be deleted.
     */
    public void deleteAllGamebusinessrulesForGameagentInAGame(GameAgent gameAgent) {
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(DELETE_ALL_GAMEBUSINESSRULES_FOR_GAMEAGENT_IN_A_GAME)) {
                    conn.setAutoCommit(false);

                    gameIdNotSetCheck(pstmt, 1);
                    pstmt.setString(2, gameAgent.getGameAgentName());

                    pstmt.executeUpdate();

                } catch (GameIdNotSetException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }
                conn.commit();
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            databaseConnection.rollBackTransaction(conn);
        }
    }

    /**
     * A method to read all the GameBusinessRules from a specific GameAgent in a specific BeerGame.
     *
     * @param gameAgent Contains the identifier of the GameAgent from which the GameBusinessRules have to be deleted.
     * @return A list containing all the GameBusinessRules from a specific GameAgent in a specific BeerGame.
     */
    public List<GameBusinessRules> readAllGameBusinessRulesForGameAgentInAGame(GameAgent gameAgent) {
        Connection conn = null;
        List<GameBusinessRules> gameBusinessRules = new ArrayList<>();
        try {
            conn = databaseConnection.connect();
            try (PreparedStatement pstmt = conn.prepareStatement(READ_ALL_GAMEBUSINESSRULES_FOR_GAMEAGENT_IN_A_GAME)) {
                conn.setAutoCommit(false);

                gameIdNotSetCheck(pstmt, 1);
                pstmt.setString(2, gameAgent.getGameAgentName());

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        gameBusinessRules.add(new GameBusinessRules(rs.getString("GameBusinessRule"), rs.getString("GameAST")));
                    }
                }
                conn.commit();
            } catch (GameIdNotSetException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return gameBusinessRules;
    }

    /**
     * A method to execute a prepared statement for creating or deleting a specific GameBusinessRule.
     *
     * @param gameAgent         The data required to create or delete the GameAgentName and FacilityId.
     * @param gameBusinessRules The data required to create or delete the GameBusinessRule and GameAST.
     * @param query             The query that has to be executed on the database.
     */
    private void executePreparedStatement(GameAgent gameAgent, GameBusinessRules gameBusinessRules, String query) {
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    conn.setAutoCommit(false);

                    pstmt.setInt(1, gameAgent.getFacility().getFacilityId());
                    gameIdNotSetCheck(pstmt, 2);
                    pstmt.setString(3, gameAgent.getGameAgentName());
                    pstmt.setString(4, gameBusinessRules.getGameBusinessRule());
                    pstmt.setString(5, gameBusinessRules.getGameAST());

                    pstmt.executeUpdate();

                } catch (GameIdNotSetException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }
                conn.commit();
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            databaseConnection.rollBackTransaction(conn);
        }
    }

    /**
     * A method to check if a gameId has been set and if so adds it to the prepared statement.
     *
     * @param pstmt The prepared statement to whom the gameId needs to be added.
     * @param index The index on which place the gameId has to be set.
     */
    private void gameIdNotSetCheck(PreparedStatement pstmt, int index) throws GameIdNotSetException, SQLException {
        pstmt.setString(index, DaoConfig.getCurrentGameId());
    }
}

