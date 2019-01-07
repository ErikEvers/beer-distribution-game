package org.han.ica.asd.c.GameConfiguration;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.Exceptions.NoProgrammedAgentsFoundException;
import org.han.ica.asd.c.interfaces.gameconfiguration.IPersistenceProgrammedAgents;
import org.han.ica.asd.c.model.domain_objects.*;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.*;

public class AgentAutomaticOwnerOfFacilityTest {

  private IAgentController agentController;
  private IGameConfigurationUserInterface gameConfigurationUserInterface;
  private IPersistenceProgrammedAgents persistenceProgrammedAgents;

  /**
   * To test the exception
   */
  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Before
  public void executedOnce() {
    gameConfigurationUserInterface = mock(IGameConfigurationUserInterface.class);
    persistenceProgrammedAgents = mock(IPersistenceProgrammedAgents.class);
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(IGameConfigurationUserInterface.class).toInstance(gameConfigurationUserInterface);
        bind(IPersistenceProgrammedAgents.class).toInstance(persistenceProgrammedAgents);
      }
    });
    agentController = mock(IAgentController.class);
    agentController = injector.getInstance(AgentController.class);
  }

  @Test
  public void testAgentsAutomaticInUnownedFacilities() {
    List<ProgrammedAgent> agents = new ArrayList<>();
    List<Facility> facilities = new ArrayList<>();
    String[] agentNames = {"Harry", "Default", "Pieter"};
    for(int i = 0; i < 3; ++i) {
      agents.add(new ProgrammedAgent(agentNames[i]));
    }
    for(int i = 0; i < 10; ++i) {
      facilities.add(new Facility());
      facilities.get(i).setFacilityId(i);
    }
    // Do Test
    List<Facility> testedList = agentController.agentsFinished(facilities, agents);
    for(int i = 0; i < facilities.size(); ++i)
      facilities.get(i).setAgent(new GameAgent("Default", facilities.get(i)));

    Assert.assertEquals(facilities, testedList);
  }

  /**
   * This test, the list index 1 is the answer.
   */
  @Test
  public void testGetDefaultAgent() {
    List<ProgrammedAgent> programmedAgents = new ArrayList<>();
    String[] agentNames = {"TestAgent", "Default", "Barry Badpak"};
    for(String name: agentNames) {
      programmedAgents.add(new ProgrammedAgent(name));
    }
    try {
      ProgrammedAgent defaultAgent = agentController.getDefaultAgent(programmedAgents, "Default");
      Assert.assertEquals(defaultAgent, programmedAgents.get(1));
    } catch(NoProgrammedAgentsFoundException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetDefaultAgentException() {
    List<ProgrammedAgent> programmedAgents = new ArrayList<>();
    String[] agentNames = {"TestAgent", "Not-default", "Barry Badpak"};
    for(String name: agentNames) {
      programmedAgents.add(new ProgrammedAgent(name));
    }
    try {
      ProgrammedAgent defaultAgent = agentController.getDefaultAgent(programmedAgents, "Default");
      fail("Expected a exception to be thrown");
    } catch(NoProgrammedAgentsFoundException e) {
      Assert.assertEquals("No default agent found. Please create a default agent.", e.getMessage());
    }
  }

}
