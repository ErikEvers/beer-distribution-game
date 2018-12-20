package org.han.ica.asd.c.bootstrap;

import com.google.inject.name.Names;

import org.han.ica.asd.c.businessrule.BusinessRuleHandler;
import org.han.ica.asd.c.dao.BeergameDAO;
import org.han.ica.asd.c.dao.FacilityTurnDAO;
import org.han.ica.asd.c.dao.GameBusinessRulesInFacilityTurnDAO;
import org.han.ica.asd.c.dao.RoundDAO;


import org.han.ica.asd.c.dbconnection.DBConnection;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.fxml_helper.AbstractModuleExtension;
import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.gui_main_menu.MainMenu;
import org.han.ica.asd.c.gui_program_agent.ProgramAgent;
import org.han.ica.asd.c.gui_program_agent.ProgramAgentList;
import org.han.ica.asd.c.gui_replay_game.ReplayGame;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRules;

public class BootstrapModule extends AbstractModuleExtension {
	@Override
	protected void configure() {
		bind(AbstractModuleExtension.class).to(BootstrapModule.class);
		bind(IDatabaseConnection.class).to(DBConnection.class);
		bind(RoundDAO.class).to(RoundDAO.class);
		bind(BeergameDAO.class).to(BeergameDAO.class);
		bind(GameBusinessRulesInFacilityTurnDAO.class).to(GameBusinessRulesInFacilityTurnDAO.class);
		bind(FacilityTurnDAO.class).to(FacilityTurnDAO.class);
		bind(IBusinessRules.class).to(BusinessRuleHandler.class);

		bind(IGUIHandler.class).annotatedWith(Names.named("MainMenu")).to(MainMenu.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("ReplayGame")).to(ReplayGame.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("ProgramAgent")).to(ProgramAgent.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("ProgramAgentList")).to(ProgramAgentList.class);

		requestStaticInjection(FXMLLoaderOnSteroids.class);

	}
}
