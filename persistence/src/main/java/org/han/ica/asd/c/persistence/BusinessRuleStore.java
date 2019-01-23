package org.han.ica.asd.c.persistence;

import org.han.ica.asd.c.dao.FacilityDAO;
import org.han.ica.asd.c.dao.ProgrammedAgentDAO;
import org.han.ica.asd.c.dao.ProgrammedBusinessRulesDAO;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;
import org.han.ica.asd.c.model.domain_objects.ProgrammedBusinessRules;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BusinessRuleStore implements IBusinessRuleStore {
    private ProgrammedBusinessRules programmedBusinessRule = new ProgrammedBusinessRules(null, null);
    private List<Facility> facilitiesInGame = new ArrayList<>();
    private List<String> factoryList = new ArrayList<>();
    private List<String> regionalWarehouseList = new ArrayList<>();
    private List<String> wholesalerList = new ArrayList<>();
    private List<String> retailerList = new ArrayList<>();
    private List<String> defaultList = new ArrayList<>();


    @Inject
    ProgrammedBusinessRulesDAO programmedBusinessRulesDao;

    @Inject
    ProgrammedAgentDAO programmedAgentDAO;

    @Inject
    FacilityDAO facilityDAO;


    /**
     * @inheritDoc
     */
    @Override
    public synchronized List<String> readInputBusinessRules(String agentName) {
        List<String> returnBusinessRules = new ArrayList<>();
        List<ProgrammedBusinessRules> businessRules = programmedBusinessRulesDao.readAllProgrammedBusinessRulesFromAProgrammedAgent(agentName);

        businessRules.forEach((businessRule) -> returnBusinessRules.add(businessRule.getProgrammedBusinessRule()));

        return returnBusinessRules;
    }

    /**
     * @inheritDoc
     */
    @Override
    public synchronized void synchronizeBusinessRules(String agentName, Map<String, String> businessRuleMap) {
        if(!this.getAllProgrammedAgents().contains(agentName)) {
            programmedAgentDAO.createProgrammedAgent(new ProgrammedAgent(agentName, null));
        }

        programmedBusinessRulesDao.deleteAllProgrammedBusinessRulesForAProgrammedAgent(agentName);

        businessRuleMap.forEach((key, value) -> {
            programmedBusinessRule.setProgrammedBusinessRule(key);
            programmedBusinessRule.setProgrammedAST(value);
            programmedBusinessRulesDao.createProgrammedbusinessRule(programmedBusinessRule, agentName);
        });
    }

    /**
     * @inheritDoc
     */
    @Override
    public synchronized List<List<String>> getAllFacilities() {
        List<List<String>> returnList = new ArrayList<>();
        facilitiesInGame = facilityDAO.readAllFacilitiesInGame();

        switchCase();

        if(!factoryList.isEmpty()) {
            returnList.add(factoryList);
        }
        if(!regionalWarehouseList.isEmpty()) {
            returnList.add(regionalWarehouseList);
        }
        if(!wholesalerList.isEmpty()) {
            returnList.add(wholesalerList);
        }
        if(!retailerList.isEmpty()) {
            returnList.add(retailerList);
        }
        if(!defaultList.isEmpty()) {
            returnList.add(defaultList);
        }
        return returnList;
    }

    /**
     * A method that sees to what type a facility in a game belongs.
     */
    private synchronized void switchCase() {
        for (Facility facility: facilitiesInGame) {
            switch (facility.getFacilityType().getFacilityName()) {
                case "Factory":
                    factoryList.add(Integer.toString(facility.getFacilityId()));
                    break;
                case "Regional Warehouse":
                    regionalWarehouseList.add(Integer.toString(facility.getFacilityId()));
                    break;
                case "Wholesaler":
                    wholesalerList.add(Integer.toString(facility.getFacilityId()));
                    break;
                case "Retailer":
                    retailerList.add(Integer.toString(facility.getFacilityId()));
                    break;
                default:
                    defaultList.add(Integer.toString(facility.getFacilityId()));
            }
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public synchronized List<String> getAllProgrammedAgents() {
        List<String> programmedAgentNames = new ArrayList<>();

        List<ProgrammedAgent> dbProgrammedAgents = programmedAgentDAO.readAllProgrammedAgents();

        dbProgrammedAgents.forEach((programmedAgent) -> programmedAgentNames.add(programmedAgent.getProgrammedAgentName()));

        return programmedAgentNames;
    }

    /**
     * @inheritDoc
     */
    @Override
    public synchronized void deleteProgrammedAgent(String agentName) {
        ProgrammedAgent programmedAgent = new ProgrammedAgent(agentName, null);
        programmedBusinessRulesDao.deleteAllProgrammedBusinessRulesForAProgrammedAgent(agentName);
        programmedAgentDAO.deleteProgrammedAgent(programmedAgent);
    }
    /**
     * @inheritDoc
     */
    @Override
    public synchronized ProgrammedAgent getProgrammedGameAgent(String agentName) {
        List<ProgrammedBusinessRules> programmedBusinessRulesList = programmedBusinessRulesDao.readAllProgrammedBusinessRulesFromAProgrammedAgent(agentName);
        return new ProgrammedAgent(agentName, programmedBusinessRulesList);
    }
}
