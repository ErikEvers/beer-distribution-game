package org.han.ica.asd.c.businessrule.parser.evaluator;

import org.han.ica.asd.c.businessrule.parser.UserInputBusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.Default;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;

import java.util.*;

public class Evaluator {
    private boolean hasErrors = false;
    private boolean defaultBool = false;

    /**
     * Evaluates the business rules. and checks if there are any errors.
     *
     * @param businessRulesMap A map that combines the BusinessRule and UserInputBusinessRule
     * @return Returns if the businessRule has a error.
     */
    public boolean evaluate(Map<UserInputBusinessRule, BusinessRule> businessRulesMap) {
        Counter defaultCounter = new Counter();
        for (Map.Entry<UserInputBusinessRule, BusinessRule> entry : businessRulesMap.entrySet()) {
            Deque<ASTNode> deque = new LinkedList<>();
            deque.push(entry.getValue());
            evaluateBusinessRule(defaultCounter, deque, entry.getKey());
        }
        return hasErrors;
    }
    /**
     *
     *
     * @param defaultCounter    The counter that is used for counting the times default is called.
     * @param deque             The deque with all the AST nodes of the tree.
     * @param inputBusinessRule The rule that gets a the error if check fails.
     */
    private void evaluateBusinessRule(Counter defaultCounter, Deque<ASTNode> deque, UserInputBusinessRule inputBusinessRule) {
        Counter belowCounter = new Counter();
        ASTNode previous = null;

        while (!deque.isEmpty()) {
            ASTNode current = deque.pop();

            if (current != null) {
                executeChecksAndLog(defaultCounter, belowCounter, inputBusinessRule, previous, current);

                previous = current;
                List<ASTNode> children = current.getChildren();
                Collections.reverse(children);
                for (ASTNode child : children) {
                    deque.push(child);
                }
            }
        }
    }

    /**
     * Execute all functions that check if node is correct.
     *
     * @param defaultCounter    The counter that is used for counting the times default is called.
     * @param belowCounter      The counter that is used for counting the times below is called.
     * @param inputBusinessRule The rule that gets an error if check fails.
     * @param previous          Previous node that it is checking.
     * @param current           Current node that it is checking.
     */
    private void executeChecksAndLog(Counter defaultCounter, Counter belowCounter, UserInputBusinessRule inputBusinessRule, ASTNode previous, ASTNode current) {
        checkOnlyOneDefault(current, inputBusinessRule, defaultCounter);
        checkRoundIsComparedToInt(current, inputBusinessRule);
        checkDeliverOnlyUsedInIfRule(current, inputBusinessRule);
        checkLowHighOnlyComparedToGameValueAndAboveBelow(current, inputBusinessRule);
        checkDeliverOnlyUsedWithBelow(current, inputBusinessRule, belowCounter);
        checkLowHighOnlyUsedInComparison(current, inputBusinessRule, previous);
    }

    /**
     * Checks that there is only one default business rule in the collection of business rules
     *
     * @param current           Current node that it is checking.
     * @param inputBusinessRule The rule that gets an error if check fails.
     * @param defaultCounter    Counter that counts the amount of defaults.
     */
    private void checkOnlyOneDefault(ASTNode current, UserInputBusinessRule inputBusinessRule, Counter defaultCounter) {
        if (current instanceof Default) {
            defaultCounter.addOne();
            if (defaultCounter.getCountedValue() > 1) {
                this.hasErrors = true;
                inputBusinessRule.setErrorMessage("There can only be one default statement");
            }
        }
    }

    /**
     * Main: Checks that when a round is used it is compared to an int and nothing else
     * Checks if round is used in the left or right side of the comparison and calls the other side to check
     *
     * @param current           Current node that it is checking.
     * @param inputBusinessRule The rule that gets an error if check fails.
     */
    private void checkRoundIsComparedToInt(ASTNode current, UserInputBusinessRule inputBusinessRule) {
        int left = 0;
        int right = 2;

        if (current instanceof Comparison
                && current.getChildren().get(left) != null
                && current.getChildren().get(2) != null) {
            if ("round".equals(((Value) current.getChildren().get(left).getChildren().get(left)).getValue())) {
                checkRoundIsComparedToInt(current, inputBusinessRule, right);
            } else if ("round".equals(((Value) current.getChildren().get(right).getChildren().get(left)).getValue())) {
                checkRoundIsComparedToInt(current, inputBusinessRule, left);
            }
        }
    }

    /**
     * Main: Checks that when a round is used it is compared to an int and nothing else
     * Checks if a value in the sub tree is not an int and throws an error if that's the case
     *
     * @param current           Current node that it is checking.
     * @param inputBusinessRule The rule that gets an error if check fails.
     * @param side              The side that it needs to check.
     */
    private void checkRoundIsComparedToInt(ASTNode current, UserInputBusinessRule inputBusinessRule, int side) {
        Queue<ASTNode> q = new LinkedList<>();
        q.add(current.getChildren().get(side));
        while (!q.isEmpty()) {
            ASTNode qVal = q.remove();
            if (qVal instanceof Value && !((Value) qVal).getValue().matches("-?\\d+")) {
                this.hasErrors = true;
                inputBusinessRule.setErrorMessage("Round can only be compared to a number");
            }
            q.addAll(qVal.getChildren());
        }
    }

    /**
     * Main: Checks that when lowest/highest is used in a business rule it is compared to a game value combined with an above/below
     * Checks if lowest/highest is used in the left or right side of the comparison and calls the other side to check
     *
     * @param current           Current node that it is checking.
     * @param inputBusinessRule The rule that gets an error if check fails.
     */
    private void checkLowHighOnlyComparedToGameValueAndAboveBelow(ASTNode current, UserInputBusinessRule inputBusinessRule) {
        int left = 0;
        int right = 2;

        if (current instanceof Comparison && current.getChildren().get(left) != null && current.getChildren().get(right) != null) {
            if (((Value) current.getChildren().get(left).getChildren().get(left)).getValue().equals(EvaluatorType.LOWEST.getEvaluatorSymbol())
                    || ((Value) current.getChildren().get(left).getChildren().get(left)).getValue().equals(EvaluatorType.HIGHEST.getEvaluatorSymbol())) {
                checkLowHighOnlyComparedToGameValueAndAboveBelow(current, inputBusinessRule, right);
            } else if (((Value) current.getChildren().get(right).getChildren().get(left)).getValue().equals(EvaluatorType.LOWEST.getEvaluatorSymbol())
                    || ((Value) current.getChildren().get(right).getChildren().get(left)).getValue().equals(EvaluatorType.HIGHEST.getEvaluatorSymbol())) {
                checkLowHighOnlyComparedToGameValueAndAboveBelow(current, inputBusinessRule, left);
            }
        }
    }

    /**
     * Main: Checks that when lowest/highest is used in a business rule it is compared to a game value combined with an above/below.
     * Queue to loop through the sub tree
     *
     * @param current           Current node that it is checking.
     * @param inputBusinessRule The rule that gets an error if check fails.
     * @param side              The side of the tree that it needs to check
     */
    private void checkLowHighOnlyComparedToGameValueAndAboveBelow(ASTNode current, UserInputBusinessRule inputBusinessRule, int side) {
        Queue<ASTNode> q = new LinkedList<>();
        q.add(current.getChildren().get(side));
        while (!q.isEmpty()) {
            ASTNode qVal = q.remove();
            if (qVal instanceof Value) {
                checkLowHighOnlyComparedToGameValueAndAboveBelow(inputBusinessRule, (Value) qVal);
            }
            q.addAll(qVal.getChildren());
        }
    }

    /**
     * Main: Checks that when lowest/highest is used in a business rule it is compared to a game value combined with an above/below
     * Checks if game value combined with below/above is not used and throws error if that's the case
     *
     * @param inputBusinessRule The rule that gets an error if check fails.
     * @param qVal              Current node in the queue.
     */
    private void checkLowHighOnlyComparedToGameValueAndAboveBelow(UserInputBusinessRule inputBusinessRule, Value qVal) {
        List<String> gameValues = new ArrayList<>();
        Collections.addAll(gameValues, "inventory", "stock", "backlog", "incoming order", "back orders");
        for (String gameValue : gameValues) {
            if (!qVal.getValue().contains(gameValue)
                    && !((qVal.getValue().contains(EvaluatorType.ABOVE.getEvaluatorSymbol()))
                    || qVal.getValue().contains(EvaluatorType.BELOW.getEvaluatorSymbol()))) {
                this.hasErrors = true;
                inputBusinessRule.setErrorMessage("Lowest and highest can only be used with a game value combined with an above/below");
                break;
            }
        }
    }

    /**
     * Main: Checks that when lowest/highest is used in a business rule it is compared to a game value combined with an above/below
     * Counts the amount of below/above used in the comparison
     *
     * @param current           Current node that it is checking.
     * @param inputBusinessRule The rule that gets an error if check fails.
     * @param belowCounter      Counter that counts the amount of belows.
     */
    private void checkDeliverOnlyUsedWithBelow(ASTNode current, UserInputBusinessRule inputBusinessRule, Counter belowCounter) {
        int left = 0;
        int right = 2;

        if (current instanceof Comparison && current.getChildren().get(left) != null && current.getChildren().get(right) != null) {
            String leftSide = ((Value) current.getChildren().get(left).getChildren().get(left)).getValue();
            String rightSide = ((Value) current.getChildren().get(right).getChildren().get(left)).getValue();
            if (leftSide.contains(EvaluatorType.BELOW.getEvaluatorSymbol())
                    || rightSide.contains(EvaluatorType.BELOW.getEvaluatorSymbol())) {
                belowCounter.addOne();
            }
        }

        checkDeliverOnlyUsedWithBelowError(current, inputBusinessRule, belowCounter);
    }

    /**
     * Main: Checks that when lowest/highest is used in a business rule it is compared to a game value combined with an above/below
     * Checks if a deliver is used and if there are no below/above, if this is the case it throws an error
     *
     * @param current           Current node that it is checking.
     * @param inputBusinessRule The rule that gets an error if check fails.
     * @param belowCounter      Counter that counts the amount of belows.
     */
    private void checkDeliverOnlyUsedWithBelowError(ASTNode current, UserInputBusinessRule inputBusinessRule, Counter belowCounter) {
        if (current instanceof ActionReference
                && ((ActionReference) current).getAction().equals(EvaluatorType.DELIVER.getEvaluatorSymbol())
                && belowCounter.getCountedValue() == 0) {
            this.hasErrors = true;
            inputBusinessRule.setErrorMessage("Deliver can only be used with a businessrule that uses below");
        }
    }

    /**
     * Checks if lowest/highest is only used in a comparison
     *
     * @param current           Current node that it is checking.
     * @param inputBusinessRule The rule that gets an error if check fails.
     * @param previous          Previous node that is above the current one.
     */
    private void checkLowHighOnlyUsedInComparison(ASTNode current, UserInputBusinessRule inputBusinessRule, ASTNode previous) {
        if (current instanceof Value
                && (((Value) current).getValue().contains(EvaluatorType.LOWEST.getEvaluatorSymbol())
                || ((Value) current).getValue().contains(EvaluatorType.HIGHEST.getEvaluatorSymbol()))
                && !(previous instanceof ComparisonValue)) {
            this.hasErrors = true;
            inputBusinessRule.setErrorMessage("Lowest and highest can only be used in a comparison");
        }
    }

    /**
     * Checks if a default rule is not used with delivered.
     *
     * @param current           Current node that it is checking.
     * @param inputBusinessRule The rule that gets an error if check fails.
     */
    private void checkDeliverOnlyUsedInIfRule(ASTNode current, UserInputBusinessRule inputBusinessRule) {
        if (current instanceof Default) {
            defaultBool = true;
        }

        if (current instanceof ActionReference
                && defaultBool
                && ((ActionReference) current).getAction().equals("deliver")) {
            inputBusinessRule.setErrorMessage("Deliver can not be used in a default statement");
        }
    }
}
