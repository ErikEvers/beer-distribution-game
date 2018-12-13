package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.DatabaseConnection;
import org.han.ica.asd.c.model.Facility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FacilityDAO implements IBeerDisitributionGameDAO {
    private static final String CREATE_FACILITY = "INSERT INTO Facility (FacilityId, Gameid, GameAgentName, PlayerId, FacilityName, Bankrupt) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_FACILITY = "UPDATE Facility SET GameAgentName = ?, PlayerId = ?, FacilityName = ?, Bankrupt = ? WHERE FacilityId = ? AND GameId = ?;";
    private static final String DELETE_SPECIFIC_FACILITY = "DELETE FROM Facility WHERE FacilityId = ? AND GameId = ?;";
    private static final String DELETE_ALL_FACILITIES_IN_GAME = "DELETE FROM Facility WHERE GameId = ?;";
    private static final String READ_ALL_FACILITIES_IN_GAME = "SELECT (FacilityId, GameAgentNmae, PlayerId, FacilityName, Bankrupt) FROM Facility WHERE GameId = ?;";
    private static final String READ_SPECIFIC_FACILITY = "SELECT (GameAgentName, PlayerId, FacilityName, Bankrupt) FROM Facility WHERE FacilityId = ? AND GameId = ?;";

    public static final Logger LOGGER = Logger.getLogger(FacilityDAO.class.getName());
    private DatabaseConnection databaseConnection;

    /**
     * A method to create a new facility.
     *
     * @param facility A model of Facility with all the data required to create a new facility.
     */
    public void createFacility(Facility facility) {
        Connection conn;
        try {
            conn = databaseConnection.connect();
            try (PreparedStatement pstmt = conn.prepareStatement(CREATE_FACILITY)) {

                pstmt.setInt(1, facility.getFacilityId());
                pstmt.setString(2, facility.getGameId());
                pstmt.setString(3, facility.getGameAgentName());
                pstmt.setString(4, facility.getPlayerId());
                pstmt.setString(5, facility.getFacilityType().getFacilityName());
                pstmt.setBoolean(6, facility.getBankrupt());

                pstmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    /**
     * A method to update an existing facility within a specific game.
     *
     * @param facility A model of Facility with all the data required to update an existing facility.
     */
    public void updateFacility(Facility facility) {
        Connection conn;
        try {
            conn = databaseConnection.connect();
            try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_FACILITY)) {

                pstmt.setString(1, facility.getGameAgentName());
                pstmt.setString(2, facility.getPlayerId());
                pstmt.setString(3, facility.getFacilityType().getFacilityName());
                pstmt.setBoolean(4, facility.getBankrupt());
                pstmt.setInt(5, facility.getFacilityId());
                pstmt.setString(6, facility.getGameId());

                pstmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    /**
     * A method to delete a specific facility within a specific game.
     *
     * @param faciltyId The first part of the specifications to delete the specific facility.
     * @param gameId The second part of the specifications to delete the specific facility.
     */
    public void deleteSpecificFacility(int faciltyId, String gameId) {
        Connection conn;
        try {
            conn = databaseConnection.connect();
            try (PreparedStatement pstmt = conn.prepareStatement(DELETE_SPECIFIC_FACILITY)) {

                pstmt.setInt(1, faciltyId);
                pstmt.setString(2, gameId);

                pstmt.executeUpdate();
            }
            conn.commit();
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
        Connection conn;
        try {
            conn = databaseConnection.connect();
            try (PreparedStatement pstmt = conn.prepareStatement(DELETE_ALL_FACILITIES_IN_GAME)) {

                pstmt.setString(1, gameId);

                pstmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    public List<Facility> readAllFacilitiesInGame(String gameId) {
        Connection conn;
        ArrayList<Facility> facilities = new ArrayList<>();
        try {
            conn = databaseConnection.connect();
            try (PreparedStatement pstmt = conn.prepareStatement(READ_ALL_FACILITIES_IN_GAME)) {
                pstmt.setString(1, gameId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        facilities.add(new Facility(rs.getInt("FacilityId"), rs.getString("GameId"),
                                rs.getString("GameAgentName"), rs.getString("PlayerId"),
                                rs.getString("FacilityName"), rs.getBoolean("Bankrupt"));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        }
        return facilities;
    }
}
