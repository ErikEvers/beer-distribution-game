package org.han.ica.asd.c;


import org.han.ica.asd.c.dbconnection.DBConnection;
import org.han.ica.asd.c.dbconnection.DatabaseConnection;
import org.han.ica.asd.c.model.BeerGame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;



public class BeergameDAO implements IBeerDisitributionGameDAO {
	private static final String CREATE_BEERGAME = "INSERT INTO Beergame(GameId, GameName, GameDate) VALUES (?,?,?);";
	private static final String READ_BEERGAMES = "SELECT * FROM Beergame;";
	private static final String READ_BEERGAME = "SELECT * FROM Beergame WHERE GameId = ?;";
	private static final String DELETE_BEERGAME = "DELETE FROM Beergame WHERE GameId = ?;";

	public static final Logger LOGGER = Logger.getLogger(BeergameDAO.class.getName());
	private DatabaseConnection databaseConnection;


	public BeergameDAO(){
		databaseConnection = DBConnection.getInstance();
	}

	/**
	 * A method which creates a BeerGame in the Database
	 *
	 * @param gameName The specified name of the game
	 */
	public void createBeergame(String gameName) {
		Connection conn = null;
		try {
			conn = databaseConnection.connect();
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(CREATE_BEERGAME)) {

					conn.setAutoCommit(false);

					pstmt.setString(1, UUID.randomUUID().toString());
					pstmt.setString(2, gameName);
					pstmt.setString(3, new Date().toString());

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
	 * A method which returns all BeerGames which are inserted in the database
	 *
	 * @return An Arraylist of BeerGames
	 */
	public List<BeerGame> readBeergames() {
		Connection conn = null;
		ArrayList<BeerGame> beerGames = new ArrayList<>();
		try {
			conn = databaseConnection.connect();
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(READ_BEERGAMES)) {

					try (ResultSet rs = pstmt.executeQuery()) {
						while (rs.next()) {
							beerGames.add(new BeerGame(rs.getString("GameId"), rs.getString("GameName"), rs.getString("GameDate"), rs.getString("GameEndDate")));
						}
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString());
		}
		return beerGames;
	}

	/**
	 * Deletes a BeerGame from the SQLite Database
	 * @param gameId The specified Id of the game which needs to be deleted
	 */
	public void deleteBeergame(String gameId) {
		Connection conn = null;
		try {
			conn = databaseConnection.connect();
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(DELETE_BEERGAME)) {

					conn.setAutoCommit(false);

					pstmt.setString(1, gameId);

					pstmt.executeUpdate();
				}
				conn.commit();
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString());
			databaseConnection.rollBackTransaction(conn);
		}
	}

	/**
	 * A method which returns a single beergame according to the given parameters
	 * @param gameName The name of the game which needs to be returned
	 * @return A beergame object
	 */
	public BeerGame getGameLog(String gameId) {
		Connection conn = null;
		BeerGame beergame = null;
		try {
			conn = databaseConnection.connect();
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(READ_BEERGAME)) {
					pstmt.setString(1,gameId);
					try (ResultSet rs = pstmt.executeQuery()) {
						beergame = new BeerGame(rs.getString("GameId"), rs.getString("GameName"), rs.getString("GameDate"), rs.getString("GameEndDate"));
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString());
		}
		return beergame;
	}

}


