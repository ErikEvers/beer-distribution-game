package org.han.ica.asd.c.dao;

import com.google.inject.Inject;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.dao_model.GameBusinessRulesDB;

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
     * @param gameBusinessRulesDB The data required to create a new GameBusinessRule.
     */
    public void createGameBusinessRule(GameBusinessRulesDB gameBusinessRulesDB) {
        executePreparedStatement(gameBusinessRulesDB, CREATE_GAMEBUSINESSRULE);
    }

    /**
     * A method to delete a specific GameBusinessRule.
     *
     * @param gameBusinessRulesDB The data required to delete a specific GameBusinessRule.
     */
    public void deleteSpecificGamebusinessrule(GameBusinessRulesDB gameBusinessRulesDB) {
        executePreparedStatement(gameBusinessRulesDB, DELETE_SPECIFIC_GAMEBUSINESSRULE);
    }

    /**
     * A method to delete all GameBusinessRules from a specific GameAgent within a Game.
     *
     * @param gameId The identifier of the game from which the GameBusinessRules have to be deleted.
     * @param gameAgentname The identifier of tje GameAgent from which the GameBusinessRules have to be deleted.
     */
    public void deleteAllGamebusinessrulesForGameagentInAGame(String gameId, String gameAgentname) {
				Connection conn = databaseConnection.connect();
				if(conn != null) {
						try (PreparedStatement pstmt = conn.prepareStatement(DELETE_ALL_GAMEBUSINESSRULES_FOR_GAMEAGENT_IN_A_GAME)) {
								conn.setAutoCommit(false);

								pstmt.setString(1, gameId);
								pstmt.setString(2, gameAgentname);

								pstmt.executeUpdate();

								conn.commit();
						} catch (SQLException e) {
							LOGGER.log(Level.SEVERE, e.toString(), e);
							databaseConnection.rollBackTransaction(conn);
						}
				}
    }

    public List<GameBusinessRulesDB> readAllGameBusinessRulesForGameAgentInAGame(String gameId, String gameAgentName) {
        List<GameBusinessRulesDB> gameBusinessRules = new ArrayList<>();
        Connection conn = databaseConnection.connect();
        if(conn == null) {
        	return gameBusinessRules;
				}
        try (PreparedStatement pstmt = conn.prepareStatement(READ_ALL_GAMEBUSINESSRULES_FOR_GAMEAGENT_IN_A_GAME)) {
            conn.setAutoCommit(false);

            pstmt.setString(1, gameId);
            pstmt.setString(2, gameAgentName);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    gameBusinessRules.add(new GameBusinessRulesDB(rs.getInt("FacilityId"), rs.getString("GameId"),
                            rs.getString("GameAgentName"), rs.getString("GameBusinessRule"),
                            rs.getString("GameAST")));
                }
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return gameBusinessRules;
    }

    /**
     * A method to execute a prepared statement for creating or deleting a specific GameBusinessRule.
     *
     * @param gameBusinessRulesDB The data required to create or delete a specific GameBusinessRule.
     * @param query The query that has to be executed on the database.
     */
    private void executePreparedStatement(GameBusinessRulesDB gameBusinessRulesDB, String query) {
				Connection conn = databaseConnection.connect();
				if(conn != null) {
						try (PreparedStatement pstmt = conn.prepareStatement(query)) {
								conn.setAutoCommit(false);

								pstmt.setInt(1, gameBusinessRulesDB.getFacilityId());
								pstmt.setString(2, gameBusinessRulesDB.getGameId());
								pstmt.setString(3, gameBusinessRulesDB.getGameAgentName());
								pstmt.setString(4, gameBusinessRulesDB.getGameBusinessRule());
								pstmt.setString(5, gameBusinessRulesDB.getGameAST());

								pstmt.executeUpdate();
								conn.commit();
						} catch (SQLException e) {
							LOGGER.log(Level.SEVERE, e.toString(), e);
							databaseConnection.rollBackTransaction(conn);
						}
				}
    }
}
