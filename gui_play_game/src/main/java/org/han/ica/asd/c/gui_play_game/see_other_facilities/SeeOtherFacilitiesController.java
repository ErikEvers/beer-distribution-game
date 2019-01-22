package org.han.ica.asd.c.gui_play_game.see_other_facilities;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.han.ica.asd.c.fxml_helper.treebuilder.TreeBuilder;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Controller for the facility overview. Accessible when turned on in the game configuration.
 */

public class SeeOtherFacilitiesController {
    @FXML
    private AnchorPane mainContainer;

    @FXML
    private AnchorPane facilitiesContainer;

    @Inject @Named("PlayerComponent")
    private IPlayerComponent playerComponent;

    /**
     * Initialises the facility overview screen by calling the loadFacilityView() method.
     */
    public void initialize() {
        mainContainer.getChildren().addAll();
        new TreeBuilder().loadFacilityView(playerComponent.getBeerGame(), facilitiesContainer, true);
    }

    public void handleBackToGameButtonClicked(){
        Stage stage = (Stage) facilitiesContainer.getScene().getWindow();
        stage.close();
    }

    public void updateTreeBuilder() {
        new TreeBuilder().loadFacilityView(playerComponent.getBeerGame(), facilitiesContainer, true);
    }
}
