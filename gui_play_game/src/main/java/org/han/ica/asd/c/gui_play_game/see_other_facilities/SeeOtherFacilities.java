package org.han.ica.asd.c.gui_play_game.see_other_facilities;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.Configuration;

public class SeeOtherFacilities implements IGUIHandler {
	private Configuration configuration;

	@Override
	public void setData(Object[] data) {
		this.configuration = (Configuration) data[0];
	}

	@Override
	public void setupScreen() {
		SeeOtherFacilitiesController controller = FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/SeeOtherFacilities.fxml"));
		controller.setConfiguration(this.configuration);
	}
}
