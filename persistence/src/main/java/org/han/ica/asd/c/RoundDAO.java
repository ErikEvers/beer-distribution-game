package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.DBConnectionFactory;
import org.han.ica.asd.c.dbconnection.DatabaseConnection;
import org.han.ica.asd.c.model.Round;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoundDAO implements IBeerDisitributionGameDAO {
	private static final String CREATE_ROUND = "INSERT INTO ROUND VALUES(?,?);";
	private static final String DELETE_ROUND = "DELETE FROM ROUND WHERE GameId = ? AND RoundId = ?;";
	private static final String READ_ROUND = "SELECT * FROM ROUND WHERE GameId = ? AND RoundId = ?;";

	private static final Logger LOGGER = Logger.getLogger(RoundDAO.class.getName());

	private FacilityTurnDAO turnDAO;
	private DatabaseConnection databaseConnection;

	public RoundDAO(){
		turnDAO = new FacilityTurnDAO();
		databaseConnection = DBConnectionFactory.getInstance("");
	}


	/**
	 * A method to create a round in the SQLite Database
	 * @param gameId The id of the game of the new round
	 * @param roundId The id of the round that the players have played
	 */
	public void createRound(String gameId, int roundId){
		Connection conn = databaseConnection.connect();
		executePreparedStatement(gameId, roundId, conn, CREATE_ROUND);
	}

	/**
	 * A method which deletes a specific round in the SQLite Database
	 * @param gameId The id of the game of the round which needs to be deleted
	 * @param roundId The id of the round which needs to be deleted
	 */
	public void deleteRound(String gameId, int roundId){
		Connection conn = databaseConnection.connect();
		executePreparedStatement(gameId, roundId, conn, DELETE_ROUND);
	}

	/**
	 * A method which returns a specific round with all the turns from the SQLite Database
	 * @param gameId The id of the game of the round which needs to be returned
	 * @param roundId The id of the specific round which needs to be returned
	 * @return A round object with turns
	 */
	public Round getRound(String gameId, int roundId){
		Connection conn = databaseConnection.connect();
		Round round = null;
		try {
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(READ_ROUND)) {

					conn.setAutoCommit(false);

					pstmt.setString(1, gameId);
					pstmt.setInt(2, roundId);

					try (ResultSet rs = pstmt.executeQuery()) {
						while (rs.next()) {
							round = new Round(rs.getString("GameId"), rs.getInt("RoundId"));
						}
					}
					conn.commit();
				}
			}
				} catch (SQLException e) {
					LOGGER.log(Level.SEVERE, e.toString(), e);
					databaseConnection.rollBackTransaction(conn);
				}

				return round;
			}



	/**
	 * A helper method to execute a prepared statement for the SQLite Database.
	 * @param gameId The id of a specific game
	 * @param roundId The id of a specific round
	 * @param conn A connection object which can be used to execute the statement
	 * @param sqlStatement The statement which needs to be executed
	 */
	private void executePreparedStatement(String gameId, int roundId, Connection conn, String sqlStatement) {
		try {
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(sqlStatement)) {

					conn.setAutoCommit(false);

					pstmt.setString(1, gameId);
					pstmt.setInt(2, roundId);

					pstmt.executeUpdate();
				}

				conn.commit();
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE,e.toString(),e);
			databaseConnection.rollBackTransaction(conn);
		}
	}
}
