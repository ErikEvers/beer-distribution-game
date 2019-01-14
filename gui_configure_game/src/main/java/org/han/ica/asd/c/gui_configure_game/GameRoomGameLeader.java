package org.han.ica.asd.c.gui_configure_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

import java.util.ResourceBundle;

public class GameRoomGameLeader implements IGUIHandler {
	RoomModel roomModel;

	@Override
	public void setData(Object[] data) {
		this.roomModel = (RoomModel) data[0];
	}

	@Override
	public void setupScreen() {
		GameRoomGameLeaderController gameRoomController = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResourcesGuiConfigureGame"), getClass().getResource("/fxml/GameRoomGameLeader.fxml"));
		gameRoomController.setRoomModel(roomModel);
	}
}
