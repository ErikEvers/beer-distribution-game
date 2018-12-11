package org.han.ica.asd.c.dao;

import org.han.ica.asd.c.model.Facility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.han.ica.asd.c.dbconnection.DBConnection.RollBackTransaction;
import static org.han.ica.asd.c.dbconnection.DBConnection.connect;

public class FacilityDAO {
    //CRUD - Create, read, update, delete
    private static final String CREATE_FACILITY = "INSERT INTO Facility(GameName, GameDate, GameEndDate, FacilityId, FacilityType, IpAddress, GameAgentName) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String READ_FACILITY = "SELECT * FROM Facility WHERE GameName = ? AND GameDate = ? AND GameEndDate = ? AND FacilityId = ?";
    private static final String UPDATE_FACILITY = "";
    private static final String DELETE_FACILITY = "DELETE FROM Facility WHERE GameName = ? AND GameDate = ? AND GameEndDate = ? AND FacilityId = ?";

    public void createFacility() {
        Connection conn = null;
        try {
            conn = connect();
            if (conn == null) return;
            try (PreparedStatement pstmt = conn.prepareStatement(CREATE_FACILITY)) {

                conn.setAutoCommit(false);

                //pstmt
            }
            conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            RollBackTransaction(conn);
        }
    }

    public ArrayList<Facility> readFacilities() {
        return null;
    }

    public void updateFacility(){

    }

    public void deleteFacility(){

    }
}
