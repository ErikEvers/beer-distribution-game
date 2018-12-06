package org.han.ica.asd.c.businessrule.parser.ast.comparison;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.operators.BooleanOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ComparisonStatement extends Expression {
    private String prefix = "CS(";
    private String suffix = ")";
    private Expression left;
    private BooleanOperator booleanOperator;
    private Expression right;

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

    public void encode(StringBuilder stringBuilder) {
        super.encode(stringBuilder,getChildren(),prefix,suffix);
    }

    @Override
    public List<ASTNode> getChildren() {
        List<ASTNode> list = new ArrayList<>();
        list.add(left);
        if (booleanOperator != null && right != null) {
            list.add(booleanOperator);
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
        ComparisonStatement comparisonStatement = (ComparisonStatement) o;
        return Objects.equals(left, comparisonStatement.left) &&
                Objects.equals(booleanOperator, comparisonStatement.booleanOperator) &&
                Objects.equals(right, comparisonStatement.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, booleanOperator, right);
    }
}
