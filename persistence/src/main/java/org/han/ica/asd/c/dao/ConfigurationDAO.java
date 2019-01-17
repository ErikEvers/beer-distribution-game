package org.han.ica.asd.c.dao;

import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.exception.GameIdNotSetException;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Carsten Flokstra en Jelle Dimmendaal
 * zie commit f95bd5c9dea
 */
public class ConfigurationDAO {

    private static final String CREATE_CONFIGURATION = "INSERT INTO Configuration VALUES (?,?,?,?,?,?,?,?,?,?);";
    private static final String READ_CONFIGURATION = "SELECT AmountOfRounds,AmountOfFactories,AmountOfWholesalers,AmountOfWarehouses,AmountOfRetailers,MinimalOrderRetail,MaximumOrderRetail,ContinuePlayingWhenBankrupt,InsightFacilities FROM Configuration WHERE GameId = ?;";
    private static final String READ_CONFIGURATIONS = "SELECT * FROM Configuration;";
    private static final String UPDATE_CONFIGURATION = "UPDATE Configuration SET AmountOfRounds = ?, AmountOfFactories = ?, AmountOfWholesalers = ?, AmountOfWarehouses = ?,AmountOfRetailers = ?,MinimalOrderRetail = ?, MaximumOrderRetail = ?, ContinuePlayingWhenBankrupt = ?, InsightFacilities = ? WHERE GameId = ?;";
    private static final String DELETE_CONFIGURATION = "DELETE FROM Configuration WHERE GameId = ?;";
    private static final String GET_LOWER_LINKED_FACILITIES = "SELECT FacilityIdOrdering, FacilityIdDelivering FROM FacilityLinkedTo WHERE GameId = ?;";
    private static final String SET_LOWER_LINKED_FACILITIES = "INSERT INTO FacilityLinkedTo VALUES (?,?,?)";
    private static final String UPDATE_LOWER_LINKED_FACILITIES = "UPDATE FacilityLinkedTo SET FacilityIdDelivering = ? WHERE GameId = ? AND FacilityIdOrdering = ?;";
    private static final String DELETE_FACILITY_LINKS = "DELETE FROM FacilityLinkedTo WHERE GameId = ?;";
    private static final Logger LOGGER = Logger.getLogger(ConfigurationDAO.class.getName());

    @Inject
    private IDatabaseConnection databaseConnection;

    @Inject
    private FacilityDAO facilityDAO;

    @Inject
    private FacilityTypeDAO facilityTypeDAO;


    public ConfigurationDAO() {
        //Empty Constructor for GUICE
    }

    /**
     * A method which creates a configuration in the SQLite Database
     *
     * @param configuration A ConfigurationDB Object which needs to be inserted in the SQLite Database
     */
    public void createConfiguration(Configuration configuration) {
        Connection conn = databaseConnection.connect();
        if (conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(CREATE_CONFIGURATION)) {

                conn.setAutoCommit(false);

                setPreparedStatement(configuration, pstmt);
                createFacilityLinks(configuration.getFacilitiesLinkedTo());

                pstmt.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
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
     *
     * @return A list of configurations of the found configurations of a specific game
     */
    public List<Configuration> readConfigurations() {
        List<Configuration> configurations = new ArrayList<>();
        Connection conn = databaseConnection.connect();
        if (conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(READ_CONFIGURATIONS); ResultSet rs = pstmt.executeQuery()) {
                conn.setAutoCommit(false);
                if (!rs.isClosed()) {
                    while (rs.next()) {
                        configurations.add(createConfigurationObject(rs));
                    }
                    conn.commit();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                databaseConnection.rollBackTransaction(conn);
            }
        }
        return configurations;
    }

    /**
     * A method which returns a single configuration according to the gameId
     *
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
                    if (!rs.isClosed()) {
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
     *
     * @param rs result set from the database.
     * @return created configurationDB.
     * @throws SQLException thrown when result set does not contain the requested keys.
     */
    private Configuration createConfigurationObject(ResultSet rs) throws SQLException {
        return new Configuration(rs.getInt("AmountOfRounds"),
                rs.getInt("AmountOfFactories"), rs.getInt("AmountOfWholesalers"),
                rs.getInt("AmountOfWarehouses"), rs.getInt("AmountOfRetailers"),
                rs.getInt("MinimalOrderRetail"), rs.getInt("MaximumOrderRetail"),
                rs.getBoolean("ContinuePlayingWhenBankrupt"), rs.getBoolean("InsightFacilities"),
                facilityDAO.readAllFacilitiesInGame(),
                readFacilityLinks()
                );
    }

    /**
     * A method which updates a existing configuration
     *
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
                LOGGER.log(Level.SEVERE, e.toString(), e);
                databaseConnection.rollBackTransaction(conn);
            }
        }
    }


    /**
     * A method which deletes a specific configurations according to a specific gameId
     */
    public void deleteConfigurations() {
        deleteFacilityLinks();
        Connection conn = databaseConnection.connect();
        if (conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(DELETE_CONFIGURATION)) {
                conn.setAutoCommit(false);
                pstmt.setString(1, DaoConfig.getCurrentGameId());
                pstmt.execute();
                conn.commit();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                databaseConnection.rollBackTransaction(conn);
            }
        }
    }

    /**
     * Inserts the linked facilities into the database
     *
     * @param facilitiesLinkedTo The facilities that are linked
     */
    public void createFacilityLinks(Map<Facility, List<Facility>> facilitiesLinkedTo) {
        facilitiesLinkedTo.forEach((higherFacility, lowerFacilityList) -> lowerFacilityList.forEach(lowerFacility -> {
            Connection conn = databaseConnection.connect();
            if (conn != null) {

                try (PreparedStatement pstmt = conn.prepareStatement(SET_LOWER_LINKED_FACILITIES)) {
                    conn.setAutoCommit(false);

                    DaoConfig.gameIdNotSetCheck(pstmt, 1);
                    pstmt.setInt(2, higherFacility.getFacilityId());
                    pstmt.setInt(3, lowerFacility.getFacilityId());

                    pstmt.execute();
                    conn.commit();
                } catch (SQLException | GameIdNotSetException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                    databaseConnection.rollBackTransaction(conn);
                }

            }
        }));
    }

    /**
     * Deletes facility links
     */
    public void deleteFacilityLinks() {
        Connection conn = databaseConnection.connect();
        if (conn == null) return;
        try (PreparedStatement pstmt = conn.prepareStatement(DELETE_FACILITY_LINKS)) {
            conn.setAutoCommit(false);

            DaoConfig.gameIdNotSetCheck(pstmt, 1);

            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException | GameIdNotSetException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            databaseConnection.rollBackTransaction(conn);
        }
    }

    /**
     * Takes all facility links from the database
     * @return
     * Returns a map of facility links
     */
    public Map<Facility, List<Facility>> readFacilityLinks() {
        Connection conn = databaseConnection.connect();
        Map<Facility, List<Facility>> facilitiesLinkedTo = new HashMap<>();
        if (conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(GET_LOWER_LINKED_FACILITIES)) {
                conn.setAutoCommit(false);

                DaoConfig.gameIdNotSetCheck(pstmt, 1);

                packageLinkedFacilities(facilitiesLinkedTo, pstmt.executeQuery());

                conn.commit();
            } catch (SQLException | GameIdNotSetException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                databaseConnection.rollBackTransaction(conn);
            }
        }
        return facilitiesLinkedTo;
    }

    /**
     * Updates the already existing facility links with the ones in the given Map
     * @param facilitiesLinkedTo Map with the updated facility links
     */
    public void updateFacilityLinks(Map<Facility, List<Facility>> facilitiesLinkedTo) {
        facilitiesLinkedTo.forEach((higherFacility, lowerFacilityList) -> lowerFacilityList.forEach(lowerFacility -> {
            Connection conn = databaseConnection.connect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_LOWER_LINKED_FACILITIES)) {
                    conn.setAutoCommit(false);

                    pstmt.setInt(1, lowerFacility.getFacilityId());
                    DaoConfig.gameIdNotSetCheck(pstmt, 2);
                    pstmt.setInt(3, higherFacility.getFacilityId());

                    pstmt.executeUpdate();
                    conn.commit();
                } catch (SQLException | GameIdNotSetException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                    databaseConnection.rollBackTransaction(conn);
                }
            }
        }));
    }

    private void packageLinkedFacilities(Map<Facility, List<Facility>> linkedFacilities, ResultSet rs) throws SQLException {
        Facility lastKnownParent = null;

        if (rs.isClosed()) {
            return;
        }
        while (rs.next()) {
            int parent = rs.getInt("FacilityIdOrdering");
            if (lastKnownParent == null || parent != lastKnownParent.getFacilityId()) {
                lastKnownParent = facilityDAO.readSpecificFacility(parent);
                linkedFacilities.put(lastKnownParent, new ArrayList<>());
            }
            linkedFacilities.get(lastKnownParent).add(facilityDAO.readSpecificFacility(rs.getInt("FacilityIdDelivering")));
        }

    }
}
