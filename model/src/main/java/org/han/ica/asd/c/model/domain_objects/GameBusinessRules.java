package org.han.ica.asd.c.model.domain_objects;

import java.io.Serializable;

public class GameBusinessRules implements IDomainModel, Serializable {
    private String gameBusinessRuleSentence;
    private String gameBusinessRuleAST;

  public GameBusinessRules(String gameBusinessRule, String gameAST) {
    this.gameBusinessRuleSentence = gameBusinessRule;
    this.gameBusinessRuleAST = gameAST;
  }

  public String getGameBusinessRule() {
    return gameBusinessRuleSentence;
  }

  public void setGameBusinessRule(String gameBusinessRule) {
    this.gameBusinessRuleSentence = gameBusinessRule;
  }

  public String getGameAST() {
    return gameBusinessRuleAST;
  }

  public void setGameAST(String gameAST) {
    this.gameBusinessRuleAST = gameAST;
  }
}