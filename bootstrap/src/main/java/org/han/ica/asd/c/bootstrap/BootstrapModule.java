package org.han.ica.asd.c.bootstrap;

import com.google.inject.name.Names;
import org.han.ica.asd.c.*;
import org.han.ica.asd.c.dbconnection.DBConnection;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.fxml_helper.AbstractModuleExtension;

public class BootstrapModule extends AbstractModuleExtension {
	@Override
	protected void configure() {
		bind(AbstractModuleExtension.class).to(BootstrapModule.class);
		bind(IDatabaseConnection.class).to(DBConnection.class);
		bind(IBeerDisitributionGameDAO.class).annotatedWith(Names.named("RoundDAO")).to(RoundDAO.class);
		bind(IBeerDisitributionGameDAO.class).annotatedWith(Names.named("BeergameDAO")).to(BeergameDAO.class);
		bind(IBeerDisitributionGameDAO.class).annotatedWith(Names.named("GameBusinessRulesRulesInFacilityTurnDAO")).to(GameBusinessRulesInFacilityTurnDAO.class);
		bind(IBeerDisitributionGameDAO.class).annotatedWith(Names.named("FacilityTurnDAO")).to(FacilityTurnDAO.class);

	}
}
