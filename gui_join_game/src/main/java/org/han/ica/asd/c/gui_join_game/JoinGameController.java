package org.han.ica.asd.c.gui_join_game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import javax.inject.Inject;
import javax.inject.Named;

public class JoinGameController {
    @FXML
    Button joinGameButton;

    @FXML
    ListView list;
    @Inject
    @Named("ChooseFacility")
    private IGUIHandler chooseFacility;

    @Inject
    @Named("MainMenu")
    private IGUIHandler mainMenu;

    private ObservableList<String> items = FXCollections.observableArrayList();

    public void initialize() {
        //TODO connect with IConnectorForSetup getAvailableRooms;
        list.setItems(items);
        items.add("hello");
        items.add("lol");
    }

    public void handleJoinGameButtonClick() {
        //TODO Join room on IConnectorForSetup. If logged in succesful then set Room

        if (true) {
            list.getSelectionModel().getSelectedItem();
            //chooseFacility.setData(new Object[]{"hello"});
            chooseFacility.setupScreen();
        }
    }

    public void handleBackToMenuButtonClick() {
        mainMenu.setupScreen();
    }

    public void handleListClick() {
        joinGameButton.setVisible(true);
    }
}
