package org.han.ica.asd.c.businessrule.parser.evaluator;

import org.han.ica.asd.c.businessrule.parser.UserInputBusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.operations.DivideOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;

import java.util.*;

public class Evaluator {
    private boolean hasErrors = false;
    private static final String INT_VALUE = "\\d+";

    /**
     * Evaluates the business rules. and checks if there are any errors.
     *
     * @param businessRulesMap A map that combines the BusinessRule and UserInputBusinessRule
     * @return Returns if the businessRule has a error.
     */
    public boolean evaluate(Map<UserInputBusinessRule, BusinessRule> businessRulesMap) {
        for (Map.Entry<UserInputBusinessRule, BusinessRule> entry : businessRulesMap.entrySet()) {
            Deque<ASTNode> deque = new LinkedList<>();
            deque.push(entry.getValue());
            evaluateBusinessRule(deque, entry.getKey());
        }
        return hasErrors;
    }
    /**
     *
     *
     * @param deque             The deque with all the AST nodes of the tree.
     * @param inputBusinessRule The rule that gets a the error if check fails.
     */
    private void evaluateBusinessRule(Deque<ASTNode> deque, UserInputBusinessRule inputBusinessRule) {
        while (!deque.isEmpty()) {
            ASTNode current = deque.pop();

            if (current != null) {
                executeChecksAndLog(inputBusinessRule, current);

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
     * @param inputBusinessRule The rule that gets an error if check fails.
     * @param current           Current node that it is checking.
     */
    private void executeChecksAndLog(UserInputBusinessRule inputBusinessRule, ASTNode current) {
        checkRoundIsComparedToInt(current, inputBusinessRule);
        checkNotDividedByZero(current, inputBusinessRule);
    }

    /**
     * Main: Checks that when a round is used it is compared to an int and nothing else
     * Checks if round is used in the left or right side of the comparison and calls the other side to check
     *
     * @param current           Current node that it is checking.
     * @param inputBusinessRule The rule that gets an error if check fails.
     */
    private void checkRoundIsComparedToInt(ASTNode current, UserInputBusinessRule inputBusinessRule) {
        List<ASTNode> children = current.getChildren();

        if (current instanceof Comparison
                && children.get(ComparisonSide.LEFT.get()) != null
                && children.get(ComparisonSide.RIGHT.get()) != null) {
            if (getAllValues(children.get(ComparisonSide.LEFT.get()),new ArrayList<>()).contains("round")) {
                checkRoundIsComparedToInt(current, inputBusinessRule, ComparisonSide.RIGHT.get());
            } else if (getAllValues(children.get(ComparisonSide.RIGHT.get()),new ArrayList<>()).contains("round")) {
                checkRoundIsComparedToInt(current, inputBusinessRule, ComparisonSide.LEFT.get());
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
            if (qVal instanceof Value && !((Value) qVal).getValue().get(0).matches(INT_VALUE)) {
                this.hasErrors = true;
                inputBusinessRule.setErrorMessage("Round can only be compared to a number");
            }
            q.addAll(qVal.getChildren());
        }
    }

    private void checkNotDividedByZero(ASTNode current, UserInputBusinessRule inputBusinessRule){
        List<ASTNode> children = current.getChildren();

        if(current instanceof DivideOperation
                && children.get(ComparisonSide.RIGHT.get()) instanceof Value
                && ("0".equals(((Value) children.get(ComparisonSide.RIGHT.get())).getValue()))){
            this.hasErrors = true;
            inputBusinessRule.setErrorMessage("Cannot divide a value by zero");
        }
    }

    /**
     * Recursive method that searches for all values in a side of a Comparison and puts them in a list.
     *
     * @param current Current node that is checked
     * @param nodes List of nodes that contains all Values at the end
     */
    private List<String> getAllValues(ASTNode current, List<String> nodes){
        if(current instanceof Value){
            nodes.addAll(((Value) current).getValue());
        }

        if(!current.getChildren().isEmpty()){
            getAllValues(current.getChildren().get(ComparisonSide.LEFT.get()),nodes);
            if(current.getChildren().size() > 1){
                getAllValues(current.getChildren().get(ComparisonSide.RIGHT.get()),nodes);
            }
        }

        return nodes;
    }
}
