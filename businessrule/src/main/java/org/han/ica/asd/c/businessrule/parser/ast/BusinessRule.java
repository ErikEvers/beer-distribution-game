package org.han.ica.asd.c.businessrule.parser.ast;


import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.Person;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Operation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.OperationValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.replacer.Replacer;
import org.han.ica.asd.c.gamevalue.GameValue;
import org.han.ica.asd.c.model.domain_objects.Round;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

public class BusinessRule extends ASTNode {
    private Replacer replacer;
    private Condition condition;
    private Action action;
    private static final String PREFIX = "BR(";

    private GameValue attribute;
    private GameValue highestOrLowest;
    private GameValue facilityType;

    public BusinessRule() {
    }

    @Inject
    public BusinessRule(Provider<Replacer> replacer) {
        this.replacer = replacer.get();
    }

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
     * Transforms an {@link ASTNode} based on his type. Will calculate values with percentages to it's result
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

        if(node instanceof Action){
            Action actionNode = (Action) node;
            OperationValue operationValue = actionNode.getOperationValue();
            if (operationValue instanceof Operation) {
                actionNode.setOperationValue(((Operation) operationValue).resolveOperation());
            }
        }

        if(node instanceof Value) {
            Value value = ((Value)node);
            Integer integerValue = value.getIntegerValue();
            value.resetValues().addValue(String.valueOf(integerValue));
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
        super.encode(stringBuilder, getChildren(), PREFIX);
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


    @Override
    public ASTNode getLeftChild() {
        return condition;
    }

    @Override
    public ASTNode getRightChild() {
        return this.getAction();
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
        findLeafAndReplaceComparisonStatement(condition, round, facilityId);
        if(action.hasPerson()) {
            facilityType = getFacilityType(action.getPerson());
        }
        findAttributes(action.getComparisonStatement());
        if (action.hasPerson() && this.attribute != null && this.facilityType != null && this.highestOrLowest != null) {
            replacePerson(round);
        } else {
            findLeafAndReplaceComparisonStatement(action.getComparisonStatement(), round, facilityId);
            findLeafAndReplaceComparisonStatement(action.getRightChild(), round, facilityId);
        }
    }

    private void replacePerson(Round round) {

        if (this.attribute != null && this.highestOrLowest != null && this.facilityType != null) {
            replacer.replacePerson(action, round, facilityType, attribute, highestOrLowest);
        }
    }

    /***
     * Finds the leaf of the astnode and replaces the value
     *
     * @param astNode the node of the ast
     * @param round the game data, used to replace data in function replace(Value value, int facilityId)
     * @param facilityId the id of the facility
     */
    private void findLeafAndReplaceComparisonStatement(ASTNode astNode, Round round, int facilityId) {
        if (astNode instanceof Value) {
            replacer.replace((Value) astNode, round, facilityId);
        }
        if (astNode != null) {
            findLeafAndReplaceComparisonStatement(astNode.getLeftChild(), round, facilityId);
            if (hasMultipleChildren(astNode)) {
                findLeafAndReplaceComparisonStatement(astNode.getRightChild(), round, facilityId);
            }
        }
    }


    /***
     * finds the leaf and replaces the action
     * @param astNode a node of the tree
     */
    private void findAttributes(ASTNode astNode) {
        if (astNode instanceof Value) {
            GameValue foundAttribute = getAttribute((Value) astNode);
            GameValue foundHighestOrLowest = getHighestOrLowest((Value) astNode);
            if (this.attribute == null) {
                this.attribute = foundAttribute;
            }
            if (this.highestOrLowest == null) {
                this.highestOrLowest = foundHighestOrLowest;
            }
        }
        if (astNode != null) {
            findAttributes(astNode.getLeftChild());
            if (hasMultipleChildren(astNode)) {
                findAttributes(astNode.getRightChild());
            }
        }
    }

    /***
     * The value that probably contains an attribute
     * @param value
     * @return the corresponding game value
     */
    private GameValue getAttribute(Value value) {
        if (containsAttribute(value, GameValue.STOCK.getValue())) {
            return GameValue.STOCK;
        } else if (containsAttribute(value, GameValue.ORDERED.getValue())) {
            return GameValue.ORDERED;
        } else if (containsAttribute(value, GameValue.OUTGOINGGOODS.getValue())) {
            return GameValue.OUTGOINGGOODS;
        } else if (containsAttribute(value, GameValue.BACKLOG.getValue())) {
            return GameValue.BACKLOG;
        } else if (containsAttribute(value, GameValue.INCOMINGORDER.getValue())) {
            return GameValue.INCOMINGORDER;
        } else if (containsAttribute(value, GameValue.BUDGET.getValue())) {
            return GameValue.BUDGET;
        }
        return null;
    }

    /***
     * Checks if the node has more than one child
     * @param astNode a node of the tree
     * @return true if the node has more than one child
     */
    private boolean hasMultipleChildren(ASTNode astNode) {
        return astNode.getChildren().size() > 1;
    }

    /**
     * States if the business rule is triggered
     *
     * @return Returns if the business rule is triggered
     */
    public boolean isTriggered() {
        return ((BooleanLiteral) this.condition).getValue();
    }

    /***
     * The given facility type
     * @param person the person which contains the facility type
     * @return the corresponding facility
     */
    private GameValue getFacilityType(Person person) {
        if (containsGameValue(person.getPerson(), GameValue.FACTORY.getValue())) {
            return GameValue.FACTORY;
        } else if (containsGameValue(person.getPerson(), GameValue.WHOLESALER.getValue())) {
            return GameValue.WHOLESALER;
        } else if (containsGameValue(person.getPerson(), GameValue.REGIONALWAREHOUSE.getValue())) {
            return GameValue.REGIONALWAREHOUSE;
        } else if (containsGameValue(person.getPerson(), GameValue.RETAILER.getValue())) {
            return GameValue.RETAILER;
        }
        return null;
    }

    /***
     * Checks if the given value contains an attribute
     * @param value the given value
     * @param items the collection which holds the attributes
     * @return true if the value contains an attribute
     */
    private boolean containsAttribute(Value value, String[] items) {
        String part1 = value.getFirstPartVariable();
        String part2 = "";
        if (value.getValue().size() > 1) {
            part2 = value.getSecondPartVariable();
        }
        return containsGameValue(part1, items) || containsGameValue(part2, items);
    }

    /***
     * Checks if the string is an game value
     * @param inputStr the string to check
     * @param items the collection of gamevalues
     * @return returns true if the game value has been found
     */
    private boolean containsGameValue(String inputStr, String[] items) {
        return Arrays.stream(items).parallel().anyMatch(inputStr::contains);
    }

    /***
     * Checks if the value has the game value highest or lowest
     * @param value the given value
     * @returns highest if value has highest, returns lowest if the value has lowest and returns null of it doesn't contain highest or lowest.
     */
    private GameValue getHighestOrLowest(Value value) {
        if (containsAttribute(value, GameValue.HIGHEST.getValue())) {
            return GameValue.HIGHEST;
        } else if (containsAttribute(value, GameValue.LOWEST.getValue())) {
            return GameValue.LOWEST;
        }
        return null;
    }
}
