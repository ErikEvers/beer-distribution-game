package org.han.ica.asd.c.businessrule.parser.walker;

import org.han.ica.asd.c.businessrule.PrototypeBaseListener;
import org.han.ica.asd.c.businessrule.PrototypeParser;
import org.han.ica.asd.c.businessrule.parser.ast.*;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonStatement;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.*;
import org.han.ica.asd.c.businessrule.parser.ast.operators.BooleanOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class ASTListener extends PrototypeBaseListener {
    private List<BusinessRule> businessRules;
    private Deque<ASTNode> currentContainer;

    public ASTListener() {
        businessRules  = new ArrayList<>();
        currentContainer = new LinkedList<>();
    }

    public List<BusinessRule> getBusinessRules() {
        return businessRules;
    }

    @Override
    public void enterDefaultRule(PrototypeParser.DefaultRuleContext ctx) {
        BusinessRule businessRule = new BusinessRule();
        businessRules.add(businessRule);
        businessRule.addChild(new Default());
        currentContainer.push(businessRule);
    }

    @Override
    public void exitDefaultRule(PrototypeParser.DefaultRuleContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterIfRule(PrototypeParser.IfRuleContext ctx) {
        BusinessRule businessRule = new BusinessRule();
        businessRules.add(businessRule);
        currentContainer.push(businessRule);
    }

    @Override
    public void exitIfRule(PrototypeParser.IfRuleContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterComparisonstatement(PrototypeParser.ComparisonstatementContext ctx) {
        ComparisonStatement comparisonStatement = new ComparisonStatement();
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(comparisonStatement);
        }
        if(ctx.getChildCount() > 1){
            comparisonStatement.addChild(new BooleanOperator(ctx.getChild(1).toString()));
        }
        currentContainer.push(comparisonStatement);
    }

    @Override
    public void exitComparisonstatement(PrototypeParser.ComparisonstatementContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterComparison(PrototypeParser.ComparisonContext ctx) {
        Comparison comparison = new Comparison();
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(comparison);
        }
        currentContainer.push(comparison);
    }

    @Override
    public void exitComparison(PrototypeParser.ComparisonContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterComparison_value(PrototypeParser.Comparison_valueContext ctx) {
        ComparisonValue comparisonValue = new ComparisonValue();
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(comparisonValue);
        }
        if("round".equals(ctx.getChild(0).toString())){
            comparisonValue.addChild(new Value().addValue("round"));
        }
        currentContainer.push(comparisonValue);
    }

    @Override
    public void exitComparison_value(PrototypeParser.Comparison_valueContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterComparison_operator(PrototypeParser.Comparison_operatorContext ctx) {
        ComparisonOperator comparisonOperator = new ComparisonOperator(ctx.getChild(0).toString());
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(comparisonOperator);
        }
        currentContainer.push(comparisonOperator);
    }

    @Override
    public void exitComparison_operator(PrototypeParser.Comparison_operatorContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterValue(PrototypeParser.ValueContext ctx) {
        Value value = new Value();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            value.addValue(ctx.getChild(i).toString());
        }
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(value);
        }
        currentContainer.push(value);
    }

    @Override
    public void exitValue(PrototypeParser.ValueContext ctx) {
       currentContainer.pop();
    }

    @Override
    public void enterMulOperation(PrototypeParser.MulOperationContext ctx) {
        Operation operation = new MultiplyOperation();
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(operation);
        }
        currentContainer.push(operation);
    }

    @Override
    public void exitMulOperation(PrototypeParser.MulOperationContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterDivOperation(PrototypeParser.DivOperationContext ctx) {
        Operation operation = new DivideOperation();
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(operation);
        }
        currentContainer.push(operation);
    }

    @Override
    public void exitDivOperation(PrototypeParser.DivOperationContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterMinOperation(PrototypeParser.MinOperationContext ctx) {
        Operation operation = new SubtractOperation();
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(operation);
        }
        currentContainer.push(operation);
    }

    @Override
    public void exitMinOperation(PrototypeParser.MinOperationContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterPlusOperation(PrototypeParser.PlusOperationContext ctx) {
        Operation operation = new AddOperation();
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(operation);
        }
        currentContainer.push(operation);
    }

    @Override
    public void exitPlusOperation(PrototypeParser.PlusOperationContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterAction(PrototypeParser.ActionContext ctx) {
        Action action = new Action();
        ASTNode parent = currentContainer.peek();
        if (parent != null) {
            parent.addChild(action);
        }
        action.addChild(new ActionReference(ctx.getChild(0).toString()));
        currentContainer.push(action);
    }

    @Override
    public void exitAction(PrototypeParser.ActionContext ctx) {
        currentContainer.pop();
    }
}
