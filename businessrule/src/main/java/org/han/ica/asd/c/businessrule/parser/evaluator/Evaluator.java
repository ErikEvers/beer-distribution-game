package org.han.ica.asd.c.businessrule.parser.evaluator;

import org.han.ica.asd.c.businessrule.parser.UserInputBusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;

import java.util.*;

public class Evaluator {
    private boolean hasErrors = false;

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
        ASTNode previous = null;

        while (!deque.isEmpty()) {
            ASTNode current = deque.pop();

            if (current != null) {
                executeChecksAndLog(inputBusinessRule, previous, current);

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
     * @param inputBusinessRule The rule that gets an error if check fails.
     * @param previous          Previous node that it is checking.
     * @param current           Current node that it is checking.
     */
    private void executeChecksAndLog(UserInputBusinessRule inputBusinessRule, ASTNode previous, ASTNode current) {
        checkRoundIsComparedToInt(current, inputBusinessRule);
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
                && current.getChildren().get(right) != null) {
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
     * Recursive method that searches for all values in a side of a Comparison and puts them in a list.
     *
     * @param current Current node that is checked
     * @param nodes List of nodes that contains all Values at the end
     * @param side Side of the Comparison on which to get Values from
     */
    private void getAllValues(ASTNode current, List<String> nodes, int side){
        int left = 0;
        int right = 2;

        if(current instanceof Value){
            nodes.add(((Value) current).getValue());
        }

        if(!current.getChildren().isEmpty()){
            getAllValues(current.getChildren().get(side),nodes,left);
            if(current.getChildren().size() > 1){
                getAllValues(current.getChildren().get(side),nodes,right);
            }
        }
    }
}
