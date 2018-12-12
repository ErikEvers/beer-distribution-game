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
				gameBusinessRulesInFacilityTurn = getGame(pstmt);
			}
			conn.commit();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	return gameBusinessRulesInFacilityTurn;
	}

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

	private GameBusinessRulesInFacilityTurn getGame(PreparedStatement pstmt) {
		GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn = null;
		try (ResultSet rs = pstmt.executeQuery()) {
			gameBusinessRulesInFacilityTurn = new GameBusinessRulesInFacilityTurn(rs.getInt("RoundId"), rs.getInt("FaciltyIdOrder"), rs.getInt("FacilityIdDeliver"),rs.getString("GameId"),  rs.getString("AgentName"), rs.getString("GameBusinessRule"), rs.getString("GameAST"));

		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return gameBusinessRulesInFacilityTurn;
	}
}
