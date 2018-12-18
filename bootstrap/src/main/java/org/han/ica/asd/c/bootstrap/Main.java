package org.han.ica.asd.c.bootstrap;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;
import org.han.ica.asd.c.gui_main_menu.MainMenu;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		final Injector injector = Guice.createInjector(new BootstrapModule());
		injector.getInstance(MainMenu.class).setupScreen(primaryStage);
	}
}
