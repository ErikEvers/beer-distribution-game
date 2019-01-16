package org.han.ica.asd.c.bootstrap;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.interfaces.communication.IConnectorForSetup;

public class main3 {
    public static void main(String[] args) {

        final Injector injector = Guice.createInjector(new BootstrapModule());

        injector.getInstance(IConnectorForSetup.class).start();

        while(1==1){



        }

    }
}
