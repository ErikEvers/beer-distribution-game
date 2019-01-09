package org.han.ica.asd.c.gui_manage_players;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import java.util.ResourceBundle;

public class MonitorPlayers implements IGUIHandler {


	@Override
	public void setData(Object[] data) {
		// implement interface
	}

	public void setupScreen() {
		MonitorPlayersScreenController controller = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResources"), getClass().getResource("/fxml/MonitorPlayersScreen.fxml"));
	}
}
