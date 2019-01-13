package org.han.ica.asd.c.gui_join_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

import java.util.ResourceBundle;

public class GameRoom implements IGUIHandler {
    private RoomModel roomModel;
    private BeerGame beerGame;
    private String playerId;

    @Override
    public void setData(Object[] data) {
        this.roomModel = (RoomModel) data[0];
        this.beerGame = (BeerGame) data[1];
        this.playerId = (String) data[2];
    }

    @Override
    public void setupScreen() {
        GameRoomController gameRoomController = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResourcesGuiJoinGame"), getClass().getResource("/fxml/GameRoom.fxml"));
        gameRoomController.setGameData(roomModel, beerGame, playerId);
    }
}
