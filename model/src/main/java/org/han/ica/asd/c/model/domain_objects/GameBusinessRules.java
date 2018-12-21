package org.han.ica.asd.c.model.domain_objects;

public class GameBusinessRules implements IDomainModel{
    public final String gameBusinessRuleSentence;
    public final String gameBusinessRuleAST;

    public GameBusinessRules(String gameBusinessRuleSentence, String gameBusinessRuleAST) {
        this.gameBusinessRuleSentence = gameBusinessRuleSentence;
        this.gameBusinessRuleAST = gameBusinessRuleAST;
    }
}
