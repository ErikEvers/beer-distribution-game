package org.han.ica.asd.c.businessrule.parser.ast.comparison;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.BooleanLiteral;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Operation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.OperationValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
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

    @Override
    public void encode(StringBuilder stringBuilder) {
        super.encode(stringBuilder, getChildren(), prefix, suffix);
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
        Collections.addAll(list, left, comparisonOperator, right);
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

    @Override
    public BooleanLiteral resolveCondition() {
        OperationValue operationValueLeft = this.left.getOperationValue();
        OperationValue operationValueRight = this.right.getOperationValue();

        if (operationValueLeft instanceof Operation) {
            Operation operation = (Operation) operationValueLeft;
            operationValueLeft = operation.resolveOperation();
        }

        if (operationValueRight instanceof Operation){
            Operation operation = (Operation) operationValueRight;
            operationValueRight = operation.resolveOperation();
        }


        int valueLeft = ((Value) operationValueLeft).getIntegerValue();
        int valueRight = ((Value) operationValueRight).getIntegerValue();

        switch (this.comparisonOperator.getValue()){
            case NOT:
                return new BooleanLiteral(valueLeft != valueRight);
            case LESS:
                return new BooleanLiteral(valueLeft < valueRight);
            case EQUAL:
                return new BooleanLiteral(valueLeft == valueRight);
            case GREATER:
                return new BooleanLiteral(valueLeft > valueRight);
            default:
                return new BooleanLiteral(false);
        }
    }
}
