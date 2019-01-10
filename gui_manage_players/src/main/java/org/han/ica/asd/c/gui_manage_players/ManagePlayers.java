package org.han.ica.asd.c.gui_manage_players;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import java.util.ResourceBundle;

public class ManagePlayers implements IGUIHandler {


	@Override
	public void setData(Object[] data) {
		// implement interface
	}

	public void setupScreen() {
		FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResourcesManagePlayers"), getClass().getResource("/fxml/ManagePlayersScreen.fxml"));
	}
}
