package org.han.ica.asd.c.businessrule.stubs;

import com.google.common.collect.Lists;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BusinessRuleStoreStub implements IBusinessRuleStore {
    @Override
    public List<String> readInputBusinessRules(String agentName) {
        return null;
    }

    @Override
    public void synchronizeBusinessRules(String agentName, Map<String, String> businessRuleMap) {

    }

    @Override
    public List<List<String>> getAllFacilities() {
        return Arrays.asList(
                Lists.newArrayList("1", "2", "3"),
                Lists.newArrayList("4", "5"),
                Lists.newArrayList("6", "7", "8", "9"),
                Lists.newArrayList("10", "11", "12", "13", "14", "15")
        );
    }

    @Override
    public List<String> getAllProgrammedAgents() {
        return null;
    }

    @Override
    public void deleteProgrammedAgent(String agentName) {

    }

    @Override
    public ProgrammedAgent getProgrammedGameAgent(String agentName) {
        return null;
    }
}
