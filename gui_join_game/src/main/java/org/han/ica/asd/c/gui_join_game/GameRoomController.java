package org.han.ica.asd.c.gui_join_game;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.dao.DaoConfig;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.fxml_helper.treebuilder.TreeBuilder;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.interfaces.communication.IConnectorForSetup;
import org.han.ica.asd.c.interfaces.persistence.IGameStore;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.RoomModel;
import org.han.ica.asd.c.player.PlayerComponent;

import javax.inject.Inject;
import javax.inject.Named;

public class GameRoomController {
	private RoomModel roomModel;
	private BeerGame beerGame;

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
	@Named("PlayerComponent")
	private IPlayerComponent playerComponent;

    public void initialize() {
		}

    public void handleBackToJoinGameButtonClick() {
    	iConnectorForSetup.removeYourselfFromRoom(roomModel);
        joinGame.setupScreen();

    }

    public void handleReadyButtonClick() {
    	playerComponent.chooseFacility(TreeBuilder.getLastClickedFacility());
    }

    public void setGameData(RoomModel roomModel, BeerGame beerGame) {
        this.roomModel = roomModel;
				this.beerGame = beerGame;
        gameRoom.setText(roomModel.getRoomName());

				DaoConfig.setCurrentGameId(beerGame.getGameId());
				new TreeBuilder().loadFacilityView(beerGame, facilitiesContainer, false);
    }
}
