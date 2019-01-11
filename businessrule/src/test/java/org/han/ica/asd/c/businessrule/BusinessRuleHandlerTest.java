package org.han.ica.asd.c.businessrule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.Comparison;
import org.han.ica.asd.c.businessrule.parser.ast.comparison.ComparisonValue;
import org.han.ica.asd.c.businessrule.parser.ast.operations.Value;
import org.han.ica.asd.c.businessrule.parser.ast.operators.ComparisonOperator;
import org.han.ica.asd.c.businessrule.stubs.BusinessRuleStoreStub;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.model.interface_models.ActionModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BusinessRuleHandlerTest {
	private Provider<Value> valueProvider;
	private Provider<Comparison> comparisonProvider;
	private Provider<ComparisonValue> comparisonValueProvider;
	private Provider<ComparisonOperator> comparisonOperatorProvider;
	private Provider<Action> actionProvider;
	private Provider<ActionReference> actionReferenceProvider;
	private Provider<BusinessRule> businessRuleProvider;
	private Provider<BusinessRuleHandler> businessRuleHandlerProvider;


	@BeforeEach
	public void setup() {
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IBusinessRuleStore.class).annotatedWith(Names.named("BusinessruleStore")).to(BusinessRuleStoreStub.class);
			}
		});
		valueProvider = injector.getProvider(Value.class);
		comparisonOperatorProvider = injector.getProvider(ComparisonOperator.class);
		comparisonProvider = injector.getProvider(Comparison.class);
		comparisonValueProvider = injector.getProvider(ComparisonValue.class);
		actionProvider = injector.getProvider(Action.class);
		actionReferenceProvider = injector.getProvider(ActionReference.class);
		businessRuleProvider = injector.getProvider(BusinessRule.class);
		businessRuleHandlerProvider = injector.getProvider(BusinessRuleHandler.class);
	}

	@Test
	void evaluateBusinessRulesOfBusinessRuleHandlerReturnsActionOfBusinessRule() {
		BusinessRule businessRule = (BusinessRule) businessRuleProvider.get()
			.addChild(comparisonProvider.get()
					.addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("5")))
					.addChild(comparisonOperatorProvider.get().addValue("equal"))
					.addChild(comparisonValueProvider.get().addChild(valueProvider.get().addValue("5"))))
			.addChild(actionProvider.get()
					.addChild(actionReferenceProvider.get().addValue("order"))
					.addChild(valueProvider.get().addValue("1")));

		Action expectedAction = (Action) businessRule.getChildren().get(1);
		ActionModel actualAction = businessRuleHandlerProvider.get().evaluateBusinessRule(businessRule.encode(), new Round(),0);

		assertEquals(expectedAction.getType(), actualAction.type);
		assertEquals(expectedAction.getAmount(), actualAction.amount);
	}
}
