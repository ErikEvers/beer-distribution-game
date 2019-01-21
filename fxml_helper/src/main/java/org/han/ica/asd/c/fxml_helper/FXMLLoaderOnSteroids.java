package org.han.ica.asd.c.fxml_helper;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FXMLLoaderOnSteroids extends FXMLLoader {
    private static final String GAME_TITLE = "Beer Distribution Game";
    private static Stage primaryStage;

    @Inject
    private static Provider<FXMLLoaderOnSteroids> loaderProvider;
    @Inject
    private static Logger logger; //NOSONAR

    @Inject
    public FXMLLoaderOnSteroids(AbstractModuleExtension module) {
        super();
        Injector injector = Guice.createInjector(module);
        this.setControllerFactory(injector::getInstance);
    }

    public static void setPrimaryStage(Stage primaryStage) {
        FXMLLoaderOnSteroids.primaryStage = primaryStage;
    }

    public static <T> T getScreen(ResourceBundle resourceBundle, URL fxmlPath) {
        return setScreen(resourceBundle, fxmlPath, false).getController();
    }

    private static FXMLLoaderOnSteroids setScreen(ResourceBundle resourceBundle, URL fxmlPath, boolean isPopup) {
        FXMLLoaderOnSteroids loader = loaderProvider.get();
        loader.setResources(resourceBundle);
        loader.setLocation(fxmlPath);
        try {
            Parent root = loader.load();

            if (!ComponentOrientation.getOrientation(new Locale(System.getProperty("user.language"))).isLeftToRight()) {
                root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            }

            if (isPopup) {
                Stage stage = new Stage();
                setStage(stage, root);
            } else {
                setStage(primaryStage, root);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }

        return loader;
    }

    private static void setStage(Stage stage, Parent root) {
        stage.setTitle(GAME_TITLE);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static <T> T getPopupScreen(ResourceBundle resourceBundle, URL fxmlPath) {
        return setScreen(resourceBundle, fxmlPath, true).getController();
    }
}
