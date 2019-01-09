package org.han.ica.asd.c.businessrule.engine;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.Default;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.action.Person;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.AddOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.DivideOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.MultiplyOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.SubtractOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.BooleanOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BusinessRuleFactory {
    private static final Logger LOGGER = Logger.getLogger(Logger.class.getName());
    private static final Map<String, Class<? extends ASTNode>> ASTNODE_CLASS_MAP;

    static {
        Map<String, Class<? extends ASTNode>> astNodeClassMap = new TreeMap<>();
        astNodeClassMap.put("A", Action.class);
        astNodeClassMap.put("C", Comparison.class);
        astNodeClassMap.put("D", Default.class);
        astNodeClassMap.put("P", Person.class);
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

    /**
     * Takes the identifier of the business rule script and transforms it into the object of the identifier
     *
     * @param identifier The identifier of the business rule script
     * @return The object which the identifier represents
     */
    public ASTNode create(String identifier) {
        if (ASTNODE_CLASS_MAP.containsKey(identifier)) {
            try {
                return ASTNODE_CLASS_MAP.get(identifier).newInstance();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }
        return null;
    }
}
