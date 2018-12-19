package org.han.ica.asd.c.model.domain_objects;

import com.google.inject.Inject;
import org.han.ica.asd.c.model.pojo.GameAgentAction;

import java.util.Random;

public class GameBusinessRules {
    private String gameBusinessRule;
    private String gameAST;
    @Inject
//    private IBusinessRules businessRules;

    public GameBusinessRules(String gameBusinessRule, String gameAST) {
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

    public GameAgentAction retrieveAction(Round round) {
//        return businessRules.evaluateBusinessRule(this.gameBusinessRule, round);
        return new GameAgentAction() {
            @Override
            public String getType() {
                return (new Random().nextBoolean()) ? "Deliver" : "Order";
            }

            @Override
            public int getAmount(){
                return new Random().nextInt(50);
            }

            @Override
            public int getTargetFacilityId(){
                return new Random().nextInt(1);
            }
        };
    }
}
