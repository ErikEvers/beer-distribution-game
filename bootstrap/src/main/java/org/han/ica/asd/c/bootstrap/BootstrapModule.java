package org.han.ica.asd.c.bootstrap;

import com.google.inject.name.Names;
import org.han.ica.asd.c.businessrule.BusinessRuleHandler;
import org.han.ica.asd.c.dbconnection.DBConnection;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.fxml_helper.AbstractModuleExtension;
import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.gui_configure_game.controllers.GameSetup;
import org.han.ica.asd.c.gui_configure_game.controllers.GameSetupStart;
import org.han.ica.asd.c.gui_configure_game.controllers.GameSetupType;
import org.han.ica.asd.c.gui_join_game.AgentList;
import org.han.ica.asd.c.gui_join_game.ChooseAgent;
import org.han.ica.asd.c.gui_join_game.ChooseFacility;
import org.han.ica.asd.c.gui_join_game.JoinGame;
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
        bind(IBusinessRules.class).to(BusinessRuleHandler.class);
        bind(IGUIHandler.class).annotatedWith(Names.named("MainMenu")).to(MainMenu.class);
        bind(IGUIHandler.class).annotatedWith(Names.named("ReplayGame")).to(ReplayGame.class);
        bind(IGUIHandler.class).annotatedWith(Names.named("ProgramAgent")).to(ProgramAgent.class);
        bind(IGUIHandler.class).annotatedWith(Names.named("ProgramAgentList")).to(ProgramAgentList.class);
        bind(IGUIHandler.class).annotatedWith(Names.named("JoinGame")).to(JoinGame.class);
        bind(IGUIHandler.class).annotatedWith(Names.named("ChooseFacility")).to(ChooseFacility.class);
        bind(IGUIHandler.class).annotatedWith(Names.named("ChooseAgent")).to(ChooseAgent.class);
        bind(IGUIHandler.class).annotatedWith(Names.named("AgentList")).to(AgentList.class);
        bind(IGUIHandler.class).annotatedWith(Names.named("GameSetupStart")).to(GameSetupStart.class);
        bind(IGUIHandler.class).annotatedWith(Names.named("GameSetup")).to(GameSetup.class);
        bind(IGUIHandler.class).annotatedWith(Names.named("GameSetupType")).to(GameSetupType.class);

        requestStaticInjection(FXMLLoaderOnSteroids.class);

    }
}
