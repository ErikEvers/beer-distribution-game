package org.han.ica.asd.c.businessrule.parser.ast.operations;

import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;

public class DivideOperation extends Operation {
    private String prefix = "Div(";
    private String suffix = ")";

    public DivideOperation() {
        super.calculationOperator = new CalculationOperator("/");
    }

    @Override
    public String toString() {
        return prefix + left.toString() + calculationOperator.toString() + right.toString() + suffix;
    }
}
