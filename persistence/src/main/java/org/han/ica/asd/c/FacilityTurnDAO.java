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
	private static final String CREATE_TURN = "INSERT INTO FacilityTurn VALUES (?,?,?,?,?,?,?,?,?);";
	private static final String UPDATE_TURN = "UPDATE FacilityTurn SET Stock = ?,RemainingBudget = ?,OrderAmount = ?, OpenOrderAmount = ?, OutgoingGoodsAmount = ? WHERE GameId = ? && RoundId = ? && FacilityIdOrder = ? && FacilityIdDeliver = ?)";
	private static final String READ_TURNS = "SELECT * FROM FacilityTurn WHERE GameId = ? && RoundId = ?;";
	private static final String DELETE_TURN = "DELETE FROM FacilityTurn WHERE GameId = ? && RoundId = ?;";


	public void createTurn(FacilityTurn facilityTurn){
		Connection conn;
		try {
			conn = connect();
			try (PreparedStatement pstmt = conn.prepareStatement(CREATE_TURN)) {

				pstmt.setString(1,facilityTurn.getGameId());
				pstmt.setInt(2,facilityTurn.getRoundId());
				pstmt.setInt(3, facilityTurn.getFacilityIdOrder());
				pstmt.setInt(4,facilityTurn.getFacilityIdDeliver());
				pstmt.setInt(5,facilityTurn.getStock());
				pstmt.setInt(6,facilityTurn.getRemainingBudget());
				pstmt.setInt(7,facilityTurn.getOrder());
				pstmt.setInt(8,facilityTurn.getOpenOrder());
				pstmt.setInt(9,facilityTurn.getOutgoingGoods());

				pstmt.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public List<FacilityTurn> fetchTurns(String gameId, int roundId) {
		Connection conn;
		ArrayList<FacilityTurn> turns = new ArrayList<>();
		try {
			conn = connect();
			if (conn == null)
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

	public void updateTurn(FacilityTurn facilityTurn){
		Connection conn;
		try {
			conn = connect();
			try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_TURN)) {

				pstmt.setInt(1,facilityTurn.getStock());
				pstmt.setInt(2,facilityTurn.getRemainingBudget());
				pstmt.setInt(3,facilityTurn.getOrder());
				pstmt.setInt(4,facilityTurn.getOpenOrder());
				pstmt.setInt(5,facilityTurn.getOutgoingGoods());
				pstmt.setString(6,facilityTurn.getGameId());
				pstmt.setInt(7,facilityTurn.getRoundId());
				pstmt.setInt(8, facilityTurn.getFacilityIdOrder());
				pstmt.setInt(9,facilityTurn.getFacilityIdDeliver());

				pstmt.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void deleteTurn(FacilityTurn facilityTurn){
		Connection conn;
		try {
			conn = connect();
			try (PreparedStatement pstmt = conn.prepareStatement(DELETE_TURN)) {
				pstmt.setString(1,facilityTurn.getGameId());
				pstmt.setInt(2,facilityTurn.getRoundId());

				pstmt.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
