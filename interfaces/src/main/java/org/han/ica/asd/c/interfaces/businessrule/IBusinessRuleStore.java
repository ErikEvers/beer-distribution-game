package org.han.ica.asd.c.interfaces.businessrule;

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
}
