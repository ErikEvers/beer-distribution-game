package org.han.ica.asd.c.gui_join_game;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.gui_join_game.IConnecterForSetup;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

import javax.inject.Inject;
import javax.inject.Named;

public class GameRoomController {
    private RoomModel roomModel;
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
    private IConnecterForSetup iConnectorForSetup;

    public void handleBackToJoinGameButtonClick() {
        joinGame.setupScreen();
    }

    public void handleReadyButtonClick() {
        playGame.setupScreen();
    }

    public void setGameRoom(RoomModel roomModel) {
        this.roomModel = roomModel;
        gameRoom.setText(roomModel.getRoomName());
    }
}
