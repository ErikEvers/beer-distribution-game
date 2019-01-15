package org.han.ica.asd.c.agent;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.han.ica.asd.c.agent.stubs.GameLogicStub;
import org.han.ica.asd.c.businessrule.BusinessRuleHandler;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRules;
import org.han.ica.asd.c.interfaces.gameleader.IPersistence;
import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.interfaces.gamelogic.IPlayerGameLogic;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRules;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRulesInFacilityTurn;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;

class Fixtures {
    public static class LinearConfiguration {
        private static LinearConfiguration testConfiguration;
        final List<Facility> facilityList;
        final Map<Facility, List<Facility>> facilitiesLinkedTo;
        final Configuration configuration;
        final List<List<String>> facilityIdList;
        private Injector participantInjector;
        final IPlayerGameLogic gameLogic = mock(IPlayerGameLogic.class);

        private LinearConfiguration() {
            facilityList = Collections.unmodifiableList(Lists.newArrayList(
                    new Facility(
                            new FacilityType("factory 1", 3, 3, 10, 30, 1000, 3, 40), 1),
                    new Facility(
                            new FacilityType("regional warehouse 1", 3, 3, 10, 30, 1000, 3, 40), 2),
                    new Facility(
                            new FacilityType("wholesale 1", 3, 3, 10, 30, 1000, 3, 40), 3),
                    new Facility(
                            new FacilityType("retail 1", 3, 3, 10, 30, 1000, 3, 40), 4)));

            Map<Facility, List<Facility>> facilitiesLinkedToLocal = new HashMap<>();
            facilitiesLinkedToLocal.put(facilityList.get(0), Collections.emptyList());
            facilitiesLinkedToLocal.put(facilityList.get(1), Collections.singletonList(facilityList.get(0)));
            facilitiesLinkedToLocal.put(facilityList.get(2), Collections.singletonList(facilityList.get(1)));
            facilitiesLinkedToLocal.put(facilityList.get(3), Collections.singletonList(facilityList.get(2)));
            facilitiesLinkedTo = Collections.unmodifiableMap(facilitiesLinkedToLocal);

            configuration = new Configuration(
                    10,
                    1,
                    1,
                    1,
                    1,
                    1,
                    1,
                    true,
                    false,
                    facilityList,
                    facilitiesLinkedTo);

            facilityIdList = Collections.unmodifiableList(Arrays.asList(
                    Collections.singletonList("1"),
                    Collections.singletonList("2"),
                    Collections.singletonList("3"),
                    Collections.singletonList("4")));

            participantInjector = Guice.createInjector(new AbstractModule() {
                @Override
                protected void configure() {
                    bind(IBusinessRules.class).annotatedWith(Names.named("businessRules")).to(BusinessRuleHandler.class);
                    bind(IPlayerGameLogic.class).toInstance(gameLogic);
                    bind(IPersistence.class).annotatedWith(Names.named("persistence")).toInstance(new IPersistence() {
                        @Override
                        public void saveGameLog(BeerGame beerGame, boolean isStarted) {

                        }

                        @Override
                        public void saveFacilityTurn(Round data) {
                        }

                        @Override
                        public Round fetchFacilityTurn(int roundId) {
                            return null;
                        }

                        @Override
                        public void saveRoundData(Round data) {

                        }

                        @Override
                        public Round fetchRoundData(int roundId) {
                            return null;
                        }

                        @Override
                        public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn) {
                        }
                    });
                    bind(IBusinessRuleStore.class).annotatedWith(Names.named("BusinessruleStore")).toInstance(new IBusinessRuleStore() {
                        @Override
                        public List<String> readInputBusinessRules(String agentName) {
                            return Collections.emptyList();
                        }

                        @Override
                        public void synchronizeBusinessRules(String agentName, Map<String, String> businessRuleMap) {
                        }

                        @Override
                        public List<List<String>> getAllFacilities() {
                            return facilityIdList;
                        }

                        @Override
                        public List<String> getAllProgrammedAgents() {
                            return Collections.emptyList();
                        }

                        @Override
                        public void deleteProgrammedAgent(String agentName) {
                        }
                    });
                }
            });
        }

        public static LinearConfiguration getInstance() {
            if(testConfiguration == null) {
                testConfiguration = new LinearConfiguration();
            }
            return testConfiguration;
        }

        IParticipant createAgent(int facilityId, List<GameBusinessRules> gameBusinessRulesList) {
            IParticipant participant = new Agent(configuration, "TestDummy", facilityList.get(facilityId), gameBusinessRulesList);
            participantInjector.injectMembers(participant);
            return participant;
        }
    }
    public static class TreeConfiguration {
        private static TreeConfiguration testConfiguration;
        final List<Facility> facilityList;
        final Map<Facility, List<Facility>> facilitiesLinkedTo;
        final Configuration configuration;
        final List<List<String>> facilityIdList;
        private final Injector participantInjector;
        final IPlayerGameLogic gameLogic = mock(IPlayerGameLogic.class);

        private TreeConfiguration() {
            facilityList = Collections.unmodifiableList(Lists.newArrayList(
                    new Facility(
                            new FacilityType("factory 1", 3, 3, 10, 30, 1000, 3, 40), 1),
                    new Facility(
                            new FacilityType("regional warehouse 1", 3, 3, 10, 30, 1000, 3, 40), 2),
                    new Facility(
                            new FacilityType("regional warehouse 2", 3, 3, 10, 30, 1000, 3, 40), 3),
                    new Facility(
                            new FacilityType("wholesale 1", 3, 3, 10, 30, 1000, 3, 40), 4),
                    new Facility(
                            new FacilityType("wholesale 2", 3, 3, 10, 30, 1000, 3, 40), 5),
                    new Facility(
                            new FacilityType("wholesale 3", 3, 3, 10, 30, 1000, 3, 40), 6),
                    new Facility(
                            new FacilityType("wholesale 4", 3, 3, 10, 30, 1000, 3, 40), 7),
                    new Facility(
                            new FacilityType("retail 1", 3, 3, 10, 30, 1000, 3, 40), 8),
                    new Facility(
                            new FacilityType("retail 2", 3, 3, 10, 30, 1000, 3, 40), 9),
                    new Facility(
                            new FacilityType("retail 3", 3, 3, 10, 30, 1000, 3, 40), 10),
                    new Facility(
                            new FacilityType("retail 4", 3, 3, 10, 30, 1000, 3, 40), 11),
                    new Facility(
                            new FacilityType("retail 5", 3, 3, 10, 30, 1000, 3, 40), 12),
                    new Facility(
                            new FacilityType("retail 6", 3, 3, 10, 30, 1000, 3, 40), 13),
                    new Facility(
                            new FacilityType("retail 7", 3, 3, 10, 30, 1000, 3, 40), 14),
                    new Facility(
                            new FacilityType("retail 8", 3, 3, 10, 30, 1000, 3, 40), 15)));


            Map<Facility, List<Facility>> facilitiesLinkedToLocal = new HashMap<>();

            //Factories
            facilitiesLinkedToLocal.put(facilityList.get(0), Collections.emptyList());

            //Regional Warehouses
            facilitiesLinkedToLocal.put(facilityList.get(1), Collections.singletonList(
                    facilityList.get(0)));
            facilitiesLinkedToLocal.put(facilityList.get(2), Collections.singletonList(
                    facilityList.get(0)));

            //Wholesales
            facilitiesLinkedToLocal.put(facilityList.get(3), Collections.singletonList(
                    facilityList.get(1)));
            facilitiesLinkedToLocal.put(facilityList.get(4), Collections.singletonList(
                    facilityList.get(1)));
            facilitiesLinkedToLocal.put(facilityList.get(5), Collections.singletonList(
                    facilityList.get(2)));
            facilitiesLinkedToLocal.put(facilityList.get(6), Collections.singletonList(
                    facilityList.get(2)));

            //Retail
            facilitiesLinkedToLocal.put(facilityList.get(7), Collections.singletonList(
                    facilityList.get(3)));
            facilitiesLinkedToLocal.put(facilityList.get(8), Collections.singletonList(
                    facilityList.get(3)));
            facilitiesLinkedToLocal.put(facilityList.get(9), Collections.singletonList(
                    facilityList.get(4)));
            facilitiesLinkedToLocal.put(facilityList.get(10), Collections.singletonList(
                    facilityList.get(4)));
            facilitiesLinkedToLocal.put(facilityList.get(11), Collections.singletonList(
                    facilityList.get(5)));
            facilitiesLinkedToLocal.put(facilityList.get(12), Collections.singletonList(
                    facilityList.get(5)));
            facilitiesLinkedToLocal.put(facilityList.get(13), Collections.singletonList(
                    facilityList.get(6)));
            facilitiesLinkedToLocal.put(facilityList.get(14), Collections.singletonList(
                    facilityList.get(6)));

            facilitiesLinkedTo = Collections.unmodifiableMap(facilitiesLinkedToLocal);

            configuration = new Configuration(
                    10,
                    1,
                    1,
                    1,
                    1,
                    1,
                    1,
                    true,
                    false,
                    facilityList,
                    facilitiesLinkedTo);

            facilityIdList = Collections.unmodifiableList(Arrays.asList(
                    Collections.singletonList("1"),
                    Arrays.asList("2", "3"),
                    Arrays.asList("4", "5", "6", "7"),
                    Arrays.asList("8", "9", "10", "11", "12", "13", "14", "15")));

            participantInjector = Guice.createInjector(new AbstractModule() {
                @Override
                protected void configure() {
                    bind(IBusinessRules.class).annotatedWith(Names.named("businessRules")).to(BusinessRuleHandler.class);
                    bind(IPlayerGameLogic.class).toInstance(gameLogic);
                    bind(IPersistence.class).annotatedWith(Names.named("persistence")).toInstance(new IPersistence() {
                        @Override
                        public void saveGameLog(BeerGame beerGame, boolean isStarted) {

                        }

                        @Override
                        public void saveFacilityTurn(Round data) {
                        }

                        @Override
                        public Round fetchFacilityTurn(int roundId) {
                            return null;
                        }

                        @Override
                        public void saveRoundData(Round data) {

                        }

                        @Override
                        public Round fetchRoundData(int roundId) {
                            return null;
                        }

                        @Override
                        public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn) {
                        }
                    });
                    bind(IBusinessRuleStore.class).annotatedWith(Names.named("BusinessruleStore")).toInstance(new IBusinessRuleStore() {
                        @Override
                        public List<String> readInputBusinessRules(String agentName) {
                            return Collections.emptyList();
                        }

                        @Override
                        public void synchronizeBusinessRules(String agentName, Map<String, String> businessRuleMap) {
                        }

                        @Override
                        public List<List<String>> getAllFacilities() {
                            return facilityIdList;
                        }

                        @Override
                        public List<String> getAllProgrammedAgents() {
                            return Collections.emptyList();
                        }

                        @Override
                        public void deleteProgrammedAgent(String agentName) {
                        }
                    });
                }
            });
        }

        public static TreeConfiguration getInstance() {
            if(testConfiguration == null) {
                testConfiguration = new TreeConfiguration();
            }
            return testConfiguration;
        }

        IParticipant createAgent(int facilityId, List<GameBusinessRules> gameBusinessRulesList) {
            IParticipant participant = new Agent(configuration, "TestDummy", facilityList.get(facilityId), gameBusinessRulesList);
            participantInjector.injectMembers(participant);
            return participant;
        }
    }
    public static class GraphConfiguration {
        private static GraphConfiguration testConfiguration;
        final List<Facility> facilityList;
        final Map<Facility, List<Facility>> facilitiesLinkedTo;
        final Configuration configuration;
        final List<List<String>> facilityIdList;
        private final Injector participantInjector;
        final IPlayerGameLogic gameLogic = mock(IPlayerGameLogic.class);

        private GraphConfiguration() {
            facilityList = Collections.unmodifiableList(Lists.newArrayList(
                    new Facility(
                            new FacilityType("factory 1", 3, 3, 10, 30, 1000, 3, 40), 1),
                    new Facility(
                            new FacilityType("factory 2", 3, 3, 10, 30, 1000, 3, 40), 2),
                    new Facility(
                            new FacilityType("factory 3", 3, 3, 10, 30, 1000, 3, 40), 3),
                    new Facility(
                            new FacilityType("regional warehouse 1", 3, 3, 10, 30, 1000, 3, 40), 4),
                    new Facility(
                            new FacilityType("regional warehouse 2", 3, 3, 10, 30, 1000, 3, 40), 5),
                    new Facility(
                            new FacilityType("wholesale 1", 3, 3, 10, 30, 1000, 3, 40), 6),
                    new Facility(
                            new FacilityType("wholesale 2", 3, 3, 10, 30, 1000, 3, 40), 7),
                    new Facility(
                            new FacilityType("wholesale 3", 3, 3, 10, 30, 1000, 3, 40), 8),
                    new Facility(
                            new FacilityType("wholesale 4", 3, 3, 10, 30, 1000, 3, 40), 9),
                    new Facility(
                            new FacilityType("retail 1", 3, 3, 10, 30, 1000, 3, 40), 10),
                    new Facility(
                            new FacilityType("retail 2", 3, 3, 10, 30, 1000, 3, 40), 11),
                    new Facility(
                            new FacilityType("retail 3", 3, 3, 10, 30, 1000, 3, 40), 12),
                    new Facility(
                            new FacilityType("retail 4", 3, 3, 10, 30, 1000, 3, 40), 13),
                    new Facility(
                            new FacilityType("retail 5", 3, 3, 10, 30, 1000, 3, 40), 14),
                    new Facility(
                            new FacilityType("retail 6", 3, 3, 10, 30, 1000, 3, 40), 15)));


            Map<Facility, List<Facility>> facilitiesLinkedToLocal = new HashMap<>();

            //Factories
            facilitiesLinkedToLocal.put(facilityList.get(0), Collections.emptyList());
            facilitiesLinkedToLocal.put(facilityList.get(1), Collections.emptyList());
            facilitiesLinkedToLocal.put(facilityList.get(2), Collections.emptyList());

            //Regional Warehouses
            facilitiesLinkedToLocal.put(facilityList.get(3), Arrays.asList(
                    facilityList.get(0),
                    facilityList.get(1),
                    facilityList.get(2)));
            facilitiesLinkedToLocal.put(facilityList.get(4), Collections.singletonList(
                    facilityList.get(1)));

            //Wholesales
            facilitiesLinkedToLocal.put(facilityList.get(5), Arrays.asList(
                    facilityList.get(3),
                    facilityList.get(4)));
            facilitiesLinkedToLocal.put(facilityList.get(6), Collections.singletonList(
                    facilityList.get(3)));
            facilitiesLinkedToLocal.put(facilityList.get(7), Collections.singletonList(
                    facilityList.get(3)));
            facilitiesLinkedToLocal.put(facilityList.get(8), Collections.singletonList(
                    facilityList.get(4)));

            //Retail
            facilitiesLinkedToLocal.put(facilityList.get(9), Arrays.asList(
                    facilityList.get(5),
                    facilityList.get(6),
                    facilityList.get(7)));
            facilitiesLinkedToLocal.put(facilityList.get(10), Collections.singletonList(
                    facilityList.get(5)));
            facilitiesLinkedToLocal.put(facilityList.get(11), Arrays.asList(
                    facilityList.get(6),
                    facilityList.get(7)));
            facilitiesLinkedToLocal.put(facilityList.get(12), Arrays.asList(
                    facilityList.get(6),
                    facilityList.get(7)));
            facilitiesLinkedToLocal.put(facilityList.get(13), Collections.singletonList(
                    facilityList.get(8)));
            facilitiesLinkedToLocal.put(facilityList.get(14), Collections.singletonList(
                    facilityList.get(7)));

            facilitiesLinkedTo = Collections.unmodifiableMap(facilitiesLinkedToLocal);

            configuration = new Configuration(
                    10,
                    1,
                    1,
                    1,
                    1,
                    1,
                    1,
                    true,
                    false,
                    facilityList,
                    facilitiesLinkedTo);

            facilityIdList = Collections.unmodifiableList(Arrays.asList(
                    Arrays.asList("1", "2", "3"),
                    Arrays.asList("4", "5"),
                    Arrays.asList("6", "7", "8", "9"),
                    Arrays.asList("10", "11", "12", "13", "14", "15")));

            participantInjector = Guice.createInjector(new AbstractModule() {
                @Override
                protected void configure() {
                    bind(IBusinessRules.class).annotatedWith(Names.named("businessRules")).to(BusinessRuleHandler.class);
                    bind(IPlayerGameLogic.class).toInstance(gameLogic);
                    bind(IPersistence.class).annotatedWith(Names.named("persistence")).toInstance(new IPersistence() {
                        @Override
                        public void saveGameLog(BeerGame beerGame, boolean isStarted) {

                        }

                        @Override
                        public void saveFacilityTurn(Round data) {
                        }

                        @Override
                        public Round fetchFacilityTurn(int roundId) {
                            return null;
                        }

                        @Override
                        public void saveRoundData(Round data) {

                        }

                        @Override
                        public Round fetchRoundData(int roundId) {
                            return null;
                        }

                        @Override
                        public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn) {
                        }
                    });
                    bind(IBusinessRuleStore.class).annotatedWith(Names.named("BusinessruleStore")).toInstance(new IBusinessRuleStore() {
                        @Override
                        public List<String> readInputBusinessRules(String agentName) {
                            return Collections.emptyList();
                        }

                        @Override
                        public void synchronizeBusinessRules(String agentName, Map<String, String> businessRuleMap) {
                        }

                        @Override
                        public List<List<String>> getAllFacilities() {
                            return facilityIdList;
                        }

                        @Override
                        public List<String> getAllProgrammedAgents() {
                            return Collections.emptyList();
                        }

                        @Override
                        public void deleteProgrammedAgent(String agentName) {
                        }
                    });
                }
            });
        }

        public static GraphConfiguration getInstance() {
            if(testConfiguration == null) {
                testConfiguration = new GraphConfiguration();
            }
            return testConfiguration;
        }

        IParticipant createAgent(int facilityId, List<GameBusinessRules> gameBusinessRulesList) {
            IParticipant participant = new Agent(configuration, "TestDummy", facilityList.get(facilityId), gameBusinessRulesList);
            participantInjector.injectMembers(participant);
            return participant;
        }
    }
}
