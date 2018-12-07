package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.parser.BusinessRuleDecoder;
import org.han.ica.asd.c.businessrule.parser.ast.Action;
import org.han.ica.asd.c.businessrule.parser.ast.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.*;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BusinessRuleDecoderTest {

	@Test
	void parseBusinessRuleStringWithLesserThanComparisonToBusinessRuleIsEqual() {
		String businessRuleString = "BR(CS(C(CV(V(inventory))ComO(<)CV(V(10))))A(AR(order)V(0)))";
		BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRuleString);
		assertEquals(businessRuleString, businessRuleParsed.encode());
	}

	@Test
	void parseBusinessRuleStringWithGreaterThanComparisonToBusinessRuleIsEqual() {
		String businessRuleString = "BR(CS(C(CV(V(inventory))ComO(>)CV(V(20))))A(AR(order)V(20)))";
		BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRuleString);
		assertEquals(businessRuleString, businessRuleParsed.encode());
	}

	@Test
	void parseBusinessRuleStringWithAndOperationComparisonToBusinessRuleIsEqual() {
		String businessRuleString = "BR(CS(C(CV(V(inventory))ComO(<)CV(V(20)))BoolO(==)CS(C(CV(V(round))ComO(>)CV(V(3)))))A(AR(order)V(20)))";
		BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRuleString);
		assertEquals(businessRuleString, businessRuleParsed.encode());
	}

	@Test
	void parseBusinessRuleStringWithOrOperationComparisonToBusinessRuleIsEqual() {
		String businessRuleString = "BR(CS(C(CV(V(inventory))ComO(<)CV(V(20)))BoolO(||)CS(C(CV(V(round))ComO(>)CV(V(3)))))A(AR(order)V(20)))";
		BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRuleString);
		assertEquals(businessRuleString, businessRuleParsed.encode());
	}

	@Test
	void parseBusinessRuleStringWithEqualsToAndIsNotEqualsToToBusinessRuleIsEqual() {
		String businessRuleString = "BR(CS(C(CV(V(inventory))ComO(==)CV(V(inventory)))BoolO(==)CS(C(CV(V(round))ComO(!=)CV(V(3)))))A(AR(order)V(20)))";
		BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRuleString);
		assertEquals(businessRuleString, businessRuleParsed.encode());
	}

	@Test
	void parseDefaultOrderBusinessRuleString() {
		String businessRuleString = "BR(D()A(AR(order)V(10)))";
		BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRuleString);
		assertEquals(businessRuleString, businessRuleParsed.encode());
	}

	@Test
	void parseBusinessRuleStringWithAddOperationBusinessRuleIsEqual() {
		BusinessRule businessRule = (BusinessRule) new BusinessRule()
			.addChild(new Comparison()
				.addChild(new ComparisonValue().addChild(new Value().addValue("5")))
				.addChild(new ComparisonOperator("equal"))
				.addChild(new ComparisonValue().addChild(new Value().addValue("5"))))
			.addChild(new Action()
				.addChild(new ActionReference("order"))
				.addChild(new AddOperation()
					.addChild(new Value().addValue("10"))
					.addChild(new Value().addValue("1"))));

		BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRule.encode());
		assertEquals(businessRule.encode(), businessRuleParsed.encode());
	}

	@Test
	void parseBusinessRuleStringWithSubtractOperationBusinessRuleIsEqual() {
		BusinessRule businessRule = (BusinessRule) new BusinessRule()
				.addChild(new Comparison()
						.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")))
						.addChild(new ComparisonOperator("higher"))
						.addChild(new ComparisonValue().addChild(new Value().addValue("backlog"))))
				.addChild(new Action()
						.addChild(new ActionReference("order"))
						.addChild(new SubtractOperation()
								.addChild(new Value().addValue("inventory"))
								.addChild(new Value().addValue("2"))));

		BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRule.encode());
		assertEquals(businessRule.encode(), businessRuleParsed.encode());
	}

	@Test
	void parseBusinessRuleStringWithMultiplyOperationBusinessRuleIsEqual() {
		BusinessRule businessRule = (BusinessRule) new BusinessRule()
				.addChild(new Comparison()
						.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")))
						.addChild(new ComparisonOperator("higher"))
						.addChild(new ComparisonValue().addChild(new Value().addValue("backlog"))))
				.addChild(new Action()
						.addChild(new ActionReference("order"))
						.addChild(new MultiplyOperation()
								.addChild(new Value().addValue("inventory"))
								.addChild(new Value().addValue("2"))));

		BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRule.encode());
		assertEquals(businessRule.encode(), businessRuleParsed.encode());
	}

	@Test
	void parseBusinessRuleStringWithDivideOperationBusinessRuleIsEqual() {
		BusinessRule businessRule = (BusinessRule) new BusinessRule()
				.addChild(new Comparison()
						.addChild(new ComparisonValue().addChild(new Value().addValue("inventory")))
						.addChild(new ComparisonOperator("higher"))
						.addChild(new ComparisonValue().addChild(new Value().addValue("backlog"))))
				.addChild(new Action()
						.addChild(new ActionReference("order"))
						.addChild(new DivideOperation()
								.addChild(new Value().addValue("inventory"))
								.addChild(new Value().addValue("2"))));

		BusinessRule businessRuleParsed = new BusinessRuleDecoder().decodeBusinessRule(businessRule.encode());
		assertEquals(businessRule.encode(), businessRuleParsed.encode());
	}
}
