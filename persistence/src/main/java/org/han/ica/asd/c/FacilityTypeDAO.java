package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.dao_model.FacilityType;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FacilityTypeDAO implements IBeerDisitributionGameDAO {
    private static final String CREATE_FACILITYTYPE = "INSERT INTO FacilityType Values (?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_FACILITYTYPE = "UPDATE FacilityType SET " +
            "ValueIncomingGoods = ?, ValueOutgoingGoods = ?, StockHoldingCosts = ?, " +
            "OpenOrderCosts = ?, StartingBudget = ?, StartingOrder = ? " +
            "WHERE GameId = ? AND FacilityName = ?;";
    private static final String DELETE_SPECIFIC_FACILITYTYPE = "DELETE FROM FacilityType WHERE GameId = ? AND FacilityName = ?;";
    private static final String DELETE_ALL_FACILITYTYPES_FOR_A_BEERGAME = "DELETE FROM FacilityType WHERE GameId = ?;";
    private static final String READ_FACILITYTYPES_FOR_A_BEERGAME = "SELECT FacilityName, ValueIncomingGoods, ValueOutgoingGoods, " +
            "StockHoldingCosts, OpenOrderCosts, StartingBudget, StartingOrder FROM FacilityType WHERE GameId = ?;";
    private static final String READ_SPECIFIC_FACILITYTYPE = "SELECT ValueIncomingGoods, ValueOutgoingGoods, " +
            "StockHoldingCosts, OpenOrderCosts, StartingBudget, StartingOrder FROM FacilityType WHERE GameId = ? AND FacilityName = ?;";
    private static final Logger LOGGER = Logger.getLogger(FacilityTypeDAO.class.getName());

    @Inject
    private IDatabaseConnection databaseConnection;

    public FacilityTypeDAO() {
        //There has to be a constructor to inject the value above.
    }

    /**
     * A method to insert a new FacilityType in the database.
     *
     * @param facilityType A FacilityType model that contains all the data needed to create a new FacilityType.
     */
    public void createFacilityType(FacilityType facilityType) {
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(CREATE_FACILITYTYPE)) {

                    conn.setAutoCommit(false);

                    pstmt.setString(1, facilityType.getGameId());
                    pstmt.setString(2, facilityType.getFacilityName());
                    pstmt.setInt(3, facilityType.getValueIncomingGoods());
                    pstmt.setInt(4, facilityType.getValueOutgoingGoods());
                    pstmt.setInt(5, facilityType.getStockHoldingCosts());
                    pstmt.setInt(6, facilityType.getOpenOrderCosts());
                    pstmt.setInt(7, facilityType.getStartingBudget());
                    pstmt.setInt(8, facilityType.getStartingOrder());

                    pstmt.executeUpdate();
                }
                conn.commit();
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            databaseConnection.rollBackTransaction(conn);
        }
    }

    /**
     * A method to update an existing FacilityType.
     *
     * @param facilityType A FacilityType model that contains all the data needed to update an existing FacilityType.
     */
    public void updateFacilityType(FacilityType facilityType) {
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_FACILITYTYPE)) {
                    conn.setAutoCommit(false);
                    pstmt.setInt(1, facilityType.getValueIncomingGoods());
                    pstmt.setInt(2, facilityType.getValueOutgoingGoods());
                    pstmt.setInt(3, facilityType.getStockHoldingCosts());
                    pstmt.setInt(4, facilityType.getOpenOrderCosts());
                    pstmt.setInt(5, facilityType.getStartingBudget());
                    pstmt.setInt(6, facilityType.getStartingBudget());
                    pstmt.setString(7, facilityType.getGameId());
                    pstmt.setString(8, facilityType.getFacilityName());

                    pstmt.executeUpdate();
                }
                conn.commit();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    /**
     * A method to delete all FacilityTypes linked to a specific game.
     *
     * @param gameId The identifier of the game from which all FacilityTypes have to be deleted.
     */
    public void deleteAllFacilitytypesForABeergame(String gameId) {
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            try (PreparedStatement pstmt = conn.prepareStatement(DELETE_ALL_FACILITYTYPES_FOR_A_BEERGAME)) {
                conn.setAutoCommit(false);

                pstmt.setString(1, gameId);

                pstmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    /**
     * A method to delete a specific FacilityType within a specific game.
     *
     * @param gameId       The first part of the identifier of the FacilityType from witch a FacilityTypes has to be deleted.
     * @param facilityName The second part of the identifier of the FacilityType from witch a FacilityTypes has to be deleted.
     */
    public void deleteSpecificFacilityType(String gameId, String facilityName) {
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(DELETE_SPECIFIC_FACILITYTYPE)) {
                    conn.setAutoCommit(false);

                    pstmt.setString(1, gameId);
                    pstmt.setString(2, facilityName);

                    pstmt.executeUpdate();
                }
                conn.commit();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            databaseConnection.rollBackTransaction(conn);
        }
    }

    /**
     * A method to retrieve all the FacilityTypes from a specific game.
     *
     * @param gameId The identifier of a game from witch the FacilityTypes need to be retrieved.
     * @return A list of FacilityTypes from a specific game.
     */
    public List<FacilityType> readAllFacilityTypes(String gameId) {
        Connection conn;
        ArrayList<FacilityType> types = new ArrayList<>();
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                conn.setAutoCommit(false);
                try (PreparedStatement pstmt = conn.prepareStatement(READ_FACILITYTYPES_FOR_A_BEERGAME)) {
                    pstmt.setString(1, gameId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            types.add(new FacilityType(gameId,
                                    rs.getString("FacilityName"), rs.getInt("ValueIncomingGoods"), rs.getInt("ValueOutgoingGoods"),
                                    rs.getInt("StockholdingCosts"), rs.getInt("OpenOrderCosts"),
                                    rs.getInt("StartingBudget"), rs.getInt("StartingOrder")));
                        }
                    }
                    conn.commit();
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return types;
    }

    public FacilityType readSpecificFacilityType(String gameId, String facilityName) {
        Connection conn = null;
        FacilityType type = null;
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                conn.setAutoCommit(false);

                try (PreparedStatement pstmt = conn.prepareStatement(READ_SPECIFIC_FACILITYTYPE)) {
                    pstmt.setString(1, gameId);
                    pstmt.setString(2, facilityName);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        type = new FacilityType(facilityName, gameId, rs.getInt("ValueIncomingGoods"),
                                rs.getInt("ValueOutgoingGoods"), rs.getInt("StockholdingCosts"),
                                rs.getInt("OpenOrderCosts"), rs.getInt("StartingBudget"),
                                rs.getInt("StartingOrder"));
                    }
                }
                conn.commit();
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return type;
    }
}