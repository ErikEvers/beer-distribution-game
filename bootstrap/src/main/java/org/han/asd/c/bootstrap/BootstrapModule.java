package org.han.asd.c.bootstrap;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.han.ica.asd.c.PlayerComponent;
import org.han.ica.asd.c.public_interfaces.IPlayerComponent;

public class BootstrapModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IPlayerComponent.class).annotatedWith(Names.named("PlayerComponent")).to(PlayerComponent.class);
    }
}
