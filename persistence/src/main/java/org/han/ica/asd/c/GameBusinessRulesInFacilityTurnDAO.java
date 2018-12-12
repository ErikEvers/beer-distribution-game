package org.han.ica.asd.c;

import org.han.ica.asd.c.model.GameBusinessRulesInFacilityTurn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.han.ica.asd.c.dbconnection.DBConnection.connect;

public class GameBusinessRulesInFacilityTurnDAO implements IBeerDisitributionGameDAO {
	private static final String CREATE_BUSINESSRULETURN = "INSERT INTO GameBusinessRuleInFacility VALUES (?,?,?,?,?,?,?);";
	private static final String READ_BUSINESSRULETURN = "SELECT FROM GameBusinessRuleInFacility WHERE GameId = ? && RoundId = ? && FacillityIdOrder = ?, FacilityIdDeliver = ? ;";
	private static final String DELETE_BUSINESSRULETURN = "DELETE FROM GameBusinessRuleInFacility WHERE GameId = ? && RoundId = ? && FacillityIdOrder = ?, FacilityIdDeliver = ? ;";
	private static final Logger LOGGER = Logger.getLogger(GameBusinessRulesInFacilityTurnDAO.class.getName());

	/**
	 * A method which creates a GameBusinessRulesInFacilityTurn object in the SQLite Database
	 * @param gameBusinessRulesInFacilityTurn A GameBusinessRuleInFacilityTurn object which contains data which needs to be inserted in to the SQLite Database
	 */
	public void createTurn(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn) {
		Connection conn;
		try {
			conn = connect();
			try (PreparedStatement pstmt = conn.prepareStatement(CREATE_BUSINESSRULETURN)) {
				pstmt.setInt(1,gameBusinessRulesInFacilityTurn.getRoundId());
				pstmt.setInt(2,gameBusinessRulesInFacilityTurn.getFacilityIdOrder());
				pstmt.setInt(3,gameBusinessRulesInFacilityTurn.getFacilityIdDeliver());
				pstmt.setString(4,gameBusinessRulesInFacilityTurn.getGameId());
				pstmt.setString(5,gameBusinessRulesInFacilityTurn.getGameAgentName());
				pstmt.setString(6,gameBusinessRulesInFacilityTurn.getGameAST());

				pstmt.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}

	/**
	 * A method which returns a GameBusinessRulesInFacilityTurn object from the SQLite Database
	 * @param gameId The Id of the game where the specific turns are located
	 * @param roundId The Id of the round where the specific turns are located
	 * @param facililtyIdOrder The Id of the Facility which placed an order
	 * @param facilityIdDeliver The Id of the Facility which delivered an order
	 * @return A GameBusinessRulesInFacilityTurn object which contains data from the database according to the search parameters
	 */
	public GameBusinessRulesInFacilityTurn readTurn(String gameId, int roundId, int facililtyIdOrder, int facilityIdDeliver){
		Connection conn;
		GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn = null;
		try {
			conn = connect();
			try (PreparedStatement pstmt = conn.prepareStatement(READ_BUSINESSRULETURN)) {
				pstmt.setString(1,gameId);
				pstmt.setInt(2,roundId);
				pstmt.setInt(3,facililtyIdOrder);
				pstmt.setInt(4,facilityIdDeliver);


				pstmt.executeUpdate();
				gameBusinessRulesInFacilityTurn = getGameBusinessRulesInFacilityTurn(pstmt);
			}
			conn.commit();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	return gameBusinessRulesInFacilityTurn;
	}

	/**
	 * A method which deletes a specific turn from the SQLite Database
	 * @param gameId The Id of the game where the specific turns are located
	 * @param roundId The Id of the round where the specific turns are located
	 * @param facililtyIdOrder The Id of the Facility which placed an order
	 * @param facilityIdDeliver The Id of the Facility which delivered an order
	 */
	public void deleteTurn(String gameId, int roundId, int facililtyIdOrder, int facilityIdDeliver){
		Connection conn;
		try {
			conn = connect();
			try (PreparedStatement pstmt = conn.prepareStatement(DELETE_BUSINESSRULETURN)) {
				pstmt.setString(1,gameId);
				pstmt.setInt(2,roundId);
				pstmt.setInt(3,facililtyIdOrder);
				pstmt.setInt(4,facilityIdDeliver);


				pstmt.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}

	}

	/**
	 * A helper method which executes an prepared statement which returns a GameBusinessRuleInFacilityTurn
	 * @param pstmt A prepared statement string which needs to be executed
	 * @return A GameBusinessRulesInFacilityTurn object which contains data from the database according to the search parameters given in the prepared statement
	 */
	private GameBusinessRulesInFacilityTurn getGameBusinessRulesInFacilityTurn(PreparedStatement pstmt) {
		GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn = null;
		try (ResultSet rs = pstmt.executeQuery()) {
			gameBusinessRulesInFacilityTurn = new GameBusinessRulesInFacilityTurn(rs.getInt("RoundId"), rs.getInt("FaciltyIdOrder"), rs.getInt("FacilityIdDeliver"),rs.getString("GameId"),  rs.getString("AgentName"), rs.getString("GameBusinessRule"), rs.getString("GameAST"));

		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return gameBusinessRulesInFacilityTurn;
	}
}
