package org.han.ica.asd.c;

import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.Round;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.han.ica.asd.c.dbconnection.DBConnection.RollBackTransaction;
import static org.han.ica.asd.c.dbconnection.DBConnection.connect;

public class RoundDAO {
	private static final String CREATE_ROUND = "INSERT INTO ROUND VALUES(?,?)";
	private static final String READ_ROUND = "";
	private static final String UPDATE_ROUND = "";
	private static final String DELETE_ROUND = "";

	private FacilityTurnDAO turnDAO;


	public void createRound(String gameId, int roundId){
		Connection conn = null;
		try {
			conn = connect();
			if (conn == null) return;
			try (PreparedStatement pstmt = conn.prepareStatement(CREATE_ROUND)) {

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

	public Round fetchRoundData(String gameId, int roundId){
		Round round = new Round(gameId,roundId);
		round.setTurns(turnDAO.fetchTurns(gameId,roundId));
		return round;
	}
}
