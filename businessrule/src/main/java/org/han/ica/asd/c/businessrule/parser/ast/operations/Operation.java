package org.han.ica.asd.c.businessrule.parser.ast.operations;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Operation extends OperationValue {
    private OperationValue left;
    CalculationOperator calculationOperator;
    private OperationValue right;

    /**
     * Adds a child ASTNode to a parent(this) ASTNode
     * @param child Child that has the be added to this ASTNode
     * @return Returns itself so that it can be used immediately
     */
    @Override
    public ASTNode addChild(ASTNode child) {
        if (child instanceof CalculationOperator) {
            calculationOperator = (CalculationOperator) child;
        } else {
            if (left == null) {
                left = (OperationValue) child;
            } else {
                right = (OperationValue) child;
            }
        }
        return this;
    }

    /**
     * Return the children that are assigned to the ASTNode
     * @return Return the children
     */
    @Override
    public List<ASTNode> getChildren() {
        List<ASTNode> list = new ArrayList<>();
        list.add(left);
        if (calculationOperator != null && right != null) {
            Collections.addAll(list,calculationOperator,right);
        }
        return list;
    }

    /**
     * Equals function used for unit testing
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
        Operation operation = (Operation) o;
        return Objects.equals(left, operation.left) &&
                Objects.equals(calculationOperator, operation.calculationOperator) &&
                Objects.equals(right, operation.right);
    }

    /**
     * Hash function used for unit testing
     * @return Returns the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(left, calculationOperator, right);
    }

    /**
     * Encodes the parsed tree in a single string so that it can be stored in the database
     * @param stringBuilder Stringbuilder that is used to encode the tree
     */
    public void encode(StringBuilder stringBuilder, String prefix, String suffix) {
        super.encode(stringBuilder,getChildren(),prefix,suffix);
    }
}
