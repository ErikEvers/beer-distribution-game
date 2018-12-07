package org.han.ica.asd.c.businessrule.parser.ast.operators;

public class CalculationOperator extends Operator {
    private String prefix = "CalO(";
    private String suffix = ")";
    private String operator;

    public CalculationOperator() {
        this.operator = "";
    }
    public CalculationOperator(String operator) {
        this.operator = operator;
    }

	@Override
    public CalculationOperator addValue(String value) {
        operator = value;
        return this;
    }

    @Override
    public void encode(StringBuilder stringBuilder) {
        stringBuilder.append(prefix).append(operator).append(suffix);
        this.operatorVal = operator;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
