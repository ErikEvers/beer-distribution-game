package org.han.ica.asd.c.gui_play_game;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.han.ica.asd.c.exceptions.communication.SendGameMessageException;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayGame;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityTurn;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * Standard class for all the facility interfaces when in-game
 */
public abstract class PlayGame implements IPlayGame {
    @FXML
    private GridPane playGridPane;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    protected TextField incomingOrderTextField;

    @FXML
    protected TextField outgoingOrderTextField;

    @FXML
    protected TextField incomingOrdersTextField;

    @FXML
    protected Button submitTurnButton;

	@FXML
	protected Button seeOtherFacilitiesButton;

    @Inject
    @Named("SeeOtherFacilities")
    private IGUIHandler seeOtherFacilities;

    @FXML
    protected Label inventory;

    @FXML
    protected Label backOrders;

		@FXML
		protected Label stockHoldingCost;

		@FXML
		protected Label openOrderCost;

    @Inject
    @Named("PlayerComponent")
    protected IPlayerComponent playerComponent;

    @FXML
    protected TextField incomingGoodsNextRound;

    @FXML
    protected ComboBox<Facility> comboBox;

    @FXML
    protected ComboBox<Facility> cmbChooseOutgoingDelivery;

    @FXML
    protected TextField txtOutgoingDelivery;

    @FXML
    protected ListView<String> deliverList;
    @FXML
    protected ListView<String> orderList;

    ObservableList<String> orderFacilities;

    ObservableList<String> deliverFacilities;

    @FXML
    protected Button deleteOrderButton;

    @FXML
    protected Button deleteDeliveryButton;

    protected static Alert currentAlert;

    /**
     * superInitialization of the two controller subclasses. Has code needed for both initializations.
     */
    protected void superInitialize() {
        mainContainer.getChildren().addAll();
        playGridPane.setStyle("-fx-border-style: solid inside; -fx-border-color: black; -fx-border-radius: 40;");

        //Make sure only numbers can be filled in the order textBox. This is done using a textFormatter
        UnaryOperator<TextFormatter.Change> textFieldFilter = getChangeUnaryOperator();

        outgoingOrderTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, textFieldFilter));
        if(playerComponent.getBeerGame().getConfiguration().isInsightFacilities()) {
					seeOtherFacilitiesButton.setDisable(false);
				}
        playerComponent.setUi(this);
        orderFacilities = FXCollections.observableArrayList();
        deliverFacilities = FXCollections.observableArrayList();
    }

    /**
     * Retrieves a formatter that can be applied to text fields to ensure numbers only
     * @return
     * The text formatter
     */
    protected UnaryOperator<TextFormatter.Change> getChangeUnaryOperator() {
        return change -> {
                String newText = change.getControlNewText();
                if (newText.matches("([0-9]*)?")){
                    return change;
                }
                return null;
            };
    }

    /**
     * Replaces the current screen with the graph view of the facilities in the current game
     */
    public void seeOtherFacilitiesButtonClicked() {
        seeOtherFacilities.setupScreen();
    }

    /**
     * Replaces the current screen with the activity log
     */
		public void handleSeeActivityLogButtonClicked() {
    	Parent parent;
    	try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ActivityLogPopup.fxml"));
				parent = loader.load();
				ActivityLogPopupController activityLogPopupController = loader.getController();
				activityLogPopupController.setLogContent(playerComponent.getBeerGame(), playerComponent.getPlayer().getFacility().getFacilityId());
				Stage stage = new Stage();
				stage.setScene(new Scene(parent));
				stage.show();
			} catch (IOException e) {
				currentAlert = new Alert(Alert.AlertType.ERROR, "Can't display activity log", ButtonType.CLOSE);
				currentAlert.show();
			}
		}

    /**
     * Fills the comboboxes with the correct facilities
     */
    public abstract void fillComboBox();

    private void initDeliverBox() {
        if (cmbChooseOutgoingDelivery != null && !cmbChooseOutgoingDelivery.getItems().isEmpty() ) {
            cmbChooseOutgoingDelivery.getSelectionModel().selectFirst();
        }
    }

    private void initOrderBox() {
        if (comboBox != null && !comboBox.getItems().isEmpty()) {
            comboBox.getSelectionModel().selectFirst();
        }
    }

    /**
     * Fill the outgoing delivery combobox
     * @param comboBox
     * Combobox to be filled
     */
    protected void fillOutGoingDeliveryFacilityComboBox(ComboBox comboBox) {
        List<Facility> facilities = new ArrayList<>();

				int facilityPlayedByPlayerId = playerComponent.getPlayer().getFacility().getFacilityId();
				playerComponent.getBeerGame().getConfiguration().getFacilities().forEach(f -> {
					if (f.getFacilityId() != facilityPlayedByPlayerId) {
						List<Facility> facilitiesLinkedToFacilities = playerComponent.getBeerGame().getConfiguration().getFacilitiesLinkedToFacilitiesByFacilityId(f.getFacilityId());
						if (facilitiesLinkedToFacilities != null && facilitiesLinkedToFacilities.stream().anyMatch(facility -> facility.getFacilityId() == facilityPlayedByPlayerId)) {
							facilities.add(f);
						}
					}
				});

				ObservableList<Facility> facilityListView = FXCollections.observableArrayList();
				facilityListView.addAll(facilities);
				comboBox.setItems(facilityListView);
				initDeliverBox();
    }

    /**
     * Fill the outgoing order combobox
     * @param comboBox
     * Combobox to be filled
     */
    protected void fillOutGoingOrderFacilityComboBox(ComboBox comboBox) {
        ObservableList<Facility> facilityListView = FXCollections.observableArrayList();
				facilityListView.addAll(playerComponent.getBeerGame().getConfiguration().getFacilitiesLinkedToFacilitiesByFacilityId(playerComponent.getPlayer().getFacility().getFacilityId()));
				comboBox.setItems(facilityListView);
				initOrderBox();
    }

    /**
     * Button event handling the order sending.
     */
    @FXML
    protected void handleSendOrderButtonClick() {
        if (!outgoingOrderTextField.getText().isEmpty() && comboBox.getValue() != null) {
            playerComponent.placeOrder(comboBox.getValue(), Integer.parseInt(outgoingOrderTextField.getText()));
            refillOrdersList();
            outgoingOrderTextField.clear();
        }
    }

    /**
     * Button event handling the delivery sending
     */
    @FXML
    protected void handleSendDeliveryButtonClick() {
        if (!txtOutgoingDelivery.getText().isEmpty()) {
            playerComponent.sendDelivery(cmbChooseOutgoingDelivery.getValue(), Integer.parseInt(txtOutgoingDelivery.getText()));
            refillDeliveriesList();
            txtOutgoingDelivery.clear();
        }
    }

    /**
     * Concatenates the facility and order/delivery
     * @param facilityName
     * The facility where the order/delivery is going to
     * @param facilityid
     * The ID of the facility the order/delivery is going to
     * @param amount
     * The amount to be delivered/ordered
     * @return
     * The concatenated string with the above data. Inteded for use in ListViews.
     */
    protected String concatFacilityAndIdAndOrder(String facilityName, int facilityid, int amount) {
        return facilityName.concat(" id: " + Integer.toString(facilityid)).concat(" Amount: " + Integer.toString(amount));
    }

    /**
     * Button event that submits the current round to the gameleader
     */
    @FXML
    protected void submitTurnButtonClicked() {
				submitTurnButton.setDisable(true);
        try {
            playerComponent.submitTurn();
						currentAlert = new Alert(Alert.AlertType.INFORMATION, "Your turn was successfully submitted, please wait for the new turn to begin", ButtonType.OK);
						currentAlert.show();
						orderFacilities.clear();
						deliverFacilities.clear();
        } catch (SendGameMessageException e) {
            currentAlert = new Alert(Alert.AlertType.ERROR, e.toString(), ButtonType.OK, ButtonType.CLOSE);
            Optional<ButtonType> result = currentAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                currentAlert.close();
                submitTurnButtonClicked();
            }
            submitTurnButton.setDisable(false);
        }
    }

    /**
     * Refreshes the current view with the correct round data
     * @param roundId
     * The round to use for the refresh
     */
    @Override
    public void refreshInterfaceWithCurrentStatus(int roundId) {
        BeerGame beerGame = playerComponent.getBeerGame();
        Facility facility = playerComponent.getPlayer().getFacility();
        int budget = 0;
        List<FacilityTurn> facilityTurns = beerGame.getRoundById(roundId).getFacilityTurns();
        for (FacilityTurn f: facilityTurns) {
            if(f.getFacilityId() == facility.getFacilityId()){
								inventory.setText(Integer.toString(f.getStock()));
								backOrders.setText(Integer.toString(f.getBackorders()));

								stockHoldingCost.setText("€" + Integer.toString(facility.getFacilityType().getStockHoldingCosts()) + "/pc/week");
								openOrderCost.setText("€" + Integer.toString(facility.getFacilityType().getOpenOrderCosts()) + "/pc/week");

								budget = f.getRemainingBudget();
            }
        }

        int incomingOrders = 0;
        List<FacilityTurnOrder> facilityTurnOrders = beerGame.getRoundById(roundId).getFacilityOrders();
        for (FacilityTurnOrder f: facilityTurnOrders) {
            if(f.getFacilityIdOrderTo() == facility.getFacilityId()){
                incomingOrders += f.getOrderAmount();
            }
        }
        final int incomingOrdersDisplay = incomingOrders;
				incomingOrdersTextField.setText(Integer.toString(incomingOrdersDisplay));
				outgoingOrderTextField.setText(Integer.toString(0));
				if(currentAlert != null && currentAlert.isShowing()) {
					currentAlert.close();
				}
				currentAlert = new Alert(Alert.AlertType.INFORMATION, "Turn " + roundId + " has begun. Your budget is: " + budget, ButtonType.OK);
				currentAlert.show();
				submitTurnButton.setDisable(false);
				refillOrdersList();
				refillDeliveriesList();
    }

    /**
     * Refills the orders list with the current orders for this turn
     */
    protected void refillOrdersList() {
        orderFacilities.clear();

        playerComponent.getRound().getFacilityOrders().stream().filter(order -> order.getFacilityId() == playerComponent.getPlayer().getFacility().getFacilityId()).forEach(order ->
                orderFacilities.add(concatFacilityAndIdAndOrder(playerComponent.getBeerGame().getFacilityById(order.getFacilityIdOrderTo()).getFacilityType().getFacilityName(), order.getFacilityIdOrderTo(), order.getOrderAmount())));
    }

    /**
     * Refills the deliveries list with the current deliveries for this turn
     */
    protected void refillDeliveriesList() {
        deliverFacilities.clear();
        playerComponent.getRound().getFacilityTurnDelivers().stream().filter(deliver -> deliver.getFacilityId() == playerComponent.getPlayer().getFacility().getFacilityId()).forEach(deliver ->
                deliverFacilities.add(concatFacilityAndIdAndOrder(playerComponent.getBeerGame().getFacilityById(deliver.getFacilityIdDeliverTo()).getFacilityType().getFacilityName(), deliver.getFacilityIdDeliverTo(), deliver.getDeliverAmount())));
    }

    /**
     * Deletes an order from the order list view
     */
    @FXML
    protected void deletePlacedOrder() {
        int index = orderList.getItems().indexOf(orderList.getSelectionModel().getSelectedItem());
        if (index >= 0) {
            playerComponent.getRound().getFacilityOrders().remove(index);
            orderList.getItems().remove(index);
        }
    }

    /**
     * Deletes a delivery from the list view
     */
    @FXML
    protected void deletePlacedDelivery() {
        int index = deliverList.getItems().indexOf(deliverList.getSelectionModel().getSelectedItem());
        if (index >= 0) {
            playerComponent.getRound().getFacilityTurnDelivers().remove(index);
            deliverList.getItems().remove(index);
        }
    }
}
