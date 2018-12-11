package org.han.ica.asd.c.businessrule.parser.ast.comparison;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.BooleanLiteral;
import org.han.ica.asd.c.businessrule.parser.ast.operators.BooleanOperator;

import java.util.ArrayList;
import java.util.Collections;
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

    @Override
    public void encode(StringBuilder stringBuilder) {
        super.encode(stringBuilder, getChildren(), prefix, suffix);
    }

    @Override
    public List<ASTNode> getChildren() {
        List<ASTNode> list = new ArrayList<>();
        list.add(left);
        if (booleanOperator != null && right != null) {
            Collections.addAll(list, booleanOperator, right);
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

    public BooleanLiteral resolveComparisonStatement() {
        boolean result = false;

        if (this.left instanceof ComparisonStatement) {
            this.left = ((ComparisonStatement) this.left).resolveComparisonStatement();
        } else if (this.left instanceof Comparison) {
            this.left = ((Comparison) this.left).resolveComparison();
        }

        if (this.right instanceof ComparisonStatement) {
            this.right = ((ComparisonStatement) this.right).resolveComparisonStatement();
        } else if (this.right instanceof Comparison) {
            this.right = ((Comparison) this.right).resolveComparison();
        }

        if (this.booleanOperator != null) {
            if (this.left instanceof BooleanLiteral && this.right instanceof BooleanLiteral) {
                BooleanLiteral booleanLiteralLeft = (BooleanLiteral) this.left;
                BooleanLiteral booleanLiteralRight = (BooleanLiteral) this.right;

                switch (booleanOperator.getValue()) {
                    case AND:
                        result = booleanLiteralLeft.getValue() && booleanLiteralRight.getValue();
                        break;
                    case OR:
                        result = booleanLiteralLeft.getValue() || booleanLiteralRight.getValue();
                        break;
                    default:
                        break;
                }
            }
        } else {
            BooleanLiteral booleanLiteral = (BooleanLiteral) this.left;
            result = booleanLiteral.getValue();
        }

        return new BooleanLiteral(result);
    }
}
