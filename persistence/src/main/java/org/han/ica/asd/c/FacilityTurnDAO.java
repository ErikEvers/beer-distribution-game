package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.DBConnection;
import org.han.ica.asd.c.dbconnection.DatabaseConnection;
import org.han.ica.asd.c.model.FacilityLinkedTo;
import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.Round;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FacilityTurnDAO implements IBeerDisitributionGameDAO {
	private static final String CREATE_TURN = "INSERT INTO FacilityTurn VALUES (?,?,?,?,?,?,?,?,?);";
	private static final String UPDATE_TURN = "UPDATE FacilityTurn SET Stock = ?,RemainingBudget = ?,OrderAmount = ?, OpenOrderAmount = ?, OutgoingGoodsAmount = ? WHERE GameId = ? AND RoundId = ? AND FacilityIdOrder = ? AND FacilityIdDeliver = ?;";
	private static final String READ_TURNS = "SELECT * FROM FacilityTurn WHERE GameId = ? AND RoundId = ?;";
	private static final String READ_TURN = "SELECT * FROM FacilityTurn WHERE GameId = ? AND RoundId = ? AND FacilityIdOrder = ? AND FacilityIdDeliver = ?;";
	private static final String DELETE_TURN = "DELETE FROM FacilityTurn WHERE GameId = ? AND RoundId = ? AND FacilityIdOrder = ? AND FacilityIdDeliver = ?;";
	private static final Logger LOGGER = Logger.getLogger(FacilityTurnDAO.class.getName());

	private DatabaseConnection databaseConnection;

	public FacilityTurnDAO(){
		databaseConnection = DBConnection.getInstance();
	}

	/**
	 * A method to create a FacilityTurn in the SQLite Database
	 * @param facilityTurn A FacilityTurn object which contains the data which needs to be inserted in the SQLite Database
	 */
	public void createTurn(FacilityTurn facilityTurn) {
		Connection conn = null;
		try {
			conn = databaseConnection.connect();
			if(conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(CREATE_TURN)) {
					conn.setAutoCommit(false);

					pstmt.setString(1, facilityTurn.getGameId());
					pstmt.setInt(2, facilityTurn.getRoundId());
					pstmt.setInt(3, facilityTurn.getFacilityIdOrder());
					pstmt.setInt(4, facilityTurn.getFacilityIdDeliver());
					pstmt.setInt(5, facilityTurn.getStock());
					pstmt.setInt(6, facilityTurn.getRemainingBudget());
					pstmt.setInt(7, facilityTurn.getOrder());
					pstmt.setInt(8, facilityTurn.getOpenOrder());
					pstmt.setInt(9, facilityTurn.getOutgoingGoods());

					pstmt.executeUpdate();

				}
				conn.commit();
			}

		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			databaseConnection.rollBackTransaction(conn);
		}
	}

	/**
	 * A method which returns all the turns from a specific round from the SQLite Database
	 * @param gameId  The id of the game of the turn which needs to be returned
	 * @param roundId The id of the specific round which needs to be returned
	 * @return Returns a list of all the turns in a specific round in a specific game
	 */

	public List<FacilityTurn> fetchTurns(String gameId, int roundId) {
		Connection conn;
		ArrayList<FacilityTurn> turns = new ArrayList<>();
		try {
			conn = databaseConnection.connect();
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(READ_TURNS)) {
					conn.setAutoCommit(false);
					pstmt.setString(1, gameId);
					pstmt.setInt(2, roundId);
					try (ResultSet rs = pstmt.executeQuery()) {
						while (rs.next()) {
							turns.add(new FacilityTurn(rs.getInt("RoundId"), rs.getInt("FacilityIdOrder"), rs.getInt("FacilityIdDeliver"), rs.getString("GameId"),rs.getInt("Stock"), rs.getInt("RemainingBudget"), rs.getInt("OrderAmount"), rs.getInt("OpenOrderAmount"), rs.getInt("OutgoingGoodsAmount")));
						}
					}
				}
				conn.commit();
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(),e);
		}
		return turns;

	}

	/**
	 * A method which returns a single turn of a round from a facililty
	 * @param round The specific round of the turn
	 * @param facilityLinkedTo The link between the facilities
	 * @return
	 */

	public FacilityTurn fetchTurn(Round round, FacilityLinkedTo facilityLinkedTo) {
		Connection conn;
		FacilityTurn facilityTurn = null;
		try {
			conn = databaseConnection.connect();
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(READ_TURN)) {
					pstmt.setString(1,round.getGameId());
					pstmt.setInt(2,round.getRoundId());
					pstmt.setInt(3,facilityLinkedTo.getFacilityIdOrder().getFacilityId());
					pstmt.setInt(4,facilityLinkedTo.getFacilityIdDeliver().getFacilityId());
					try (ResultSet rs = pstmt.executeQuery()){
						facilityTurn = new FacilityTurn(rs.getInt("RoundId"), rs.getInt("FacilityIdOrder"), rs.getInt("FacilityIdDeliver"), rs.getString("GameId"),rs.getInt("Stock"), rs.getInt("RemainingBudget"), rs.getInt("OrderAmount"), rs.getInt("OpenOrderAmount"), rs.getInt("OutgoingGoodsAmount"));
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(),e);
		}
		return facilityTurn;
	}

	/**
	 * A method which updates a specific turn in the SQLite Database
	 * @param facilityTurn A FacilityTurn object which contains the data which needs to be updated in the SQLite Database
	 */
	public void updateTurn(FacilityTurn facilityTurn) {
		Connection conn;
		try {
			conn = databaseConnection.connect();
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_TURN)) {
					conn.setAutoCommit(false);
					pstmt.setInt(1, facilityTurn.getStock());
					pstmt.setInt(2, facilityTurn.getRemainingBudget());
					pstmt.setInt(3, facilityTurn.getOrder());
					pstmt.setInt(4, facilityTurn.getOpenOrder());
					pstmt.setInt(5, facilityTurn.getOutgoingGoods());
					pstmt.setString(6, facilityTurn.getGameId());
					pstmt.setInt(7, facilityTurn.getRoundId());
					pstmt.setInt(8, facilityTurn.getFacilityIdOrder());
					pstmt.setInt(9, facilityTurn.getFacilityIdDeliver());

					pstmt.executeUpdate();
				}
				conn.commit();
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(),e);
		}
	}

	/**
	 * A method which deletes a specific turn in the SQLite Database
	 * @param facilityTurn A FacilityTurn object which contains the data which needs to be inserted in the SQLite Database
	 */
	public void deleteTurn(FacilityTurn facilityTurn) {
		Connection conn;
		try {
			conn = databaseConnection.connect();
			try (PreparedStatement pstmt = conn.prepareStatement(DELETE_TURN)) {
				conn.setAutoCommit(false);
				pstmt.setString(1, facilityTurn.getGameId());
				pstmt.setInt(2, facilityTurn.getRoundId());
				pstmt.setInt(3, facilityTurn.getFacilityIdOrder());
				pstmt.setInt(4, facilityTurn.getFacilityIdDeliver());

				pstmt.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(),e);
		}
	}
}
