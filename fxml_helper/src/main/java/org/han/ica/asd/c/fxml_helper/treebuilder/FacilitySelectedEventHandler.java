package org.han.ica.asd.c.fxml_helper.treebuilder;

import javafx.event.EventHandler;

public abstract class FacilitySelectedEventHandler implements EventHandler<FacilitySelectedEvent> {

	public abstract void handleFacilitySelection(FacilityRectangle facilityRectangle);

	@Override
	public void handle(FacilitySelectedEvent event) {
		event.invokeHandler(this);
	}
}
