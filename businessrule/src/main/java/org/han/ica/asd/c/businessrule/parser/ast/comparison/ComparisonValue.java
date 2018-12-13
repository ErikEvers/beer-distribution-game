package org.han.ica.asd.c.businessrule.parser.ast.comparison;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.operations.OperationValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ComparisonValue extends ASTNode {
    private String prefix = "CV(";
    private String suffix = ")";
    private OperationValue operationValue;

    /**
     * Gets the operationValue
     *
     * @return Returns the operationValue belonging to the {@link ComparisonValue}
     */
    public OperationValue getOperationValue() {
        return this.operationValue;
    }

    /**
     * Sets the operationValue
     *
     * @param operationValue the operationValue to set in the {@link ComparisonValue}
     */
    public void setOperationValue(OperationValue operationValue) {
        this.operationValue = operationValue;
    }

    /**
     * Adds a child ASTNode to a parent(this) ASTNode
     *
     * @param child Child that has the be added to this ASTNode
     * @return Returns itself so that it can be used immediately
     */
    @Override
    public ASTNode addChild(ASTNode child) {
        operationValue = (OperationValue) child;
        return this;
    }

    /**
     * Adds an operationValue to the ComparisonValue
     *
     * @param value the value to add to the {@link ComparisonValue}
     * @return the own instance
     */
    @Override
    public ComparisonValue addValue(String value) {
        operationValue.addValue(value);
        return this;
    }

    /**
     * Encodes the parsed tree in a single string so that it can be stored in the database
     *
     * @param stringBuilder Stringbuilder that is used to encode the tree
     */
    @Override
    public void encode(StringBuilder stringBuilder) {
        super.encode(stringBuilder, getChildren(), prefix, suffix);
    }

    /**
     * Return the children that are assigned to the ASTNode
     *
     * @return Return the children
     */
    @Override
    public List<ASTNode> getChildren() {
        List<ASTNode> list = new ArrayList<>();
        list.add(operationValue);
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
        ComparisonValue comparisonValue = (ComparisonValue) o;
        return Objects.equals(operationValue, comparisonValue.operationValue);
    }

    /**
     * Hash function used for unit testing
     *
     * @return Returns the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(operationValue);
    }
}
