package org.han.ica.asd.c.gui_spelers_beheren;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;
import org.han.ica.asd.c.fxml_helper.AbstractModuleExtension;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		final Injector injector = Guice.createInjector(new AbstractModuleExtension() {
			@Override
			protected void configure() {
				bind(AbstractModuleExtension.class).toInstance(this);
			}
		});
		injector.getInstance(MonitorPlayers.class).setupScreen(primaryStage);
	}
}
