package org.han.ica.asd.c.businessrule.ast.comparison;

import org.han.ica.asd.c.businessrule.ast.ASTNode;
import org.han.ica.asd.c.businessrule.ast.operations.OperationValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ComparisonValue extends ASTNode {
    private OperationValue operationValue;

    @Override
    public ASTNode addChild(ASTNode child) {
        operationValue = (OperationValue) child;
        return this;
    }

    @Override
    public String toString() {
          return operationValue.toString();
    }

    @Override
    public List<ASTNode> getChildren() {
        List<ASTNode> list = new ArrayList<>();
        list.add(operationValue);
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
        ComparisonValue comparisonValue = (ComparisonValue) o;
        return Objects.equals(operationValue, comparisonValue.operationValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationValue);
    }
}
