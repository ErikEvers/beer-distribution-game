package org.han.ica.asd.c.gui_main_menu;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

public class ChooseFacilityController {
    private ToggleGroup radioButtons= new ToggleGroup();

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

    public void initialize(){
        factory.setToggleGroup(radioButtons);
        regionalWarehouse.setToggleGroup(radioButtons);
        wholesale.setToggleGroup(radioButtons);
        retailer.setToggleGroup(radioButtons);
        mainContainer.getChildren().addAll();
    }
}
