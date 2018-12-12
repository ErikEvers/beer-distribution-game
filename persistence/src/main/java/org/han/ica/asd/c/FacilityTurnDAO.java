package org.han.ica.asd.c;

import org.han.ica.asd.c.model.FacilityTurn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.han.ica.asd.c.dbconnection.DBConnection.RollBackTransaction;
import static org.han.ica.asd.c.dbconnection.DBConnection.connect;

public class FacilityTurnDAO {
	private static final String READ_TURNS = "SELECT* FROM FacilityTurn WHERE GameId = ? && RoundId = ?;";

	public List<FacilityTurn> fetchTurns(String gameId, int roundId) {
		Connection conn = null;
		try {
			conn = connect();
			if (conn == null) return;
			try (PreparedStatement pstmt = conn.prepareStatement(READ_TURNS)) {

				conn.setAutoCommit(false);

				pstmt.setString(1, gameId);
				pstmt.setInt(2, roundId);

				pstmt.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			RollBackTransaction(conn);
		}
	}
}
