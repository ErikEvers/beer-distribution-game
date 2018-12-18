package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.DatabaseConnection;
import org.han.ica.asd.c.model.ProgrammedAgent;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgrammedAgentDAO implements IBeerDisitributionGameDAO {
    private static final String CREATE_PROGRAMMEDAGENT = "INSERT INTO ProgrammedAgent VALUES (?)";
    private static final String UPDATE_PROGRAMMEDAGENT = "UPDATE ProgrammedAgent SET ProgrammedAgentName = ? WHERE ProgrammedAgentName = ?";
    private static final String DELETE_PROGRAMMEDAGENT = "DELETE FROM ProgrammedAgent WHERE ProgrammedAgentName = ?";
    private static final String READ_ALL_PROGRAMMEDAGENTS = "SELECT * FROM ProgrammedAgent";
    private static final Logger LOGGER = Logger.getLogger(FacilityTypeDAO.class.getName());

    @Inject
    DatabaseConnection databaseConnection;

    public ProgrammedAgentDAO() {
        //There has to be a constructor to inject the value above.
    }

    public void createProgrammedAgent(ProgrammedAgent programmedAgent) {
        executePreparedStatement(programmedAgent, CREATE_PROGRAMMEDAGENT);
    }

    public void updateProgrammedAgent(ProgrammedAgent programmedAgentOld, ProgrammedAgent programmedAgentNew) {
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_PROGRAMMEDAGENT)) {

                    conn.setAutoCommit(false);

                    pstmt.setString(1, programmedAgentNew.getProgrammedAgentName());
                    pstmt.setString(2, programmedAgentOld.getProgrammedAgentName());


                    pstmt.executeUpdate();
                }
                conn.commit();
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            databaseConnection.rollBackTransaction(conn);
        }
    }

    public void deleteProgrammedAgent(ProgrammedAgent programmedAgent) {
        executePreparedStatement(programmedAgent, DELETE_PROGRAMMEDAGENT);
    }

    public List<ProgrammedAgent> readAllProgrammedAgents () {
        Connection conn;
        ArrayList<ProgrammedAgent> programmedAgents = new ArrayList<>();
        try {
            conn = databaseConnection.connect();
            if (conn != null) {

                conn.setAutoCommit(false);
                try (PreparedStatement pstmt = conn.prepareStatement(READ_ALL_PROGRAMMEDAGENTS)) {
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            programmedAgents.add(new ProgrammedAgent(rs.getString("ProgrammedAgentName")));
                        }
                    }
                    conn.commit();
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return programmedAgents;
    }

    private void executePreparedStatement(ProgrammedAgent programmedAgent, String query) {
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {

                    conn.setAutoCommit(false);

                    pstmt.setString(1, programmedAgent.getProgrammedAgentName());

                    pstmt.executeUpdate();
                }
                conn.commit();
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            databaseConnection.rollBackTransaction(conn);
        }
    }
}
