package org.han.ica.asd.c.businessrule;

import org.han.ica.asd.c.businessrule.engine.BusinessRuleDecoder;
import org.han.ica.asd.c.businessrule.parser.ParserPipeline;
import org.han.ica.asd.c.businessrule.parser.ast.BusinessRule;
import org.han.ica.asd.c.businessrule.parser.ast.action.Action;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRules;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.model.interface_models.ActionModel;
import org.han.ica.asd.c.model.interface_models.UserInputBusinessRule;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.util.List;

public class BusinessRuleHandler implements IBusinessRules {
    private Provider<ParserPipeline> parserPipelineProvider;
    private Provider<BusinessRuleDecoder> businessRuleDecoderProvider;

    private ParserPipeline parserPipeline;
    private BusinessRuleDecoder businessRuleDecoder;

    @Inject
    @Named("BusinessruleStore")
    public IBusinessRuleStore iBusinessRuleStore;

    @Inject
    public BusinessRuleHandler(Provider<ParserPipeline> parserPipelineProvider, Provider<BusinessRuleDecoder> businessRuleDecoderProvider) {
        this.parserPipelineProvider = parserPipelineProvider;
        this.businessRuleDecoderProvider = businessRuleDecoderProvider;
        parserPipeline = this.parserPipelineProvider.get();
        businessRuleDecoder = this.businessRuleDecoderProvider.get();
    }

    /**
     * Parses the business rules and sends it to the persistence component
     *
     * @param agentName     Name for the agent
     * @param businessRules Business rules for the agent
     */
    public List<UserInputBusinessRule> programAgent(String agentName, String businessRules) {
        if (!parserPipeline.parseString(businessRules)) {
            return parserPipeline.getBusinessRulesInput();
        }
        iBusinessRuleStore.synchronizeBusinessRules(agentName,parserPipeline.getBusinessRulesMap());
        return parserPipeline.getBusinessRulesInput();
    }

    public ActionModel evaluateBusinessRule(String businessRule, Round roundData, int facilityId) {
        BusinessRule businessRuleAST = businessRuleDecoder.decodeBusinessRule(businessRule);

        businessRuleAST.substituteTheVariablesOfBusinessruleWithGameData(roundData, facilityId);
        businessRuleAST.evaluateBusinessRule();

        if (businessRuleAST.isTriggered()) {
            Action action = businessRuleAST.getAction();
            return new ActionModel(
                    action.getType(),
                    action.getAmount(),
                    action.getFacilityId(facilityId));
        }
        return null;
    }
}
