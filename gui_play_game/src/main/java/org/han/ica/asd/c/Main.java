package org.han.ica.asd.c;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.han.ica.asd.c.public_interfaces.IPlayerComponent;

public class Main extends Application {


    @Inject
    @Named("PlayerComponent") protected IPlayerComponent playerComponent;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/PlayGameFacilities.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();
    }

    public void setup(){
        launch();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
