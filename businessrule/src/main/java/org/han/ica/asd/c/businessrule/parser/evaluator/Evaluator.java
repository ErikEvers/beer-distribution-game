package org.han.ica.asd.c.businessrule.parser.evaluator;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.Default;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Evaluator {
    private static final Logger LOGGER = Logger.getLogger(Logger.class.getName());


    public void evaluate(List<BusinessRule> businessRules) {
        List<BusinessRule> businessRulesList = new ArrayList<>();
        businessRulesList.addAll(businessRules);
        Counter defaultCounter = new Counter();
        int lineNumber = 0;

        Collections.reverse(businessRulesList);
        Deque<ASTNode> deque = new LinkedList<>(businessRulesList);

        while (!deque.isEmpty()) {
            ASTNode current = deque.pop();

            if (current instanceof BusinessRule) {
                lineNumber++;
            }
            try {
                checkOnlyOneDefault(current, lineNumber, defaultCounter);
                checkRoundIsComparedToInt(current, lineNumber);
            } catch (BusinessRuleException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }

            deque.addAll(current.getChildren());
        }
    }

    public void checkOnlyOneDefault(ASTNode current, int lineNumber, Counter defaultCounter) throws BusinessRuleException {
        if (current instanceof Default) {
            defaultCounter.addOne();
            if (defaultCounter.getCountedValue() > 1) {
                throw new BusinessRuleException("There can only be one default statement", lineNumber);
            }
        }
    }

    private void checkRoundIsComparedToInt(ASTNode current, int lineNumber) throws BusinessRuleException {
        int left = 0;
        int right = 2;

        if (current instanceof Comparison && current.getChildren().get(left) != null && current.getChildren().get(2) != null) {
            if (current.getChildren().get(left).getChildren().get(left).toString().contains("round")) {
                checkSubTreeNotInt(current, lineNumber, right);
            } else if (current.getChildren().get(right).getChildren().get(left).toString().contains("round")) {
                checkSubTreeNotInt(current, lineNumber, left);
            }
        }
    }

    public void checkSubTreeNotInt(ASTNode current, int lineNumber, int side) throws BusinessRuleException {
        Queue<ASTNode> q = new LinkedList<>();
        q.add(current.getChildren().get(side));
        while (!q.isEmpty()) {
            ASTNode qVal = q.remove();
            if (qVal instanceof Value && !((Value) qVal).getValue().matches("-?\\d+")) {
                throw new BusinessRuleException("Round can only be compared to a number", lineNumber);
            }
            q.addAll(qVal.getChildren());
        }
    }
}