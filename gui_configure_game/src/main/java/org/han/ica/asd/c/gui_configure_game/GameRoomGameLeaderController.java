package org.han.ica.asd.c.gui_configure_game;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
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
	@Named("PlayGame")
	private IGUIHandler playGame;

	@Inject
	@Named("Connector")
	private IConnectorForSetup iConnectorForSetup;

	@Inject
	@Named("GameLeader")
	private IGameLeader gameLeader;

	private RoomModel roomModel;

	public void initialize() {
		gameRoom.setText(gameLeader.getBeerGame().getGameName());
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
		iConnectorForSetup.startRoom(gameLeader.getRoomModel());
		playGame.setData(new Object[]{gameLeader.getBeerGame()});
		playGame.setupScreen();
	}

	public void setRoomModel(RoomModel roomModel) {
		this.roomModel = roomModel;
	}

	private void reloadTree() {
		new TreeBuilder().loadFacilityView(gameLeader.getBeerGame(), facilitiesContainer, false);
	}

}

