package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.BusinessRuleHandler;
import org.han.ica.asd.c.businessrule.parser.BusinessRuleDecoder;
import org.han.ica.asd.c.businessrule.parser.ast.Action;
import org.han.ica.asd.c.businessrule.parser.ast.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.AddOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BusinessRuleHandlerTest {

	@Test
	void evaluateBusinessRulesOfBusinessRuleHandlerReturnsActionOfBusinessRule() {
		BusinessRule businessRule = (BusinessRule) new BusinessRule()
			.addChild(new Comparison()
					.addChild(new ComparisonValue().addChild(new Value().addValue("5")))
					.addChild(new ComparisonOperator("equal"))
					.addChild(new ComparisonValue().addChild(new Value().addValue("5"))))
			.addChild(new Action()
					.addChild(new ActionReference("order"))
					.addChild(new AddOperation()
							.addChild(new Value().addValue("10"))
							.addChild(new CalculationOperator("+"))
							.addChild(new Value().addValue("1"))));

		BusinessRuleDecoder businessRuleDecoder = mock(BusinessRuleDecoder.class);
		when(businessRuleDecoder.decodeBusinessRule(businessRule.encode())).thenReturn(businessRule);

		Action expectedAction = (Action) businessRule.getChildren().get(1);
		StringBuilder expectedStringBuilder = new StringBuilder();
		expectedAction.encode(expectedStringBuilder);

		Action actualAction = new BusinessRuleHandler().evaluateBusinessRules(businessRule.encode(), new RoundData());
		StringBuilder actualStringBuilder = new StringBuilder();
		actualAction.encode(actualStringBuilder);

		System.out.println(actualStringBuilder.toString());

		assertEquals(expectedStringBuilder.toString(), actualStringBuilder.toString());
	}
}
