package org.han.ica.asd.c;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class MainMenuController {
    @FXML
    private AnchorPane mainContainer;
    @FXML
    private Button createGameButton;

    public void initialize(){
        mainContainer.getChildren().addAll();
    }

    public void handleCloseGameButtonClick(){
        Platform.exit();
    }

    public void handleCreateGameButtonClick() throws Exception{
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/PlayGameFactory.fxml")));
        Stage stage = (Stage) createGameButton.getScene().getWindow();
        stage.setScene(scene);
    }

}
