package org.han.ica.asd.c.model;

public class ProgrammedBusinessRules {
    private String programmedAgentName;
    private String programmedBusinessRule;
    private String programmedAST;

    public ProgrammedBusinessRules(String programmedAgentName, String programmedBusinessRule, String programmedAST) {
        this.programmedAgentName = programmedAgentName;
        this.programmedBusinessRule = programmedBusinessRule;
        this.programmedAST = programmedAST;
    }

    public String getProgrammedAgentName() {
        return programmedAgentName;
    }

    public void setProgrammedAgentName(String programmedAgentName) {
        this.programmedAgentName = programmedAgentName;
    }

    public String getProgrammedBusinessRule() {
        return programmedBusinessRule;
    }

    public void setProgrammedBusinessRule(String programmedBusinessRule) {
        this.programmedBusinessRule = programmedBusinessRule;
    }

    public String getProgrammedAST() {
        return programmedAST;
    }

    public void setProgrammedAST(String programmedAST) {
        this.programmedAST = programmedAST;
    }
}
