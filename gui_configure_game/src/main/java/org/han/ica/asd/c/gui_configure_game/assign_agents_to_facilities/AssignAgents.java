package org.han.ica.asd.c.gui_configure_game.assign_agents_to_facilities;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import java.util.ResourceBundle;

public class AssignAgents implements IGUIHandler {
    @Override
    public void setData(Object[] data) {
        //impl
    }

    @Override
    public void setupScreen() {
        AssignAgentsController controller = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResources"), getClass().getResource("/fxml/AssignAgentsToFacilities.fxml"));
    }
}
