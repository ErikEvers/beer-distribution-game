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
	
	private static final String CREATE_CONFIGURATION = "INSERT INTO Configuration VALUES (?,?,?,?,?,?,?,?,?,?,?);";
	private static final String READ_CONFIGURATION = "SELECT* FROM Configuration WHERE GameId = ?;";
	private static final String UPDATE_CONFIGURATION = "UPDATE Configuration SET AmountOfRounds = ?, AmountOfFactories = ?, AmountOfWholesales = ?, AmountOfDistributors = ?,AmountOfRetailers = ?,MinimalOrderRetail = ?, MaximumOrderRetail = ?, ContinuePlayingWhenBankrupt = ?, InsightFacilities = ? WHERE GameId = ?";
	private static final String DELETE_CONFIGURATION = "DELETE FROM Configuration WHERE GameId = ?";


	/**
	 * A method which creates a configuration in the SQLite Database
	 * @param GameId The Id of the game which the configuration has to be set
	 * @param AmountOfRounds The amount of rounds that are going to be played in a game
	 * @param AmountOfFactories The amount of Factories that are available in a game
	 * @param AmountOfWholesales The amount of Wholesales that are going to be available in a game
	 * @param AmountOfDistributors The amount of Distributors that are going to be available in a game
	 * @param AmountOfRetailers The amount of Retailers that are going to be available in a game
	 * @param MinimalOrderRetail The minimal amount that a retailer must order at another facility
	 * @param MaximumOrderRetail The minimal amount that a retailer can order at another facility
	 * @param ContinuePlayingWhenBankrupt A boolean which represents if a player can keep on playing if they are bankrupt
	 * @param InsightFacilities A boolean which represents if a player can see the status and orders of other facilities
	 */
	public void createConfiguration(String gameId, int amountOfRounds, int amountOfFactories, int amountOfWholesales, int amountOfDistributors, int amountOfRetailers, int minimalOrderRetail, int maximumOrderRetail, boolean continuePlayingWhenBankrupt, boolean insightFacilities) {
		Connection conn = null;
		try {
			conn = connect();
			if (conn == null) return;
			try (PreparedStatement pstmt = conn.prepareStatement(CREATE_CONFIGURATION)) {

				conn.setAutoCommit(false);

				pstmt.setString(1, gameId);
				pstmt.setInt(2, amountOfRounds);
				pstmt.setInt(3, amountOfFactories);
				pstmt.setInt(4, amountOfWholesales);
				pstmt.setInt(5, amountOfDistributors);
				pstmt.setInt(6, amountOfRetailers);
				pstmt.setInt(7, minimalOrderRetail);
				pstmt.setInt(8, maximumOrderRetail);
				pstmt.setBoolean(9, continuePlayingWhenBankrupt);
				pstmt.setBoolean(10, insightFacilities);

				pstmt.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			RollBackTransaction(conn);
		}
	}

	/**
	 * A method which reads and returns configurations of a specific game
	 * @return A list of configurations of the found configurations of a specific game
	 */
	public List<Configuration> readConfigurations(String gameId) {
		Connection conn = null;
		ArrayList<Configuration> configurations = new ArrayList<>();
		try {
			conn = connect();
			if (conn == null) return null;
			try (PreparedStatement pstmt = conn.prepareStatement(READ_CONFIGURATION)) {
				pstmt.setString(1,gameId);
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


	/**
	 * A method which updates a existing configuration
	 * @param configuration A Configuration Object which is the new configuration
	 */
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


	/**
	 * A method which deletes a specific configurations according to a specific gameId
	 * @param gameId An Id which can be traced to a specific game
	 */
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
