package org.han.ica.asd.c.model.domain_objects;

import org.han.ica.asd.c.model.pojo.GameAgentAction;

import java.util.*;

public class GameAgent {
    private String gameAgentName;
    private int facilityId;
    private List<GameBusinessRules> gameBusinessRulesList = new ArrayList<>();
    Map<String, GameAgentAction> mapActionOfBusinessRule = new HashMap<>();

    public GameAgent(String gameAgentName, int facilityId) {
        this.gameAgentName = gameAgentName;
        this.facilityId = facilityId;
    }

    public String getGameAgentName() {
        return gameAgentName;
    }

    public void setGameAgentName(String gameAgentName) {
        this.gameAgentName = gameAgentName;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public void generateOrder() {
        gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 and inventory lower than 40 then order 30", "awesomerepresentationofthetreeasastring"));
        gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 20 then order 30", "awesomerepresentationofthetreeasastring2"));
        gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 20 then order 30", "awesomerepresentationofthetreeasastring3"));

        for (GameBusinessRules gameBusinessRules : gameBusinessRulesList) {
            GameAgentAction action = gameBusinessRules.retrieveAction(null);
            if (!action.getType().equals("Empty")){
                this.mapActionOfBusinessRule.put(gameBusinessRules.getGameBusinessRule(), action);
            }
        }

        String businessRuleSelected = "";

        mapActionOfBusinessRule.entrySet().stream().max()

        for (Iterator<Map.Entry<String, GameAgentAction>> it = mapActionOfBusinessRule.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, GameAgentAction> entry = it.next();
            String businessRule = entry.getKey();

            if (businessRule.length() > businessRuleSelected.length()) {
                businessRuleSelected = businessRule;
            } else {
                it.remove();
            }
        }

        mapActionOfBusinessRule.get(businessRuleSelected);
    }
}
