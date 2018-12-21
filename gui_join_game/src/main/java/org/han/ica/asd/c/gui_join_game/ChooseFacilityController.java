package org.han.ica.asd.c.gui_join_game;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.interface_models.RoomModel;

import javax.inject.Inject;
import javax.inject.Named;

public class ChooseFacilityController {
    private ToggleGroup radioButtons = new ToggleGroup();

    @Inject
    @Named("ChooseAgent")
    private IGUIHandler chooseAgent;

    @Inject
    @Named("MainMenu")
    private IGUIHandler mainMenu;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private RadioButton factory;

    @FXML
    private RadioButton regionalWarehouse;

    @FXML
    private RadioButton wholesale;

    @FXML
    private RadioButton retailer;

    public void initialize() {
        factory.setToggleGroup(radioButtons);
        regionalWarehouse.setToggleGroup(radioButtons);
        wholesale.setToggleGroup(radioButtons);
        retailer.setToggleGroup(radioButtons);
        mainContainer.getChildren().addAll();
    }

    public void handleChooseFacilityButtonClick() {
        chooseAgent.setupScreen();
    }

    public void handleBackToMenuButtonClick() {
        mainMenu.setupScreen();
    }

    public void setGameRoom(RoomModel roomModel){
        //TODO GET ROOM
    }
}
