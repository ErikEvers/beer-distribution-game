package org.han.ica.asd.c.gui_manage_players;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;
import org.han.ica.asd.c.fxml_helper.AbstractModuleExtension;
import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		final Injector injector = Guice.createInjector(new AbstractModuleExtension() {
			@Override
			protected void configure() {
				bind(AbstractModuleExtension.class).toInstance(this);
				requestStaticInjection(FXMLLoaderOnSteroids.class);
			}
		});
		FXMLLoaderOnSteroids.setPrimaryStage(primaryStage);
		injector.getInstance(MonitorPlayers.class).setupScreen();
	}
}
