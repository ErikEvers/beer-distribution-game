package org.han.ica.asd.c;

import org.han.ica.asd.c.dao.GameBusinessRulesDAO;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRules;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BusinessRuleStore implements IBusinessRuleStore {
    private GameAgent temporaryAgent = new GameAgent("", null, null);

    @Inject
    GameBusinessRulesDAO gameBusinessRulesDAO;

    @Override
    public List<String> readInputBusinessRules(String agentName) {
        List<String> businessRules = new ArrayList<>();
        temporaryAgent.setAgentName(agentName);
        List<GameBusinessRules> gameBusinessRules = gameBusinessRulesDAO.readAllGameBusinessRulesForGameAgentInAGame(temporaryAgent);
        for(GameBusinessRules gameBusinessRules1 : gameBusinessRules){
            businessRules.add(gameBusinessRules1.getGameBusinessRule());
        }
        return businessRules;
    }

    @Override
    public void synchronizeBusinessRules(String agentName, Map<String, String> businessRuleMap) {

    }
}
