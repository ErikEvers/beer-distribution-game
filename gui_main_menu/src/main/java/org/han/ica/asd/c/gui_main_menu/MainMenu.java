package org.han.ica.asd.c.gui_main_menu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import javax.inject.Inject;
import javax.inject.Provider;

public class MainMenu extends Application {
	private final Provider<FXMLLoaderOnSteroids> loaderProvider;

	@Inject
	public MainMenu(Provider<FXMLLoaderOnSteroids> loaderProvider) {
		this.loaderProvider = loaderProvider;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoaderOnSteroids loader = loaderProvider.get();
		loader.setLocation(getClass().getResource("/fxml/MainMenu.fxml"));
		primaryStage.setScene(new Scene(loader.load()));
		primaryStage.show();
	}

	public static void main(String[] args){
		launch(args);
	}

}
