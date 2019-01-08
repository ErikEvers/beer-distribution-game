package org.han.ica.asd.c.gui_play_game.see_other_facilities;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.fxml_helper.treebuilder.TreeBuilder;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.player.PlayerComponent;

/**
 * Controller for the facility overview. Accessible when turned on in the game configuration.
 */

public class SeeOtherFacilitiesController {
    @FXML
    private AnchorPane mainContainer;
    
    @FXML
    private AnchorPane facilitiesContainer;

    private PlayerComponent playerComponent;

    private Configuration configuration;

    /**
     * Initialises the facility overview screen by calling the loadFacilityView() method.
     */
    public void initialize() {
        playerComponent = new PlayerComponent();
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        playerComponent.setConfiguration(configuration);
        TreeBuilder.loadFacilityView(configuration.getFacilitiesLinkedTo(), facilitiesContainer, true);
    }
}
