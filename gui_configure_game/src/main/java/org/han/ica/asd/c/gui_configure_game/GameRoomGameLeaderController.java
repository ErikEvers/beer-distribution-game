package org.han.ica.asd.c.gui_configure_game;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.exceptions.communication.TransactionException;
import org.han.ica.asd.c.exceptions.gameleader.BeerGameException;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.fxml_helper.treebuilder.FacilityRectangle;
import org.han.ica.asd.c.fxml_helper.treebuilder.FacilitySelectedEvent;
import org.han.ica.asd.c.fxml_helper.treebuilder.FacilitySelectedEventHandler;
import org.han.ica.asd.c.fxml_helper.treebuilder.TreeBuilder;
import org.han.ica.asd.c.interfaces.communication.IConnectorForSetup;
import org.han.ica.asd.c.interfaces.gameleader.IGameLeader;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

import javax.inject.Inject;
import javax.inject.Named;

public class GameRoomGameLeaderController {

	@FXML
	private AnchorPane facilitiesContainer;

	@FXML
	private Label gameRoom;

	@Inject
	@Named("JoinGame")
	private IGUIHandler joinGame;

	@Inject
	@Named("Connector")
	private IConnectorForSetup iConnectorForSetup;

	@Inject
	@Named("GameLeader")
	private IGameLeader gameLeader;

	private RoomModel roomModel;

	public void initialize() {
		gameRoom.setText(gameLeader.getBeerGame().getGameName());
		facilitiesContainer.addEventHandler(FacilitySelectedEvent.FACILITY_SELECTED_EVENT, new FacilitySelectedEventHandler(){
			@Override
			public void handleFacilitySelection(FacilityRectangle facilityRectangle) {
				handleRefreshButtonClick();
			}
		});
		reloadTree();
	}

	public void handleRefreshButtonClick() {
		reloadTree();
	}

	public void handleBackToJoinGameButtonClick() {
		iConnectorForSetup.removeYourselfFromRoom(roomModel);
		joinGame.setupScreen();

	}

	public void handleStartButtonClick() {
		try {
			gameLeader.startGame();
		} catch (BeerGameException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR, "Can't start a game unless every player controls a facility", ButtonType.CLOSE);
			alert.showAndWait();
		} catch (TransactionException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
			alert.showAndWait();
		}
	}

	public void setRoomModel(RoomModel roomModel) {
		this.roomModel = roomModel;
	}

	private void reloadTree() {
		new TreeBuilder().loadFacilityView(gameLeader.getBeerGame(), facilitiesContainer, false);
	}

}

