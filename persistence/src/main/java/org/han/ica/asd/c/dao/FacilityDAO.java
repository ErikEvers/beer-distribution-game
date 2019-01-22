package org.han.ica.asd.c.dao;

import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.exception.GameIdNotSetException;
import org.han.ica.asd.c.model.domain_objects.Facility;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FacilityDAO {
    private static final String CREATE_FACILITY = "INSERT INTO Facility VALUES (?, ?, ?)";
    private static final String UPDATE_FACILITY = "UPDATE Facility SET FacilityName = ? WHERE FacilityId = ? AND GameId = ?;";
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
    public synchronized void createFacility(Facility facility) {
        Connection conn = databaseConnection.connect();
        if (conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(CREATE_FACILITY)) {
                conn.setAutoCommit(false);

                pstmt.setInt(1, facility.getFacilityId());
                DaoConfig.gameIdNotSetCheck(pstmt, 2);
                pstmt.setString(3, facility.getFacilityType().getFacilityName());

                pstmt.executeUpdate();
                conn.commit();
            } catch (GameIdNotSetException | SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
								databaseConnection.rollBackTransaction(conn);
            }
        }
    }

    /**
     * A method to update an existing facility within a specific game.
     *
     * @param facility A domain_objects of FacilityDB with all the data required to update an existing facility.
     */
    public synchronized void updateFacility(Facility facility) {
        Connection conn = databaseConnection.connect();
				if (conn != null) {
						try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_FACILITY)) {
								conn.setAutoCommit(false);

								pstmt.setString(1, facility.getFacilityType().getFacilityName());
								pstmt.setInt(2, facility.getFacilityId());
								DaoConfig.gameIdNotSetCheck(pstmt, 3);


								pstmt.executeUpdate();
								conn.commit();
						} catch (GameIdNotSetException | SQLException e) {
							LOGGER.log(Level.SEVERE, e.toString(), e);
							databaseConnection.rollBackTransaction(conn);
						}
				}
    }

    /**
     * A method to delete a specific facility within a specific game.
     *
     * @param facility The data required to delete a specific facility.
     */
    public synchronized void deleteSpecificFacility(Facility facility) {
        Connection conn = databaseConnection.connect();
				if (conn != null) {
						try (PreparedStatement pstmt = conn.prepareStatement(DELETE_SPECIFIC_FACILITY)) {
								conn.setAutoCommit(false);

								pstmt.setInt(1, facility.getFacilityId());
								DaoConfig.gameIdNotSetCheck(pstmt, 2);

								pstmt.executeUpdate();
								conn.commit();
						} catch (GameIdNotSetException | SQLException e) {
							LOGGER.log(Level.SEVERE, e.toString(), e);
							databaseConnection.rollBackTransaction(conn);
						}
				}

    }

    /**
     * A method to delete all facilities within a specific game.
     */
    public synchronized void deleteAllFacilitiesInGame() {
        Connection conn = databaseConnection.connect();
				if (conn != null) {
						try (PreparedStatement pstmt = conn.prepareStatement(DELETE_ALL_FACILITIES_IN_GAME)) {
								conn.setAutoCommit(false);

								DaoConfig.gameIdNotSetCheck(pstmt, 1);

								pstmt.executeUpdate();
								conn.commit();
						} catch (GameIdNotSetException | SQLException e) {
							LOGGER.log(Level.SEVERE, e.toString(), e);
							databaseConnection.rollBackTransaction(conn);
						}
        }
    }


    /**
     * A method to get all facilities within a specific game.
     *
     * @return The facilities that have been retrieved from the database.
     */
    public synchronized List<Facility> readAllFacilitiesInGame() {
        Connection conn = databaseConnection.connect();
        List<Facility> facilities = new ArrayList<>();
				try (PreparedStatement pstmt = conn.prepareStatement(READ_ALL_FACILITIES_IN_GAME)) {
						conn.setAutoCommit(false);

						DaoConfig.gameIdNotSetCheck(pstmt, 1);

						try (ResultSet rs = pstmt.executeQuery()) {
								while (rs.next()) {
										facilities.add(new Facility(facilityTypeDAO.readSpecificFacilityType(rs.getString("FacilityName")),
														rs.getInt("FacilityId")));
								}
						}
						conn.commit();
				} catch (GameIdNotSetException | SQLException e) {
					LOGGER.log(Level.SEVERE, e.toString(), e);
					databaseConnection.rollBackTransaction(conn);
				}
        //NOSONAR
        return facilities;
    }

    /**
     * A method to retrieve a specific facility from the database.
     *
     * @param facilityId The data required to retrieve a specific Facility.
     * @return The retrieved facility from the database.
     */
    public synchronized Facility readSpecificFacility(int facilityId) {
    	if(facilityId == 0) {
    		return null;
		}
        Connection conn = databaseConnection.connect();
        Facility facilityFromDb = null;
				if (conn != null) {
						try (PreparedStatement pstmt = conn.prepareStatement(READ_SPECIFIC_FACILITY)) {
								conn.setAutoCommit(false);

								pstmt.setInt(1, facilityId);
								DaoConfig.gameIdNotSetCheck(pstmt, 2);

								try (ResultSet rs = pstmt.executeQuery()) {
									if(!rs.isClosed()) {
										facilityFromDb = new Facility(facilityTypeDAO.readSpecificFacilityType(rs.getString("FacilityName")),
												rs.getInt("FacilityId"));
									}
								}
								conn.commit();
						} catch (GameIdNotSetException | SQLException e) {
							LOGGER.log(Level.SEVERE, e.toString(), e);
							databaseConnection.rollBackTransaction(conn);
						}
        }
        return facilityFromDb;
    }

}
