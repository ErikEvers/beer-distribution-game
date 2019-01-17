package org.han.ica.asd.c.dao;

import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.model.domain_objects.ProgrammedBusinessRules;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

//It is not possible to update a BusinessRule. To update a BusinessRule you must delete all the existing ones and insert the new BusinessRules.
public class ProgrammedBusinessRulesDAO {
    private static final String CREATE_PROGRAMMEDBUSINESSRULE = "INSERT INTO ProgrammedBusinessRules VALUES (?,?,?);";
    private static final String DELETE_SPECIFIC_PROGRAMMEDBUSINESSRULE = "DELETE FROM ProgrammedBusinessRules WHERE ProgrammedAgentName = ? AND ProgrammedBusinessRule = ? AND ProgrammedAST = ?;";
    private static final String DELETE_ALL_PROGRAMMEDBUSINESSRULES_FOR_A_PROGRAMMEDAGENT = "DELETE FROM ProgrammedBusinessRules WHERE ProgrammedAgentName = ?";
    private static final String READ_ALL_PROGRAMMEDBUSINESSRULES_FOR_A_PROGRAMMEDAGENT = "SELECT * FROM ProgrammedBusinessRules WHERE ProgrammedAgentName = ?";
    private static final Logger LOGGER = Logger.getLogger(ProgrammedBusinessRulesDAO.class.getName());

    @Inject
    private IDatabaseConnection databaseConnection;

    public ProgrammedBusinessRulesDAO() {
        //Empty Constructor for GUICE
    }

    /**
     * A method to create a new ProgrammedBusinessRule in the database
     *
     * @param programmedBusinessRules The data required to create a new ProgrammedBusinessRule in the database.
     * @param programmedAgentName The data required to know from which ProgrammedAgent the data needs to be deleted.
     */
    public void createProgrammedbusinessRule(ProgrammedBusinessRules programmedBusinessRules, String programmedAgentName) {
        executePreparedStatement(programmedBusinessRules, programmedAgentName, CREATE_PROGRAMMEDBUSINESSRULE);
    }

    /**
     * A method to delete a specific ProgrammedBusinessRule from the database.
     *
     * @param programmedBusinessRules The data required to delete a specific ProgrammedBusinessRule from the database.
     * @param programmedAgentName The data required to know from which ProgrammedAgent the data needs to be deleted.
     */
    public void deleteSpecificProgrammedBusinessRule(ProgrammedBusinessRules programmedBusinessRules, String programmedAgentName) {
        executePreparedStatement(programmedBusinessRules, programmedAgentName, DELETE_SPECIFIC_PROGRAMMEDBUSINESSRULE);

    }

    /**
     * A method to delete all ProgrammedBusinessRules from a ProgrammedAgent.
     *
     * @param programmedAgentName The data required to delete all the ProgrammedBusinessRules from a specific ProgrammedAgent.
     */
    public void deleteAllProgrammedBusinessRulesForAProgrammedAgent(String programmedAgentName) {
        Connection conn = databaseConnection.connect();
        if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(DELETE_ALL_PROGRAMMEDBUSINESSRULES_FOR_A_PROGRAMMEDAGENT)) {
                    conn.setAutoCommit(false);

                    pstmt.setString(1, programmedAgentName);

                pstmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                databaseConnection.rollBackTransaction(conn);
            }
        }
    }

    /**
     * A method to retrieve all ProgrammedBusinessRules from a ProgrammedAgent from the database.
     *
     * @param programmedAgentName The ProgrammedAgent from whom the ProgrammedBusinessRules have to be retrieved.
     * @return A list from all the ProgrammedBusinessRules from the ProgrammedAgent.
     */
    public List<ProgrammedBusinessRules> readAllProgrammedBusinessRulesFromAProgrammedAgent(String programmedAgentName) {
        Connection conn = databaseConnection.connect();
        List<ProgrammedBusinessRules> programmedBusinessRules = new ArrayList<>();
        if (conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(READ_ALL_PROGRAMMEDBUSINESSRULES_FOR_A_PROGRAMMEDAGENT)) {

                conn.setAutoCommit(false);

                    pstmt.setString(1, programmedAgentName);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        programmedBusinessRules.add(new ProgrammedBusinessRules(rs.getString("ProgrammedBusinessRule"),
                                rs.getString("ProgrammedAST")));
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                databaseConnection.rollBackTransaction(conn);
            }
        }

        return removeSortingIndex(programmedBusinessRules);
    }


    /**
     * A method to execute the prepared statement with all the required data to create or delete a specific ProgrammedBusinessRule.
     *
     * @param programmedBusinessRules The data that is required to execute the prepared statement.
     * @param programmedAgentName     The data required to know for which ProgrammedAgent the query has to be executed.
     * @param query                   The query that needs to be executed on the database.
     */
    private void executePreparedStatement(ProgrammedBusinessRules programmedBusinessRules, String programmedAgentName, String query) {
        Connection conn = databaseConnection.connect();
        if (conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                conn.setAutoCommit(false);

                    pstmt.setString(1, programmedAgentName);
                    pstmt.setString(2, programmedBusinessRules.getProgrammedBusinessRule());
                    pstmt.setString(3, programmedBusinessRules.getProgrammedAST());

                pstmt.executeUpdate();
                conn.commit();

            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                databaseConnection.rollBackTransaction(conn);
            }
        }
    }

    /**
     * A method that removes the sorting index from all business rules it is given.
     *
     * @param businessRules Business rules that it needs to remove the sorting index (e.g. 1.) from
     * @return Returns the Business rules without the sorting index
     */
    private List<ProgrammedBusinessRules> removeSortingIndex(List<ProgrammedBusinessRules> businessRules){
        Map<Integer, String> map = new HashMap<>();
        List<ProgrammedBusinessRules> returnBusinessRule = new ArrayList<>();

        for (ProgrammedBusinessRules businessRule : businessRules) {
            String[] strSplit = businessRule.getProgrammedBusinessRule().split(" ", 2);
            map.put(Integer.parseInt(strSplit[0]), strSplit[1]);
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                fillAndSet(entry.getKey(), new ProgrammedBusinessRules(entry.getValue(),businessRule.getProgrammedAST()), returnBusinessRule);
            }
        }

        returnBusinessRule.removeAll(Collections.singleton(null));
        return returnBusinessRule;
    }

    /**
     * A method that adds the object to a specified index in the list.
     *
     * @param index Index that it needs to set the object on.
     * @param object Object that has to be inserted in the list
     * @param list List that Object is inserted in.
     * @param <T> Generic Object that list consists of.
     */
    private static <T> void fillAndSet(int index, T object, List<T> list) {
        if (index > (list.size() - 1)) {
            for (int i = list.size(); i < index; i++) {
                list.add(null);
            }
            list.add(object);
        }
        else {
            list.set(index, object);
        }
    }
}
