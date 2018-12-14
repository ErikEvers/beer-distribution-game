package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.DBConnection;
import org.han.ica.asd.c.dbconnection.DatabaseConnection;
import org.han.ica.asd.c.model.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigurationDAO implements IBeerDisitributionGameDAO {
	
	private static final String CREATE_CONFIGURATION = "INSERT INTO Configuration VALUES (?,?,?,?,?,?,?,?,?,?);";
	private static final String READ_CONFIGURATION = "SELECT * FROM Configuration WHERE GameId = ?;";
	private static final String READ_CONFIGURATIONS = "SELECT * FROM Configuration;";
	private static final String UPDATE_CONFIGURATION = "UPDATE Configuration SET AmountOfRounds = ?, AmountOfFactories = ?, AmountOfWholesales = ?, AmountOfDistributors = ?,AmountOfRetailers = ?,MinimalOrderRetail = ?, MaximumOrderRetail = ?, ContinuePlayingWhenBankrupt = ?, InsightFacilities = ? WHERE GameId = ?;";
	private static final String DELETE_CONFIGURATION = "DELETE FROM Configuration WHERE GameId = ?;";
	public static final Logger LOGGER = Logger.getLogger(Configuration.class.getName());

	private DatabaseConnection databaseConnection;

	public ConfigurationDAO(){
		databaseConnection = DBConnection.getInstance();
	}
	/**
	 * A method which creates a configuration in the SQLite Database
	 @param configuration A Configuration Object which needs to be inserted in the SQLite Database
	 */
	public void createConfiguration(Configuration configuration) {
		Connection conn = null;
		try {
			conn = databaseConnection.connect();
			if (conn != null) {
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
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE,e.toString());
			databaseConnection.rollBackTransaction(conn);
		}
	}

	/**
	 * A method which reads and returns all configurations
	 * @return A list of configurations of the found configurations of a specific game
	 */
	public List<Configuration> readConfigurations() {
		Connection conn = null;
		ArrayList<Configuration> configurations = new ArrayList<>();
		try {
			conn = databaseConnection.connect();
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(READ_CONFIGURATIONS)) {
					try (ResultSet rs = pstmt.executeQuery()) {
						while (rs.next()) {
							configurations.add(new Configuration(rs.getString("GameId"), rs.getInt("AmountOfRounds"),
									rs.getInt("AmountOfFactories"), rs.getInt("AmountOfWholesales"),
									rs.getInt("AmountOfDistributors"), rs.getInt("AmountOfRetailers"),
									rs.getInt("MinimalOrderRetail"), rs.getInt("MaximumOrderRetail"),
									rs.getBoolean("ContinuePlayingWhenBankrupt"), rs.getBoolean("InsightFacilities")));
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
	 * A method which returns a single configuration according to the gameId
	 * @param gameId The Id of a game
	 * @return A configuration according to the gameId
	 */
	public Configuration readConfiguration(String gameId){
		Connection conn;
		Configuration configuration = null;
		try {
			conn = databaseConnection.connect();
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(READ_CONFIGURATION)) {
					pstmt.setString(1,gameId);
					try (ResultSet rs = pstmt.executeQuery()) {
						while (rs.next()) {
							configuration = new Configuration(rs.getString("GameId"), rs.getInt("AmountOfRounds"),
									rs.getInt("AmountOfFactories"), rs.getInt("AmountOfWholesales"),
									rs.getInt("AmountOfDistributors"), rs.getInt("AmountOfRetailers"),
									rs.getInt("MinimalOrderRetail"), rs.getInt("MaximumOrderRetail"),
									rs.getBoolean("ContinuePlayingWhenBankrupt"), rs.getBoolean("InsightFacilities"));
						}
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE,e.toString());
		}
		return configuration;
	}




	/**
	 * A method which updates a existing configuration
	 * @param configuration A updated Configuration Object which is going to be the new configuration
	 */
	public void updateConfigurations(Configuration configuration) {
		Connection conn = null;
		try {
			conn = databaseConnection.connect();
			if (conn != null) {
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
					pstmt.execute();
				}
				conn.commit();
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
			conn = databaseConnection.connect();
			if (conn != null) {
				try (PreparedStatement pstmt = conn.prepareStatement(DELETE_CONFIGURATION)) {
					conn.setAutoCommit(false);
					pstmt.setString(1, gameId);
					pstmt.execute();
				}
			}
			conn.commit();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE,e.toString(),e);
		}

	}
}
