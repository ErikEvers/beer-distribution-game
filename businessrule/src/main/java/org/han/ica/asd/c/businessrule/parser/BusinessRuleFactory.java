package nl.han.bre;

import nl.han.asd.prototype.parser.ast.*;
import nl.han.asd.prototype.parser.ast.comparison.Comparison;
import nl.han.asd.prototype.parser.ast.comparison.ComparisonStatement;
import nl.han.asd.prototype.parser.ast.comparison.ComparisonValue;
import nl.han.asd.prototype.parser.ast.operations.*;
import nl.han.asd.prototype.parser.ast.operators.BooleanOperator;
import nl.han.asd.prototype.parser.ast.operators.CalculationOperator;
import nl.han.asd.prototype.parser.ast.operators.ComparisonOperator;

public class BusinessRuleFactory {
    //      BR(CS(C(V(inventory)CO(>)V(20))BO(&&)CS(C(V(round)CO(==)V(3))))A(order V(40)))default order 10, BR(D()A(order V(10)))

    public ASTNode create(Class<?> parentClassType, String identifier) {
        switch (identifier) {
            case "A":       return new Action();
            case "C":       return new Comparison();
            case "D":       return new Default();
            case "V":       return (parentClassType == Comparison.class) ? new ComparisonValue().addChild(new Value()) : new Value();
            case "CS":      return new ComparisonStatement();
            case "DO":      return new DefaultOperation();
            case "Add":     return new AddOperation();
            case "Div":     return new DivideOperation();
            case "Mul":     return new MultiplyOperation();
            case "Sub":     return new SubtractOperation();
            case "CalO":    return new CalculationOperator("");
            case "ComO":    return new ComparisonOperator("");
            case "BoolO":   return new BooleanOperator("");
            case "order":   return new ActionReference(identifier);
            default:        return null;
        }
    }
}
