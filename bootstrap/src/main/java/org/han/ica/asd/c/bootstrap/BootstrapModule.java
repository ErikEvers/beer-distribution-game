package org.han.ica.asd.c.bootstrap;

import com.google.inject.name.Names;
import org.han.ica.asd.c.Connector;
import org.han.ica.asd.c.MessageDirector;
import org.han.ica.asd.c.businessrule.BusinessRuleHandler;
import org.han.ica.asd.c.dbconnection.DBConnection;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.faultdetection.FailLog;
import org.han.ica.asd.c.faultdetection.FaultDetectionClient;
import org.han.ica.asd.c.faultdetection.FaultDetectorLeader;
import org.han.ica.asd.c.faultdetection.FaultResponder;
import org.han.ica.asd.c.fxml_helper.AbstractModuleExtension;
import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.gui_join_game.AgentList;
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
import org.han.ica.asd.c.messagehandler.sending.GameMessageClient;
import org.han.ica.asd.c.socketrpc.IServerObserver;
import org.han.ica.asd.c.socketrpc.SocketClient;
import org.han.ica.asd.c.socketrpc.SocketServer;
import org.han.ica.asd.c.interfaces.gui_join_game.IConnecterForSetup;


public class BootstrapModule extends AbstractModuleExtension {
	@Override
	protected void configure() {
		bind(AbstractModuleExtension.class).to(BootstrapModule.class);
		bind(IDatabaseConnection.class).to(DBConnection.class);
		bind(IConnecterForSetup.class).annotatedWith(Names.named("Connector")).to(Connector.class);
		bind(IBusinessRules.class).to(BusinessRuleHandler.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("MainMenu")).to(MainMenu.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("ReplayGame")).to(ReplayGame.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("ReplayGameList")).to(ReplayGameList.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("ProgramAgent")).to(ProgramAgent.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("ProgramAgentList")).to(ProgramAgentList.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("JoinGame")).to(JoinGame.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("AgentList")).to(AgentList.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("PlayGame")).to(PlayGame.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("SeeOtherFacilities")).to(SeeOtherFacilities.class);
        bind(IGUIHandler.class).annotatedWith(Names.named("GameRoom")).to(GameRoom.class);

		bind(IServerObserver.class).annotatedWith(Names.named("MessageDirector")).to(MessageDirector.class);

		staticRequests();
	}

	private void staticRequests(){
		requestStaticInjection(FXMLLoaderOnSteroids.class);
		requestStaticInjection(SocketClient.class);
		requestStaticInjection(SocketServer.class);
		requestStaticInjection(FailLog.class);
		requestStaticInjection(FaultDetectionClient.class);
		requestStaticInjection(FaultDetectorLeader.class);
		requestStaticInjection(FaultResponder.class);
		requestStaticInjection(Connector.class);
		requestStaticInjection(GameMessageClient.class);
	}
}
