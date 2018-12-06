package org.han.ica.asd.c.businessrule.parser.ast.operations;


import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;

public class AddOperation extends Operation {
    private String prefix = "Add(";
    private String suffix = ")";

    public AddOperation() {
        super.calculationOperator = new CalculationOperator("+");
    }

    @Override
    public String toString() {
        return prefix + left.toString() + calculationOperator.toString() + right.toString() + suffix;
    }
}
