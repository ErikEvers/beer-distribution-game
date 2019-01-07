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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

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
    List<ProgrammedAgent> agents = mock(List.class);
    List<Facility> facilities = mock(List.class);
    String[] agentNames = {"piet", "Default", "kees"};
    for(int i = 0; i < 3; ++i) {
      agents.add(new ProgrammedAgent(agentNames[i]));
    }
    for(int i = 0; i < 10; ++i) {
      facilities.add(mock(Facility.class));
    }
    for(int i = 0; i < facilities.size(); ++i)
      facilities.get(i).setAgent(null);


    GameAgent gameAgent = mock(GameAgent.class);
    try {
      for (int i = 0; i < facilities.size(); ++i) {
        doNothing().when(agentController.getDefaultAgent(agents, anyString()));
        agentController.agentsFinished(facilities);
        System.out.println(facilities.get(i).getAgent());
        verify(facilities.get(i), times(1)).setAgent(gameAgent);
      }
    } catch(NoProgrammedAgentsFoundException e) {
      e.printStackTrace();
    }
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
