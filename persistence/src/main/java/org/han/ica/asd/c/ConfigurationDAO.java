package org.han.ica.asd.c;

import org.han.ica.asd.c.model.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.han.ica.asd.c.dbconnection.DBConnection.rollBackTransaction;
import static org.han.ica.asd.c.dbconnection.DBConnection.connect;

public class ConfigurationDAO implements IBeerDisitributionGameDAO {
	
	private static final String CREATE_CONFIGURATION = "INSERT INTO Configuration VALUES (?,?,?,?,?,?,?,?,?,?,?);";
	private static final String READ_CONFIGURATION = "SELECT* FROM Configuration WHERE GameId = ?;";
	private static final String UPDATE_CONFIGURATION = "UPDATE Configuration SET AmountOfRounds = ?, AmountOfFactories = ?, AmountOfWholesales = ?, AmountOfDistributors = ?,AmountOfRetailers = ?,MinimalOrderRetail = ?, MaximumOrderRetail = ?, ContinuePlayingWhenBankrupt = ?, InsightFacilities = ? WHERE GameId = ?";
	private static final String DELETE_CONFIGURATION = "DELETE FROM Configuration WHERE GameId = ?";
	public static final Logger LOGGER = Logger.getLogger(Configuration.class.getName());

	/**
	 * A method which creates a configuration in the SQLite Database
	 @param configuration A Configuration Object which ineeds to be inserted in the SQLite Database
	 */
	public void createConfiguration(Configuration configuration) {
		Connection conn = null;
		try {
			conn = connect();
			if (conn == null) return;
			try (PreparedStatement pstmt = conn.prepareStatement(CREATE_CONFIGURATION)) {

				conn.setAutoCommit(false);

				pstmt.setString(1, configuration.getGameId());
				pstmt.setInt(2, configuration.getAmountOfRounds());
				pstmt.setInt(3, configuration.getAmountOfFactories());
				pstmt.setInt(4, configuration.getAmountOfWholesales());
				pstmt.setInt(5, configuration.getAmountOfDistributors());
				pstmt.setInt(6, configuration.getAmountOfRetailers());
				pstmt.setInt(7, configuration.getMinimalOrderRetail());
				pstmt.setInt(8, configuration.getMaximumOrderRetail());
				pstmt.setBoolean(9, configuration.isContinuePlayingWhenBankrupt());
				pstmt.setBoolean(10, configuration.isInsightFacilities());

				pstmt.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE,e.toString());
			rollBackTransaction(conn);
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
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(READ_CONFIGURATION)) {
					pstmt.setString(1, gameId);
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
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE,e.toString());
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
			if (conn != null)
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
			LOGGER.log(Level.SEVERE,e.toString());
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
			if (conn != null)
				try (PreparedStatement pstmt = conn.prepareStatement(DELETE_CONFIGURATION)) {
					conn.setAutoCommit(false);
					pstmt.setString(1,gameId);
				}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE,e.toString(),e);
		}

	}
}
