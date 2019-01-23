package org.han.ica.asd.c.gui_main_menu;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.communication.IConnectorForSetup;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.interfaces.communication.IConnectorProvider;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;


public class MainMenuController {

    @FXML
    private Button close;

    @FXML
    private ComboBox<String> chooseIpComboBox;

    @FXML
    private TextField usernameField;

    @Inject
    @Named("GameSetupStart")
    private IGUIHandler gameSetupStart;

    @Inject
    @Named("ProgramAgentList")
    private IGUIHandler programAgentList;

    @Inject
    @Named("ReplayGameList")
    private IGUIHandler replayGameList;

    @Inject
    @Named("JoinGame")
    private IGUIHandler joinGame;

    @FXML
    private void closeButtonAction() {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    @Inject
    IConnectorForSetup connector;

    @Inject
    IConnectorProvider connectorProvider;

    public void initialize() {
        connectorProvider.setInstance(connector);
        connector = connectorProvider.forSetup();
        connector.start();

        fillIpComboBox();
    }

    private void fillIpComboBox(){
        ObservableList<String> ips = FXCollections.observableArrayList();
        for (Map.Entry e :connector.listAllIPs().entrySet()) {
            ips.add(e.getKey() + ": " + e.getValue());
        }
        chooseIpComboBox.setItems(ips);
        chooseIpComboBox.getSelectionModel().selectFirst();
        String value = chooseIpComboBox.getValue();
        connector.setMyIp(value.substring(value.indexOf(": ") + 2));
        if(Player.globalUsername != null) {
        	usernameField.setText(Player.globalUsername);
				}
				usernameField.textProperty().addListener((observable, oldValue, newValue) -> Player.globalUsername = newValue);
    }

    @FXML
    public void programAgentButtonAction() {
        programAgentList.setupScreen();
    }

    @FXML
    private void replayButtonAction() {
        replayGameList.setupScreen();
    }

    @FXML
    public void handleCreateGameButtonClick() {
        gameSetupStart.setupScreen();
    }

    @FXML
    public void handleJoinGameButtonClick() {
        joinGame.setupScreen();
    }

    @FXML
    public void onSelectionChanged(ActionEvent actionEvent) {
        String value = chooseIpComboBox.getValue();
        connector.setMyIp(value.substring(value.indexOf(": ") + 2));
    }
}
