package org.han.ica.asd.c.bootstrap;

import org.han.ica.asd.c.fxml_helper.AbstractModuleExtension;

public class BootstrapModule extends AbstractModuleExtension {
	@Override
	protected void configure() {
		bind(AbstractModuleExtension.class).to(BootstrapModule.class);
	}
}