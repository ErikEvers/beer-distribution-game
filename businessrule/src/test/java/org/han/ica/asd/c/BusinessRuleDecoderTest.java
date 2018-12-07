package org.han.ica.asd.c;

import org.han.ica.asd.c.businessrule.parser.BusinessRuleParser;
import org.han.ica.asd.c.businessrule.parser.ast.Action;
import org.han.ica.asd.c.businessrule.parser.ast.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.*;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BusinessRuleParserTest {

	@Test
	public void parseBusinessRuleStringWithLesserThanComparisonToBusinessRuleIsEqual() {
		String businessRuleString = "BR(CS(C(V(inventory)ComO(<)V(10)))A(order(V(0))))";
		BusinessRule businessRuleParsed = new BusinessRuleParser().parse(businessRuleString);
		assertEquals(businessRuleString, businessRuleParsed.encode());
	}

	@Test
	public void parseBusinessRuleStringWithGreaterThanComparisonToBusinessRuleIsEqual() {
		String businessRuleString = "BR(CS(C(V(inventory)ComO(>)V(20)))A(order(V(20))))";
		BusinessRule businessRuleParsed = new BusinessRuleParser().parse(businessRuleString);
		assertEquals(businessRuleString, businessRuleParsed.encode());
	}

	@Test
	public void parseBusinessRuleStringWithAndOperationComparisonToBusinessRuleIsEqual() {
		String businessRuleString = "BR(CS(C(V(inventory)ComO(<)V(20))BoolO(==)CS(C(V(round)ComO(>)V(3))))A(order(V(20))))";
		BusinessRule businessRuleParsed = new BusinessRuleParser().parse(businessRuleString);
		assertEquals(businessRuleString, businessRuleParsed.encode());
	}

	@Test
	public void parseBusinessRuleStringWithOrOperationComparisonToBusinessRuleIsEqual() {
		String businessRuleString = "BR(CS(C(V(inventory)ComO(<)V(20))BoolO(||)CS(C(V(round)ComO(>)V(3))))A(order(V(20))))";
		BusinessRule businessRuleParsed = new BusinessRuleParser().parse(businessRuleString);
		assertEquals(businessRuleString, businessRuleParsed.encode());
	}

	@Test
	public void parseBusinessRuleStringWithEqualsToAndIsNotEqualsToToBusinessRuleIsEqual() {
		String businessRuleString = "BR(CS(C(V(inventory)ComO(==)V(inventory))BoolO(==)CS(C(V(round)ComO(!=)V(3))))A(order(V(20))))";
		BusinessRule businessRuleParsed = new BusinessRuleParser().parse(businessRuleString);
		assertEquals(businessRuleString, businessRuleParsed.encode());
	}

	@Test
	public void parseDefaultOrderBusinessRuleString() {
		String businessRuleString = "BR(D()A(order(V(10))))";
		BusinessRule businessRuleParsed = new BusinessRuleParser().parse(businessRuleString);
		assertEquals(businessRuleString, businessRuleParsed.encode());
	}

	@Test
	public void parseBusinessRuleStringWithAddOperationBusinessRuleIsEqual() {
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

		BusinessRule businessRuleParsed = new BusinessRuleParser().parse(businessRule.encode());
		assertEquals(businessRule.encode(), businessRuleParsed.encode());
	}

	@Test
	public void parseBusinessRuleStringWithSubtractOperationBusinessRuleIsEqual() {
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

		BusinessRule businessRuleParsed = new BusinessRuleParser().parse(businessRule.encode());
		assertEquals(businessRule.encode(), businessRuleParsed.encode());
	}

	@Test
	public void parseBusinessRuleStringWithMultiplyOperationBusinessRuleIsEqual() {
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

		BusinessRule businessRuleParsed = new BusinessRuleParser().parse(businessRule.encode());
		assertEquals(businessRule.encode(), businessRuleParsed.encode());
	}

	@Test
	public void parseBusinessRuleStringWithDivideOperationBusinessRuleIsEqual() {
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

		BusinessRule businessRuleParsed = new BusinessRuleParser().parse(businessRule.encode());
		assertEquals(businessRule.encode(), businessRuleParsed.encode());
	}
}
