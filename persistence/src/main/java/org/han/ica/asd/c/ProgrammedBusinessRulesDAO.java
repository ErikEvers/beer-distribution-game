package org.han.ica.asd.c;

import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.ProgrammedBusinessRules;

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

    public ProgrammedBusinessRulesDAO(){
        //Empty Constructor for GUICE
    }

    public void createProgrammedbusinessrule(ProgrammedBusinessRules programmedBusinessRules){
        Connection conn = null;
        try {
            conn = databaseConnection.connect();
            if(conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(CREATE_PROGRAMMEDBUSINESSRULE)) {
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
