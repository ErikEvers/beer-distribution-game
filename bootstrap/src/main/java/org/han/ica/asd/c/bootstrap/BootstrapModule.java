package org.han.ica.asd.c.bootstrap;

import com.google.inject.name.Names;
import org.han.ica.asd.c.dao.*;
import org.han.ica.asd.c.businessrule.BusinessRuleHandler;
import org.han.ica.asd.c.dao.BeergameDAO;
import org.han.ica.asd.c.dao.FacilityTurnDAO;
import org.han.ica.asd.c.dao.GameBusinessRulesInFacilityTurnDAO;
import org.han.ica.asd.c.dao.IBeerDisitributionGameDAO;
import org.han.ica.asd.c.dao.RoundDAO;
import org.han.ica.asd.c.dbconnection.DBConnection;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.fxml_helper.AbstractModuleExtension;
import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.gui_join_game.AgentList;
import org.han.ica.asd.c.gui_join_game.GameRoom;
import org.han.ica.asd.c.gui_join_game.GameRoom;
import org.han.ica.asd.c.gui_join_game.JoinGame;
import org.han.ica.asd.c.gui_main_menu.MainMenu;
import org.han.ica.asd.c.gui_play_game.PlayGame;
import org.han.ica.asd.c.gui_play_game.see_other_facilities.SeeOtherFacilities;
import org.han.ica.asd.c.gui_program_agent.ProgramAgent;
import org.han.ica.asd.c.gui_program_agent.ProgramAgentList;
import org.han.ica.asd.c.gui_replay_game.ReplayGame;
import org.han.ica.asd.c.gui_replay_game.ReplayGameList;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRules;
import org.han.ica.asd.c.interfaces.gui_join_game.IConnecterForSetup;

public class BootstrapModule extends AbstractModuleExtension {
	@Override
	protected void configure() {
		bind(AbstractModuleExtension.class).to(BootstrapModule.class);
		bind(IDatabaseConnection.class).to(DBConnection.class);
		bind(IBeerDisitributionGameDAO.class).annotatedWith(Names.named("RoundDAO")).to(RoundDAO.class);
		bind(IBeerDisitributionGameDAO.class).annotatedWith(Names.named("BeergameDAO")).to(BeergameDAO.class);
		bind(IBeerDisitributionGameDAO.class).annotatedWith(Names.named("GameBusinessRulesRulesInFacilityTurnDAO")).to(GameBusinessRulesInFacilityTurnDAO.class);
		bind(IBeerDisitributionGameDAO.class).annotatedWith(Names.named("FacilityTurnDAO")).to(FacilityTurnDAO.class);
		bind(IConnecterForSetup.class).annotatedWith(Names.named("Connector")).to(Connector.class);
		bind(IBusinessRules.class).to(BusinessRuleHandler.class);

		bind(IGUIHandler.class).annotatedWith(Names.named("MainMenu")).to(MainMenu.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("ReplayGame")).to(ReplayGame.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("ReplayGameList")).to(ReplayGameList.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("ProgramAgent")).to(ProgramAgent.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("ProgramAgentList")).to(ProgramAgentList.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("JoinGame")).to(JoinGame.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("ChooseFacility")).to(ChooseFacility.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("ChooseAgent")).to(ChooseAgent.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("AgentList")).to(AgentList.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("PlayGame")).to(PlayGame.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("SeeOtherFacilities")).to(SeeOtherFacilities.class);
        bind(IGUIHandler.class).annotatedWith(Names.named("GameRoom")).to(GameRoom.class);

		requestStaticInjection(FXMLLoaderOnSteroids.class);

	}
}
