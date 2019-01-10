package org.han.ica.asd.c.gui_configure_game.assign_agents_to_facilities;

import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import org.han.ica.asd.c.Exceptions.NoProgrammedAgentsFoundException;
import org.han.ica.asd.c.agent.Agent;
import org.han.ica.asd.c.dao.DaoConfig;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.fxml_helper.treebuilder.FacilityRectangle;
import org.han.ica.asd.c.fxml_helper.treebuilder.TreeBuilder;
import org.han.ica.asd.c.gameconfiguration.IGameAgentService;
import org.han.ica.asd.c.model.domain_objects.*;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignAgentsController {

    @FXML
    ComboBox<ProgrammedAgent> agentComboBox;

    @FXML
    private AnchorPane facilitiesContainer;

    @Inject
    private IGameAgentService gameAgentService;

    @Inject
    @Named("ManagePlayers")
    private IGUIHandler managePlayers;

    @Inject
    @Named("MainMenu")
    private IGUIHandler mainMenu;

    private BeerGame beerGame;
    private FacilityRectangle lastClickedFacilityRectangle;

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

        this.beerGame = new BeerGame();
        this.beerGame.setConfiguration(configuration);
        Player henk = new Player("1", "111", retailer, "Henk", true);
        this.beerGame.getPlayers().add(henk);
        this.beerGame.setLeader(new Leader(henk));
        this.beerGame.setGameId("123");
        this.beerGame.setGameName("Henks spel");
        this.beerGame.setGameDate("2019-01-08");

        DaoConfig.setCurrentGameId("123");

        initTree();

        try {
            agentComboBox.setItems(FXCollections.observableArrayList(gameAgentService.getAgentsForUI()));
        } catch (NoProgrammedAgentsFoundException e) {
            agentComboBox.setPromptText("No local agents found.");
        }
    }

    private void initTree() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.loadFacilityView(beerGame, facilitiesContainer, false);

        for (FacilityRectangle rectangle : treeBuilder.getDrawnFacilities()) {
            addEventHandlerToFacilityRectangle(rectangle);
        }
    }

    public void setBeerGame(BeerGame beerGame) {
        this.beerGame = beerGame;
    }

    @FXML
    public void handleAddAgentsButtonClick() {
        if (agentComboBox.getValue() != null) {
            for (int i = 0; i < beerGame.getAgents().size(); i++) {
                if (agentComboBox.getValue().getProgrammedAgentName().equals(beerGame.getAgents().get(i).getGameAgentName()) && (lastClickedFacilityRectangle.getFacility().getFacilityId() == beerGame.getAgents().get(i).getFacility().getFacilityId())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "This agent is already assigned to the selected facility.");
                    alert.setHeaderText("Warning");
                    alert.setTitle("Warning");
                    alert.showAndWait();
                }

                if (lastClickedFacilityRectangle.getFacility().getFacilityId() == beerGame.getAgents().get(i).getFacility().getFacilityId()) {
                    beerGame.getAgents().remove(i);
                }
            }
            beerGame.getAgents().add(gameAgentService.createGameAgentFromProgrammedAgent(lastClickedFacilityRectangle.getFacility(), agentComboBox.getValue()));
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Choose an agent from the available agents.");
            alert.setHeaderText("Warning");
            alert.setTitle("Warning");
            alert.showAndWait();
        }
        initTree();
    }

    @FXML
    public void handleRemoveAgentsButtonClick() {
        for (int i = 0; i < beerGame.getAgents().size(); i++) {
            if (lastClickedFacilityRectangle.getFacility().getFacilityId() == beerGame.getAgents().get(i).getFacility().getFacilityId()) {
                beerGame.getAgents().remove(i);
            }
        }
        initTree();
    }

    @FXML
    public void handleBackToMenuButtonClick() {
        mainMenu.setupScreen();
    }

    @FXML
    public void handleManagePlayersButtonClick() {
        managePlayers.setupScreen();
    }

    private void addEventHandlerToFacilityRectangle(FacilityRectangle rectangle) {
        rectangle.setOnMouseClicked(event -> lastClickedFacilityRectangle = rectangle);
    }

}

