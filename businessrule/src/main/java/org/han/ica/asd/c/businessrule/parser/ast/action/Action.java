package org.han.ica.asd.c.businessrule.parser.ast.action;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.NodeConverter;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.operations.OperationValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Action extends ASTNode {
    private static final String PREFIX = "A(";

    private ActionReference actionName;
    private OperationValue operation;
    private Person person;
    private ComparisonStatement comparisonStatement;

    //@Inject
    private IBusinessRuleStore businessRuleStore;

    @Inject
    private NodeConverter nodeConverter;

    /**
     * Adds a child ASTNode to a parent(this) ASTNode
     *
     * @param child Child that has the be added to this ASTNode
     * @return Returns itself so that it can be used immediately
     */
    @Override
    public ASTNode addChild(ASTNode child) {
        if (child instanceof ActionReference) {
            this.actionName = (ActionReference) child;
        } else if (child instanceof OperationValue){
            this.operation = (OperationValue) child;
        } else if (child instanceof ComparisonStatement) {
            this.comparisonStatement = (ComparisonStatement) child;
        } else {
            this.person = (Person) child;
        }
        return this;
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
        Collections.addAll(list, actionName, operation);

        if(person != null){
            list.add(person);
        }

        if(comparisonStatement != null){
            list.add(comparisonStatement);
        }
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
        Action action = (Action) o;
        return Objects.equals(actionName, action.actionName) &&
                Objects.equals(operation, action.operation) &&
                Objects.equals(person, action.person) &&
                Objects.equals(comparisonStatement, action.comparisonStatement);
    }

    /**
     * Hash function used for unit testing
     *
     * @return Returns the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(actionName, operation, person, comparisonStatement);
    }

    /**
     * Get type of action as String
     *
     * @return Returns a String that determines te type of the action.
     */
    public String getType() {
        return this.actionName.getAction();
    }

    /**
     * Gets the amount of the Action
     *
     * @return Returns the amount
     */
    public int getAmount() {
        return ((Value) operation).getIntegerValue();
    }

    /**
     * Gets the id of the facility of the Action
     *
     * @return Returns the facility id of the receiving end
     */
    public int getFacilityId() {
        int facilityId;

        if(person != null){
            facilityId = nodeConverter.getFacilityId(person.getPerson());
        } else {
            // TO-DO: Randomly pick one below/above
            facilityId = 1;
        }

        return facilityId;
    }

    /***
     * gets the right child of an action
     * @return operation
     */
    @Override
    public ASTNode getRightChild() {
        return operation;
    }
}