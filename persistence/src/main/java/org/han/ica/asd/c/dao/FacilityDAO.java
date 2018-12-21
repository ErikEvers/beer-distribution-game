package org.han.ica.asd.c.dao;


import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.dao_model.FacilityDB;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FacilityDAO implements IBeerDisitributionGameDAO {
    private static final String CREATE_FACILITY = "INSERT INTO Facility VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_FACILITY = "UPDATE Facility SET GameAgentName = ?, PlayerId = ?, FacilityName = ?, Bankrupt = ? WHERE FacilityId = ? AND GameId = ?;";
    private static final String DELETE_SPECIFIC_FACILITY = "DELETE FROM Facility WHERE FacilityId = ? AND GameId = ?;";
    private static final String DELETE_ALL_FACILITIES_IN_GAME = "DELETE FROM Facility WHERE GameId = ?;";
    private static final String READ_ALL_FACILITIES_IN_GAME = "SELECT * FROM Facility WHERE GameId = ?;";
    private static final String READ_SPECIFIC_FACILITY = "SELECT * FROM Facility WHERE FacilityId = ? AND GameId = ?;";
    private static final Logger LOGGER = Logger.getLogger(FacilityDAO.class.getName());

    @Inject
    private IDatabaseConnection databaseConnection;

    @Inject
    private FacilityTypeDAO facilityTypeDAO;

    public FacilityDAO() {
        //There has to be a constructor to inject the values above.
    }

    /**
     * A method to create a new facility.
     *
     * @param facility A domain_objects of FacilityDB with all the data required to create a new facility.
     */
    public void createFacility(FacilityDB facility) {
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(CREATE_FACILITY)) {
                    conn.setAutoCommit(false);

                    pstmt.setInt(1, facility.getFacilityId());
                    pstmt.setString(2, facility.getGameId());
                    pstmt.setString(3, facility.getGameAgentName());
                    pstmt.setString(4, facility.getPlayerId());
                    pstmt.setString(5, facility.getFacilityType());
                    pstmt.setBoolean(6, facility.isBankrupt());

                    pstmt.executeUpdate();
                }
                conn.commit();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    /**
     * A method to update an existing facility within a specific game.
     *
     * @param facility A domain_objects of FacilityDB with all the data required to update an existing facility.
     */
    public void updateFacility(FacilityDB facility) {
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_FACILITY)) {
                    conn.setAutoCommit(false);

                    pstmt.setString(1, facility.getGameAgentName());
                    pstmt.setString(2, facility.getPlayerId());
                    pstmt.setString(3, facility.getFacilityType());
                    pstmt.setBoolean(4, facility.isBankrupt());
                    pstmt.setInt(5, facility.getFacilityId());
                    pstmt.setString(6, facility.getGameId());

                    pstmt.executeUpdate();
                }
                conn.commit();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    /**
     * A method to delete a specific facility within a specific game.
     *
     * @param faciltyId The first part of the specifications to delete the specific facility.
     * @param gameId    The second part of the specifications to delete the specific facility.
     */
    public void deleteSpecificFacility(int faciltyId, String gameId) {
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(DELETE_SPECIFIC_FACILITY)) {
                    conn.setAutoCommit(false);

                    pstmt.setInt(1, faciltyId);
                    pstmt.setString(2, gameId);

                    pstmt.executeUpdate();
                }
                conn.commit();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }

    }

    /**
     * A method to delete all facilities within a specific game.
     *
     * @param gameId The game identifier from witch the facilities have to be deleted.
     */
    public void deleteAllFacilitiesInGame(String gameId) {
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(DELETE_ALL_FACILITIES_IN_GAME)) {
                    conn.setAutoCommit(false);

                    pstmt.setString(1, gameId);

                    pstmt.executeUpdate();
                }
                conn.commit();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }


    /**
     * A method to get all facilities within a specific game.
     *
     * @param gameId The identifier of a game.
     * @return The facilities that have been retrieved from the database.
     */
    public List<FacilityDB> readAllFacilitiesInGame(String gameId) {
        List<FacilityDB> facilities = new ArrayList<>();
        Connection conn = databaseConnection.connect();
        if(conn == null) {
            return facilities;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(READ_ALL_FACILITIES_IN_GAME)) {
            conn.setAutoCommit(false);

            pstmt.setString(1, gameId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    facilities.add(createEntireFacilityObject(rs));
                }
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        //NOSONAR
        return facilities;
    }

    /**
     * A method to retrieve a specific facility from the database.
     *
     * @param gameId     The first part of the identifier to retrieve the specific facility.
     * @param facilityId The second part of the identifier to retrieve the specific facility.
     * @return The retrieved facility from the database.
     */
    public FacilityDB readSpecificFacility(int facilityId, String gameId) {
        FacilityDB facility = null;
        Connection conn = databaseConnection.connect();
        if (conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(READ_SPECIFIC_FACILITY)) {
                conn.setAutoCommit(false);

                pstmt.setInt(1, facilityId);
                pstmt.setString(2, gameId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    rs.next();
                    facility = createFacilityObject(rs, gameId, facilityId);
                }
                conn.commit();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }
        return facility;
    }

    private FacilityDB createEntireFacilityObject(ResultSet rs) throws SQLException{
        return this.createFacilityObject(rs, rs.getString("GameId"), rs.getInt("FacilityId"));
    }

    private FacilityDB createFacilityObject(ResultSet rs, String gameId, int facilityId) throws SQLException {
        return new FacilityDB(gameId, facilityId,
            rs.getString("FacilityName"), rs.getString("PlayerId"),
            rs.getString("GameAgentName"), rs.getBoolean("Bankrupt"));
    }
}
