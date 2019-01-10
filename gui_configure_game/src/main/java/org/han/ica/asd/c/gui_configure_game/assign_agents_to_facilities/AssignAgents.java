package org.han.ica.asd.c.gui_configure_game.assign_agents_to_facilities;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.BeerGame;

import java.util.ResourceBundle;

public class AssignAgents implements IGUIHandler {

    private BeerGame beerGame;

    @Override
    public void setData(Object[] data) {
        this.beerGame = (BeerGame) data[0];
    }

    @Override
    public void setupScreen() {
        AssignAgentsController controller = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResourcesAssignAgents"), getClass().getResource("/fxml/AssignAgentsToFacilities.fxml"));
        //controller.setBeerGame(this.beerGame);
    }
}
