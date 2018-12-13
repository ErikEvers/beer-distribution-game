package org.han.asd.c.bootstrap;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.Main;
import org.han.ica.asd.c.PlayGame;

public class Bootstrap {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new BootstrapModule());
        Main main = injector.getInstance(Main.class);
        injector.injectMembers(PlayGame.class);
        System.out.println(main);
        main.setup();

    }
}
