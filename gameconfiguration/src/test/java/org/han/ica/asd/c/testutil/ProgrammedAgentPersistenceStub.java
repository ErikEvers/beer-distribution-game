package org.han.ica.asd.c.testutil;

import org.han.ica.asd.c.interfaces.gameconfiguration.IPersistenceProgrammedAgents;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;

import java.util.ArrayList;
import java.util.List;

public class ProgrammedAgentPersistenceStub implements IPersistenceProgrammedAgents {

  @Override
  public List<ProgrammedAgent> getAllAgents() {
    return new ArrayList<>();
  }
}
