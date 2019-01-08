package org.han.ica.asd.c.gui_play_game.see_other_facilities;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.BeerGame;

public class SeeOtherFacilities implements IGUIHandler {
	private BeerGame beerGame;

	@Override
	public void setData(Object[] data) {
		this.beerGame = (BeerGame) data[0];
	}

	@Override
	public void setupScreen() {
		SeeOtherFacilitiesController controller = FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/SeeOtherFacilities.fxml"));
		controller.setBeerGame(this.beerGame);
	}
}
