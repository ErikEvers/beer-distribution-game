package org.han.ica.asd.c.businessrule.ast.operations;

import org.han.ica.asd.c.businessrule.ast.operators.CalculationOperator;

public class DivideOperation extends Operation {
    public DivideOperation() {
        super.calculationOperator = new CalculationOperator("/");
    }

    @Override
    public String toString() {
        return "Div(" + left.toString() + calculationOperator.toString() + right.toString() + ")";
    }
}
