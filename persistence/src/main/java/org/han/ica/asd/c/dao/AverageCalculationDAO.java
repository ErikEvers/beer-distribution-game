package org.han.ica.asd.c.dao;

import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.exception.GameIdNotSetException;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AverageCalculationDAO {
    private static final Logger LOGGER = Logger.getLogger(AverageCalculationDAO.class.getName());
    private static final String READ_ALL_FACILITIES_WITH_FACILITYTYPE = "SELECT FacilityId FROM Facility WHERE GameId = ? AND FacilityName = ?;";
    private static final String READ_FACILITYTURN_FOR_FACILITY = "SELECT * FROM FacilityTurn WHERE GameId = ? AND RoundId = ? AND FacilityId = ?;";
    private static final String READ_FACILITYTURNORDER_FOR_FACILITY = "SELECT OrderAmount FROM FacilityTurnOrder WHERE GameId = ? AND RoundId = ? AND FacilityId = ?;";
    private static final String READ_FACILITYTURNDELIVER_FOR_FACILITY = "SELECT DeliverAmount, OpenOrderAmount FROM FacilityTurnOrder WHERE GameId = ? AND RoundId = ? AND FacilityId = ?;";

    @Inject
    private IDatabaseConnection databaseConnection;

    public AverageCalculationDAO() {
        //Empty constructor for GUICE.
    }

    /**
     * A method to retrieve all the facilities of a certain type.
     *
     * @param facilityType The facilityType of which the facilities need to be recovered.
     */
    public void readAllFacilitiesWithFacilityType(String facilityType) {
        Connection conn = databaseConnection.connect();
        if (conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(READ_ALL_FACILITIES_WITH_FACILITYTYPE)) {
                conn.setAutoCommit(false);

                DaoConfig.gameIdNotSetCheck(pstmt, 1);
                pstmt.setString(2, facilityType);

                pstmt.executeUpdate();

                conn.commit();
            } catch (GameIdNotSetException | SQLException e) {
                LOGGER.log(Level.SEVERE,e.toString(),e);
                databaseConnection.rollBackTransaction(conn);
            }
        }
    }

    /**
     * A method to retrieve all the FacilityTurns for a certain facility.
     *
     * @param roundId The first part of the identifier for what data needs to be recovered.
     * @param facilityId The first part of the identifier for what data needs to be recovered.
     */
    public void readFacilityTurnForFacility(int roundId, int facilityId) {
        executePreparedStatement(roundId, facilityId, READ_FACILITYTURN_FOR_FACILITY);
    }

    /**
     * A method to retrieve all the FacilityTurnOrders for a certain facility.
     *
     * @param roundId The first part of the identifier for what data needs to be recovered.
     * @param facilityId The first part of the identifier for what data needs to be recovered.
     */
    public void readFacilityTurnOrderForFacility(int roundId, int facilityId) {
        executePreparedStatement(roundId, facilityId, READ_FACILITYTURNDELIVER_FOR_FACILITY);

    }

    /**
     * A method to retrieve all the FacilityTurnDelivers for a certain facility.
     *
     * @param roundId The first part of the identifier for what data needs to be recovered.
     * @param facilityId The first part of the identifier for what data needs to be recovered.
     */
    public void readFacilityTurnDeliverForFacility(int roundId, int facilityId) {
        executePreparedStatement(roundId, facilityId, READ_FACILITYTURNORDER_FOR_FACILITY);
    }

    /**
     * A helper method to set and execute the prepared statement.
     *
     * @param roundId The first part of the identifier for what data needs to be recovered.
     * @param facilityId The first part of the identifier for what data needs to be recovered.
     * @param query The statement that needs to be executed on the database.
     */
    private void executePreparedStatement(int roundId, int facilityId, String query) {
        Connection conn = databaseConnection.connect();
        if (conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                conn.setAutoCommit(false);

                DaoConfig.gameIdNotSetCheck(pstmt, 1);
                pstmt.setInt(2, roundId);
                pstmt.setInt(3, facilityId);

                pstmt.executeUpdate();

                conn.commit();
            } catch (GameIdNotSetException | SQLException e) {
                LOGGER.log(Level.SEVERE,e.toString(),e);
                databaseConnection.rollBackTransaction(conn);
            }
        }
    }
}
