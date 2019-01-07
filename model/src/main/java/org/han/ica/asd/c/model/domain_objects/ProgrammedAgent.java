package org.han.ica.asd.c.model.domain_objects;

import java.util.List;

public class ProgrammedAgent implements IDomainModel{
    private String programmedAgentName;
    private List<ProgrammedBusinessRules> programmedBusinessRules;

    public ProgrammedAgent(String programmedAgentName, List<ProgrammedBusinessRules> programmedBusinessRules) {
        this.programmedAgentName = programmedAgentName;
        this.programmedBusinessRules = programmedBusinessRules;
    }

    public String getProgrammedAgentName() {
        return programmedAgentName;
    }

    public void setProgrammedAgentName(String programmedAgentName) {
        this.programmedAgentName = programmedAgentName;
    }

    public List<ProgrammedBusinessRules> getProgrammedBusinessRules() {
        return programmedBusinessRules;
    }

    public void setProgrammedBusinessRules(List<ProgrammedBusinessRules> programmedBusinessRules) {
        this.programmedBusinessRules = programmedBusinessRules;
    }
}
