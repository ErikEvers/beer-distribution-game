package org.han.ica.asd.c.businessrule.parser.evaluator;

import org.han.ica.asd.c.businessrule.parser.ast.*;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Evaluator {
    private static final  Logger LOGGER = Logger.getLogger(Logger.class.getName());
    private List<BusinessRule> businessRules;

    public void evaluate(List<BusinessRule> businessRules) {
        this.businessRules = businessRules;
        evaluate();
    }

    private void evaluate() {
        Counter defaultCounter = new Counter();
        Counter belowAboveCounter = new Counter();
        int lineNumber = 0;

        Collections.reverse(businessRules);
        Deque<ASTNode> deque = new LinkedList<>(businessRules);

        while (!deque.isEmpty()) {
            ASTNode current = deque.pop();

            if(current != null) {
                if (current instanceof BusinessRule) {
                    lineNumber++;
                }

                try {
                    checkOnlyOneDefault(current, lineNumber, defaultCounter);
                    checkRoundIsComparedToInt(current, lineNumber);
                    checkLowHighOnlyUsedWithGameValue(current, lineNumber);
                    checkDeliverOnlyUsedWithBelowAbove(current, lineNumber, belowAboveCounter);
                } catch (BusinessRuleException e) {
                    // TODO: Level.SEVERE vervangen door FINE.
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }

                deque.addAll(current.getChildren());
            }
        }
    }

    private void checkOnlyOneDefault(ASTNode current, int lineNumber, Counter defaultCounter) throws BusinessRuleException {
        if (current instanceof Default) {
            defaultCounter.addOne();
            if (defaultCounter.getCountedValue() > 1) {
                throw new BusinessRuleException("There can only be one default statement", lineNumber);
            }
        }
    }

    private void checkRoundIsComparedToInt(ASTNode current, int lineNumber) throws BusinessRuleException {
        int left = 0;
        int right = 2;

        if (current instanceof Comparison && current.getChildren().get(left) != null && current.getChildren().get(2) != null) {
            if (((Value) current.getChildren().get(left).getChildren().get(left)).getValue().equals("round")) {
                checkRoundIsComparedToInt(current, lineNumber, right);
            } else if (((Value) current.getChildren().get(right).getChildren().get(left)).getValue().equals("round")) {
                checkRoundIsComparedToInt(current, lineNumber, left);
            }
        }
    }

    private void checkRoundIsComparedToInt(ASTNode current, int lineNumber, int side) throws BusinessRuleException {
        Queue<ASTNode> q = new LinkedList<>();
        q.add(current.getChildren().get(side));
        while (!q.isEmpty()) {
            ASTNode qVal = q.remove();
            if (qVal instanceof Value && !((Value) qVal).getValue().matches("-?\\d+")) {
                throw new BusinessRuleException("Round can only be compared to a number", lineNumber);
            }
            q.addAll(qVal.getChildren());
        }
    }

    private void checkLowHighOnlyUsedWithGameValue(ASTNode current, int lineNumber) throws BusinessRuleException {
        int left = 0;
        int right = 2;

        if (current instanceof Comparison && current.getChildren().get(left) != null && current.getChildren().get(right) != null) {
            if (((Value) current.getChildren().get(left).getChildren().get(left)).getValue().equals("lowest") || ((Value) current.getChildren().get(left).getChildren().get(left)).getValue().equals("highest")) {
                checkLowHighOnlyUsedWithGameValue(current, lineNumber, right);
            } else if (((Value) current.getChildren().get(right).getChildren().get(left)).getValue().equals("lowest") || ((Value) current.getChildren().get(right).getChildren().get(left)).getValue().equals("highest")) {
                checkLowHighOnlyUsedWithGameValue(current, lineNumber, left);
            }
        }
    }

    private void checkLowHighOnlyUsedWithGameValue(ASTNode current, int lineNumber, int side) throws  BusinessRuleException {
        List<String> gameValues = new ArrayList<>();
        Collections.addAll(gameValues, "inventory", "stock", "backlog", "incoming order", "back orders");
        Queue<ASTNode> q = new LinkedList<>();
        q.add(current.getChildren().get(side));
        while (!q.isEmpty()) {
            ASTNode qVal = q.remove();
            if(qVal instanceof Value && !gameValues.contains(((Value) qVal).getValue())){
                throw new BusinessRuleException("Lower and higher can only be used with a game value", lineNumber);
            }
            q.addAll(qVal.getChildren());
        }
    }

    private void checkDeliverOnlyUsedWithBelowAbove(ASTNode current, int lineNumber, Counter belowAboveCounter) throws BusinessRuleException {
        int left = 0;
        int right = 2;

        if (current instanceof Comparison && current.getChildren().get(left) != null && current.getChildren().get(right) != null) {
            if (((Value) current.getChildren().get(left).getChildren().get(left)).getValue().equals("lowest") || ((Value) current.getChildren().get(left).getChildren().get(left)).getValue().equals("highest")) {
                belowAboveCounter.addOne();
            }
        }

        if (current instanceof ActionReference) {
            if(((ActionReference) current).getAction().equals("deliver")){
                if(belowAboveCounter.getCountedValue() == 0){
                    throw new BusinessRuleException("Deliver can only be used with a businessrule that uses above/below", lineNumber);
                }
            }
        }
    }

    // Smallest/biggest mag alleen gebruikt worden als er below/above is gebruikt.
    // Smallest/biggest mag alleen gebruikt worden in een vergelijking (geen operaties en niet in een order).
}
