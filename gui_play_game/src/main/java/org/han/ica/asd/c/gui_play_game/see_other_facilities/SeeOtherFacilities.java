package org.han.ica.asd.c.gui_play_game.see_other_facilities;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

public class SeeOtherFacilities implements IGUIHandler {

	@Override
	public void setData(Object[] data) {
		// stub
	}

	@Override
	public void setupScreen() {
		FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/SeeOtherFacilities.fxml"));
	}
}
