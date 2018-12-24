package org.han.ica.asd.c.gui_spelers_beheren;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

public class MonitorPlayers implements IGUIHandler {
	public void setupScreen() {
		MonitorPlayersScreenController controller = FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/MonitorPlayersScreen.fxml"));
	}
}
