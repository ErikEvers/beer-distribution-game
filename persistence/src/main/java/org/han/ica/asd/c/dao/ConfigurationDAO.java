package org.han.ica.asd.c.dao;


import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.Configuration;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigurationDAO {
	
	private static final String CREATE_CONFIGURATION = "INSERT INTO Configuration VALUES (?,?,?,?,?,?,?,?,?,?);";
	private static final String READ_CONFIGURATION = "SELECT AmountOfRounds,AmountOfFactories,AmountOfWholesalers,AmountOfWarehouses,AmountOfRetailers,MinimalOrderRetail,MaximumOrderRetail,ContinuePlayingWhenBankrupt,InsightFacilities FROM Configuration WHERE GameId = ?;";
	private static final String READ_CONFIGURATIONS = "SELECT * FROM Configuration;";
	private static final String UPDATE_CONFIGURATION = "UPDATE Configuration SET AmountOfRounds = ?, AmountOfFactories = ?, AmountOfWholesalers = ?, AmountOfWarehouses = ?,AmountOfRetailers = ?,MinimalOrderRetail = ?, MaximumOrderRetail = ?, ContinuePlayingWhenBankrupt = ?, InsightFacilities = ? WHERE GameId = ?;";
	private static final String DELETE_CONFIGURATION = "DELETE FROM Configuration WHERE GameId = ?;";
	private static final Logger LOGGER = Logger.getLogger(ConfigurationDAO.class.getName());

	@Inject
	private IDatabaseConnection databaseConnection;

	@Inject
	private FacilityDAO facilityDAO;

	@Inject
	private FacilityTypeDAO facilityTypeDAO;


	public ConfigurationDAO(){
		//Empty Constructor for GUICE
	}

	/**
	 * A method which creates a configuration in the SQLite Database
	 @param configuration A ConfigurationDB Object which needs to be inserted in the SQLite Database
	 */
	public void createConfiguration(Configuration configuration) {
		Connection conn = databaseConnection.connect();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(CREATE_CONFIGURATION)) {

				conn.setAutoCommit(false);

				setPreparedStatement(configuration, pstmt);

				pstmt.executeUpdate();

				conn.commit();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE,e.toString(),e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
	}

	private void setPreparedStatement(Configuration configuration, PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, DaoConfig.getCurrentGameId());
		pstmt.setInt(2, configuration.getAmountOfRounds());
		pstmt.setInt(3, configuration.getAmountOfFactories());
		pstmt.setInt(4, configuration.getAmountOfWholesalers());
		pstmt.setInt(5, configuration.getAmountOfWarehouses());
		pstmt.setInt(6, configuration.getAmountOfRetailers());
		pstmt.setInt(7, configuration.getMinimalOrderRetail());
		pstmt.setInt(8, configuration.getMaximumOrderRetail());
		pstmt.setBoolean(9, configuration.isContinuePlayingWhenBankrupt());
		pstmt.setBoolean(10, configuration.isInsightFacilities());
	}

	/**
	 * A method which reads and returns all configurations
	 * @return A list of configurations of the found configurations of a specific game
	 */
	public List<Configuration> readConfigurations() {
		List<Configuration> configurations = new ArrayList<>();
		Connection conn = databaseConnection.connect();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(READ_CONFIGURATIONS); ResultSet rs = pstmt.executeQuery()) {
				conn.setAutoCommit(false);
				if(!rs.isClosed()){
				while (rs.next()) {
					configurations.add(createConfigurationObject(rs));
				}
				conn.commit();
			}} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
		return configurations;
	}

	/**
	 * A method which returns a single configuration according to the gameId
	 * @return A configuration according to the gameId
	 */
	public Configuration readConfiguration() {
		Configuration configuration = null;
		Connection conn = databaseConnection.connect();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(READ_CONFIGURATION)) {
				conn.setAutoCommit(false);
				pstmt.setString(1, DaoConfig.getCurrentGameId());
				try (ResultSet rs = pstmt.executeQuery()) {
					if(!rs.isClosed()) {
						rs.next();
						configuration = createConfigurationObject(rs);
					}
				}
				conn.commit();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
		return configuration;
	}

	/**
	 * Port resultset to configurationDB object.
	 * @param rs result set from the database.
	 * @return created configurationDB.
	 * @throws SQLException thrown when result set does not contain the requested keys.
	 */
	private Configuration createConfigurationObject(ResultSet rs) throws SQLException {
		return new Configuration(rs.getInt("AmountOfRounds"),
				rs.getInt("AmountOfFactories"), rs.getInt("AmountOfWholesalers"),
				rs.getInt("AmountOfWarehouses"), rs.getInt("AmountOfRetailers"),
				rs.getInt("MinimalOrderRetail"), rs.getInt("MaximumOrderRetail"),
				rs.getBoolean("ContinuePlayingWhenBankrupt"), rs.getBoolean("InsightFacilities"));
	}

	/**
	 * A method which updates a existing configuration
	 * @param configuration A updated ConfigurationDB Object which is going to be the new configuration
	 */
	public void updateConfigurations(Configuration configuration) {
		Connection conn = databaseConnection.connect();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_CONFIGURATION)) {
				conn.setAutoCommit(false);
				pstmt.setInt(1, configuration.getAmountOfRounds());
				pstmt.setInt(2, configuration.getAmountOfFactories());
				pstmt.setInt(3, configuration.getAmountOfWholesalers());
				pstmt.setInt(4, configuration.getAmountOfWarehouses());
				pstmt.setInt(5, configuration.getAmountOfRetailers());
				pstmt.setInt(6, configuration.getMinimalOrderRetail());
				pstmt.setInt(7, configuration.getMaximumOrderRetail());
				pstmt.setBoolean(8, configuration.isContinuePlayingWhenBankrupt());
				pstmt.setBoolean(9, configuration.isInsightFacilities());
				pstmt.setString(10, DaoConfig.getCurrentGameId());
				pstmt.execute();

				conn.commit();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE,e.toString(),e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
	}


	/**
	 * A method which deletes a specific configurations according to a specific gameId
	 */
	public void deleteConfigurations(){
		Connection conn = databaseConnection.connect();
		if (conn != null) {
			try (PreparedStatement pstmt = conn.prepareStatement(DELETE_CONFIGURATION)) {
				conn.setAutoCommit(false);
				pstmt.setString(1, DaoConfig.getCurrentGameId());
				pstmt.execute();
				conn.commit();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE,e.toString(),e);
				databaseConnection.rollBackTransaction(conn);
			}
		}
	}
}
