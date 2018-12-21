package org.han.ica.asd.c.gui_replay_game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import javax.inject.Inject;
import javax.inject.Named;

public class ReplayGameListController {
    @FXML
    AnchorPane mainContainer;

    @FXML
    Button replay;

    @FXML
    ListView list;

    @Inject
    @Named("ReplayGame")
    IGUIHandler replayGame;

    @Inject
    @Named("MainMenu")
    IGUIHandler mainMenu;

    private ObservableList<String> items = FXCollections.observableArrayList();


    /***
     * Function for initialising the current ProgramAgentInfo FXML. It also sets the actions of the button to close current window on click.
     */
    public void initialize() {
//TODO INJECT PRESSICTENCE AND GET ALL GAMES.
        list.setItems(items);
        items.add("hello");
        items.add("lol");
    }

    @FXML
    private void replayButtonAction() {
        replayGame.setData(new Object[]{list.getSelectionModel().getSelectedItem()});
        replayGame.setupScreen();
    }

    @FXML
    private void backButtonAction() {
        mainMenu.setupScreen();
    }

    @FXML
    public void handleMouseClickOnList(MouseEvent mouseEvent) {
        replay.setVisible(true);
    }
}
