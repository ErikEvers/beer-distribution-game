package org.han.ica.asd.c.gui_configure_game.assign_agents_to_facilities;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.exceptions.communication.TransactionException;
import org.han.ica.asd.c.exceptions.gameconfiguration.NoProgrammedAgentsFoundException;
import org.han.ica.asd.c.exceptions.gameleader.BeerGameException;
import org.han.ica.asd.c.exceptions.gameleader.FacilityNotAvailableException;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.fxml_helper.treebuilder.FacilityRectangle;
import org.han.ica.asd.c.fxml_helper.treebuilder.TreeBuilder;
import org.han.ica.asd.c.interfaces.gameconfiguration.IGameAgentService;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;
import org.han.ica.asd.c.interfaces.gameleader.IGameLeader;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;
import java.util.ResourceBundle;

public class AssignAgentsController {

    @FXML
    ComboBox<ProgrammedAgent> agentComboBox;

		@FXML
		private Button chooseFacilityButton;

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

		@Inject
		@Named("GameLeader")
		private IGameLeader gameLeader;

    private FacilityRectangle lastClickedFacilityRectangle;
    private ResourceBundle resourceBundle;

    public void initialize() {
        this.resourceBundle = ResourceBundle.getBundle("languageResourcesAssignAgents");
        try {
            agentComboBox.setItems(FXCollections.observableArrayList(gameAgentService.getAgentsForUI()));
        } catch (NoProgrammedAgentsFoundException e) {
            agentComboBox.setPromptText(resourceBundle.getString("no_agents_found_warning"));
        }
        initTree();
    }

    private void initTree() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.loadFacilityView(gameLeader.getBeerGame(), facilitiesContainer, false);

        for (FacilityRectangle rectangle : treeBuilder.getDrawnFacilities()) {
            addEventHandlerToFacilityRectangle(rectangle);
        }
    }

		public void handleChooseFacilityButtonClicked() {
    	try {
				gameLeader.chooseFacility(lastClickedFacilityRectangle.getFacility(), "0");
			} catch (FacilityNotAvailableException e) {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Facility already taken", ButtonType.CLOSE);
				alert.show();
			}
			initTree();
		}

    public void handleAddAgentsButtonClick() {
    		BeerGame beerGame = gameLeader.getBeerGame();
        if (agentComboBox.getValue() != null && lastClickedFacilityRectangle != null) {
            for (int i = 0; i < beerGame.getAgents().size(); i++) {
                if (agentComboBox.getValue().getProgrammedAgentName().equals(beerGame.getAgents().get(i).getGameAgentName()) && (lastClickedFacilityRectangle.getFacility().getFacilityId() == beerGame.getAgents().get(i).getFacility().getFacilityId())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, resourceBundle.getString("agent_assigned_warning"));
                    alert.setHeaderText(resourceBundle.getString("warning_title"));
                    alert.show();
                }

                if (lastClickedFacilityRectangle.getFacility().getFacilityId() == beerGame.getAgents().get(i).getFacility().getFacilityId()) {
                    beerGame.getAgents().remove(i);
                }
            }
            beerGame.getAgents().add(gameAgentService.createGameAgentFromProgrammedAgent(lastClickedFacilityRectangle.getFacility(), agentComboBox.getValue()));
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, resourceBundle.getString("choose_agent_alert"));
            alert.setHeaderText(resourceBundle.getString("warning_title"));
            alert.show();
        }
        initTree();
    }

    public void handleRemoveAgentsButtonClick() {
				BeerGame beerGame = gameLeader.getBeerGame();
        for (int i = 0; i < beerGame.getAgents().size(); i++) {
            if (lastClickedFacilityRectangle.getFacility().getFacilityId() == beerGame.getAgents().get(i).getFacility().getFacilityId()) {
                beerGame.getAgents().remove(i);
            }
        }
        initTree();
    }

		public void handleRefreshButtonClick() {
		initTree();
	}

    public void handleBackToMenuButtonClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, resourceBundle.getString("assign_agents_menu_warning"));
        alert.setTitle(resourceBundle.getString("warning_title"));
        Optional<ButtonType> clicked = alert.showAndWait();

        if (clicked.get() == ButtonType.OK) {
            mainMenu.setupScreen();
        }
    }

    public void handleManagePlayersButtonClick() {
        managePlayers.setupScreen();
    }

    public void handleStartGameButtonClick() {
        for (GameAgent agent : gameAgentService.fillEmptyFacilitiesWithDefaultAgents(gameLeader.getBeerGame())) {
            gameLeader.getBeerGame().getAgents().add(agent);
        }
        initTree();
				try {
					gameLeader.startGame();
				} catch (BeerGameException e) {
					Alert alert = new Alert(Alert.AlertType.ERROR, "Can't start a game unless every player controls a facility", ButtonType.CLOSE);
					alert.show();
				} catch (TransactionException e) {
					Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
					alert.show();
				}
    }

    private void addEventHandlerToFacilityRectangle(FacilityRectangle rectangle) {
        rectangle.setOnMouseClicked(event -> {
        	lastClickedFacilityRectangle = rectangle;
        	initTree();
				});
    }

}

