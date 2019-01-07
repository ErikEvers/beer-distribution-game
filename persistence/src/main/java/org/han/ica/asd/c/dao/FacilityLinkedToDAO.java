package org.han.ica.asd.c.dao;

import com.google.inject.Inject;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.Facility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FacilityLinkedToDAO {
    private static final String GET_LOWER_LINKED_FACILITIES= "SELECT flt.FacilityIdOrdering FROM FacilityLinkedTo flt WHERE flt.GameId = ? AND flt.FacilityIdOrdering = ?";
    private static final Logger LOGGER = Logger.getLogger(FacilityLinkedToDAO.class.getName());

    @Inject
    private IDatabaseConnection databaseConnection;

    @Inject
    private FacilityDAO facilityDAO;

    public FacilityLinkedToDAO () {
        // Empty for GUICE
    }

    /**
     * Get the facilities the main facility can deliver to
     * @param gameId
     * The game from which the linked facilities need to be retrieved
     * @param facilityId
     * The facility for which the facilities lower in the chain need to be retrieved
     * @return
     * The facilities that are linked to the given facilityId
     */
    public List<Facility> getLowerLinkedFacilities(String gameId, int facilityId) {
        Connection conn = databaseConnection.connect();
        List<Facility> facilities = new ArrayList<>();
        if (conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(GET_LOWER_LINKED_FACILITIES)) {
                pstmt.setString(1, gameId);
                pstmt.setInt(2, facilityId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.isClosed()) {
                        rs.next();
                        facilities.add(facilityDAO.readSpecificFacility(rs.getInt("FacilityIdDelivering")));
                    }
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }
        return facilities;
    }

}
