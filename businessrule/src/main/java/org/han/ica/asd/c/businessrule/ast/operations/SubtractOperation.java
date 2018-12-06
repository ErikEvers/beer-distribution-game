package org.han.ica.asd.c.businessrule.ast.operations;

import org.han.ica.asd.c.businessrule.ast.operators.CalculationOperator;

public class SubtractOperation extends Operation {
    public SubtractOperation() {
        super.calculationOperator = new CalculationOperator("-");
    }

    @Override
    public String toString() {
        return "Sub(" + left.toString() + calculationOperator.toString() + right.toString() + ")";
    }
}
