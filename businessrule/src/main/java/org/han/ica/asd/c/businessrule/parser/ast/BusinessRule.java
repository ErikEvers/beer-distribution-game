package org.han.ica.asd.c.businessrule.parser.ast;


import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Operation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.OperationValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.gamevalue.GameValue;
import org.han.ica.asd.c.model.domain_objects.*;

import java.util.*;
import java.util.regex.Pattern;

public class BusinessRule extends ASTNode {
    private Condition condition;
    private Action action;
    private static final String PREFIX = "BR(";
    private static final String HAS_CHARACTERS = "[a-zA-Z ]+";

    /**
     * Gets the action of the BusinessRule
     *
     * @return Returns an action
     */
    public Action getAction() {
        return action;
    }

    /**
     * Adds a child ASTNode to a parent(this) ASTNode
     *
     * @param child Child that has the be added to this ASTNode
     * @return Returns itself so that it can be used immediately
     */
    @Override
    public ASTNode addChild(ASTNode child) {
        if (condition == null) {
            this.condition = (Condition) child;
        } else {
            this.action = (Action) child;
        }
        return this;
    }

    /**
     * Start of the encoding of a tree
     *
     * @return Returns encoded tree
     */
    public String encode() {
        StringBuilder stringBuilder = new StringBuilder();
        encode(stringBuilder);
        return stringBuilder.toString();
    }

    /**
     * Evaluates the businessRule to a businessRule with a single condition and action.
     */
    public void evaluateBusinessRule() {
        transformCondition();
        transformAction();
    }

    /**
     * Transforms the condition of the businessRule to a single {@link BooleanLiteral}
     */
    private void transformCondition() {
        this.condition = this.condition.resolveCondition();
    }

    /**
     * Transforms the action of the businessRule to a single {@link Action}
     */
    private void transformAction() {
        transformChild(this.action);
    }

    /**
     * Transforms an {@link ASTNode} based on his type
     *
     * @param node The node to transform
     */
    private void transformChild(ASTNode node) {
        if (node instanceof ComparisonValue) {
            ComparisonValue comparisonValue = (ComparisonValue) node;
            OperationValue operationValue = comparisonValue.getOperationValue();
            if (operationValue instanceof Operation) {
                Operation operation = (Operation) operationValue;
                comparisonValue.setOperationValue(operation.resolveOperation());
            }
        }

        node.getChildren().forEach(this::transformChild);
    }

    /**
     * Encodes the parsed tree in a single string so that it can be stored in the database
     *
     * @param stringBuilder Stringbuilder that is used to encode the tree
     */
    @Override
    public void encode(StringBuilder stringBuilder) {
        super.encode(stringBuilder, getChildren(), PREFIX, SUFFIX);
    }

    /**
     * Return the children that are assigned to the ASTNode
     *
     * @return Return the children
     */
    @Override
    public List<ASTNode> getChildren() {
        List<ASTNode> list = new ArrayList<>();
        Collections.addAll(list, condition, action);
        return list;
    }

    /**
     * Equals function used for unit testing
     *
     * @param o Object that needs to be checked if it's equal to this object
     * @return Returns true or false depending on if it's equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        BusinessRule businessRule = (BusinessRule) o;
        return Objects.equals(condition, businessRule.condition) &&
                Objects.equals(action, businessRule.action);
    }

    /**
     * Hash function used for unit testing
     *
     * @return Returns the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(condition, action);
    }

    /***
     * Replaces the variables of the business rule with data using dept first
     *
     * When its a leaf (a Value) it replaces the value with the game data(gameData)
     * @param round data of a gameData
     * @param facilityId identifier of the facility
     */
    public void substituteTheVariablesOfBusinessruleWithGameData(Round round, int facilityId) {
        findLeafAndReplace(condition, round, facilityId);
        findLeafAndReplace(action, round, facilityId);
    }

    /***
     * Finds the leaf of the astnode and replaces the value
     *
     * @param astNode the node of the ast
     * @param round the game data, used to replace data in function replace(Value value, int facilityId)
     * @param facilityId the id of the facility
     */
    private void findLeafAndReplace(ASTNode astNode, Round round, int facilityId) {
        if (astNode instanceof Value) {
            replace((Value) astNode, round, facilityId);
        }
        if (astNode != null) {
            findLeafAndReplace(astNode.getLeftChild(), round, facilityId);
            if (hasMultipleChildren(astNode)) {
                findLeafAndReplace(astNode.getRightChild(), round, facilityId);
            }
        }
    }


    /***
     * Checks if the node has more than one child
     * @param astNode a node of the tree
     * @return true if the node has more than one child
     */
    private boolean hasMultipleChildren(ASTNode astNode) {
        return astNode.getChildren().size() > 1;
    }

    /***
     * Gets one part of the value replaces it with game data
     *
     * @param value the value
     * @param round the previous round
     * @param facilityId the id of the facility
     */
    private void replace(Value value, Round round, int facilityId) {
        if (value.getValue().size() > 1) {
            String secondVariable = value.getSecondPartVariable();
            String id;
            if (GameValue.checkIfFacility(secondVariable)) {
                id = getReplacementValue(getGameValue(secondVariable), round, facilityId);
                replaceOnVariable(value, round, facilityId, id);
            } else if (Pattern.matches(HAS_CHARACTERS, value.getSecondPartVariable())) {
                replaceOnVariable(value, round, facilityId, secondVariable);
            }
        }
        String firstVariable = value.getFirstPartVariable();
        if (Pattern.matches(HAS_CHARACTERS, value.getFirstPartVariable())) {
            replaceOnVariable(value, round, facilityId, firstVariable);
        }
    }

    /***
     * Replaces exactly one part of the variable
     *
     * @param value the value
     * @param round the previous round
     * @param facilityId the id of the facility
     * @param variable one part of value
     */
    private void replaceOnVariable(Value value, Round round, int facilityId, String variable) {
        GameValue gameValue = getGameValue(variable);
        if (gameValue != null) {
            String newReplacementValue = getReplacementValue(gameValue, round, facilityId);
            value.replaceValueWithValue(newReplacementValue);
        }
    }

    /***
     * If the variable is a variable then it returns the corresponding game value
     *
     * @param variable one part of the value
     * @return the corresponding game value
     */
    private GameValue getGameValue(String variable) {
        for (GameValue gameValue : GameValue.values()) {
            if (gameValue.contains(variable)) {
                return gameValue;
            }
        }
        return null;
    }

    /***
     * Gets the replacementValue from the previous round
     *
     * @param gameValue the type of game value
     * @param round from the previous round
     * @param facilityId the id of the facility
     * @return the replacement Value
     */
    private String getReplacementValue(GameValue gameValue, Round round, int facilityId) {
        switch (gameValue) {
            case ORDERED:
                return getOrder(round,facilityId);
            case STOCK:
                return getStock(round,facilityId);
            case BUDGET:
                return getBudget(round,facilityId);
            case BACKLOG:
                return getBacklog(round,facilityId);
            case INCOMINGORDER:
                return getIncomingOrder(round,facilityId);
            case OUTGOINGGOODS:
                return getOutgoingGoods(round,facilityId);
            default:
                return String.valueOf(getFacilityIdBasedOnType(round.getTurnStock(), gameValue));
        }
    }

    /***
     * Gets the number of orders of an facility
     * @param round the given round
     * @param facilityId the id of the given facility
     * @return the order amount
     */
    private String getOrder(Round round, int facilityId){
        String replacementValue = "";
        for(FacilityTurnOrder facilityTurnOrder:round.getFacilityOrders()){
            if(facilityTurnOrder.getFacilityId()==facilityId){
                replacementValue = String.valueOf(facilityTurnOrder.getOrderAmount());
            }
        }
        return replacementValue;
    }

    /***
     * Gets the stock of an facility
     * @param round the given round
     * @param facilityId the id of the given facility
     * @return stock
     */
    private String getStock(Round round, int facilityId){
        String replacementValue = "";
        for(FacilityTurn facilityTurn:round.getFacilityTurns()){
            if(facilityTurn.getFacilityId()==facilityId){
                replacementValue= String.valueOf(facilityTurn.getStock());
            }
        }
        return replacementValue;
    }
    /***
     * Gets the remaining budget of an facility
     * @param round the given round
     * @param facilityId the id of the given facility
     * @return budget
     */
    private String getBudget(Round round, int facilityId){
        String replacementValue = "";
        for(FacilityTurn facilityTurn:round.getFacilityTurns()){
            if(facilityTurn.getFacilityId()==facilityId){
                replacementValue= String.valueOf(facilityTurn.getRemainingBudget());
            }
        }
        return replacementValue;
    }

    /***
     * Gets the number of open orders of an facility
     * @param round the given round
     * @param facilityId the id of the given facility
     * @return budget
     */
    private String getBacklog(Round round, int facilityId){
        String replacementValue = "";
        for(FacilityTurn facilityTurn:round.getFacilityTurns()){
            if(facilityTurn.getFacilityId()==facilityId){
                replacementValue= String.valueOf(facilityTurn.getBackorders());
            }
        }
        return replacementValue;
    }

    /***
     * Gets the incoming order of an facility
     * @param round the given round
     * @param facilityId the id of the given facility
     * @return incoming order amount
     */
    private String getIncomingOrder(Round round,int facilityId){
        String replacementValue = "";
        for(FacilityTurnOrder facilityTurn:round.getFacilityOrders()){
            if(facilityTurn.getFacilityIdOrderTo()==facilityId){
                replacementValue= String.valueOf(facilityTurn.getOrderAmount());
            }
        }
        return replacementValue;
    }

    /***
     * Gets the outgoing order of an facility
     * @param round the given round
     * @param facilityId the id of the given facility
     * @return outgoing goods amount
     */
    private String getOutgoingGoods(Round round,int facilityId){
        String replacementValue = "";
        for(FacilityTurnDeliver facilityTurnDeliver:round.getFacilityTurnDelivers()){
            if(facilityTurnDeliver.getFacilityId()==facilityId){
                replacementValue= String.valueOf(facilityTurnDeliver.getDeliverAmount());
            }
        }
        return replacementValue;
    }

    /**
     * Gets the id based on the facility Type
     *
     * @param map          the map to search through
     * @param facilityType the type of facility
     * @return the type of facility. returns -1 when no type is found
     */
    private int getFacilityIdBasedOnType(Map<Facility, Integer> map, GameValue facilityType) {
        Facility facility = null;
        for (Map.Entry<Facility, Integer> entry : map.entrySet()) {
            if (GameValue.valueOf(entry.getKey().getFacilityType().getFacilityName().toUpperCase()) == facilityType) {
                facility = entry.getKey();
                break;
            }
        }
        if (facility == null) {
            return -1;
        } else {
            return facility.getFacilityId();
        }
    }


    /**
     * States if the business rule is triggered
     *
     * @return Returns if the business rule is triggered
     */
    public boolean isTriggered() {
        return ((BooleanLiteral) this.condition).getValue();
    }
}
