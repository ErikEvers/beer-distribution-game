package org.han.ica.asd.c.businessrule.stubs;

import com.google.common.collect.Lists;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;

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
                Lists.newArrayList("0","4","5"),
                Lists.newArrayList("1","6","7"),
                Lists.newArrayList("2","8","9"),
                Lists.newArrayList("3","10","11")
        );
    }

    @Override
    public List<String> getAllProgrammedAgents() {
        return null;
    }

    @Override
    public void deleteProgrammedAgent(String agentName) {

    }
}
