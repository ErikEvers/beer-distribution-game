package org.han.ica.asd.c.businessrule.parser.ast.operations;

import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;

public class MultiplyOperation extends Operation {
    private String prefix = "Mul(";
    private String suffix = ")";

    public MultiplyOperation() {
        super.calculationOperator = new CalculationOperator("*");
    }

    @Override
    public String toString() {
        return prefix + left.toString() + calculationOperator.toString() + right.toString() + suffix;
    }
}
