package org.han.ica.asd.c.gui_join_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.communication.IConnectorForSetup;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

import java.util.ResourceBundle;

public class GameRoom implements IGUIHandler {
    private RoomModel roomModel;
    private IConnectorForSetup iConnectorForSetup;

    @Override
    public void setData(Object[] data) {
        this.roomModel = (RoomModel) data[0];
        this.iConnectorForSetup = (IConnectorForSetup) data[1];
    }

    @Override
    public void setupScreen() {
        GameRoomController gameRoomController = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResourcesGuiJoinGame"), getClass().getResource("/fxml/GameRoom.fxml"));
        gameRoomController.setGameRoom(roomModel);
        gameRoomController.setIConnectorForSetup(iConnectorForSetup);
    }
}
