package org.han.ica.asd.c.bootstrap;

import com.google.inject.name.Names;
import org.han.ica.asd.c.fxml_helper.AbstractModuleExtension;
import org.han.ica.asd.c.leadermigration.ElectionHandler;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class BootstrapModule extends AbstractModuleExtension {
	@Override
	protected void configure() {
		bind(AbstractModuleExtension.class).to(BootstrapModule.class);
		try {
			bind(String.class).annotatedWith(Names.named("localIp")).toInstance(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		requestStaticInjection(ElectionHandler.class);
	}
}
