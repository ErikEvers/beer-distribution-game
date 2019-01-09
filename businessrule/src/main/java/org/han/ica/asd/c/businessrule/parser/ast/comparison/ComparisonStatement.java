package org.han.ica.asd.c.businessrule.parser.ast.comparison;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.BooleanLiteral;
import org.han.ica.asd.c.businessrule.parser.ast.operators.BooleanOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.BooleanType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ComparisonStatement extends Expression {
    private static final String PREFIX = "CS(";
    private Expression left;
    private BooleanOperator booleanOperator;
    private Expression right;

    /**
     * Adds a child ASTNode to a parent(this) ASTNode
     *
     * @param child Child that has the be added to this ASTNode
     * @return Returns itself so that it can be used immediately
     */
    @Override
    public ASTNode addChild(ASTNode child) {
        if (child instanceof BooleanOperator) {
            this.booleanOperator = (BooleanOperator) child;
        } else {
            if (left == null) {
                this.left = (Expression) child;
            } else {
                this.right = (Expression) child;
            }
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
        list.add(left);
        if (booleanOperator != null && right != null) {
            Collections.addAll(list, booleanOperator, right);
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
        ComparisonStatement comparisonStatement = (ComparisonStatement) o;
        return Objects.equals(left, comparisonStatement.left) &&
                Objects.equals(booleanOperator, comparisonStatement.booleanOperator) &&
                Objects.equals(right, comparisonStatement.right);
    }

    /**
     * Hash function used for unit testing
     *
     * @return Returns the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(left, booleanOperator, right);
    }

    /**
     * Resolves the {@link ComparisonStatement} to a single {@link BooleanLiteral}
     *
     * @return the {@link BooleanLiteral} that resolves from the ComparisonStatement
     */
    @Override
    public BooleanLiteral resolveCondition() {
        if (this.booleanOperator != null) {
            if (booleanOperator.getValue() == BooleanType.AND) {
                return new BooleanLiteral(this.left.resolveCondition().getValue() && this.right.resolveCondition().getValue());
            } else if (booleanOperator.getValue() == BooleanType.OR) {
                return new BooleanLiteral(this.left.resolveCondition().getValue() || this.right.resolveCondition().getValue());
            }
        }

        return this.left.resolveCondition();
    }
}
