package org.han.ica.asd.c.dao;


import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRules;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRulesInFacilityTurn;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameBusinessRulesInFacilityTurnDAO {
	private static final String CREATE_BUSINESSRULETURN = "INSERT INTO GameBusinessRulesInFacilityTurn VALUES (?,?,?,?,?);";
	private static final String READ_BUSINESSRULETURN = "SELECT * FROM GameBusinessRulesInFacilityTurn WHERE GameId = ? AND RoundId = ? AND FacilityId = ?;";
	private static final String DELETE_BUSINESSRULETURN = "DELETE FROM GameBusinessRulesInFacilityTurn WHERE GameId = ? AND RoundId = ? AND FacilityId = ?;";
	private static final Logger LOGGER = Logger.getLogger(GameBusinessRulesInFacilityTurnDAO.class.getName());

	@Inject
	private IDatabaseConnection databaseConnection;

	@Inject
	private GameBusinessRulesDAO gameBusinessRulesDAO;

	public GameBusinessRulesInFacilityTurnDAO() {
		//Empty Constructor for GUICE
	}

	/**
	 * A method which creates a GameBusinessRulesInFacilityTurnDB object in the SQLite Database
	 * @param gameBusinessRulesInFacilityTurn A GameBusinessRuleInFacilityTurn object which contains data which needs to be inserted in to the SQLite Database
	 */
	public void createTurn(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn) {
		Connection conn = databaseConnection.connect();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(CREATE_BUSINESSRULETURN)) {
				conn.setAutoCommit(false);

				for (GameBusinessRules gamebusinessrule: gameBusinessRulesInFacilityTurn.getGameBusinessRulesList()) {
					pstmt.setInt(1, gameBusinessRulesInFacilityTurn.getRoundId());
					pstmt.setInt(2, gameBusinessRulesInFacilityTurn.getFacilityId());
					pstmt.setString(3, DaoConfig.getCurrentGameId());
					pstmt.setString(4, gameBusinessRulesInFacilityTurn.getGameAgentName());
					pstmt.setString(5, gamebusinessrule.toString());

					pstmt.execute();
				}

				conn.commit();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
	}

	/**
	 * A method which returns a GameBusinessRulesInFacilityTurnDB object from the SQLite Database
	 * @param roundId The Id of the round where the specific turns are located
	 * @return A GameBusinessRulesInFacilityTurnDB object which contains data from the database according to the search parameters
	 */
	public GameBusinessRulesInFacilityTurn readTurn(int roundId, int facilityId, String gameAgentName) {
		Connection conn = databaseConnection.connect();
		GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn = null;
		List<GameBusinessRules> gameBusinessRules = new ArrayList<>();

		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(READ_BUSINESSRULETURN)) {
				conn.setAutoCommit(false);
				pstmt.setString(1, DaoConfig.getCurrentGameId());
				pstmt.setInt(2, roundId);
				pstmt.setInt(3, facilityId);
				try (ResultSet rs = pstmt.executeQuery()){
					gameBusinessRulesInFacilityTurn = createGameBusinessRulesInFacilityTurnModel(roundId, facilityId, gameAgentName, gameBusinessRules, rs);
				}
				conn.commit();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
		return gameBusinessRulesInFacilityTurn;
	}

	private GameBusinessRulesInFacilityTurn createGameBusinessRulesInFacilityTurnModel(int roundId, int facilityId, String gameAgentName, List<GameBusinessRules> gameBusinessRules, ResultSet rs) throws SQLException {
		GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn = new GameBusinessRulesInFacilityTurn();
		gameBusinessRulesInFacilityTurn.setFacilityId(rs.getInt("FacilityId"));
		gameBusinessRulesInFacilityTurn.setRoundId(rs.getInt("RoundId"));
		gameBusinessRulesInFacilityTurn.setGameAgentName(rs.getString("GameAgentName"));
		while(!rs.isClosed() && rs.next()) {
			gameBusinessRules.add(new GameBusinessRules(rs.getString("GameBusinessRule"), gameBusinessRulesDAO.getGameAST(rs.getString("GameBusinessRule"), gameAgentName, facilityId)));
		}
		gameBusinessRulesInFacilityTurn.setGameBusinessRulesList(gameBusinessRules);
		return gameBusinessRulesInFacilityTurn;
	}


	/**
		 * A method which deletes a specific turn from the SQLite Database
		 * @param roundId           The Id of the round where the specific turns are located
		 * @param facilityId  The Id of the FacilityDB which did the action
		 */
		public void deleteTurn (int roundId, int facilityId){
			Connection conn = databaseConnection.connect();
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(DELETE_BUSINESSRULETURN)) {

					conn.setAutoCommit(false);

					pstmt.setString(1, DaoConfig.getCurrentGameId());
					pstmt.setInt(2, roundId);
					pstmt.setInt(3, facilityId);

					pstmt.executeUpdate();
					conn.commit();
				} catch (SQLException e) {
					LOGGER.log(Level.SEVERE, e.toString(), e);
					databaseConnection.rollBackTransaction(conn);
				}
			}
		}
	}
