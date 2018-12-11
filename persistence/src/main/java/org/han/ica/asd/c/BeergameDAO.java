package org.han.ica.asd.c;

import org.han.ica.asd.c.model.Beergame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static org.han.ica.asd.c.dbconnection.DBConnection.RollBackTransaction;
import static org.han.ica.asd.c.dbconnection.DBConnection.connect;

public class BeergameDAO extends BeerDisitributionGameDAO {
	//CRUD - Create, read, update, delete
	private static final String CREATE_BEERGAME = "INSERT INTO Beergame(GameId, GameName, GameDate) VALUES (?,?,?)";
	private static final String READ_BEERGAME = "SELECT * FROM Beergame";
	//Updating is not possible for this table
	private static final String DELETE_BEERGAME = "DELETE FROM Beergame WHERE GameId = ?";

	/**
	 * A method which creates a BeerGame in the Database
	 * @param gameName The specified name of the game
	 */
	public void createBeergame(String gameName) {
		Connection conn = null;
		try {
			conn = connect();
			if (conn == null) return;
			try (PreparedStatement pstmt = conn.prepareStatement(CREATE_BEERGAME)) {

				conn.setAutoCommit(false);

				pstmt.setString(1, UUID.randomUUID().toString());
				pstmt.setString(2, gameName);
				pstmt.setString(3, new Date().toString());

				pstmt.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			RollBackTransaction(conn);
		}
	}

	/**
	 * A method which returns all BeerGames which are inserted in the database
	 * @return An Arraylist of BeerGames
	 */
	public ArrayList<Beergame> readBeergames() {
		Connection conn = null;
		ArrayList<Beergame> beerGames = new ArrayList<>();
		try {
			conn = connect();
			if (conn == null) return null;
			try (PreparedStatement pstmt = conn.prepareStatement(READ_BEERGAME)) {

				try (ResultSet rs = pstmt.executeQuery()) {
					while (rs.next()) {
						beerGames.add(new Beergame(rs.getString("GameId"), rs.getString("GameName"), rs.getString("GameDate"), rs.getString("GameEndDate")));
					}
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
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
			conn = connect();
			if (conn == null) return;
			try (PreparedStatement pstmt = conn.prepareStatement(DELETE_BEERGAME)) {

				conn.setAutoCommit(false);

				pstmt.setString(1, gameId);

				pstmt.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			RollBackTransaction(conn);
		}
	}





}
