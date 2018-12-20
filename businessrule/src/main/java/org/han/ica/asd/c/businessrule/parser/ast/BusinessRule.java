package org.han.ica.asd.c.businessrule.parser.ast;

import org.han.ica.asd.c.businessrule.mocks.GameData;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Operation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.OperationValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.gamevalue.GameValue;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.util.*;
import java.util.regex.Pattern;

public class BusinessRule extends ASTNode {
    private static final String PREFIX = "BR(";
    private Condition condition;
    private Action action;

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
     * When its a leaf (a Value) it replaces the value with the game data(gameData)
     * @param round data of a gameData
     * @param facilityId identifier of the facility
     */
    public void substituteTheVariablesOfBusinessruleWithGameData(Round round, int facilityId){
        int left = 0;
        int right = 2;
        int actionValue = 1;

        findLeafAndReplace(condition.getChildren().get(left),round,facilityId);
        if(hasMultipleChildren(condition)) {
            findLeafAndReplace(condition.getChildren().get(right),round, facilityId);
        }
        if (action != null) {
            findLeafAndReplace(action.getChildren().get(actionValue),round, facilityId);
        }
    }

    /***
     * finds the leaf and replaces the leaf with game data.
     *
     * @param astNode a node of the tree
     * @param facilityId
     */
    private void findLeafAndReplace(ASTNode astNode, Round round, int facilityId){
        int left = 0;
        int right = 2;
        if(astNode instanceof Value){
            replace((Value) astNode,round,facilityId);
        }
        if (!astNode.getChildren().isEmpty()) {
            findLeafAndReplace(astNode.getChildren().get(left),round, facilityId);
            if (hasMultipleChildren(astNode)) {
                findLeafAndReplace(astNode.getChildren().get(right),round, facilityId);
            }
        }
    }


    /***
     * checks if the node has more than one child
     * @param astNode a node of the tree
     * @return true if the node has more than one child
     */
    private boolean hasMultipleChildren(ASTNode astNode){
        int oneChild = 1;
        return astNode.getChildren().size()>oneChild;
    }

    /***
     *replaces the value with the replacementvalue(gamedata)
     *
     * @param value a node of the tree
     * @param facilityId
     */
    private void replace(Value value, Round round, int facilityId) {
        String REGEXHASCHARACTERS = "[a-zA-Z ]+";
        String currentVariable = value.getFirstPartVariable();
        if(Pattern.matches(REGEXHASCHARACTERS,value.getFirstPartVariable())) {
            GameValue gameValue = getGameValue(currentVariable);
            if (gameValue != null) {
                replaceValue(gameValue, value,round, facilityId, 0);
            }
        }
        if(value.getValue().size()>1) {
            String secondVariable = value.getSecondPartVariable();
            if (Pattern.matches(REGEXHASCHARACTERS, value.getSecondPartVariable())) {
                GameValue gameValue = getGameValue(secondVariable);
                if (gameValue != null) {
                    replaceValue(gameValue, value, round, facilityId, 1);
                }
            }
        }
    }

    private GameValue getGameValue(String variable){
        for(GameValue gameValue: GameValue.values()){
            if(gameValue.contains(variable)){
                return gameValue;
            }
        }
        return null;
    }
    private void replaceValue(GameValue gameValue,Value value,Round round,int facilityId, int part){
        if(gameValue !=null) {
            String newReplacementValue = getReplacementValue(gameValue,round,facilityId);
            value.replaceValueWithValue(newReplacementValue,part);
        }
    }

    public String getReplacementValue(GameValue gameValue,Round round, int facilityId) {
        switch (gameValue){
            case ORDERED:
                return getValueFromHashmapInHasmap(round.getTurnOrder(),facilityId);
            case STOCK:
                return  getValue(round.getTurnStock(),facilityId);
            case BUDGET:
                return  getValue(round.getRemainingBudget(),facilityId);
            case BACKLOG:
                return getValueFromHashmapInHasmap(round.getTurnBackOrder(),facilityId);
            case INCOMINGORDER:
                return getValueFromHashmapInHasmap(round.getTurnReceived(),facilityId);
            case OUTGOINGGOODS:
                return getValueFromHashmapInHasmap(round.getTurnDeliver(),facilityId);
            default:
                return String.valueOf(getFacilityIdBasedOnType(round.getTurnStock(), gameValue));
        }
    }

    private String getValueFromHashmapInHasmap(Map<Facility,Map<Facility,Integer>> map, int facilityId){
        Map.Entry<Facility,Map<Facility,Integer>> entry = map.entrySet().iterator().next();
        Map<Facility,Integer> value = entry.getValue();
        return getValue(value,facilityId);
    }

    private String getValue(Map<Facility,Integer> map,int facilityId){
        for(Map.Entry<Facility,Integer> entry:map.entrySet()){
            if(entry.getKey().getFacilityId()==facilityId){
                return entry.getValue().toString();
            }
        }
        return "";
    }

    private int getFacilityIdBasedOnType(Map<Facility,Integer> map, GameValue facilityType){
        Facility facility = null;
        for(Map.Entry<Facility,Integer> entry:map.entrySet()){
            if(GameValue.valueOf(entry.getKey().getFacilityType().getFacilityName())==facilityType){
                facility= entry.getKey();
                break;
            }
        }
        return facility==null?null:facility.getFacilityId();
    }
}
