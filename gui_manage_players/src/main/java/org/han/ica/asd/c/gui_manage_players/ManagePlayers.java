package org.han.ica.asd.c.gui_manage_players;

import javafx.fxml.FXMLLoader;
import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.BeerGame;

import java.net.URL;
import java.util.ResourceBundle;

public class ManagePlayers implements IGUIHandler {

	private BeerGame beerGame;

	@Override
	public void setData(Object[] data) {
		this.beerGame = (BeerGame) data[0];
	}

	public void setupScreen() {
		URL test = getClass().getResource("/fxml/ManagePlayersScreen.fxml");
		ManagePlayersScreenController controller = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResourcesManagePlayers"), getClass().getResource("/fxml/ManagePlayersScreen.fxml"));
		controller.setBeerGame(beerGame);
	}
}
