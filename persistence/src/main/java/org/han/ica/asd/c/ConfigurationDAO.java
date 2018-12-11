package org.han.ica.asd.c;

import org.han.ica.asd.c.model.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.han.ica.asd.c.dbconnection.DBConnection.RollBackTransaction;
import static org.han.ica.asd.c.dbconnection.DBConnection.connect;

public class ConfigurationDAO {
	//CRUD - Create, read, update, delete
	private static final String CREATE_CONFIGURATION = "INSERT INTO Configuration VALUES (?,?,?,?,?,?,?,?,?,?,?);";
	private static final String READ_CONFIGURATION = "SELECT* FROM Configuration;";
	private static final String UPDATE_CONFIGURATION = "UPDATE Configuration SET AmountOfRounds = ?, AmountOfFactories = ?, AmountOfWholesales = ?, AmountOfDistributors = ?,AmountOfRetailers = ?,MinimalOrderRetail = ?, MaximumOrderRetail = ?, ContinuePlayingWhenBankrupt = ?, InsightFacilities = ? WHERE GameId = ?";
	private static final String DELETE_CONFIGURATION = "DELETE FROM Configuration WHERE GameId = ?";

	public void createConfiguration(String GameId, int AmountOfRounds, int AmountOfFactories, int AmountOfWholesales, int AmountOfDistributors, int AmountOfRetailers, int MinimalOrderRetail, int MaximumOrderRetail, boolean ContinuePlayingWhenBankrupt, boolean InsightFacilities) {
		Connection conn = null;
		try {
			conn = connect();
			if (conn == null) return;
			try (PreparedStatement pstmt = conn.prepareStatement(CREATE_CONFIGURATION)) {

				conn.setAutoCommit(false);

				pstmt.setString(1, GameId);
				pstmt.setInt(2, AmountOfRounds);
				pstmt.setInt(3, AmountOfFactories);
				pstmt.setInt(4, AmountOfWholesales);
				pstmt.setInt(5, AmountOfDistributors);
				pstmt.setInt(6, AmountOfRetailers);
				pstmt.setInt(7, MinimalOrderRetail);
				pstmt.setInt(8, MaximumOrderRetail);
				pstmt.setBoolean(9, ContinuePlayingWhenBankrupt);
				pstmt.setBoolean(10, InsightFacilities);

				pstmt.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			RollBackTransaction(conn);
		}
	}

	public List<Configuration> readConfigurations() {
		Connection conn = null;
		ArrayList<Configuration> configurations = new ArrayList<>();
		try {
			conn = connect();
			if (conn == null) return null;
			try (PreparedStatement pstmt = conn.prepareStatement(READ_CONFIGURATION)) {

				try (ResultSet rs = pstmt.executeQuery()) {
					while (rs.next()) {
						configurations.add(new Configuration(rs.getString("GameId"), rs.getInt("AmountOfRounds"),
								rs.getInt("AmoundOfFactories"), rs.getInt("AmountOfWholesales"),
								rs.getInt("AmountOfDistributors"), rs.getInt("AmountOfRetailers"),
								rs.getInt("MinimalOrderRetail"), rs.getInt("MaximalOrderRetail"),
								rs.getBoolean("ContinuePlayingWhileBankrupt"), rs.getBoolean("InsightFacilities")));
					}
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return configurations;
	}

	public void updateConfigurations(Configuration configuration) {
		Connection conn = null;
		try {
			conn = connect();
			if (conn == null)
				try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_CONFIGURATION)) {
					conn.setAutoCommit(false);
					pstmt.setInt(1, configuration.getAmountOfRounds());
					pstmt.setInt(2, configuration.getAmountOfFactories());
					pstmt.setInt(3, configuration.getAmountOfWholesales());
					pstmt.setInt(4, configuration.getAmountOfDistributors());
					pstmt.setInt(5, configuration.getAmountOfRetailers());
					pstmt.setInt(6, configuration.getMinimalOrderRetail());
					pstmt.setInt(7, configuration.getMaximumOrderRetail());
					pstmt.setBoolean(8, configuration.isContinuePlayingWhenBankrupt());
					pstmt.setBoolean(9, configuration.isInsightFacilities());
					pstmt.setString(10, configuration.getGameId());
				}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void deleteConfigurations(String gameId){
		Connection conn = null;
		try {
			conn = connect();
			if (conn == null)
				try (PreparedStatement pstmt = conn.prepareStatement(DELETE_CONFIGURATION)) {
					conn.setAutoCommit(false);
					pstmt.setString(1,gameId);
				}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}
}
