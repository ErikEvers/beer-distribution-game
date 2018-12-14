package org.han.ica.asd.c.fxml_helper;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.fxml.FXMLLoader;

import javax.inject.Inject;

public class FXMLLoaderOnSteroids extends FXMLLoader {
	@Inject
	public FXMLLoaderOnSteroids(AbstractModuleExtension module) {
		super();
		Injector injector = Guice.createInjector(module);
		this.setControllerFactory(injector::getInstance);
	}
}
