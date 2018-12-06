package org.han.ica.asd.c.businessrule.ast.operations;


import org.han.ica.asd.c.businessrule.ast.operators.CalculationOperator;

public class AddOperation extends Operation {
    public AddOperation() {
        super.calculationOperator = new CalculationOperator("+");
    }

    @Override
    public String toString() {
        return "Add(" + left.toString() + calculationOperator.toString() + right.toString() + ")";
    }
}
