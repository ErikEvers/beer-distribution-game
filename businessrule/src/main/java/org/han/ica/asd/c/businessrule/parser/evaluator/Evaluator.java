package org.han.ica.asd.c.businessrule.parser.evaluator;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.Default;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.action.Person;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.operations.DivideOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.gamevalue.GameValue;
import org.han.ica.asd.c.model.interface_models.UserInputBusinessRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Evaluator {
    private boolean hasErrors = false;
    private static final String INT_VALUE = "\\d+";
    private static final String REGEX_START_WITH_IF_OR_DEFAULT = "(if|default|If|Default)[A-Za-z 0-9*/+\\-%=<>!\\t]+.";
    private boolean defaultOrderBool = false;
    private boolean defaultDeliverBool = false;
    private boolean personBool;

    /**
     * Evaluates the business rules. and checks if there are any errors.
     *
     * @param businessRulesMap A map that combines the BusinessRule and UserInputBusinessRule
     * @return Returns if the businessRule has a error.
     */
    public boolean evaluate(Map<UserInputBusinessRule, BusinessRule> businessRulesMap) {
        for (Map.Entry<UserInputBusinessRule, BusinessRule> entry : businessRulesMap.entrySet()) {
            if(!entry.getKey().getBusinessRule().matches(REGEX_START_WITH_IF_OR_DEFAULT)){
                break;
            }

            Deque<ASTNode> deque = new LinkedList<>();
            deque.push(entry.getValue());
            evaluateBusinessRule(deque, entry.getKey());
        }
        checkMinimumOfOneDefaultForOrderAndDeliver(businessRulesMap);
        return hasErrors;
    }

    /**
     *
     *
     * @param deque             The deque with all the AST nodes of the tree.
     * @param inputBusinessRule The rule that gets a the error if check fails.
     */
    private void evaluateBusinessRule(Deque<ASTNode> deque, UserInputBusinessRule inputBusinessRule) {
        personBool = false;
        while (!deque.isEmpty()) {
            ASTNode current = deque.pop();

            if (current != null) {
                executeChecksAndLog(current, inputBusinessRule);

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
    private void executeChecksAndLog(ASTNode current, UserInputBusinessRule inputBusinessRule) {
        checkRoundIsComparedToInt(current, inputBusinessRule);
        checkNotDividedByZero(current, inputBusinessRule);
        checkOnlyOneDefaultOrderAndOneDefaultDeliver(current, inputBusinessRule);
        checkDefaultWithoutDestination(current, inputBusinessRule);
        checkLowestHighestOnlyUsedAfterPerson(current, inputBusinessRule);
        checkLowestNotComparedToHighest(current, inputBusinessRule);
    }

    /**
     * Main: Checks that when a round is used it is compared to an int and nothing else.
     * Checks if round is used in the left or right side of the comparison and calls the other side to check.
     *
     * @param current           Current node that it is checking.
     * @param inputBusinessRule The rule that gets an error if check fails.
     */
    private void checkRoundIsComparedToInt(ASTNode current, UserInputBusinessRule inputBusinessRule) {
        List<ASTNode> children = current.getChildren();
        String round = GameValue.ROUND.getValue()[0];

        if (current instanceof Comparison
                && children.get(ComparisonSide.LEFT.get()) != null
                && children.get(ComparisonSide.RIGHT.get()) != null) {
            if (getAllValues(children.get(ComparisonSide.LEFT.get()),new ArrayList<>()).contains(round)) {
                checkRoundIsComparedToInt(current, inputBusinessRule, ComparisonSide.RIGHT.get());
            } else if (getAllValues(children.get(ComparisonSide.RIGHT.get()),new ArrayList<>()).contains(round)) {
                checkRoundIsComparedToInt(current, inputBusinessRule, ComparisonSide.LEFT.get());
            }
        }
    }

    /**
     * Main: Checks that when a round is used it is compared to an int and nothing else.
     * Checks if a value in the sub tree is not an int and throws an error if that's the case.
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
            if (qVal instanceof Value && !((Value) qVal).getFirstPartVariable().matches(INT_VALUE)) {
                this.hasErrors = true;
                inputBusinessRule.setErrorMessage("Round can only be compared to a number");
            }
            q.addAll(qVal.getChildren());
        }
    }

    /**
     * Checks if there is a division by 0 and sets an error if that's the case.
     *
     * @param current           Current node that it is checking.
     * @param inputBusinessRule The rule that gets an error if check fails.
     */
    private void checkNotDividedByZero(ASTNode current, UserInputBusinessRule inputBusinessRule){
        List<ASTNode> children = current.getChildren();

        if(current instanceof DivideOperation
                && children.get(ComparisonSide.RIGHT.get()) instanceof Value
                && ((((Value) children.get(ComparisonSide.RIGHT.get())).getValue()).contains("0"))){
            this.hasErrors = true;
            inputBusinessRule.setErrorMessage("Cannot divide a value by zero");
        }
    }

    /**
     * Checks if there are more than one default for order and deliver, if that's the case it sets an error.
     *
     * @param current           Current node that it is checking.
     * @param inputBusinessRule The rule that gets an error if check fails.
     */
    private void checkOnlyOneDefaultOrderAndOneDefaultDeliver(ASTNode current, UserInputBusinessRule inputBusinessRule){
        if(current instanceof BusinessRule
            && current.getLeftChild() instanceof Default){
                String action = ((ActionReference) current.getRightChild().getLeftChild()).getAction();

                if("order".equals(action)){
                    if(defaultOrderBool){
                        this.hasErrors = true;
                        inputBusinessRule.setErrorMessage("There can only be one default order business rule");
                    }
                    defaultOrderBool = true;
                } else if("deliver".equals(action)){
                    if(defaultDeliverBool){
                        this.hasErrors = true;
                        inputBusinessRule.setErrorMessage("There can only be one default deliver business rule");
                    }
                    defaultDeliverBool = true;
                }
            }

    }

    /**
     * Checks if there is a default order and default deliver, if this is not the case it sets an error.
     *
     * @param businessRulesMap A map that combines the BusinessRule and UserInputBusinessRule.
     */
    private void checkMinimumOfOneDefaultForOrderAndDeliver(Map<UserInputBusinessRule, BusinessRule> businessRulesMap){
        if(!defaultOrderBool){
            this.hasErrors = true;
            businessRulesMap.keySet().toArray(new UserInputBusinessRule[]{})[0].setErrorMessage("You're obligated to have at least one default for ordering");
        }
        if(!defaultDeliverBool){
            this.hasErrors = true;
           businessRulesMap.keySet().toArray(new UserInputBusinessRule[]{})[0].setErrorMessage("You're obligated to have at least one default for delivering");
        }
    }

    /**
     * Checks if the default rule has no destination. If it does it sets an error.
     *
     * @param current           Current node that it is checking.
     * @param inputBusinessRule The rule that gets an error if check fails.
     */
    private void checkDefaultWithoutDestination(ASTNode current, UserInputBusinessRule inputBusinessRule){
        if(current instanceof BusinessRule
                && current.getLeftChild() instanceof Default
                && current.getRightChild().getChildren().size() > 2){
                inputBusinessRule.setErrorMessage("A default rule can't specify the destination");
        }
    }

    /**
     * Checks that lowest/highest is only used in a condition for another person/player. If not it sets an error.
     *
     * @param current           Current node that it is checking.
     * @param inputBusinessRule The rule that gets an error if check fails.
     */
    private void checkLowestHighestOnlyUsedAfterPerson(ASTNode current, UserInputBusinessRule inputBusinessRule){
        if(current instanceof Person){
            personBool = true;
        }

        if(current instanceof Value
                && (((Value) current).getValue().contains(GameValue.LOWEST.getValue()[0]) || ((Value) current).getValue().contains(GameValue.HIGHEST.getValue()[0]))
                && !personBool){
            this.hasErrors = true;
            inputBusinessRule.setErrorMessage("Lowest/Highest can only be used in a condition for another player");
        }
    }

    private void checkLowestNotComparedToHighest(ASTNode current, UserInputBusinessRule inputBusinessRule){
        List<ASTNode> children = current.getChildren();
        String lowest = GameValue.LOWEST.getValue()[0];
        String highest = GameValue.HIGHEST.getValue()[0];

        if (current instanceof Comparison
                && children.get(ComparisonSide.LEFT.get()) != null
                && children.get(ComparisonSide.RIGHT.get()) != null) {
            if (getAllValues(children.get(ComparisonSide.LEFT.get()),new ArrayList<>()).contains(lowest) || getAllValues(children.get(ComparisonSide.RIGHT.get()),new ArrayList<>()).contains(lowest) ) {
                checkLowestNotComparedToHighest(current, inputBusinessRule, highest);
            } else if (getAllValues(children.get(ComparisonSide.LEFT.get()),new ArrayList<>()).contains(highest) || getAllValues(children.get(ComparisonSide.RIGHT.get()),new ArrayList<>()).contains(highest)) {
                checkLowestNotComparedToHighest(current, inputBusinessRule, lowest);
            }
        }
    }

    private void checkLowestNotComparedToHighest(ASTNode current, UserInputBusinessRule inputBusinessRule, String opposite) {
        Queue<ASTNode> q = new LinkedList<>(current.getChildren());
        while (!q.isEmpty()) {
            ASTNode qVal = q.remove();
            if (qVal instanceof Value && ((Value) qVal).getFirstPartVariable().equals(opposite)) {
                this.hasErrors = true;
                inputBusinessRule.setErrorMessage("Lowest and highest can't be compared to each other");
            }
            q.addAll(qVal.getChildren());
        }
    }

    /**
     * Recursive method that searches for all values in a side of a Comparison and puts them in a list.
     *
     * @param current Current node that is checked.
     * @param nodes List of nodes that contains all Values at the end.
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
