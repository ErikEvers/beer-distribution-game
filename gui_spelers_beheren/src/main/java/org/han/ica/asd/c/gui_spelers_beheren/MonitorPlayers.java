package org.han.ica.asd.c.gui_spelers_beheren;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import javax.inject.Inject;
import javax.inject.Provider;

public class MonitorPlayers {
	private final Provider<FXMLLoaderOnSteroids> loaderProvider;

	@Inject
	public MonitorPlayers(Provider<FXMLLoaderOnSteroids> loaderProvider){
		this.loaderProvider = loaderProvider;
	}


	public void setupScreen(Stage primaryStage) throws Exception {
		FXMLLoaderOnSteroids loader = loaderProvider.get();
		loader.setLocation(getClass().getResource("/fxml/MonitorPlayersScreen.fxml"));

		primaryStage.setScene(new Scene(loader.load()));
		primaryStage.show();
	}
}
