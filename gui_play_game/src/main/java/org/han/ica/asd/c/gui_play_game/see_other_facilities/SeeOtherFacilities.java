package org.han.ica.asd.c.gui_play_game.see_other_facilities;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

public class SeeOtherFacilities implements IGUIHandler {
	private RoomModel roomModel;

	@Override
	public void setData(Object[] data) {
		this.roomModel = (RoomModel) data[0];
	}

	@Override
	public void setupScreen() {
		SeeOtherFacilitiesController controller = FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/SeeOtherFacilities.fxml"));
		controller.setGameRoom(this.roomModel);
	}
}
