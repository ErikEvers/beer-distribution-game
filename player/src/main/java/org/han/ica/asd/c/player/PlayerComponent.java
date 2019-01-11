package org.han.ica.asd.c.player;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.han.ica.asd.c.exceptions.gameleader.FacilityNotAvailableException;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.gamelogic.IPlayerGameLogic;
import org.han.ica.asd.c.interfaces.communication.IConnectorForSetup;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayGame;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.interfaces.player.IPlayerRoundListener;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnDeliver;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.util.List;
import java.util.Optional;

public class PlayerComponent implements IPlayerComponent, IPlayerRoundListener {
    private Provider<Round> roundProvider;
    private Provider<FacilityTurnOrder> facilityTurnOrderProvider;
    private Provider<FacilityTurnDeliver> facilityTurnDeliverProvider;

    private static Player player;
    private Round round;
    private static IPlayGame ui;

    private IPlayerGameLogic gameLogic;

    @Inject
    private IConnectorForSetup communication;

    @Inject
		@Named("PlayGame")
    private IGUIHandler playGame;

    @Inject
		public PlayerComponent(Provider<Round> roundProvider, Provider<FacilityTurnOrder> facilityTurnOrderProvider, Provider<FacilityTurnDeliver> facilityTurnDeliverProvider, IPlayerGameLogic gameLogic) {
				this.roundProvider = roundProvider;
				this.facilityTurnOrderProvider = facilityTurnOrderProvider;
				this.facilityTurnDeliverProvider = facilityTurnDeliverProvider;
				this.gameLogic = gameLogic;
				gameLogic.setPlayer(this);
    }

	@Override
	public void activatePlayer() {
		//stub for now
	}

    @Override
    public void activateAgent() {
        //Yet to be implemented.
    }

    @Override
    public void requestFacilityUsage(Facility facility) {
        gameLogic.requestFacilityUsage(facility);
    }


    @Override
    public void selectAgent() {
        //Yet to be implemented.
    }

    @Override
    public List<String> getAllGames() {
        return gameLogic.getAllGames();
    }

    @Override
    public void connectToGame(String game) {
        gameLogic.connectToGame(game);
    }

    @Override
    public List<Facility> getAllFacilities() {
        return gameLogic.getAllFacilities();
    }

    @Override
    public BeerGame getBeerGame() {
        return gameLogic.getBeerGame();
    }

    public void startNewTurn() {
        round = roundProvider.get();
        round.setRoundId(gameLogic.getRoundId());
    }
    
    @Override
    public void placeOrder(Facility facility, int amount) {

        Optional<FacilityTurnOrder> facilityTurnOrderOptional = round.getFacilityOrders().stream().filter(facilityTurnDeliver -> facilityTurnDeliver.getFacilityId() == player.getFacility().getFacilityId() && facilityTurnDeliver.getFacilityIdOrderTo() == facility.getFacilityId()).findFirst();
        if(!facilityTurnOrderOptional.isPresent()) {
            FacilityTurnOrder facilityTurnOrder = facilityTurnOrderProvider.get();
            facilityTurnOrder.setFacilityId(player.getFacility().getFacilityId());
            facilityTurnOrder.setFacilityIdOrderTo(facility.getFacilityId());
            facilityTurnOrder.setOrderAmount(amount);
            round.getFacilityOrders().add(facilityTurnOrder);
        } else {
            facilityTurnOrderOptional.get().setOrderAmount(facilityTurnOrderOptional.get().getOrderAmount() + amount);
        }
    }

    @Override
    public void sendDelivery(Facility facility, int amount) {
        Optional<FacilityTurnDeliver> facilityTurnDeliverOptional = round.getFacilityTurnDelivers().stream().filter(facilityTurnDeliver -> facilityTurnDeliver.getFacilityId() == player.getFacility().getFacilityId() && facilityTurnDeliver.getFacilityIdDeliverTo() == facility.getFacilityId()).findFirst();
        if(!facilityTurnDeliverOptional.isPresent()) {
            FacilityTurnDeliver facilityTurnDeliver = facilityTurnDeliverProvider.get();
            facilityTurnDeliver.setFacilityId(player.getFacility().getFacilityId());
            facilityTurnDeliver.setFacilityIdDeliverTo(facility.getFacilityId());
            facilityTurnDeliver.setDeliverAmount(amount);
            round.getFacilityTurnDelivers().add(facilityTurnDeliver);
        } else {
            facilityTurnDeliverOptional.get().setDeliverAmount(facilityTurnDeliverOptional.get().getDeliverAmount() + amount);
        }
    }

    public boolean submitTurn() {
        return gameLogic.submitTurn(round);
    }

    @Override
    public void chooseFacility(Facility facility) {
        try {
            communication.chooseFacility(facility, player.getPlayerId());
            player.setFacility(facility);
						Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Facility assigned, please wait for tha game to start", ButtonType.CLOSE);
						alert.showAndWait();
        } catch (FacilityNotAvailableException e) {
					Alert alert = new Alert(Alert.AlertType.ERROR, "Can't choose this particular facility, try another one :)", ButtonType.CLOSE);
					alert.showAndWait();
        }
    }

    @Override
    public String getFacilityName() {
        return player.getFacility().getFacilityType().getFacilityName();
    }

    public void setPlayer(Player player) {
        PlayerComponent.player = player;
    }

	@Override
	public void clearPlayer() {
		player = null;
	}
    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void setUi(IPlayGame game) {
        ui = game;
    }

	@Override
	public void clearUi() {
		ui = null;
	}

	public Facility getFacility() {
        return player.getFacility();
    }

    public void startGame() {
			Platform.runLater(() -> playGame.setupScreen());
		}

    /**
     * doOrder will notify the  participant to make an order.
     *
     * @return The current beergame status.
     */
    @Override
    public void roundStarted() {
    	Platform.runLater(() ->
        ui.refreshInterfaceWithCurrentStatus(gameLogic.getRoundId()));
    }

    /**
     * Returns the facility for the ParticipantPool to compare with other participants.
     *
     * @return The facility instance.
     */
    @Override
    public int getFacilityId() {
        return player.getFacility().getFacilityId();
    }
}
