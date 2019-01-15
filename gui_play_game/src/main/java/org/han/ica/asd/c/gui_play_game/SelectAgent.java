package org.han.ica.asd.c.gui_play_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.Facility;

public class SelectAgent implements IGUIHandler {
    private Facility facility;

    @Override
    public void setData(Object[] data) {
        facility = (Facility) data[0];
    }

    @Override
    public void setupScreen() {
        SelectAgentController controller = FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/SelectAgent.fxml"));
        controller.setFacility(facility);
    }
}