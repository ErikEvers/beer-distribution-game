package org.han.ica.asd.c.businessrule.ast.comparison;

import org.han.ica.asd.c.businessrule.ast.ASTNode;
import org.han.ica.asd.c.businessrule.ast.operators.ComparisonOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Comparison extends Expression {
    private ComparisonValue left;
    private ComparisonOperator comparisonOperator;
    private ComparisonValue right;

    @Override
    public String toString() {
        return "C(" + left.toString() + comparisonOperator.toString() + right.toString() + ")";
    }

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

    @Override
    public List<ASTNode> getChildren() {
        List<ASTNode> list = new ArrayList<>();
        list.add(left);
        if (comparisonOperator != null && right != null) {
            list.add(comparisonOperator);
            list.add(right);
        }
        return list;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(left, comparisonOperator, right);
    }
}
