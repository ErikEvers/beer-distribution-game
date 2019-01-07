package org.han.ica.asd.c.gui_join_game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.gui_join_game.IConnecterForSetup;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

import javax.inject.Inject;
import javax.inject.Named;

public class JoinGameController {
    @FXML
    Button joinGameButton;

    @FXML
    ListView list;
    @Inject
    @Named("GameRoom")
    private IGUIHandler gameRoom;

    @Inject
    @Named("MainMenu")
    private IGUIHandler mainMenu;

    @Inject
    @Named("Connector")
    private IConnecterForSetup iConnectorForSetup;

    private ObservableList<String> items = FXCollections.observableArrayList();

    public void initialize() {
        items.addAll(iConnectorForSetup.getAvailableRooms());
        list.setItems(items);
    }

    public void handleJoinGameButtonClick() {
        //TODO Join room on IConnectorForSetup. If logged in succesful then set Room
        RoomModel result = iConnectorForSetup.joinRoom(list.getSelectionModel().getSelectedItem().toString(),"145.74.199.201","");
        if (result != null) {
            gameRoom.setData(new Object[]{result});
            gameRoom.setupScreen();
        }
    }

    public void handleBackToMenuButtonClick() {
        mainMenu.setupScreen();
    }

    public void handleListClick() {
        if(list.getSelectionModel().getSelectedItem() !=null) {
            joinGameButton.setVisible(true);
        }
    }
}
