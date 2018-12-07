package org.han.ica.asd.c.businessrule.parser.ast.operators;

public class BooleanOperator extends Operator {
    private String prefix = "BoolO(";
    private String suffix = ")";

    public BooleanOperator(String operator) {
        this.operatorVal = findBooleanOperator(operator);
    }

    @Override
    public BooleanOperator addValue(String value) {
        operatorVal = value;
        return this;
    }

    private String findBooleanOperator(String operator) {
        if ("and".equals(operator)) {
            return BooleanType.AND.getBooleanSymbol();
        } else {
            return BooleanType.OR.getBooleanSymbol();
        }
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public void encode(StringBuilder stringBuilder) {
        stringBuilder.append(prefix).append(operatorVal).append(suffix);
    }
}
