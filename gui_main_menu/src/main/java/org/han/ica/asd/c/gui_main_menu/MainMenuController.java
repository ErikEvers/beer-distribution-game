package org.han.ica.asd.c.gui_main_menu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import org.han.ica.asd.c.dao.DaoConfig;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.gameleader.GameLeader;
import org.han.ica.asd.c.interfaces.communication.IConnectorForSetup;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.Leader;
import org.han.ica.asd.c.model.domain_objects.Player;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainMenuController {

    @FXML
    private Button close;

    @Inject
    @Named("ProgramAgentList")
    private IGUIHandler programAgentList;

    @Inject
    @Named("ReplayGameList")
    private IGUIHandler replayGameList;

    @Inject
    @Named("JoinGame")
    private IGUIHandler joinGame;

    @Inject
    @Named("AssignAgents")
    private IGUIHandler assignAgents;

    @Inject
    private IConnectorForSetup connector;

    @Inject
    private BeerGame beerGame;

    public void initialize() {
        Configuration configuration = new Configuration();

        Facility retailer = new Facility(new FacilityType("Retailer", 0, 0, 0, 0, 0, 0, 0), 0);
        Facility wholesale = new Facility(new FacilityType("Wholesaler", 0, 0, 0, 0, 0, 0, 0), 1);
        Facility warehouse = new Facility(new FacilityType("Regional Warehouse", 0, 0, 0, 0, 0, 0, 0), 2);
        Facility factory = new Facility(new FacilityType("Factory", 0, 0, 0, 0, 0, 0, 0), 3);

        List<Facility> facilityList = new ArrayList<>();
        facilityList.add(retailer);
        facilityList.add(wholesale);
        facilityList.add(warehouse);
        facilityList.add(factory);

        configuration.setFacilities(facilityList);

        Map<Facility, List<Facility>> links = new HashMap<>();
        List<Facility> list = new ArrayList<>();
        list.add(wholesale);
        links.put(retailer, list);

        list = new ArrayList<>();
        list.add(warehouse);
        links.put(wholesale, list);

        list = new ArrayList<>();
        list.add(factory);
        links.put(warehouse, list);

        configuration.setFacilitiesLinkedTo(links);

        configuration.setAmountOfWarehouses(1);
        configuration.setAmountOfFactories(1);
        configuration.setAmountOfWholesalers(1);
        configuration.setAmountOfRetailers(1);

        configuration.setAmountOfRounds(20);

        configuration.setContinuePlayingWhenBankrupt(false);

        configuration.setInsightFacilities(true);

        configuration.setMaximumOrderRetail(99);
        configuration.setMinimalOrderRetail(5);

        this.beerGame.setConfiguration(configuration);
        Player henk = new Player("1", "111", retailer, "Henk", true);
        this.beerGame.getPlayers().add(henk);
        this.beerGame.setLeader(new Leader(henk));
        this.beerGame.setGameId("123");
        this.beerGame.setGameName("Henks spel");
        this.beerGame.setGameDate("2019-01-08");

        DaoConfig.setCurrentGameId("123");


    }

    @FXML
    private void closeButtonAction() {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void programAgentButtonAction() {
        programAgentList.setupScreen();
    }

    @FXML
    private void replayButtonAction() {
        replayGameList.setupScreen();
    }

    public void handleCreateGameButtonClick() {
        assignAgents.setData(new Object[] { beerGame });
        assignAgents.setupScreen();
        //connector.start();
        //connector.createRoom("12345", "");
    }

    public void handleJoinGameButtonClick() {
        joinGame.setupScreen();
    }

    @FXML
    public void handleAssignAgentsButtonClick() {
        assignAgents.setupScreen();
    }


}
