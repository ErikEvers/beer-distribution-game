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
	private static final String CREATE_FACILITYTURN = "INSERT INTO FacilityTurn VALUES (?,?,?,?,?,?,?)";
	private static final String READ_ROUNDS = "SELECT* FROM Round WHERE GameId = ?";
	private static final String DELETE_ORDERS = "DELETE FROM FacilityTurnOrder WHERE GameId = ? AND RoundId = ?;";
	private static final String DELETE_DELIVERS = "DELETE FROM FacilityTurnDeliver WHERE GameId = ? AND RoundId = ?;";
	private static final String DELETE_FACILITIES = "DELETE FROM FacilityTurn WHERE GameId = ? AND RoundId = ?;";
	private static final String ROUND_ID = "RoundId";
	private static final String FACILITY_ID = "FacilityId";
	private static final String UPDATE_FACILITYDELIVER = "UPDATE FacilityTurnDeliver SET FacilityId = ?, FacilityIdDeliver = ?,DeliverAmount =?,OpenOrderAmount = ? WHERE GameId = ? AND RoundId = ?;";
	private static final String UPDATE_FACILITYORDER = "UPDATE FacilityTurnOrder SET FacilityId = ?, FacilityIdOrder = ?, OrderAmount = ? WHERE GameId = ?, RoundId = ?;";
	private static final String UPDATE_FACILITYTURN = "UPDATE FacilityTurn SET FacilityId = ?, Stock = ?, Backorders = ?, RemainingBudget = ?, Bankrupt = ? WHERE GameId = ? AND RoundId = ?;";


	@Inject
	private IDatabaseConnection databaseConnection;

	public RoundDAO() {
		//Empty Constructor for GUICE
	}


	/**
	 * A method to create a round in the SQLite Database
	 *
	 * @param round The round that the players have played
	 */
	public void createRound(Round round) {
		if(getRound(round.getRoundId()) != null){
			updateRound(round);
		}
		else {
			Connection conn = databaseConnection.connect();
			executePreparedStatement(round.getRoundId(), conn, CREATE_ROUND);

			round.getFacilityTurns().forEach(facilityTurn -> createFacilityTurn(round.getRoundId(),facilityTurn));
			round.getFacilityOrders().forEach(facilityTurnOrder -> createFacilityOrder(round.getRoundId(),facilityTurnOrder));
			round.getFacilityTurnDelivers().forEach(facilityTurnDeliver -> createFacilityDeliver(round.getRoundId(),facilityTurnDeliver));

		}
	}


	/**
	 * A method which inserts multiple rounds into the database
	 * @param rounds
	 */
	public void insertRounds(List<Round> rounds) {
		rounds.forEach(this::createRound);
	}

	/**
	 * Updates a list with rounds in the database
	 * @param rounds
	 */
	public void updateRounds(List<Round> rounds) {
		rounds.forEach(this::createRound);
	}


	/**
	 * Updates a single round in the database
	 * @param round
	 */
	private void updateRound(Round round) {
		round.getFacilityTurnDelivers().forEach(facilityTurnDeliver -> updateFacilityDeliver(round.getRoundId(),facilityTurnDeliver));
		round.getFacilityOrders().forEach(facilityTurnOrder -> updateFacilityOrder(round.getRoundId(),facilityTurnOrder));
		round.getFacilityTurnDelivers().forEach(facilityTurnDeliver -> updateFacilityDeliver(round.getRoundId(),facilityTurnDeliver));
	}

	private void updateFacilityDeliver(int roundId, FacilityTurnDeliver facilityTurnDeliver) {
		if (getFacilityDeliversInRound(roundId).contains(facilityTurnDeliver)) {
			Connection conn = databaseConnection.connect();
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_FACILITYDELIVER)) {

					conn.setAutoCommit(false);

					pstmt.setInt(1, facilityTurnDeliver.getFacilityId());
					pstmt.setInt(2, facilityTurnDeliver.getFacilityIdDeliverTo());
					pstmt.setInt(3, facilityTurnDeliver.getDeliverAmount());
					pstmt.setInt(4, facilityTurnDeliver.getOpenOrderAmount());
					pstmt.setString(5, DaoConfig.getCurrentGameId());
					pstmt.setInt(6, roundId);

					pstmt.executeUpdate();
					conn.commit();
				} catch (SQLException e) {
					LOGGER.log(Level.SEVERE, e.toString(), e);
					databaseConnection.rollBackTransaction(conn);
				}
			}
		}
	}

	private void updateFacilityOrder(int roundId, FacilityTurnOrder facilityTurnOrder) {
		if(getFacilityOrdersInRound(roundId).contains(facilityTurnOrder)) {
			Connection conn = databaseConnection.connect();
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_FACILITYORDER)) {

					conn.setAutoCommit(false);


					pstmt.setInt(1, facilityTurnOrder.getFacilityId());
					pstmt.setInt(2, facilityTurnOrder.getFacilityIdOrderTo());
					pstmt.setInt(3, facilityTurnOrder.getOrderAmount());
					pstmt.setString(4, DaoConfig.getCurrentGameId());
					pstmt.setInt(5, roundId);

					pstmt.executeUpdate();
					conn.commit();
				} catch (SQLException e) {
					LOGGER.log(Level.SEVERE, e.toString(), e);
					databaseConnection.rollBackTransaction(conn);
				}
			}
		}
	}

	private void updateFacilityTurn(int roundId, FacilityTurn facilityTurn) {
		if (getFacilityDeliversInRound(roundId).contains(facilityTurn)) {
			Connection conn = databaseConnection.connect();
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_FACILITYTURN)) {

					conn.setAutoCommit(false);


					pstmt.setInt(1, facilityTurn.getFacilityId());
					pstmt.setInt(2, facilityTurn.getStock());
					pstmt.setInt(3, facilityTurn.getBackorders());
					pstmt.setInt(4, facilityTurn.getRemainingBudget());
					pstmt.setBoolean(5, facilityTurn.isBankrupt());
					pstmt.setString(6, DaoConfig.getCurrentGameId());
					pstmt.setInt(7, roundId);

					pstmt.executeUpdate();
					conn.commit();
				} catch (SQLException e) {
					LOGGER.log(Level.SEVERE, e.toString(), e);
					databaseConnection.rollBackTransaction(conn);
				}
			}
		}
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
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(READ_ROUND)) {

				conn.setAutoCommit(false);

				pstmt.setString(1, DaoConfig.getCurrentGameId());
				pstmt.setInt(2, roundId);

				try (ResultSet rs = pstmt.executeQuery()) {
					if(!rs.isClosed()) {
						round = createRoundModel(rs);
					}
				}

				conn.commit();

			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				databaseConnection.rollBackTransaction(conn);
			}
		}

		return round;
	}

	public List<FacilityTurnOrder> getFacilityOrdersInRound(int roundId) {
		Connection conn = databaseConnection.connect();
		List<FacilityTurnOrder> orders = new ArrayList<>();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(READ_ORDERS)) {

				conn.setAutoCommit(false);

				pstmt.setString(1, DaoConfig.getCurrentGameId());
				pstmt.setInt(2, roundId);

				try (ResultSet rs = pstmt.executeQuery()) {
					if(!rs.isClosed()) {
						orders.add(new FacilityTurnOrder(rs.getInt(FACILITY_ID), rs.getInt("FacilityIdOrder"), rs.getInt("OrderAmount")));
					}
				}

				conn.commit();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				databaseConnection.rollBackTransaction(conn);
			}
		}

		return orders;
	}

	public List<FacilityTurnDeliver> getFacilityDeliversInRound(int roundId) {
		Connection conn = databaseConnection.connect();
		List<FacilityTurnDeliver> delivers = new ArrayList<>();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(READ_DELIVERS)) {

				conn.setAutoCommit(false);

				pstmt.setString(1, DaoConfig.getCurrentGameId());
				pstmt.setInt(2, roundId);

				try (ResultSet rs = pstmt.executeQuery()) {
					if(!rs.isClosed()) {
						delivers.add(new FacilityTurnDeliver(rs.getInt(FACILITY_ID), rs.getInt("FacilityIdDeliver"), rs.getInt("OpenOrderAmount"), rs.getInt("DeliverAmount")));
					}
				}

				conn.commit();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				databaseConnection.rollBackTransaction(conn);
			}
		}

		return delivers;
	}

	public List<FacilityTurn> getFacilitiesInRound(int roundId) {
		Connection conn = databaseConnection.connect();
		List<FacilityTurn> facilities = new ArrayList<>();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(READ_FACILITIES)) {

				conn.setAutoCommit(false);

				pstmt.setString(1, DaoConfig.getCurrentGameId());
				pstmt.setInt(2, roundId);

				try (ResultSet rs = pstmt.executeQuery()) {
					if(!rs.isClosed()) {
						facilities.add(new FacilityTurn(rs.getInt(FACILITY_ID), rs.getInt(ROUND_ID), rs.getInt("Stock"), rs.getInt("Backorders"), rs.getInt("RemainingBudget"), rs.getBoolean("Bankrupt")));
					}
				}

				conn.commit();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				databaseConnection.rollBackTransaction(conn);
			}
		}

		return facilities;
	}

	public void createFacilityOrder(int roundId, FacilityTurnOrder facilityTurnOrder) {
		Connection conn = databaseConnection.connect();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(CREATE_FACILITYORDER)) {

				conn.setAutoCommit(false);

				pstmt.setString(1, DaoConfig.getCurrentGameId());
				pstmt.setInt(2, roundId);
				pstmt.setInt(3, facilityTurnOrder.getFacilityId());
				pstmt.setInt(4, facilityTurnOrder.getFacilityIdOrderTo());
				pstmt.setInt(5, facilityTurnOrder.getOrderAmount());

				pstmt.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
	}

	public void createFacilityDeliver(int roundId, FacilityTurnDeliver facilityTurnDeliver) {
		Connection conn = databaseConnection.connect();
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
				conn.commit();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
	}

	public void createFacilityTurn(int roundId, FacilityTurn facilityTurn) {
		Connection conn = databaseConnection.connect();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(CREATE_FACILITYTURN)) {

				conn.setAutoCommit(false);

				pstmt.setString(1, DaoConfig.getCurrentGameId());
				pstmt.setInt(2, roundId);
				pstmt.setInt(3, facilityTurn.getFacilityId());
				pstmt.setInt(4, facilityTurn.getStock());
				pstmt.setInt(5, facilityTurn.getBackorders());
				pstmt.setInt(6, facilityTurn.getRemainingBudget());
				pstmt.setBoolean(7, facilityTurn.isBankrupt());

				pstmt.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
	}

	public List<Round> getRounds() {
		Connection conn = databaseConnection.connect();
		List<Round> rounds = new ArrayList<>();
		Round round;
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(READ_ROUNDS)) {
				conn.setAutoCommit(false);
				pstmt.setString(1, DaoConfig.getCurrentGameId());
				try (ResultSet rs = pstmt.executeQuery()) {
					if(!rs.isClosed()) {
						round = createRoundModel(rs);
						rounds.add(round);
					}
				}
				conn.commit();

			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				databaseConnection.rollBackTransaction(conn);
			}
		}

		return rounds;
	}

	private Round createRoundModel(ResultSet rs) throws SQLException {
		Round round = new Round();
		round.setRoundId(rs.getInt(ROUND_ID));
		round.setFacilityOrders(getFacilityOrdersInRound(round.getRoundId()));
		round.setFacilityTurnDelivers(getFacilityDeliversInRound(round.getRoundId()));
		round.setFacilityTurns(getFacilitiesInRound(round.getRoundId()));
		return round;
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
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(sqlStatement)) {

				conn.setAutoCommit(false);

				pstmt.setString(1, DaoConfig.getCurrentGameId());
				pstmt.setInt(2, roundId);

				pstmt.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
	}
}
