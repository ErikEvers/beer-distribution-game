package org.han.ica.asd.c.dao;


import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.FacilityTurn;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnDeliver;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;
import org.han.ica.asd.c.model.domain_objects.Round;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoundDAO {
	private static final String CREATE_ROUND = "INSERT INTO ROUND VALUES(?,?);";
	private static final String DELETE_ROUND = "DELETE FROM ROUND WHERE GameId = ? AND RoundId = ?;";
	private static final String READ_ROUND = "SELECT * FROM ROUND WHERE GameId = ? AND RoundId = ?;";
	private static final String READ_ORDERS = "SELECT * FROM FacilityTurnOrder WHERE GameId = ? AND RoundId = ?;";
	private static final String READ_DELIVERS = "SELECT * FROM FacilityTurnDeliver WHERE GameId = ? AND RoundId = ?;";
	private static final String READ_FACILITIES = "SELECT * FROM FacilityTurn WHERE GameId = ? AND RoundId = ?;";
	private static final Logger LOGGER = Logger.getLogger(RoundDAO.class.getName());
	private static final String CREATE_FACILITYORDER = "INSERT INTO FacilityTurnOrder (GameId, RoundId, FacilityId, FacilityIdOrder,OrderAmount) VALUES (?,?,?,?,?);";
	private static final String CREATE_FACILITYDELIVER = "INSERT INTO FacilityTurnDeliver (GameId, RoundId, FacilityId, FacilityIdDeliver,DeliverAmount,OpenOrderAmount)  VALUES (?,?,?,?,?,?);";
	private static final String CREATE_FACILITYTURN = "INSERT INTO FacilityTurn VALUES (?,?,?,?,?,?)";
	private static final String READ_ROUNDS = "SELECT* FROM Round WHERE GameId = ?";
	private static final String DELETE_ORDERS = "DELETE FROM FacilityTurnOrder WHERE GameId = ? AND RoundId = ?;";
	private static final String DELETE_DELIVERS = "DELETE FROM FacilityTurnDeliver WHERE GameId = ? AND RoundId = ?;";
	private static final String DELETE_FACILITIES = "DELETE FROM FacilityTurn WHERE GameId = ? AND RoundId = ?;";

	@Inject
	private IDatabaseConnection databaseConnection;

	public RoundDAO() {
		//Empty Constructor for GUICE
	}


	/**
	 * A method to create a round in the SQLite Database
	 *
	 * @param roundId The id of the round that the players have played
	 */
	public void createRound(int roundId) {
		Connection conn = databaseConnection.connect();
		executePreparedStatement(roundId, conn, CREATE_ROUND);
	}

	/**
	 * A method which deletes a specific round in the SQLite Database
	 *
	 * @param roundId The id of the round which needs to be deleted
	 */
	public void deleteRound(int roundId) {
		deleteFacilityDelivers(roundId);
		deleteFacilityOrders(roundId);
		deleteFacilityTurns(roundId);

		Connection conn = databaseConnection.connect();
		executePreparedStatement(roundId, conn, DELETE_ROUND);

	}

	/**
	 * A method which returns a specific round with all the turns from the SQLite Database
	 *
	 * @param roundId The id of the specific round which needs to be returned
	 * @return A round object
	 */
	public Round getRound(int roundId) {
		Connection conn = databaseConnection.connect();
		Round round = null;
		try {
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(READ_ROUND)) {

					conn.setAutoCommit(false);

					pstmt.setString(1, DaoConfig.getCurrentGameId());
					pstmt.setInt(2, roundId);

					try (ResultSet rs = pstmt.executeQuery()) {
						round = new Round();
						round.setRoundId(rs.getInt("RoundId"));
						round.setFacilityOrders(getFacilityOrdersInRound(roundId));
						round.setFacilityTurnDelivers(getFacilityDeliversInRound(roundId));
						round.setFacilityTurns(getFacilitiesInRound(roundId));
					}

				}
				conn.commit();
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			databaseConnection.rollBackTransaction(conn);
		}

		return round;
	}

	public List<FacilityTurnOrder> getFacilityOrdersInRound(int roundId) {
		Connection conn = databaseConnection.connect();
		List<FacilityTurnOrder> orders = new ArrayList<>();
		try {
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(READ_ORDERS)) {

					conn.setAutoCommit(false);

					pstmt.setString(1, DaoConfig.getCurrentGameId());
					pstmt.setInt(2, roundId);

					try (ResultSet rs = pstmt.executeQuery()) {
						orders.add(new FacilityTurnOrder(rs.getInt("FacilityId"), rs.getInt("FacilityIdOrder"), rs.getInt("OrderAmount")));

					}
				}
				conn.commit();
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}

		return orders;
	}

	public List<FacilityTurnDeliver> getFacilityDeliversInRound(int roundId) {
		Connection conn = databaseConnection.connect();
		List<FacilityTurnDeliver> delivers = new ArrayList<>();
		try {
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(READ_DELIVERS)) {

					conn.setAutoCommit(false);

					pstmt.setString(1, DaoConfig.getCurrentGameId());
					pstmt.setInt(2, roundId);

					try (ResultSet rs = pstmt.executeQuery()) {
						delivers.add(new FacilityTurnDeliver(rs.getInt("FacilityId"), rs.getInt("FacilityIdDeliver"), rs.getInt("OpenOrderAmount"), rs.getInt("DeliverAmount")));

					}
				}
				conn.commit();
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}

		return delivers;
	}

	public List<FacilityTurn> getFacilitiesInRound(int roundId) {
		Connection conn = databaseConnection.connect();
		List<FacilityTurn> facilities = new ArrayList<>();
		try {
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(READ_FACILITIES)) {

					conn.setAutoCommit(false);

					pstmt.setString(1, DaoConfig.getCurrentGameId());
					pstmt.setInt(2, roundId);

					try (ResultSet rs = pstmt.executeQuery()) {
						facilities.add(new FacilityTurn(rs.getInt("FacilityId"), rs.getInt("RoundId"),rs.getInt("Stock"), rs.getInt("RemainingBudget"), rs.getBoolean("Bankrupt")));

					}
				}
				conn.commit();
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}

		return facilities;
	}

	public void createFacilityOrder(int roundId, FacilityTurnOrder facilityTurnOrder) {
		Connection conn = databaseConnection.connect();
		try {
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(CREATE_FACILITYORDER)) {

					conn.setAutoCommit(false);

					pstmt.setString(1, DaoConfig.getCurrentGameId());
					pstmt.setInt(2, roundId);
					pstmt.setInt(3, facilityTurnOrder.getFacilityId());
					pstmt.setInt(4, facilityTurnOrder.getFacilityIdOrderTo());
					pstmt.setInt(5, facilityTurnOrder.getOrderAmount());

					pstmt.executeUpdate();
				}
				conn.commit();
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}

	public void createFacilityDeliver(int roundId, FacilityTurnDeliver facilityTurnDeliver) {
		Connection conn = databaseConnection.connect();
		try {
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(CREATE_FACILITYDELIVER)) {

					conn.setAutoCommit(false);

					pstmt.setString(1, DaoConfig.getCurrentGameId());
					pstmt.setInt(2, roundId);
					pstmt.setInt(3, facilityTurnDeliver.getFacilityId());
					pstmt.setInt(4, facilityTurnDeliver.getFacilityIdDeliverTo());
					pstmt.setInt(5, facilityTurnDeliver.getDeliverAmount());
					pstmt.setInt(6, facilityTurnDeliver.getOpenOrderAmount());

					pstmt.executeUpdate();
				}
				conn.commit();
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}

	public void createFacilityTurn(int roundId, FacilityTurn facilityTurn) {
		Connection conn = databaseConnection.connect();
		try {
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(CREATE_FACILITYTURN)) {

					conn.setAutoCommit(false);

					pstmt.setString(1, DaoConfig.getCurrentGameId());
					pstmt.setInt(2, roundId);
					pstmt.setInt(3, facilityTurn.getFacilityId());
					pstmt.setInt(4, facilityTurn.getStock());
					pstmt.setInt(5, facilityTurn.getRemainingBudget());
					pstmt.setBoolean(6, facilityTurn.isBankrupt());

					pstmt.executeUpdate();
				}
				conn.commit();
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}

	public List<Round> getRounds() {
		Connection conn = databaseConnection.connect();
		List<Round> rounds = new ArrayList<>();
		Round round = null;
		try {
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(READ_ROUNDS)) {
					conn.setAutoCommit(false);
					pstmt.setString(1, DaoConfig.getCurrentGameId());
					try (ResultSet rs = pstmt.executeQuery()) {
						if(!rs.isClosed()) {
							round = new Round();
							round.setRoundId(rs.getInt("RoundId"));
							round.setFacilityOrders(getFacilityOrdersInRound(round.getRoundId()));
							round.setFacilityTurnDelivers(getFacilityDeliversInRound(round.getRoundId()));
							round.setFacilityTurns(getFacilitiesInRound(round.getRoundId()));
							rounds.add(round);
						}
					}

				}
				conn.commit();
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}

		return rounds;
	}

	public void deleteFacilityOrders(int roundId) {
		Connection conn = databaseConnection.connect();
		executePreparedStatement(roundId, conn, DELETE_ORDERS);

	}

	public void deleteFacilityDelivers(int roundId) {
		Connection conn = databaseConnection.connect();
		executePreparedStatement(roundId, conn, DELETE_DELIVERS);
	}

	public void deleteFacilityTurns(int roundId) {
		Connection conn = databaseConnection.connect();
		executePreparedStatement(roundId, conn, DELETE_FACILITIES);
	}


	/**
	 * A helper method to execute a prepared statement for the SQLite Database.
	 *
	 * @param roundId      The id of a specific round
	 * @param conn         A connection object which can be used to execute the statement
	 * @param sqlStatement The statement which needs to be executed
	 */
	private void executePreparedStatement(int roundId, Connection conn, String sqlStatement) {
		try {
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(sqlStatement)) {

					conn.setAutoCommit(false);

					pstmt.setString(1, DaoConfig.getCurrentGameId());
					pstmt.setInt(2, roundId);

					pstmt.executeUpdate();
				}
				conn.commit();
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
}
