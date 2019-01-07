package org.han.ica.asd.c.model.domain_objects;

public class ProgrammedBusinessRules implements IDomainModel{
    private String programmedBusinessRule;
    private String programmedAST;

    public ProgrammedBusinessRules(String programmedBusinessRule, String programmedAST) {
        this.programmedBusinessRule = programmedBusinessRule;
        this.programmedAST = programmedAST;
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
