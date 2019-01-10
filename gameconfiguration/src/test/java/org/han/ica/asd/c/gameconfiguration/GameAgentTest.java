//package org.han.ica.asd.c.GameConfiguration;
//
//import com.google.inject.AbstractModule;
//import com.google.inject.Guice;
//import com.google.inject.Injector;
//import org.han.ica.asd.c.Exceptions.NoProgrammedAgentsFoundException;
//import org.han.ica.asd.c.dao.ProgrammedAgentDAO;
//import org.han.ica.asd.c.interfaces.gameconfiguration.IPersistenceProgrammedAgents;
//import org.han.ica.asd.c.model.domain_objects.*;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.*;
//
//import static org.mockito.Mockito.*;
//
//public class GameAgentTest {
//
//  private IGameAgentService gameAgentController;
//  private IGameConfigurationUserInterface gameConfigurationUserInterface;
//  private ProgrammedAgentDAO programmedAgentDAO;
//
//  @Before
//  public void setup() {
//    programmedAgentDAO = mock(ProgrammedAgentDAO.class);
//    gameConfigurationUserInterface = mock(IGameConfigurationUserInterface.class);
//    Injector injector = Guice.createInjector(new AbstractModule() {
//      @Override
//      protected void configure() {
//        bind(ProgrammedAgentDAO.class).toInstance(programmedAgentDAO);
//        bind(IGameConfigurationUserInterface.class).toInstance(gameConfigurationUserInterface);
//      }
//    });
//    gameAgentController = mock(IGameAgentService.class);
//    gameAgentController = injector.getInstance(GameAgentService.class);
//  }
//
//  @Test
//  public void testGetAgents() {
//    List<ProgrammedBusinessRules> programmedBusinessRules = new ArrayList<>();
//    List<ProgrammedAgent> programmedAgents = new ArrayList<>();
//    String[] gameAgentNames = {"TestAgent", "Default", "Andere testagent"};
//    for(int i = 0; i < gameAgentNames.length; ++i)
//      programmedAgents.add(new ProgrammedAgent(gameAgentNames[i], programmedBusinessRules));
//
//    try {
//      List<ProgrammedAgent> testedList = gameAgentController.getAgentsForUI();
//      Assert.assertEquals(programmedAgents, testedList);
//    } catch (NoProgrammedAgentsFoundException e) {
//      e.printStackTrace();
//    }
//  }
//
//
//
//  @Test
//  public void setDefaultAgentInFacilitiesTest() {
//    Map<Facility, ProgrammedAgent> facilityProgrammedAgentMap = new HashMap<>();
//    FacilityType facilityType = mock(FacilityType.class);
//    List<ProgrammedBusinessRules> programmedBusinessRules = mock(List.class);
//    Map<Facility, ProgrammedAgent> testData = new HashMap<>();
//    String[] programmedAgentNames = {"Agent 1", "Agent 2", "Agent 3", "Agent 4", "Default", "Agent 6", "Kees",
//        "jan", "Piet", "Joost", "Brenda", "Tester", "Administrator", "Agent 14", "agent 15"
//    };
//    for(int i = 0; i < programmedAgentNames.length; ++i) {
//      facilityProgrammedAgentMap.put(new Facility(facilityType, i), new ProgrammedAgent(programmedAgentNames[i], programmedBusinessRules));
//      testData.put(new Facility(facilityType, i), null);
//    }
//    List<Facility> gameFacilities = new ArrayList<>();
//    for(Map.Entry<Facility, ProgrammedAgent> set: facilityProgrammedAgentMap.entrySet()) {
//      gameFacilities.add(set.getKey());
//    }
//    List<GameAgent> gameAgents = new ArrayList<>();
//    for(int i = 0; i < programmedAgentNames.length; ++i)
//      gameAgents.add(new GameAgent(programmedAgentNames[i], programmedBusinessRules, gameFacilities.get(i)));
//
//    List<GameAgent> testedList = gameAgentController.setAgentsInFacilities(testData);
//    Assert.assertEquals(gameAgents, testedList);
//  }
//
//}
