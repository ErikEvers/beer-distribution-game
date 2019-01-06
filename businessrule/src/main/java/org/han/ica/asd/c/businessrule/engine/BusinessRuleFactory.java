package org.han.ica.asd.c.businessrule.engine;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.Default;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.*;
import org.han.ica.asd.c.businessrule.parser.ast.operators.BooleanOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;

import java.util.*;

class BusinessRuleFactory {
    private static final Map<String, Class<? extends ASTNode>> ASTNODE_CLASS_MAP;
    static {
        Map<String, Class<? extends ASTNode>> astNodeClassMap = new TreeMap<>();
        astNodeClassMap.put("A", Action.class);
        astNodeClassMap.put("C", Comparison.class);
        astNodeClassMap.put("D", Default.class);
        astNodeClassMap.put("V", Value.class);
        astNodeClassMap.put("AR", ActionReference.class);
        astNodeClassMap.put("CV", ComparisonValue.class);
        astNodeClassMap.put("CS", ComparisonStatement.class);
        astNodeClassMap.put("Add", AddOperation.class);
        astNodeClassMap.put("Div", DivideOperation.class);
        astNodeClassMap.put("Mul", MultiplyOperation.class);
        astNodeClassMap.put("Sub", SubtractOperation.class);
        astNodeClassMap.put("CalO", CalculationOperator.class);
        astNodeClassMap.put("ComO", ComparisonOperator.class);
        astNodeClassMap.put("BoolO", BooleanOperator.class);
        ASTNODE_CLASS_MAP = Collections.unmodifiableMap(astNodeClassMap);
    }

    ASTNode create(String identifier) {
        try {
            return ASTNODE_CLASS_MAP.get(identifier).newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}
