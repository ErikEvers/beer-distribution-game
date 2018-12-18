package org.han.ica.asd.c.model;

public class GameBusinessRules {
    private String gameBusinessRule;
    private String gameAST;

    public GameBusinessRules(String gameAgentName, String gameBusinessRule, String gameAST) {
        this.gameBusinessRule = gameBusinessRule;
        this.gameAST = gameAST;
    }

    public String getGameBusinessRule() {
        return gameBusinessRule;
    }

    public void setGameBusinessRule(String gameBusinessRule) {
        this.gameBusinessRule = gameBusinessRule;
    }

    public String getGameAST() {
        return gameAST;
    }

    public void setGameAST(String gameAST) {
        this.gameAST = gameAST;
    }
}
