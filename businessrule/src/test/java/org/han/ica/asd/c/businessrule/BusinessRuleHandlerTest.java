package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.engine.BusinessRuleDecoder;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.AddOperation;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.model.interface_models.ActionModel;
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
					.addChild(new Value().addValue("1")));

		Action expectedAction = (Action) businessRule.getChildren().get(1);
		ActionModel actualAction = new BusinessRuleHandler().evaluateBusinessRule(businessRule.encode(), new Round());

		assertEquals(expectedAction.getType(), actualAction.type);
		assertEquals(expectedAction.getAmount(), actualAction.amount);
	}
}
