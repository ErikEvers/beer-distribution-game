package org.han.ica.asd.c.gui_join_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.interface_models.RoomModel;

import java.util.ResourceBundle;

public class ChooseFacility implements IGUIHandler {
    private RoomModel roomModel;

    @Override
    public void setData(Object[] data) {
        this.roomModel = (RoomModel) data[0];
    }

    @Override
    public void setupScreen() {
        ChooseFacilityController chooseFacilityController = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResourcesGuiJoinGame"), getClass().getResource("/fxml/ChooseFacility.fxml"));
        chooseFacilityController.setGameRoom(roomModel);
    }
}
