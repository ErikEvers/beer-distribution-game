package org.han.ica.asd.c.businessrule.parser.ast.operators;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;

import java.util.Objects;

public class BooleanOperator extends ASTNode {
    private String prefix = "BoolO(";
    private String suffix = ")";
    private String operator;

    public BooleanOperator(String operator) {
        this.operator = findBooleanOperator(operator);
    }

    private String findBooleanOperator(String operator) {
        if ("and".equals(operator)) {
            return BooleanType.AND.getBooleanSymbol();
        } else {
            return BooleanType.OR.getBooleanSymbol();
        }
    }

    @Override
    public void encode(StringBuilder stringBuilder) {
        stringBuilder.append(prefix).append(operator).append(suffix);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        BooleanOperator booleanOperator = (BooleanOperator) o;
        return Objects.equals(operator, booleanOperator.operator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator);
    }
}
