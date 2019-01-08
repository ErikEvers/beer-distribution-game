package org.han.ica.asd.c.gui_join_game;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.dao.DaoConfig;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.fxml_helper.treebuilder.TreeBuilder;
import org.han.ica.asd.c.interfaces.gui_join_game.IConnecterForSetup;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.Leader;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameRoomController {
    private RoomModel roomModel;
    private BeerGame beerGame;

		@FXML
		private AnchorPane facilitiesContainer;

    @FXML
    private Label gameRoom;

    @Inject
    @Named("JoinGame")
    private IGUIHandler joinGame;

    @Inject
    @Named("PlayGame")
    private IGUIHandler playGame;

    @Inject
    @Named("Connector")
    private IConnecterForSetup iConnectorForSetup;

    public void initialize() {
			Configuration configuration = new Configuration();

			Facility retailer = new Facility(new FacilityType("Retailer", 0, 0,0,0,0,0, 0), 0);
			Facility wholesale = new Facility(new FacilityType("Wholesaler", 0, 0,0,0,0,0, 0), 1);
			Facility warehouse = new Facility(new FacilityType("Regional Warehouse", 0, 0,0,0,0,0, 0), 2);
			Facility factory = new Facility(new FacilityType("Factory", 0, 0,0,0,0,0, 0), 3);

			List<Facility> facilityList = new ArrayList<>();
			facilityList.add(retailer);
			facilityList.add(wholesale);
			facilityList.add(warehouse);
			facilityList.add(factory);

			configuration.setFacilities(facilityList);

			Map<Facility, List<Facility>> links = new HashMap<>();
			List<Facility> list = new ArrayList<>();
			list.add(wholesale);
			links.put(retailer, list);

			list = new ArrayList<>();
			list.add(warehouse);
			links.put(wholesale, list);

			list = new ArrayList<>();
			list.add(factory);
			links.put(warehouse, list);

			configuration.setFacilitiesLinkedTo(links);

			configuration.setAmountOfWarehouses(1);
			configuration.setAmountOfFactories(1);
			configuration.setAmountOfWholesalers(1);
			configuration.setAmountOfRetailers(1);

			configuration.setAmountOfRounds(20);

			configuration.setContinuePlayingWhenBankrupt(false);

			configuration.setInsightFacilities(true);

			configuration.setMaximumOrderRetail(99);
			configuration.setMinimalOrderRetail(5);

			this.beerGame = new BeerGame();
			this.beerGame.setConfiguration(configuration);
			Player henk = new Player("1", "111", retailer, "Henk", true);
			this.beerGame.getPlayers().add(henk);
			this.beerGame.getAgents().add(new GameAgent("wholesaleAgent", wholesale, new ArrayList<>()));
			this.beerGame.getAgents().add(new GameAgent("warehouseAgent", warehouse, new ArrayList<>()));
			this.beerGame.getAgents().add(new GameAgent("factoryAgent", factory, new ArrayList<>()));
			this.beerGame.setLeader(new Leader(henk));
			this.beerGame.setGameId("123");
			this.beerGame.setGameName("Henks spel");
			this.beerGame.setGameDate("2019-01-08");

			DaoConfig.setCurrentGameId("123");

			TreeBuilder.loadFacilityView(beerGame, facilitiesContainer, false);
		}

    public void handleBackToJoinGameButtonClick() {
        joinGame.setupScreen();
    }

    public void handleReadyButtonClick() {
        playGame.setData(new Object[]{beerGame});
        playGame.setupScreen();
    }

    public void setGameRoom(RoomModel roomModel) {
        this.roomModel = roomModel;
        gameRoom.setText(roomModel.getRoomName());
    }
}
