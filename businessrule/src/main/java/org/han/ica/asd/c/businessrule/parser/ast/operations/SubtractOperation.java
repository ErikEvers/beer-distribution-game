package org.han.ica.asd.c.businessrule.parser.ast.operations;

import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;

public class SubtractOperation extends Operation {
    private String prefix = "Sub(";
    private String suffix = ")";

    public SubtractOperation() {
        super.calculationOperator = new CalculationOperator("-");
    }

    @Override
    public String toString() {
        return prefix + left.toString() + calculationOperator.toString() + right.toString() + suffix;
    }
}
