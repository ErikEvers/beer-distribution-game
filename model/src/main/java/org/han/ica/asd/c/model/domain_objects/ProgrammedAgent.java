package org.han.ica.asd.c.model.domain_objects;

public class ProgrammedAgent implements IDomainModel{
    private String programmedAgentName;

    public ProgrammedAgent(String programmedAgentName) {
        this.programmedAgentName = programmedAgentName;
    }

    public String getProgrammedAgentName() {
        return programmedAgentName;
    }

    public void setProgrammedAgentName(String programmedAgentName) {
        this.programmedAgentName = programmedAgentName;
    }
}
