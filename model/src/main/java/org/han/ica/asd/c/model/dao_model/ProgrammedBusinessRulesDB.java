package org.han.ica.asd.c.model.dao_model;

public class ProgrammedBusinessRulesDB implements IDaoModel{
    private String programmedAgentName;
    private String programmedBusinessRule;
    private String programmedAST;

    public ProgrammedBusinessRulesDB(String programmedAgentName, String programmedBusinessRule, String programmedAST) {
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
