package org.han.ica.asd.c.model.domain_objects;

import org.han.ica.asd.c.model.pojo.GameAgentAction;

import java.util.*;

public class GameAgent {
    private String gameAgentName;
    private Facility facility;
    private List<GameBusinessRules> gameBusinessRulesList = new ArrayList<>();
    Map<String, GameAgentAction> mapActionOfBusinessRule = new HashMap<>();

    public GameAgent(String gameAgentName, Facility facility) {
        this.gameAgentName = gameAgentName;
        this.facility = facility;
    }

    public String getGameAgentName() {
        return gameAgentName;
    }

    public void setGameAgentName(String gameAgentName) {
        this.gameAgentName = gameAgentName;
    }

    public Facility getFacilityId() {
        return facility;
    }

    public void setFacilityId(Facility facilityId) {
        this.facility = facilityId;
    }

    public List<Map<Facility, Map<Facility, Integer>>> generateOrder() {
        gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 and inventory lower than 40 and 0 < 1 then order 30", "awesomerepresentationofthetreeasastring"));
        gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 and inventory lower than 40 and backorders are greater than 0 then order 50", "awesomerepresentationofthetreeasastring"));
        gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 10 and inventory lower than 40 then order 30", "awesomerepresentationofthetreeasastring"));
        gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 20 and 10 > 5 then order 30", "awesomerepresentationofthetreeasastring2"));
        gameBusinessRulesList.add(new GameBusinessRules("if inventory higher than 20 then order 30", "awesomerepresentationofthetreeasastring3"));

        Map<Facility, Integer> targetOrderMap = new HashMap<>();
        Map<Facility, Integer> targetDeliverMap = new HashMap<>();

        for (GameBusinessRules gameBusinessRules : gameBusinessRulesList) {
            GameAgentAction action = gameBusinessRules.retrieveAction(null);
            Facility targetFacility = resolveFacilityId(action.getTargetFacilityId());
            String type = action.getType();
            if (targetOrderMap.isEmpty() && "Order".equals(type)){
                targetOrderMap.put(targetFacility, action.getAmount());
            } else if (targetDeliverMap.isEmpty() && "Deliver".equals(type)){
                targetDeliverMap.put(targetFacility, action.getAmount());
            } else if(!targetOrderMap.isEmpty() && !targetDeliverMap.isEmpty()) {
                break;
            }
        }

        Map<Facility, Map<Facility, Integer>> orderMap = new HashMap<>();
        orderMap.put(facility, targetOrderMap);
        Map<Facility, Map<Facility, Integer>> deliverMap = new HashMap<>();
        deliverMap.put(facility, targetDeliverMap);

        List<Map<Facility, Map<Facility, Integer>>> actionList = new ArrayList<>();
        actionList.add(orderMap);
        actionList.add(deliverMap);

        return actionList;
    }

    private Facility resolveFacilityId(int targetFacilityId) {
        for(FacilityLinkedTo link : facility.getFacilitiesLinkedTo()) {
            Facility targetFacility = link.getFacilityDeliver();
            if(targetFacilityId == targetFacility.getFacilityId()) {
                return targetFacility;
            }
        }
        return null;
    }
}
