package org.han.ica.asd.c;

import org.han.ica.asd.c.model.FacilityTurn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.han.ica.asd.c.dbconnection.DBConnection.connect;

public class FacilityTurnDAO {
	private static final String READ_TURNS = "SELECT* FROM FacilityTurn WHERE GameId = ? && RoundId = ?;";

	public List<FacilityTurn> fetchTurns(String gameId, int roundId) {
		Connection conn = null;
		ArrayList<FacilityTurn> turns = new ArrayList<>();
		try {
			conn = connect();
			if (conn == null) return null;
			try (PreparedStatement pstmt = conn.prepareStatement(READ_TURNS)) {
				pstmt.setString(1,gameId);
				pstmt.setInt(2,roundId);
				try (ResultSet rs = pstmt.executeQuery()) {
					while (rs.next()) {
						turns.add(new FacilityTurn(rs.getString("GameId"), rs.getInt("RoundId"), rs.getInt("FaciltyIdOrder"), rs.getInt("FacilityIdDeliver"),rs.getInt("Stock"),rs.getInt("RemainingBudget"),rs.getInt("OrderAmount"), rs.getInt("OpenOrderAmount"),rs.getInt("OutgoingGoodsAmount")));
					}
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return turns;
	}
}
