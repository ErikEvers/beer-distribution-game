package org.han.ica.asd.c.gui_join_game;

import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

import javax.inject.Inject;
import javax.inject.Named;

public class GameRoomController {
    private RoomModel roomModel;

    @Inject
    @Named("JoinGame")
    private IGUIHandler joinGame;

    @Inject
    @Named("PlayGame")
    private IGUIHandler playGame;

    public void handleBackToJoinGameButtonClick() {
        joinGame.setupScreen();
    }

    public void handleReadyButtonClick() {
        playGame.setupScreen();
    }

    public void setGameRoom(RoomModel roomModel) {
        this.roomModel = roomModel;
    }
}
