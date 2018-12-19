package org.han.ica.asd.c.gui_main_menu;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import javax.inject.Inject;
import javax.inject.Provider;

public class MainMenu {
	private final Provider<FXMLLoaderOnSteroids> loaderProvider;

	@Inject
	public MainMenu(Provider<FXMLLoaderOnSteroids> loaderProvider) {
		this.loaderProvider = loaderProvider;
	}

	public void setupScreen(Stage primaryStage) throws Exception {
		FXMLLoaderOnSteroids loader = loaderProvider.get();
		loader.setLocation(getClass().getResource("/fxml/MainMenu.fxml"));

		primaryStage.setScene(new Scene(loader.load()));
		primaryStage.show();
	}
}
