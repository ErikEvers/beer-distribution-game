package org.han.ica.asd.c.businessrule.engine;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.Action;
import org.han.ica.asd.c.businessrule.parser.ast.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.Default;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.*;
import org.han.ica.asd.c.businessrule.parser.ast.operators.BooleanOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;

class BusinessRuleFactory {
    ASTNode create(String identifier) {
        switch (identifier) {
            case "A":
                return new Action();
            case "C":
                return new Comparison();
            case "D":
                return new Default();
            case "V":
                return new Value();
            case "AR":
                return new ActionReference();
            case "CV":
                return new ComparisonValue();
            case "CS":
                return new ComparisonStatement();
            case "Add":
                return new AddOperation();
            case "Div":
                return new DivideOperation();
            case "Mul":
                return new MultiplyOperation();
            case "Sub":
                return new SubtractOperation();
            case "CalO":
                return new CalculationOperator();
            case "ComO":
                return new ComparisonOperator();
            case "BoolO":
                return new BooleanOperator();
            default:
                return null;
        }
    }
}
