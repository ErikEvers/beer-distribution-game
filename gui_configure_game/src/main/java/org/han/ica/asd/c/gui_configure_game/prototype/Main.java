package org.han.ica.asd.c.gui_configure_game.prototype;

import javafx.application.Application;

import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        final Injector injector = Guice.createInjector(new tempBootstrap() {
//            @Override
//            protected void configure() {
//                bind(AbstractModuleExtension.class).toInstance(this);
//            }
//        });
//        injector.getInstance(GameSetup.class).setupScreen(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
