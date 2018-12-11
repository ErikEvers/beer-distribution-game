package org.han.ica.asd.c.businessrule.parser.evaluator;

import org.han.ica.asd.c.businessrule.parser.ast.*;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Evaluator {
    private static final Logger LOGGER = Logger.getLogger(Logger.class.getName());
    private List<BusinessRule> businessRules = new ArrayList<>();

    public void evaluate(List<BusinessRule> businessRules) {
        this.businessRules.addAll(businessRules);
        evaluate();
    }

    private void evaluate() {
        Counter defaultCounter = new Counter();
        Counter belowAboveCounter = new Counter();
        int lineNumber = 0;

        Collections.reverse(businessRules);
        Deque<ASTNode> deque = new LinkedList<>(businessRules);
        ASTNode previous = null;

        while (!deque.isEmpty()) {
            ASTNode current = deque.pop();

            if (current != null) {
                if (current instanceof BusinessRule) {
                    lineNumber++;
                }

                try {
                    checkOnlyOneDefault(current, lineNumber, defaultCounter);
                    checkRoundIsComparedToInt(current, lineNumber);
                    checkLowHighOnlyUsedWithGameValueAndAboveBelow(current, lineNumber);
                    checkDeliverOnlyUsedWithBelowAbove(current, lineNumber, belowAboveCounter);
                    checkLowHighOnlyUsedInComparison(current, lineNumber, previous);
                } catch (BusinessRuleException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }

                previous = current;
                List<ASTNode> children = current.getChildren();
                Collections.reverse(children);
                for (ASTNode child : children) {
                    deque.push(child);
                }
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

        if (current instanceof Comparison
                && current.getChildren().get(left) != null
                && current.getChildren().get(2) != null) {
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

    private void checkLowHighOnlyUsedWithGameValueAndAboveBelow(ASTNode current, int lineNumber) throws BusinessRuleException {
        int left = 0;
        int right = 2;

        if (current instanceof Comparison && current.getChildren().get(left) != null && current.getChildren().get(right) != null) {
            if (((Value) current.getChildren().get(left).getChildren().get(left)).getValue().equals(EvaluatorType.LOWEST.getEvaluatorSymbol())
                    || ((Value) current.getChildren().get(left).getChildren().get(left)).getValue().equals(EvaluatorType.HIGHEST.getEvaluatorSymbol())) {
                checkLowHighOnlyUsedWithGameValueAndAboveBelow(current, lineNumber, right);
            } else if (((Value) current.getChildren().get(right).getChildren().get(left)).getValue().equals(EvaluatorType.LOWEST.getEvaluatorSymbol())
                    || ((Value) current.getChildren().get(right).getChildren().get(left)).getValue().equals(EvaluatorType.HIGHEST.getEvaluatorSymbol())) {
                checkLowHighOnlyUsedWithGameValueAndAboveBelow(current, lineNumber, left);
            }
        }
    }

    private void checkLowHighOnlyUsedWithGameValueAndAboveBelow(ASTNode current, int lineNumber, int side) throws BusinessRuleException {
        List<String> gameValues = new ArrayList<>();
        Collections.addAll(gameValues, "inventory", "stock", "backlog", "incoming order", "back orders");
        Queue<ASTNode> q = new LinkedList<>();
        q.add(current.getChildren().get(side));
        while (!q.isEmpty()) {
            ASTNode qVal = q.remove();
            if (qVal instanceof Value) {
                for (String gameValue : gameValues) {
                    if (!((Value) qVal).getValue().contains(gameValue)
                            || !((((Value) qVal).getValue().contains(EvaluatorType.ABOVE.getEvaluatorSymbol()))
                            || ((Value) qVal).getValue().contains(EvaluatorType.BELOW.getEvaluatorSymbol()))) {
                        throw new BusinessRuleException("Lowest and highest can only be used with a game value combined with an above/below", lineNumber);
                    }
                }
            }
            q.addAll(qVal.getChildren());
        }
    }

    private void checkDeliverOnlyUsedWithBelowAbove(ASTNode current, int lineNumber, Counter belowAboveCounter) throws BusinessRuleException {
        int left = 0;
        int right = 2;

        if (current instanceof Comparison && current.getChildren().get(left) != null && current.getChildren().get(right) != null) {
            String leftSide = ((Value) current.getChildren().get(left).getChildren().get(left)).getValue();
            String rightSide = ((Value) current.getChildren().get(right).getChildren().get(left)).getValue();
            if (leftSide.contains(EvaluatorType.BELOW.getEvaluatorSymbol())
                    || leftSide.contains(EvaluatorType.ABOVE.getEvaluatorSymbol())
                    || rightSide.contains(EvaluatorType.BELOW.getEvaluatorSymbol())
                    || rightSide.contains(EvaluatorType.ABOVE.getEvaluatorSymbol())) {
                belowAboveCounter.addOne();
            }
        }

        if (current instanceof ActionReference
                && ((ActionReference) current).getAction().equals(EvaluatorType.DELIVER.getEvaluatorSymbol())
                && belowAboveCounter.getCountedValue() == 0) {
            throw new BusinessRuleException("Deliver can only be used with a businessrule that uses above/below", lineNumber);
        }
    }

    private void checkLowHighOnlyUsedInComparison(ASTNode current, int lineNumber, ASTNode previous) throws BusinessRuleException {
        if (current instanceof Value
                && (((Value) current).getValue().contains(EvaluatorType.LOWEST.getEvaluatorSymbol())
                || ((Value) current).getValue().contains(EvaluatorType.HIGHEST.getEvaluatorSymbol()))
                && !(previous instanceof ComparisonValue)) {
            throw new BusinessRuleException("Lowest and highest can only be used in a comparison", lineNumber);
        }
    }
}
