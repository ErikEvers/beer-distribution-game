package org.han.ica.asd.c.model.dao_model;

public class ProgrammedAgent implements IDaoModel{
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
