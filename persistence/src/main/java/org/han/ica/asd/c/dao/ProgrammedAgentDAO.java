package org.han.ica.asd.c.dao;

import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.dao_model.ProgrammedAgentDB;

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
    private static final Logger LOGGER = Logger.getLogger(ProgrammedAgentDAO.class.getName());

    @Inject
    IDatabaseConnection databaseConnection;

    public ProgrammedAgentDAO() {
        //There has to be a constructor to inject the value above.
    }

    /**
     * A method to create a new ProgrammedAgent in the database.
     *
     * @param programmedAgent The model with all the data required to create a new ProgrammedAgent in the database.
     */
    public void createProgrammedAgent(ProgrammedAgentDB programmedAgent) {
        executePreparedStatement(programmedAgent, CREATE_PROGRAMMEDAGENT);
    }

    public void updateProgrammedAgent(ProgrammedAgentDB programmedAgentOld, ProgrammedAgentDB programmedAgentNew) {
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

    /**
     * A method to delete a programmedAgent.
     *
     * @param programmedAgent The data needed to delete a ProgrammedAgent from the database.
     */
    public void deleteProgrammedAgent(ProgrammedAgentDB programmedAgent) {
        executePreparedStatement(programmedAgent, DELETE_PROGRAMMEDAGENT);
    }

    /**
     * A method to read all ProgrammedAgents from the database.
     *
     * @return A list with all the ProgrammedAgents from the database.
     */
    public List<ProgrammedAgentDB> readAllProgrammedAgents () {
        Connection conn;
        List<ProgrammedAgentDB> programmedAgents = new ArrayList<>();
        try {
            conn = databaseConnection.connect();
            if (conn != null) {

                conn.setAutoCommit(false);
                try (PreparedStatement pstmt = conn.prepareStatement(READ_ALL_PROGRAMMEDAGENTS)) {
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            programmedAgents.add(new ProgrammedAgentDB(rs.getString("ProgrammedAgentName")));
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

    /**
     * A method to execute a prepared statement with only one set variable.
     *
     * @param programmedAgent The data needed to know what to create or delete.
     * @param query A string which contains the query that has to be executed on the database.
     */
    private void executePreparedStatement(ProgrammedAgentDB programmedAgent, String query) {
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