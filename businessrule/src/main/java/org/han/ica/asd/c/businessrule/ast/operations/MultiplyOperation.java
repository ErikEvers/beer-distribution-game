package org.han.ica.asd.c.businessrule.ast.operations;

import org.han.ica.asd.c.businessrule.ast.operators.CalculationOperator;

public class MultiplyOperation extends Operation {
    public MultiplyOperation() {
        super.calculationOperator = new CalculationOperator("*");
    }

    @Override
    public String toString() {
        return "Mul(" + left.toString() + calculationOperator.toString() + right.toString() + ")";
    }
}
