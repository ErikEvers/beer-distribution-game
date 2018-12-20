package org.han.ica.asd.c.gui_program_agent;

import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ProgramAgent {
    private static final String GAME_TITLE = "Beer Distribution Game";

    private final Provider<FXMLLoaderOnSteroids> loaderProvider;

    @Inject
    public ProgramAgent(Provider<FXMLLoaderOnSteroids> loaderProvider) {
        this.loaderProvider = loaderProvider;
    }

    public void setProgramAgentListScreen(Stage primaryStage) throws IOException {
        setScreen("/fxml/ProgramAgentList.fxml", primaryStage);
    }

    public void setProgramAgentScreen(Stage primaryStage) throws IOException {
        setScreen("/fxml/ProgramAgent.fxml", primaryStage);
    }

    private void setScreen(String pathFxml, Stage primaryStage) throws IOException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languageResources");

        FXMLLoaderOnSteroids loader = loaderProvider.get();
        loader.setResources(resourceBundle);
        loader.setLocation(getClass().getResource(pathFxml));
        Parent root = loader.load();

        if (!ComponentOrientation.getOrientation(new Locale(System.getProperty("user.language"))).isLeftToRight()) {
            root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }

        primaryStage.setTitle(GAME_TITLE);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}