package org.han.ica.asd.c.gui_join_game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import org.han.ica.asd.c.exceptions.communication.DiscoveryException;
import org.han.ica.asd.c.exceptions.communication.RoomException;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.gamelogic.GameLogic;
import org.han.ica.asd.c.interfaces.communication.IConnectorForSetup;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.model.domain_objects.GamePlayerId;
import org.han.ica.asd.c.model.domain_objects.RoomModel;
import org.han.ica.asd.c.player.PlayerComponent;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

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
    private IConnectorForSetup iConnectorForSetup;

    private ObservableList<String> items = FXCollections.observableArrayList();

    public void initialize() {
        iConnectorForSetup.start();
        items.addAll(iConnectorForSetup.getAvailableRooms());
        list.setItems(items);
    }

    public void handleJoinGameButtonClick() {
        //TODO Join room on IConnectorForSetup. If logged in succesful then set Room
        try {
            RoomModel result = iConnectorForSetup.joinRoom(list.getSelectionModel().getSelectedItem().toString(),  "");
            GamePlayerId gameData = iConnectorForSetup.getGameData();
						PlayerComponent.setPlayer(gameData.getBeerGame().getPlayerById(gameData.getPlayerId()));
            gameRoom.setData(new Object[]{result, gameData.getBeerGame(), gameData.getPlayerId()});
            gameRoom.setupScreen();
        } catch (RoomException | DiscoveryException | ClassNotFoundException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    public void handleBackToMenuButtonClick() {
        mainMenu.setupScreen();
    }

    public void handleRefreshButtonClick() {
        items.clear();
        items.addAll(iConnectorForSetup.getAvailableRooms());
    }

    public void handleListClick() {
        if(list.getSelectionModel().getSelectedItem() !=null) {
            joinGameButton.setVisible(true);
        }
    }
}
