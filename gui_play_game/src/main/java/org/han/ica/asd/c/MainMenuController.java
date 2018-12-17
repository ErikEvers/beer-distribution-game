package org.han.ica.asd.c;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ResourceBundle;


public class MainMenuController {
    @FXML
    private AnchorPane mainContainer;

    public void initialize(){
        mainContainer.getChildren().addAll();
    }

    public void handleCloseGameButtonClick(){
        Platform.exit();
    }

    public void handleCreateGameButtonClick() throws Exception{
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"), ResourceBundle.getBundle("languageResources")));
        Stage stage = (Stage) mainContainer.getScene().getWindow();
        stage.setScene(scene);
    }

    public void handleJoinGameButtonClick() throws Exception{
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"), ResourceBundle.getBundle("languageResources")));
        Stage stage = (Stage) mainContainer.getScene().getWindow();
        stage.setScene(scene);
    }

    public void handleBusinessRulesButtonClick() throws Exception{
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"), ResourceBundle.getBundle("languageResources")));
        Stage stage = (Stage) mainContainer.getScene().getWindow();
        stage.setScene(scene);
    }

    public void handleReplayGameButtonClick() throws Exception{
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"), ResourceBundle.getBundle("languageResources")));
        Stage stage = (Stage) mainContainer.getScene().getWindow();
        stage.setScene(scene);
    }

}
