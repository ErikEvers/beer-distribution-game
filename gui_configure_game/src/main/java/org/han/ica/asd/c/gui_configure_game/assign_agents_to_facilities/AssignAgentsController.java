package org.han.ica.asd.c.gui_configure_game.assign_agents_to_facilities;

import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.Exceptions.NoProgrammedAgentsFoundException;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.fxml_helper.treebuilder.FacilityRectangle;
import org.han.ica.asd.c.fxml_helper.treebuilder.TreeBuilder;
import org.han.ica.asd.c.gameconfiguration.IGameAgentService;
import org.han.ica.asd.c.model.domain_objects.*;

import javax.inject.Named;
import java.util.Optional;
import java.util.ResourceBundle;

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
    private ResourceBundle resourceBundle;

    public void initialize() {
        resourceBundle = ResourceBundle.getBundle("languageResourcesAssignAgents");
        try {
            agentComboBox.setItems(FXCollections.observableArrayList(gameAgentService.getAgentsForUI()));
        } catch (NoProgrammedAgentsFoundException e) {
            agentComboBox.setPromptText(resourceBundle.getString("no_agents_found_warning"));
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
        initTree();
    }

    @FXML
    public void handleAddAgentsButtonClick() {
        if (agentComboBox.getValue() != null) {
            for (int i = 0; i < beerGame.getAgents().size(); i++) {
                if (agentComboBox.getValue().getProgrammedAgentName().equals(beerGame.getAgents().get(i).getGameAgentName()) && (lastClickedFacilityRectangle.getFacility().getFacilityId() == beerGame.getAgents().get(i).getFacility().getFacilityId())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, resourceBundle.getString("agent_assigned_warning"));
                    alert.setHeaderText(resourceBundle.getString("warning_title"));
                    alert.showAndWait();
                }

                if (lastClickedFacilityRectangle.getFacility().getFacilityId() == beerGame.getAgents().get(i).getFacility().getFacilityId()) {
                    beerGame.getAgents().remove(i);
                }
            }
            beerGame.getAgents().add(gameAgentService.createGameAgentFromProgrammedAgent(lastClickedFacilityRectangle.getFacility(), agentComboBox.getValue()));
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, resourceBundle.getString("choose_agent_alert"));
            alert.setHeaderText(resourceBundle.getString("warning_title"));
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, resourceBundle.getString("assign_agents_menu_warning"));
        alert.setTitle(resourceBundle.getString("warning_title"));
        Optional<ButtonType> clicked = alert.showAndWait();

        if (clicked.get() == ButtonType.OK) {
            mainMenu.setupScreen();
        }
    }

    @FXML
    public void handleManagePlayersButtonClick() {
        managePlayers.setData(new Object[] { beerGame });
        managePlayers.setupScreen();
    }

    @FXML
    public void handleStartGameButtonClick() {
        this.beerGame = gameAgentService.fillEmptyFacilitiesWithDefaultAgents(beerGame);
        initTree();
    }

    private void addEventHandlerToFacilityRectangle(FacilityRectangle rectangle) {
        rectangle.setOnMouseClicked(event -> lastClickedFacilityRectangle = rectangle);
    }

}

