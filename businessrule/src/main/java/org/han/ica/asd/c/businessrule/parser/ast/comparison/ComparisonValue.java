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

    public OperationValue getOperationValue() {
        return this.operationValue;
    }

    public void setOperationValue(OperationValue operationValue) {
        this.operationValue = operationValue;
    }

    @Override
    public ASTNode addChild(ASTNode child) {
        operationValue = (OperationValue) child;
        return this;
    }

    @Override
    public ComparisonValue addValue(String value) {
        operationValue.addValue(value);
        return this;
    }

    @Override
    public void encode(StringBuilder stringBuilder) {
        super.encode(stringBuilder, getChildren(), prefix, suffix);
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
