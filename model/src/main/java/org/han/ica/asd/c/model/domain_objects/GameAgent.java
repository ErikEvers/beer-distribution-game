package org.han.ica.asd.c.model.domain_objects;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class GameAgent implements IDomainModel, Serializable {
    private String gameAgentName;
    private Facility facility;
    private List<GameBusinessRules> gameBusinessRulesList;
    private List<ProgrammedBusinessRules> programmedBusinessRules;

    public GameAgent(String gameAgentName, Facility facility, List<GameBusinessRules> gameBusinessRules) {
        this.gameAgentName = gameAgentName;
        this.facility = facility;
        this.gameBusinessRulesList = Collections.unmodifiableList(gameBusinessRules);
    }

    public GameAgent(String gameAgentName, List<ProgrammedBusinessRules> programmedBusinessRules, Facility facility) {
        this.gameAgentName = gameAgentName;
        this.programmedBusinessRules = programmedBusinessRules;
        this.facility = facility;
        gameBusinessRulesList = new ArrayList<>();
        for(ProgrammedBusinessRules businessRules: programmedBusinessRules) {
            gameBusinessRulesList.add(new GameBusinessRules(gameAgentName, businessRules.getProgrammedAST()));
        }
    }

    public String getGameAgentName() {
        return gameAgentName;
    }
    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public List<GameBusinessRules> getGameBusinessRules() {
        return gameBusinessRulesList;
    }

    public void setGameBusinessRules(List<GameBusinessRules> gameBusinessRules) {
        this.gameBusinessRulesList = gameBusinessRules;
    }

    public List<ProgrammedBusinessRules> getProgrammedBusinessRules() {
        return programmedBusinessRules;
    }

    public void setProgrammedBusinessRules(List<ProgrammedBusinessRules> programmedBusinessRules) {
        this.programmedBusinessRules = programmedBusinessRules;
    }
}
