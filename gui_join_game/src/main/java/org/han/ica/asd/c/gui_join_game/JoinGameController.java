package org.han.ica.asd.c.gui_join_game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import org.han.ica.asd.c.exceptions.communication.DiscoveryException;
import org.han.ica.asd.c.exceptions.communication.RoomException;
import org.han.ica.asd.c.exceptions.communication.SendGameMessageException;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.communication.IConnectorForSetup;
import org.han.ica.asd.c.interfaces.communication.IConnectorProvider;
import org.han.ica.asd.c.model.domain_objects.GamePlayerId;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Optional;

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
    private IConnectorProvider connectorProvider;

    private IConnectorForSetup iConnectorForSetup;



    private ObservableList<String> items = FXCollections.observableArrayList();

    public void initialize() {
        iConnectorForSetup = connectorProvider.forSetup();
        items.addAll(iConnectorForSetup.getAvailableRooms());
        list.setItems(items);
    }

    public void handleJoinGameButtonClick() {
        TextInputDialog nameInput = new TextInputDialog();
        nameInput.setHeaderText("Enter the name you want to use:");
        Optional<String> output = nameInput.showAndWait();
        if(output.isPresent()) {
            try {
                RoomModel result = iConnectorForSetup.joinRoom(list.getSelectionModel().getSelectedItem().toString(), "");
                GamePlayerId gameData = iConnectorForSetup.getGameData(output.get());
                gameRoom.setData(new Object[]{result, gameData.getBeerGame(), gameData.getPlayerId()});
                gameRoom.setupScreen();
            } catch (RoomException | DiscoveryException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
                alert.show();
            } catch (SendGameMessageException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, e.toString(), ButtonType.CLOSE);
                alert.show();
            }
        } else {
            nameInput.close();
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
