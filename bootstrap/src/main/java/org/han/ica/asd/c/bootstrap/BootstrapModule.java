package org.han.ica.asd.c.bootstrap;

import com.google.inject.name.Names;
import org.han.ica.asd.c.gameleader.GameLeader;
import org.han.ica.asd.c.gamelogic.GameLogic;
import org.han.ica.asd.c.gamelogic.public_interfaces.IPlayerGameLogic;
import org.han.ica.asd.c.gui_play_game.PlayGameSetupScreen;
import org.han.ica.asd.c.gui_replay_game.ReplayGameList;
import org.han.ica.asd.c.interfaces.gameleader.IConnectorForLeader;
import org.han.ica.asd.c.interfaces.gameleader.ILeaderGameLogic;
import org.han.ica.asd.c.interfaces.gameleader.IPersistence;
import org.han.ica.asd.c.interfaces.gamelogic.IConnectedForPlayer;
import org.han.ica.asd.c.interfaces.persistence.IGameStore;
import org.han.ica.asd.c.persistence.Persistence;
import org.han.ica.asd.c.player.PlayerComponent;
import org.han.ica.asd.c.Connector;
import org.han.ica.asd.c.MessageDirector;
import org.han.ica.asd.c.businessrule.BusinessRuleHandler;
import org.han.ica.asd.c.dbconnection.DBConnection;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;
import org.han.ica.asd.c.interfaces.communication.IFinder;
import org.han.ica.asd.c.discovery.RoomFinder;
import org.han.ica.asd.c.faultdetection.FailLog;
import org.han.ica.asd.c.faultdetection.FaultDetectionClient;
import org.han.ica.asd.c.faultdetection.FaultDetectorLeader;
import org.han.ica.asd.c.faultdetection.FaultResponder;
import org.han.ica.asd.c.fxml_helper.AbstractModuleExtension;
import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.gui_configure_game.controllers.GameSetup;
import org.han.ica.asd.c.gui_configure_game.controllers.GameSetupStart;
import org.han.ica.asd.c.gui_configure_game.controllers.GameSetupType;
import org.han.ica.asd.c.gui_join_game.AgentList;
import org.han.ica.asd.c.gui_join_game.GameRoom;
import org.han.ica.asd.c.gui_join_game.JoinGame;
import org.han.ica.asd.c.gui_main_menu.MainMenu;
import org.han.ica.asd.c.gui_play_game.see_other_facilities.SeeOtherFacilities;
import org.han.ica.asd.c.gui_program_agent.ProgramAgent;
import org.han.ica.asd.c.gui_program_agent.ProgramAgentList;
import org.han.ica.asd.c.gui_replay_game.ReplayGame;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRules;
import org.han.ica.asd.c.messagehandler.sending.GameMessageClient;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.interfaces.communication.IConnectorForSetup;
import org.han.ica.asd.c.persistence.BusinessRuleStore;
import org.han.ica.asd.c.socketrpc.IServerObserver;
import org.han.ica.asd.c.socketrpc.SocketClient;
import org.han.ica.asd.c.socketrpc.SocketServer;

public class BootstrapModule extends AbstractModuleExtension {

	@Override
	protected void configure() {
		bind(AbstractModuleExtension.class).to(BootstrapModule.class);
		bind(IBusinessRuleStore.class).annotatedWith(Names.named("BusinessruleStore")).to(BusinessRuleStore.class);
		bind(IConnectorForSetup.class).annotatedWith(Names.named("Connector")).to(Connector.class);
		bind(IDatabaseConnection.class).to(DBConnection.class);
		bind(IBusinessRules.class).to(BusinessRuleHandler.class);
		bind(IGameStore.class).to(Persistence.class);
		bind(IFinder.class).to(RoomFinder.class);

		bind(IConnectedForPlayer.class).to(Connector.class);
		bind(IConnectorForLeader.class).to(Connector.class);
		bind(IConnectorForSetup.class).to(Connector.class);
		bind(IGameStore.class).to(Persistence.class);
		bind(IPersistence.class).to(Persistence.class);
		bind(IPlayerGameLogic.class).to(GameLogic.class);
		bind(ILeaderGameLogic.class).to(GameLogic.class);

		bind(IPlayerComponent.class).annotatedWith(Names.named("PlayerComponent")).to(PlayerComponent.class);
		bind(IServerObserver.class).annotatedWith(Names.named("MessageDirector")).to(MessageDirector.class);

		guiBinds();
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
		requestStaticInjection(GameLeader.class);
	}

	private void guiBinds(){
		bind(IGUIHandler.class).annotatedWith(Names.named("MainMenu")).to(MainMenu.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("ReplayGame")).to(ReplayGame.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("ReplayGameList")).to(ReplayGameList.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("ProgramAgent")).to(ProgramAgent.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("ProgramAgentList")).to(ProgramAgentList.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("JoinGame")).to(JoinGame.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("AgentList")).to(AgentList.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("PlayGame")).to(PlayGameSetupScreen.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("SeeOtherFacilities")).to(SeeOtherFacilities.class);
		bind(IGUIHandler.class).annotatedWith(Names.named("GameRoom")).to(GameRoom.class);
	}
}
