package org.han.ica.asd.c.gui_spelers_beheren;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Player;

import java.util.ResourceBundle;

public class MonitorPlayers implements IGUIHandler {


	@Override
	public void setData(Object[] data) {

	}

	public void setupScreen() {
		MonitorPlayersScreenController controller = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResources"), getClass().getResource("/fxml/MonitorPlayersScreen.fxml"));
	}
}
