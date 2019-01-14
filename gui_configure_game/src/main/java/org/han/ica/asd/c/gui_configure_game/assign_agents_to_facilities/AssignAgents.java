package org.han.ica.asd.c.gui_configure_game.assign_agents_to_facilities;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.BeerGame;

import java.util.ResourceBundle;

public class AssignAgents implements IGUIHandler {

    @Override
    public void setData(Object[] data) {
        // unused
    }

    @Override
    public void setupScreen() {
        FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResourcesAssignAgents"), getClass().getResource("/fxml/AssignAgentsToFacilities.fxml"));
    }
}
