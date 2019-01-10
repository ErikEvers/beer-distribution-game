package org.han.ica.asd.c.fxml_helper.treebuilder;

import javafx.event.Event;
import javafx.event.EventType;

public class FacilitySelectedEvent extends Event {

	public static final EventType<FacilitySelectedEvent> FACILITY_SELECTED_EVENT = new EventType(ANY);

	private final FacilityRectangle facilityRectangle;

	public FacilitySelectedEvent(FacilityRectangle facilityRectangle) {
		super(FACILITY_SELECTED_EVENT);
		this.facilityRectangle = facilityRectangle;
	}

	public void invokeHandler(FacilitySelectedEventHandler handler) {
		handler.handleFacilitySelection(facilityRectangle);
	}

}