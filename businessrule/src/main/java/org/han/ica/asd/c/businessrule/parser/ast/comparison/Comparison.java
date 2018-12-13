package org.han.ica.asd.c.businessrule.parser.ast.comparison;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Comparison extends Expression {
    private String prefix = "C(";
    private String suffix = ")";
    private ComparisonValue left;
    private ComparisonOperator comparisonOperator;
    private ComparisonValue right;

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
     * Adds a child ASTNode to a parent(this) ASTNode
     *
     * @param child Child that has the be added to this ASTNode
     * @return Returns itself so that it can be used immediately
     */
    @Override
    public ASTNode addChild(ASTNode child) {
        if (child instanceof ComparisonOperator) {
            this.comparisonOperator = (ComparisonOperator) child;
        } else {
            if (left == null) {
                this.left = (ComparisonValue) child;
            } else {
                this.right = (ComparisonValue) child;
            }
        }
        return this;
    }

    /**
     * Return the children that are assigned to the ASTNode
     *
     * @return Return the children
     */
    @Override
    public List<ASTNode> getChildren() {
        List<ASTNode> list = new ArrayList<>();
        Collections.addAll(list, left, comparisonOperator, right);
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
        Comparison comparison = (Comparison) o;
        return Objects.equals(left, comparison.left) &&
                Objects.equals(comparisonOperator, comparison.comparisonOperator) &&
                Objects.equals(right, comparison.right);
    }

    /**
     * Hash function used for unit testing
     *
     * @return Returns the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(left, comparisonOperator, right);
    }
}
