package org.han.ica.asd.c.gui_join_game;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.dao.DaoConfig;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.fxml_helper.treebuilder.FacilityRectangle;
import org.han.ica.asd.c.fxml_helper.treebuilder.FacilitySelectedEvent;
import org.han.ica.asd.c.fxml_helper.treebuilder.FacilitySelectedEventHandler;
import org.han.ica.asd.c.fxml_helper.treebuilder.TreeBuilder;
import org.han.ica.asd.c.interfaces.communication.IConnectorForSetup;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

public class GameRoomController {
	private RoomModel roomModel;
	private BeerGame beerGame;

	@FXML
	private AnchorPane facilitiesContainer;

	@FXML
	private Label gameRoom;

	@FXML
	private Button chooseFacilityButton;

	@Inject
	@Named("JoinGame")
	private IGUIHandler joinGame;

	@Inject
	@Named("PlayGame")
	private IGUIHandler playGame;

	@Inject
	@Named("Connector")
	private IConnectorForSetup iConnectorForSetup;

	@Inject
	@Named("PlayerComponent")
	private IPlayerComponent playerComponent;

	private Facility selectedFacility;

    public void initialize() {
    	facilitiesContainer.addEventHandler(FacilitySelectedEvent.FACILITY_SELECTED_EVENT, new FacilitySelectedEventHandler(){
				@Override
				public void handleFacilitySelection(FacilityRectangle facilityRectangle) {
					selectedFacility = facilityRectangle.getFacility();
					chooseFacilityButton.setDisable(false);
					handleRefreshButtonClick();
				}
			});
		}

    public void handleBackToJoinGameButtonClick() {
    	iConnectorForSetup.removeYourselfFromRoom(roomModel);
			DaoConfig.clearCurrentGameId();
    	playerComponent.clearPlayer();
    	joinGame.setupScreen();
    }

    public void handleChooseFacilityButtonClick() {
    	playerComponent.chooseFacility(selectedFacility);
    	handleRefreshButtonClick();
    }

    public void handleRefreshButtonClick() {
    	try {
				beerGame = iConnectorForSetup.getGameData("").getBeerGame();
				new TreeBuilder().loadFacilityView(beerGame, facilitiesContainer, false);
			} catch (IOException | ClassNotFoundException e) {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Something went wrong with updating the room data", ButtonType.CLOSE);
				alert.show();
			}
		}

    public void setGameData(RoomModel roomModel, BeerGame beerGame, String playerId) {
        this.roomModel = roomModel;
				this.beerGame = beerGame;

				playerComponent.setPlayer(beerGame.getPlayerById(playerId));
        gameRoom.setText(roomModel.getRoomName());

				DaoConfig.setCurrentGameId(beerGame.getGameId());
				new TreeBuilder().loadFacilityView(beerGame, facilitiesContainer, false);
    }
}
