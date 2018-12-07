package org.han.ica.asd.c.businessrule.parser.ast.operators;

public class CalculationOperator extends Operator {
    private String prefix = "CalO(";
    private String suffix = ")";

    public CalculationOperator() {
        this.operatorVal = "";
    }
    public CalculationOperator(String operator) {
        this.operatorVal = operator;
    }

	@Override
    public CalculationOperator addValue(String value) {
        operatorVal = value;
        return this;
    }

    @Override
    public void encode(StringBuilder stringBuilder) {
        stringBuilder.append(prefix).append(operatorVal).append(suffix);
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
