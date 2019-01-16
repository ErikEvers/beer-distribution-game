package org.han.ica.asd.c.interfaces.businessrule;

import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;

import java.util.List;
import java.util.Map;

public interface IBusinessRuleStore {
    /**
     * Retrieves business rules from the database based on the given agentName.
     *
     * @param agentName the agentName for whom the business rules has to be retrieved
     * @return Returns a list of business rules as Strings
     */
    List<String> readInputBusinessRules(String agentName);

    /**
     * Synchronizes the given business rules and the string representation of their ASTs
     *
     * @param agentName the agentName for whom the business rules has to be synchronized
     * @param businessRuleMap the business rules which has to be synchronized
     */
    void synchronizeBusinessRules(String agentName, Map<String, String> businessRuleMap);

    /**
     * Gathers all facilityId's in a two-dimensional list. Each list in the two-dimensional list represents a facility type e.g. factory. The list with id's has to be sorted the same way they are represented in the game.
     *
     * @return Returns a two-dimensional list with all facilityId's sorted by facility type.
     */
    List<List<String>> getAllFacilities();

    /**
     * Gets all the agents from the database.
     *
     * @return Returns a list with all the agents programmed.
     */
    List<String> getAllProgrammedAgents();

    /**
     * Deletes a programmed agent with its name.
     *
     * @param agentName The name of a agent.
     */
    void deleteProgrammedAgent(String agentName);

    /**
     * Gets the programmedAgent belonging to the given agentName
     *
     * @param agentName the name of the agent to get
     * @return the programmedAgent
     */
    ProgrammedAgent getProgrammedGameAgent(String agentName);
}
