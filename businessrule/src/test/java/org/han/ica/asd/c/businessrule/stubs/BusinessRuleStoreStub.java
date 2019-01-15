package org.han.ica.asd.c.businessrule.stubs;

import com.google.common.collect.Lists;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;

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
        return Lists.newArrayList(
                Lists.newArrayList("0"),
                Lists.newArrayList("1"),
                Lists.newArrayList("2"),
                Lists.newArrayList("3")
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
