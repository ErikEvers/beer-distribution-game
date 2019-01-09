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

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BusinessRuleFactory implements IBusinessRuleFactory{
    private static final Logger LOGGER = Logger.getLogger(Logger.class.getName());
    private final Map<String, Provider<? extends ASTNode>> astNodeProviderMap;

    @Inject
    public BusinessRuleFactory(
            Provider<Action> actionProvider,
            Provider<Comparison> comparisonProvider,
            Provider<Default> defaultProvider,
            Provider<Person> personProvider,
            Provider<Value> valueProvider,
            Provider<ActionReference> actionReferenceProvider,
            Provider<ComparisonValue> comparisonValueProvider,
            Provider<ComparisonStatement> comparisonStatementProvider,
            Provider<AddOperation> addOperationProvider,
            Provider<DivideOperation> divideOperationProvider,
            Provider<MultiplyOperation> multiplyOperationProvider,
            Provider<SubtractOperation> subtractOperationProvider,
            Provider<CalculationOperator> calculationOperatorProvider,
            Provider<ComparisonOperator> comparisonOperatorProvider,
            Provider<BooleanOperator> booleanOperatorProvider) {
        Map<String, Provider<? extends ASTNode>> astNodeClassMap = new TreeMap<>();
        astNodeClassMap.put("A", actionProvider);
        astNodeClassMap.put("C", comparisonProvider);
        astNodeClassMap.put("D", defaultProvider);
        astNodeClassMap.put("P", personProvider);
        astNodeClassMap.put("V", valueProvider);
        astNodeClassMap.put("AR", actionReferenceProvider);
        astNodeClassMap.put("CV", comparisonValueProvider);
        astNodeClassMap.put("CS", comparisonStatementProvider);
        astNodeClassMap.put("Add", addOperationProvider);
        astNodeClassMap.put("Div", divideOperationProvider);
        astNodeClassMap.put("Mul", multiplyOperationProvider);
        astNodeClassMap.put("Sub", subtractOperationProvider);
        astNodeClassMap.put("CalO", calculationOperatorProvider);
        astNodeClassMap.put("ComO", comparisonOperatorProvider);
        astNodeClassMap.put("BoolO", booleanOperatorProvider);
        astNodeProviderMap = Collections.unmodifiableMap(astNodeClassMap);
    }

    @Override
    public ASTNode create(String identifier) {
        if (astNodeProviderMap.containsKey(identifier)) {
            try {
                return astNodeProviderMap.get(identifier).get();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }
        return null;
    }
}
