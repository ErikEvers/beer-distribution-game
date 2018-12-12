package org.han.ica.asd.c;

import org.han.ica.asd.c.model.Round;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.han.ica.asd.c.dbconnection.DBConnection.RollBackTransaction;
import static org.han.ica.asd.c.dbconnection.DBConnection.connect;

public class RoundDAO {
	private static final String CREATE_ROUND = "INSERT INTO ROUND VALUES(?,?);";
	private static final String DELETE_ROUND = "DELETE FROM ROUND WHERE GameId = ? && RoundId = ?;";

	private FacilityTurnDAO turnDAO;


	public void createRound(String gameId, int roundId){
		Connection conn = null;
		createPreparedStatement(gameId, roundId, conn, CREATE_ROUND);
	}

	public void deleteRound(String gameId, int roundId){
		Connection conn = null;
		createPreparedStatement(gameId, roundId, conn, DELETE_ROUND);
	}

	public Round getRound(String gameId, int roundId){
		Round round = new Round(gameId,roundId);
		round.setTurns(turnDAO.fetchTurns(gameId,roundId));
		return round;
	}

	private void createPreparedStatement(String gameId, int roundId, Connection conn, String createRound) {
		try {
			conn = connect();
			if (conn == null) return;
			try (PreparedStatement pstmt = conn.prepareStatement(createRound)) {

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
