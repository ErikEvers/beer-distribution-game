package org.han.ica.asd.c.dao;

import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.exception.GameIdNotSetException;
import org.han.ica.asd.c.model.domain_objects.FacilityTurn;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnDeliver;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AverageCalculationDAO {
    private static final Logger LOGGER = Logger.getLogger(AverageCalculationDAO.class.getName());
    private static final String READ_ALL_FACILITIES_WITH_FACILITYTYPE = "SELECT FacilityId FROM Facility WHERE GameId = ? AND FacilityName = ?;";
    private static final String READ_FACILITYTURN_FOR_FACILITY = "SELECT * FROM FacilityTurn WHERE GameId = ? AND RoundId = ? AND FacilityId = ?;";
    private static final String READ_FACILITYTURNORDER_FOR_FACILITY = "SELECT * FROM FacilityTurnOrder WHERE GameId = ? AND RoundId = ? AND FacilityId = ?;";
    private static final String READ_FACILITYTURNDELIVER_FOR_FACILITY = "SELECT * FROM FacilityTurnOrder WHERE GameId = ? AND RoundId = ? AND FacilityId = ?;";
    private static final String FACILITY_ID = "FacilityId";

    @Inject
    private IDatabaseConnection databaseConnection;

    public AverageCalculationDAO() {
        //Empty constructor for GUICE.
    }

    /**
     * A method to retrieve all the facilities of a certain type.
     *
     * @param facilityType The facilityType name from whom the facilities need to be recovered.
     * @return A list with found facilities from the database which contain the facilityType.
     */
    public List<Integer> readAllFacilitiesWithFacilityType(String facilityType) {
        Connection conn = databaseConnection.connect();
        List<Integer> facilityIds = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(READ_ALL_FACILITIES_WITH_FACILITYTYPE)) {
            conn.setAutoCommit(false);

            DaoConfig.gameIdNotSetCheck(pstmt, 1);
            pstmt.setString(2, facilityType);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    facilityIds.add(rs.getInt(FACILITY_ID));
                }
            }

            conn.commit();
        } catch (GameIdNotSetException | SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            databaseConnection.rollBackTransaction(conn);
        }
        return facilityIds;
    }

    /**
     * A method to retrieve all the FacilityTurns for a certain facility within a specific round.
     *
     * @param roundId    The first part of the identifier for what data needs to be recovered.
     * @param facilityId The first part of the identifier for what data needs to be recovered.
     * @return A FacilityTurn which contains the facilityTurn of a certain facility.
     */
    public FacilityTurn readFacilityTurnForFacility(int roundId, int facilityId) {
        Connection conn = databaseConnection.connect();
        int backorderAmountStub = 0;
        FacilityTurn facilityTurn = null;
        try (PreparedStatement pstmt = conn.prepareStatement(READ_FACILITYTURN_FOR_FACILITY)) {
            conn.setAutoCommit(false);

            DaoConfig.gameIdNotSetCheck(pstmt, 1);
            pstmt.setInt(2, roundId);
            pstmt.setInt(3, facilityId);

            try (ResultSet rs = pstmt.executeQuery()) {
                facilityTurn = new FacilityTurn(rs.getInt(FACILITY_ID), rs.getInt("RoundId"), rs.getInt("Stock"), backorderAmountStub, rs.getInt("RemainingBudget"), rs.getBoolean("Bankrupt"));
            }

            conn.commit();
        } catch (GameIdNotSetException | SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            databaseConnection.rollBackTransaction(conn);
        }
        return facilityTurn;
    }


    /**
     * A method to retrieve all the FacilityTurnOrders for a certain facility.
     *
     * @param roundId    The first part of the identifier for what data needs to be recovered.
     * @param facilityId The first part of the identifier for what data needs to be recovered.
     * @return Returns a list which contains all facilityTurnOrders of a certain facility in a certain round.
     */
    public List<FacilityTurnOrder> readFacilityTurnOrderForFacility(int roundId, int facilityId) {
        Connection conn = databaseConnection.connect();
        List<FacilityTurnOrder> facilityOrderAmounts = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(READ_FACILITYTURNORDER_FOR_FACILITY)) {
            conn.setAutoCommit(false);

            DaoConfig.gameIdNotSetCheck(pstmt, 1);
            pstmt.setInt(2, roundId);
            pstmt.setInt(3, facilityId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    facilityOrderAmounts.add(new FacilityTurnOrder(rs.getInt(FACILITY_ID), rs.getInt("FacilityIdOrder"), rs.getInt("OrderAmount")));
                }
            }

            conn.commit();
        } catch (GameIdNotSetException | SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            databaseConnection.rollBackTransaction(conn);
        }
        return facilityOrderAmounts;
    }


    /**
     * A method to retrieve all the FacilityTurnDelivers for a certain facility.
     *
     * @param roundId    The first part of the identifier for what data needs to be recovered.
     * @param facilityId The first part of the identifier for what data needs to be recovered.
     * @return Returns a list which contains all FacilityTurnDelivers of a certain facility in a certain round.
     */
    public List<FacilityTurnDeliver> readFacilityTurnDeliverForFacility(int roundId, int facilityId) {
        Connection conn = databaseConnection.connect();
        List<FacilityTurnDeliver> facilitydeliverAmounts = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(READ_FACILITYTURNDELIVER_FOR_FACILITY)) {
            conn.setAutoCommit(false);

            DaoConfig.gameIdNotSetCheck(pstmt, 1);
            pstmt.setInt(2, roundId);
            pstmt.setInt(3, facilityId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    facilitydeliverAmounts.add(new FacilityTurnDeliver(rs.getInt(FACILITY_ID), rs.getInt("FacilityIdDeliver"), rs.getInt("OpenOrderAmount"), rs.getInt("DeliverAmount")));
                }
            }

            conn.commit();
        } catch (GameIdNotSetException | SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            databaseConnection.rollBackTransaction(conn);
        }
        return facilitydeliverAmounts;
    }

}
