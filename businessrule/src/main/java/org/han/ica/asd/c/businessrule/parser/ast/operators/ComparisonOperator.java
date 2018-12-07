package org.han.ica.asd.c.businessrule.parser.ast.operators;

public class ComparisonOperator extends Operator {
    private String prefix = "ComO(";
    private String suffix = ")";

    public ComparisonOperator(String operator) {
        this.operatorVal = findComparisonOperator(operator);
    }

    @Override
    public ComparisonOperator addValue(String value) {
        operatorVal = value;
        return this;
    }

    private String findComparisonOperator(String operator) {
        if ("is".equals(operator) || "=".equals(operator) || "equal".equals(operator)) {
            return ComparisonType.EQUAL.getComparisonSymbol();
        } else if ("!=".equals(operator) || operator.contains("not equal")) {
            return ComparisonType.NOT.getComparisonSymbol();
        } else if (operator.contains("greater") || operator.contains("higher")) {
            return ComparisonType.GREATER.getComparisonSymbol();
        } else {
            return ComparisonType.LESS.getComparisonSymbol();
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
