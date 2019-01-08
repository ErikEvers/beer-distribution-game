package org.han.ica.asd.c.businessrule.parser.walker;

import org.han.ica.asd.c.businessrule.BusinessRuleBaseListener;
import org.han.ica.asd.c.businessrule.BusinessRuleParser;
import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.Default;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.action.Person;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.*;
import org.han.ica.asd.c.businessrule.parser.ast.operators.BooleanOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class ASTListener extends BusinessRuleBaseListener {
    private final Provider<BusinessRule> bussinessRuleProvider;
    private final Provider<Default> defaultProvider;
    private final Provider<Comparison> comparisonProvider;
    private final Provider<ComparisonStatement> comparisonStatementProvider;
    private final Provider<ComparisonValue> comparisonValueProvider;
    private final Provider<Value> valueProvider;
    private List<BusinessRule> businessRules;
    private Deque<ASTNode> currentContainer;

    /**
     * Constructor
     */
    @Inject
    public ASTListener(Provider<BusinessRule> bussinessRuleProvider,
                       Provider<Default> defaultProvider,
                       Provider<Comparison> comparisonProvider,
                       Provider<ComparisonStatement> comparisonStatementProvider,
                       Provider<ComparisonValue> comparisonValueProvider,
                       Provider<Value> valueProvider) {
        this.bussinessRuleProvider = bussinessRuleProvider;
        this.defaultProvider = defaultProvider;
        this.comparisonProvider = comparisonProvider;
        this.comparisonStatementProvider = comparisonStatementProvider;
        this.comparisonValueProvider = comparisonValueProvider;
        this.valueProvider = valueProvider;
        businessRules  = new ArrayList<>();
        currentContainer = new LinkedList<>();
    }

    /**
     * Getter
     *
     * @return Returns the businessrules
     */
    public List<BusinessRule> getBusinessRules() {
        return businessRules;
    }

    /**
     * Called when walker enters DefaultRule. Adds default businessrule to the AST
     *
     * @param ctx Context
     */
    @Override
    public void enterDefaultRule(BusinessRuleParser.DefaultRuleContext ctx) {
        BusinessRule businessRule = bussinessRuleProvider.get();
        businessRules.add(businessRule);
        businessRule.addChild(defaultProvider.get());
        currentContainer.push(businessRule);
    }

    /**
     * Called when walker exits DefaultRule
     *
     * @param ctx Context
     */
    @Override
    public void exitDefaultRule(BusinessRuleParser.DefaultRuleContext ctx) {
        currentContainer.pop();
    }

    /**
     * Called when walker enters IfRule. Adds a comparison businessrule to the AST
     *
     * @param ctx Context
     */
    @Override
    public void enterIfRule(BusinessRuleParser.IfRuleContext ctx) {
        BusinessRule businessRule = new BusinessRule();
        businessRules.add(businessRule);
        currentContainer.push(businessRule);
    }

    /**
     * Called when walker exits IfRule
     *
     * @param ctx Context
     */
    @Override
    public void exitIfRule(BusinessRuleParser.IfRuleContext ctx) {
        currentContainer.pop();
    }

    /**
     * Called when walker enters ComparisonStatement. Adds comparison statement to the AST
     *
     * @param ctx Context
     */
    @Override
    public void enterComparisonstatement(BusinessRuleParser.ComparisonstatementContext ctx) {
        ComparisonStatement comparisonStatement = comparisonStatementProvider.get();
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(comparisonStatement);
        }
        if (ctx.getChildCount() > 1) {
            comparisonStatement.addChild(new BooleanOperator(ctx.getChild(1).toString()));
        }
        currentContainer.push(comparisonStatement);
    }

    /**
     * Called when walker exits ComparisonStatement
     *
     * @param ctx Context
     */
    @Override
    public void exitComparisonstatement(BusinessRuleParser.ComparisonstatementContext ctx) {
        currentContainer.pop();
    }

    /**
     * Called when walker enters Comparison. Adds comparison to the AST
     *
     * @param ctx Context
     */
    @Override
    public void enterComparison(BusinessRuleParser.ComparisonContext ctx) {
        Comparison comparison = comparisonProvider.get();
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(comparison);
        }
        currentContainer.push(comparison);
    }

    /**
     * Called when walker exits Comparison
     *
     * @param ctx Context
     */
    @Override
    public void exitComparison(BusinessRuleParser.ComparisonContext ctx) {
        currentContainer.pop();
    }

    /**
     * Called when walker enters ComparisonValue. Adds comparison value to the AST
     *
     * @param ctx Context
     */
    @Override
    public void enterComparison_value(BusinessRuleParser.Comparison_valueContext ctx) {
        ComparisonValue comparisonValue = comparisonValueProvider.get();
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(comparisonValue);
        }
        if("round".equals(ctx.getChild(0).toString())){
            comparisonValue.addChild(valueProvider.get().addValue("round"));
        }
        currentContainer.push(comparisonValue);
    }

    /**
     * Called when walker exits ComparisonValue
     *
     * @param ctx Context
     */
    @Override
    public void exitComparison_value(BusinessRuleParser.Comparison_valueContext ctx) {
        currentContainer.pop();
    }

    /**
     * Called when walker enters ComparisonOperator. Adds comparison operator to the AST
     *
     * @param ctx Context
     */
    @Override
    public void enterComparison_operator(BusinessRuleParser.Comparison_operatorContext ctx) {
        ComparisonOperator comparisonOperator = new ComparisonOperator(ctx.getChild(0).toString());
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(comparisonOperator);
        }
        currentContainer.push(comparisonOperator);
    }

    /**
     * Called when walker exits ComparisonOperator
     *
     * @param ctx Context
     */
    @Override
    public void exitComparison_operator(BusinessRuleParser.Comparison_operatorContext ctx) {
        currentContainer.pop();
    }

    /**
     * Called when walker enters Value. Adds value to the AST
     *
     * @param ctx Context
     */
    @Override
    public void enterValue(BusinessRuleParser.ValueContext ctx) {
        Value value = valueProvider.get();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            value.addValue(ctx.getChild(i).toString());
        }
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(value);
        }
        currentContainer.push(value);
    }

    /**
     * Called when walker exits Value
     *
     * @param ctx Context
     */
    @Override
    public void exitValue(BusinessRuleParser.ValueContext ctx) {
        currentContainer.pop();
    }

    /**
     * Called when walker enters MulOperation. Adds a multiply operation to the AST
     *
     * @param ctx Context
     */
    @Override
    public void enterMulOperation(BusinessRuleParser.MulOperationContext ctx) {
        Operation operation = new MultiplyOperation();
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(operation);
        }
        currentContainer.push(operation);
    }

    /**
     * Called when walker exits MulOperation
     *
     * @param ctx Context
     */
    @Override
    public void exitMulOperation(BusinessRuleParser.MulOperationContext ctx) {
        currentContainer.pop();
    }

    /**
     * Called when walker enters DivOperation. Adds divide operation to the AST
     *
     * @param ctx Context
     */
    @Override
    public void enterDivOperation(BusinessRuleParser.DivOperationContext ctx) {
        Operation operation = new DivideOperation();
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(operation);
        }
        currentContainer.push(operation);
    }

    /**
     * Called when walker exits DivOperation
     *
     * @param ctx Context
     */
    @Override
    public void exitDivOperation(BusinessRuleParser.DivOperationContext ctx) {
        currentContainer.pop();
    }

    /**
     * Called when walker enters MinOperation. Adds minus operation to the AST
     *
     * @param ctx Context
     */
    @Override
    public void enterMinOperation(BusinessRuleParser.MinOperationContext ctx) {
        Operation operation = new SubtractOperation();
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(operation);
        }
        currentContainer.push(operation);
    }

    /**
     * Called when walker exits MinOperation
     *
     * @param ctx Context
     */
    @Override
    public void exitMinOperation(BusinessRuleParser.MinOperationContext ctx) {
        currentContainer.pop();
    }

    /**
     * Called when walker enters PlusOperation. Adds plus operation to the AST
     *
     * @param ctx Context
     */
    @Override
    public void enterPlusOperation(BusinessRuleParser.PlusOperationContext ctx) {
        Operation operation = new AddOperation();
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(operation);
        }
        currentContainer.push(operation);
    }

    /**
     * Called when walker exits PlusOperation
     *
     * @param ctx Context
     */
    @Override
    public void exitPlusOperation(BusinessRuleParser.PlusOperationContext ctx) {
        currentContainer.pop();
    }

    /**
     * Called when walker enters Action. Adds action to the AST
     *
     * @param ctx Context
     */
    @Override
    public void enterAction(BusinessRuleParser.ActionContext ctx) {
        Action action = new Action();
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(action);
        }
        action.addChild(new ActionReference(ctx.getChild(0).toString()));
        currentContainer.push(action);
    }

    /**
     * Called when walker exits Action
     *
     * @param ctx Context
     */
    @Override
    public void exitAction(BusinessRuleParser.ActionContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterPerson(BusinessRuleParser.PersonContext ctx) {
        Person person = new Person(ctx.getChild(1).toString());
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(person);
        }
    }
}
