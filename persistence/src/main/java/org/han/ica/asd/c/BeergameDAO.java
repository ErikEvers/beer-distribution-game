package org.han.ica.asd.c;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

}
