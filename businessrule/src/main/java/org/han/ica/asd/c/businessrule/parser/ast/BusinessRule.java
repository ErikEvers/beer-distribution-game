package org.han.ica.asd.c.businessrule.parser.ast;

import org.han.ica.asd.c.businessrule.mocks.GenerateOrderMock;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Operation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.OperationValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.gamevalue.GAME_VALUE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BusinessRule extends ASTNode {
    private static final String PREFIX = "BR(";
    private Condition condition;
    private Action action;
    private GenerateOrderMock turn;
    private int facilityId;

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
     * replaces the variables of the business rule with data using dept first
     * When its a leaf (a Value) it replaces the value with the game data(turn)
     * @param turn data of a turn
     * @param facilityId identifier of the facility
     */
    public void substituteBusinessRuleWithData(GenerateOrderMock turn, int facilityId){
        int left = 0;
        int right = 2;
        int actionValue = 1;
        this.turn = turn;
        this.facilityId = facilityId;
        findLeafAndReplace(condition.getChildren().get(left));
        if(hasChildren(condition)) {
            findLeafAndReplace(condition.getChildren().get(right));
        }
        if (action != null) {
            findLeafAndReplace(action.getChildren().get(actionValue));
        }
    }

    /***
     * finds the leaf and replaces the leaf with game data.
     *
     * @param astNode a node of the tree
     */
    private void findLeafAndReplace(ASTNode astNode){
        int left = 0;
        int right = 2;
        if(astNode instanceof Value){
            replace((Value) astNode);
        }
        if (!astNode.getChildren().isEmpty()) {
            findLeafAndReplace(astNode.getChildren().get(left));
            if (hasChildren(astNode)) {
                findLeafAndReplace(astNode.getChildren().get(right));
            }
        }
    }


    /***
     * checks if the node has more than one child
     * @param astNode a node of the tree
     * @return true if the node has more than one child
     */
    private boolean hasChildren(ASTNode astNode){
        int oneChild = 1;
        return astNode.getChildren().size()>oneChild;
    }

    /***
     *replaces the value with the replacementvalue(gamedata) like stock with 10
     *
     * @param value a node of the tree
     */
    private void replace(Value value) {
        GAME_VALUE gamevalue = GAME_VALUE.getGameValue(value.getValue());
        if(gamevalue!=null) {
            value.replaceValue(String.valueOf(turn.getReplacementValue(gamevalue, facilityId)));
        }
    }
}
