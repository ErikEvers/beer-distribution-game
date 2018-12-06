package org.han.ica.asd.c.businessrule.ast.operations;

import org.han.ica.asd.c.businessrule.ast.ASTNode;
import org.han.ica.asd.c.businessrule.ast.operators.CalculationOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Operation extends OperationValue {
    OperationValue left;
    CalculationOperator calculationOperator;
    OperationValue right;

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

    @Override
    public List<ASTNode> getChildren() {
        List<ASTNode> list = new ArrayList<>();
        list.add(left);
        if (calculationOperator != null && right != null) {
            list.add(calculationOperator);
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
        Operation operation = (Operation) o;
        return Objects.equals(left, operation.left) &&
                Objects.equals(calculationOperator, operation.calculationOperator) &&
                Objects.equals(right, operation.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, calculationOperator, right);
    }
}
