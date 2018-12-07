package org.han.ica.asd.c.businessrule.parser;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;
import org.han.ica.asd.c.businessrule.parser.ast.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;

import java.util.Deque;
import java.util.LinkedList;

public class BusinessRuleParser {
	private static final char   LEFT_PARENTHESES                = '(';
	private static final char   RIGHT_PARENTHESES               = ')';
	private static final byte   TOKEN_NOT_FOUND                 = -1;

	private static final byte   CURRENT_TOKEN                   = 0;
	private static final byte   REMAINING_STRING                = 1;
	private static final byte   SPLIT_IN_TWO                    = 2;

	private static final String LEFT_PARENTHESIS_SPLIT_TOKEN    = "\\(";
	private static final String RIGHT_PARENTHESIS_SPLIT_TOKEN   = "\\)";

	public BusinessRule parse(String businessRuleString) {
		BusinessRule businessRule = new BusinessRule();
		Deque<ASTNode> astNodeStack = new LinkedList<>();
		astNodeStack.push(businessRule);

		// Skip first iteration because it's always the object BusinessRule
		String[] currentIteration = nextIteration(businessRuleString);

		// When there is an identifier left to parse
		while (currentIteration[REMAINING_STRING].length() > 1) {
			// Check if identifier needs to be pushed or popped
			String previousIterationRemainingString = currentIteration[REMAINING_STRING];
			currentIteration = nextIteration(currentIteration[REMAINING_STRING]);

			// Create ASTNode of the identifier it's given, if the identifier isn't a ASTNode add it as value to
			// the ASTNode on the top of the deque(stack)
			ASTNode node = null;
			if (!currentIteration[CURRENT_TOKEN].isEmpty()) {
				node = new BusinessRuleFactory().create(astNodeStack.peek().getClass(),
						currentIteration[CURRENT_TOKEN]);

				if (node == null) {
					astNodeStack.peek()
						.addValue(currentIteration[CURRENT_TOKEN]);
				} else {
					astNodeStack.peek()
						.addChild(node);
				}
			}

			// Depending on a opening and closing parentheses it has to pop or push the node
			if (node != null && popOrPush(previousIterationRemainingString)) {
				astNodeStack.push(node);
			} else {
				astNodeStack.pop();
			}

			// Have to pop additional time to align the deque with Action for the next Node
			if (node instanceof ActionReference)
				astNodeStack.pop();
		}

		return businessRule;
	}

	public String[] nextIteration(String remainingString) {
		return popOrPush(remainingString) ?
				remainingString.split(LEFT_PARENTHESIS_SPLIT_TOKEN, SPLIT_IN_TWO) :
				remainingString.split(RIGHT_PARENTHESIS_SPLIT_TOKEN, SPLIT_IN_TWO);
	}

	public boolean popOrPush(String businessRuleString) {
		return businessRuleString.indexOf(LEFT_PARENTHESES) != TOKEN_NOT_FOUND &&
				(businessRuleString.indexOf(LEFT_PARENTHESES) < businessRuleString.indexOf(RIGHT_PARENTHESES));
	}
}
