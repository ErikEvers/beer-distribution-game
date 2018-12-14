package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.DBConnection;
import org.han.ica.asd.c.dbconnection.DatabaseConnection;
import org.han.ica.asd.c.model.GameBusinessRulesInFacilityTurn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameBusinessRulesInFacilityTurnDAO implements IBeerDisitributionGameDAO {
	private static final String CREATE_BUSINESSRULETURN = "INSERT INTO GameBusinessRulesInFacilityTurn VALUES (?,?,?,?,?,?,?);";
	private static final String READ_BUSINESSRULETURN = "SELECT* FROM GameBusinessRulesInFacilityTurn WHERE GameId = ? AND RoundId = ? AND FacilityIdOrder = ? AND FacilityIdDeliver = ?;";
	private static final String DELETE_BUSINESSRULETURN = "DELETE FROM GameBusinessRulesInFacilityTurn WHERE GameId = ? AND RoundId = ? AND FacilityIdOrder = ? AND FacilityIdDeliver = ?;";
	private static final Logger LOGGER = Logger.getLogger(GameBusinessRulesInFacilityTurnDAO.class.getName());

	private DatabaseConnection databaseConnection;

	public GameBusinessRulesInFacilityTurnDAO() {
		databaseConnection = DBConnection.getInstance();
	}

	/**
	 * A method which creates a GameBusinessRulesInFacilityTurn object in the SQLite Database
	 * @param gameBusinessRulesInFacilityTurn A GameBusinessRuleInFacilityTurn object which contains data which needs to be inserted in to the SQLite Database
	 */
	public void createTurn(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn) {
		Connection conn;
		try {
			conn = databaseConnection.connect();
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(CREATE_BUSINESSRULETURN)) {
					conn.setAutoCommit(false);
					pstmt.setInt(1, gameBusinessRulesInFacilityTurn.getRoundId());
					pstmt.setInt(2, gameBusinessRulesInFacilityTurn.getFacilityIdOrder());
					pstmt.setInt(3, gameBusinessRulesInFacilityTurn.getFacilityIdDeliver());
					pstmt.setString(4, gameBusinessRulesInFacilityTurn.getGameId());
					pstmt.setString(5, gameBusinessRulesInFacilityTurn.getGameAgentName());
					pstmt.setString(6, gameBusinessRulesInFacilityTurn.getGameBusinessRule());
					pstmt.setString(7, gameBusinessRulesInFacilityTurn.getGameAST());

					pstmt.executeUpdate();
				}
				conn.commit();
			}

		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}

	/**
	 * A method which returns a GameBusinessRulesInFacilityTurn object from the SQLite Database
	 * @param gameId            The Id of the game where the specific turns are located
	 * @param roundId           The Id of the round where the specific turns are located
	 * @param facilityIdOrder   The Id of the Facility which placed an order
	 * @param facilityIdDeliver The Id of the Facility which delivered an order
	 * @return A GameBusinessRulesInFacilityTurn object which contains data from the database according to the search parameters
	 */
	public GameBusinessRulesInFacilityTurn readTurn(String gameId, int roundId, int facilityIdOrder, int facilityIdDeliver) {
		Connection conn;
		GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn = null;

		try {
			conn = databaseConnection.connect();
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(READ_BUSINESSRULETURN)) {
					pstmt.setString(1, gameId);
					pstmt.setInt(2, roundId);
					pstmt.setInt(3, facilityIdOrder);
					pstmt.setInt(4, facilityIdDeliver);
					try (ResultSet rs = pstmt.executeQuery()){
						gameBusinessRulesInFacilityTurn = new GameBusinessRulesInFacilityTurn(rs.getInt("RoundId"), rs.getInt("FacilityIdOrder"), rs.getInt("FacilityIdDeliver"), rs.getString("GameId"), rs.getString("GameAgentName"), rs.getString("GameBusinessRule"), rs.getString("GameAST"));
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(),e);
		}
		return gameBusinessRulesInFacilityTurn;
	}


		/**
		 * A method which deletes a specific turn from the SQLite Database
		 * @param gameId            The Id of the game where the specific turns are located
		 * @param roundId           The Id of the round where the specific turns are located
		 * @param facilityIdOrder  The Id of the Facility which placed an order
		 * @param facilityIdDeliver The Id of the Facility which delivered an order
		 */
		public void deleteTurn (String gameId,int roundId, int facilityIdOrder, int facilityIdDeliver){
			Connection conn = null;
			try {
				conn = databaseConnection.connect();
				if (conn != null) {
					try (PreparedStatement pstmt = conn.prepareStatement(DELETE_BUSINESSRULETURN)) {

						conn.setAutoCommit(false);

						pstmt.setString(1, gameId);
						pstmt.setInt(2, roundId);
						pstmt.setInt(3, facilityIdOrder);
						pstmt.setInt(4, facilityIdDeliver);

						pstmt.executeUpdate();
					}
					conn.commit();
				}
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(),e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
	}
