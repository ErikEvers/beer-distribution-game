package org.han.ica.asd.c;

import org.han.ica.asd.c.dao.ProgrammedAgentDAO;
import org.han.ica.asd.c.dao.ProgrammedBusinessRulesDAO;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;
import org.han.ica.asd.c.model.domain_objects.ProgrammedBusinessRules;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BusinessRuleStore implements IBusinessRuleStore {
    ProgrammedBusinessRules programmedBusinessRule = new ProgrammedBusinessRules(null, null);

    @Inject
    ProgrammedBusinessRulesDAO programmedBusinessRulesDao;

    @Inject
    ProgrammedAgentDAO programmedAgentDAO;

    @Override
    public List<String> readInputBusinessRules(String agentName) {
        List<String> returnBusinessRules = new ArrayList<>();
        List<ProgrammedBusinessRules> businessRules = programmedBusinessRulesDao.readAllProgrammedBusinessRulesFromAProgrammedAgent(agentName);
        for(ProgrammedBusinessRules businessRules1 : businessRules){
            returnBusinessRules.add(businessRules1.getProgrammedBusinessRule());
        }
        return returnBusinessRules;
    }

    @Override
    public void synchronizeBusinessRules(String agentName, Map<String, String> businessRuleMap) {
        programmedBusinessRulesDao.deleteAllProgrammedBusinessRulesForAProgrammedAgent(agentName);

        for (Map.Entry<String, String> businessRule : businessRuleMap.entrySet()){
            programmedBusinessRule.setProgrammedBusinessRule(businessRule.getKey());
            programmedBusinessRule.setProgrammedAST(businessRule.getValue());
            programmedBusinessRulesDao.createProgrammedbusinessRule(programmedBusinessRule, agentName);
        }
    }

    @Override
    public List<String> getAllProgrammedAgents() {
        List<String> programmedAgentNames = null;

        List<ProgrammedAgent> dbProgrammedAgents = programmedAgentDAO.readAllProgrammedAgents();

        for (ProgrammedAgent programmedAgent : dbProgrammedAgents){
            programmedAgentNames.add(programmedAgent.getProgrammedAgentName());
        }
        return programmedAgentNames;
    }

    @Override
    public void deleteProgrammedAgent(String agentName) {
        ProgrammedAgent programmedAgent = new ProgrammedAgent(agentName, null);
        programmedAgentDAO.deleteProgrammedAgent(programmedAgent);
    }
}
