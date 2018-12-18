package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.dao_model.ProgrammedBusinessRules;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgrammedBusinessRulesDAO implements IBeerDisitributionGameDAO {
    private static final String CREATE_PROGRAMMEDBUSINESSRULE = "INSERT INTO ProgrammedBusinessRules VALUES (?,?,?);";
    private static final String UPDATE_PROGRAMMEDBUSINESSRULE = "Dit is niet mogelijk. Om een business rule te updaten in de database moeten eerst alle oude business rules verwijderd worden.";
    private static final String DELETE_SPECIFIC_PROGRAMMEDBUSINESSRULE = "DELETE FROM ProgrammedBusinessRules WHERE ProgrammedAgentName = ? AND ProgrammedBusinessRule = ? AND ProgrammedAST = ?;";
    private static final String DELETE_ALL_PROGRAMMEDBUSINESSRULES_FOR_A_PROGRAMMEDAGENT = "DELETE FROM ProgrammedBusinessRules WHERE ProgrammedAgentName = ?";
    private static final String READ_ALL_PROGRAMMEDBUSINESSRULES_FOR_A_PROGRAMMEDAGENT = "SELECT * FROM ProgrammedBusinessRules WHERE ProgrammedAgentName = ?";
    private static final Logger LOGGER = Logger.getLogger(FacilityTypeDAO.class.getName());

    @Inject
    private IDatabaseConnection databaseConnection;

    public ProgrammedBusinessRulesDAO() {
        //Empty Constructor for GUICE
    }

    /**
     * A method to create a new ProgrammedBusinessRule in the database
     *
     * @param programmedBusinessRules The data required to create a new ProgrammedBusinessRule in the database.
     */
    public void createProgrammedbusinessRule(ProgrammedBusinessRules programmedBusinessRules) {
        executePreparedStatement(programmedBusinessRules, CREATE_PROGRAMMEDBUSINESSRULE);
    }

    /**
     * A method to delete a specific ProgrammedBusinessRule from the database.
     *
     * @param programmedBusinessRules The data required to delete a specific ProgrammedBusinessRule from the database.
     */
    public void deleteSpecificProgrammedBusinessRule(ProgrammedBusinessRules programmedBusinessRules) {
        executePreparedStatement(programmedBusinessRules, DELETE_SPECIFIC_PROGRAMMEDBUSINESSRULE);

    }

    /**
     * A method to execute the prepared statement with all the required data to create or delete a specific ProgrammedBusinessRule.
     *
     * @param programmedBusinessRules The data that is required to execute the prepared statement.
     * @param query The query that needs to be executed on the database.
     */
    private void executePreparedStatement(ProgrammedBusinessRules programmedBusinessRules, String query) {
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    conn.setAutoCommit(false);

                    pstmt.setString(1, programmedBusinessRules.getProgrammedAgentName());
                    pstmt.setString(2, programmedBusinessRules.getProgrammedBusinessRule());
                    pstmt.setString(3, programmedBusinessRules.getProgrammedAST());

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
