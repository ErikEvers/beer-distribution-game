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
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayGame;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityTurn;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnDeliver;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.io.IOException;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

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

    protected ObservableList<String> orderFacilities;
    protected ObservableList<String> deliverFacilities;

    protected static Alert currentAlert;

    /**
     * superInitialization of the two controller subclasses. Has code needed for both initializations.
     */
    protected void superInitialize() {
        mainContainer.getChildren().addAll();
        playGridPane.setStyle("-fx-border-style: solid inside;" + "-fx-border-color: black;" + "-fx-border-radius: 40;");

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

    protected UnaryOperator<TextFormatter.Change> getChangeUnaryOperator() {
        return change -> {
                String newText = change.getControlNewText();
                if (newText.matches("-?([0-9]*)?")){
                    return change;
                }
                return null;
            };
    }

    public void seeOtherFacilitiesButtonClicked() {
        seeOtherFacilities.setupScreen();
    }


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

    public abstract void fillComboBox();

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
    }

    protected void fillOutGoingOrderFacilityComboBox(ComboBox comboBox) {
        ObservableList<Facility> facilityListView = FXCollections.observableArrayList();
				facilityListView.addAll(playerComponent.getBeerGame().getConfiguration().getFacilitiesLinkedToFacilitiesByFacilityId(playerComponent.getPlayer().getFacility().getFacilityId()));
				comboBox.setItems(facilityListView);
    }

    /**
     * Button event handling the order sending.
     */
    @FXML
    protected void handleSendOrderButtonClick() {
        if (!outgoingOrderTextField.getText().isEmpty() && comboBox.getValue() != null) {
            int order = Integer.parseInt(outgoingOrderTextField.getText());
            Facility facility = comboBox.getValue();
            String facilityAndOrderAmount = concatFacilityAndIdAndOrder(facility.getFacilityType().getFacilityName(), facility.getFacilityId(), order);
            outgoingOrderTextField.clear();
            playerComponent.placeOrder(facility, order);
            orderFacilities.add(facilityAndOrderAmount);
        }
    }

    @FXML
    protected void handleSendDeliveryButtonClick() {
        if (!txtOutgoingDelivery.getText().isEmpty()) {
            Facility chosenFacility = cmbChooseOutgoingDelivery.getValue();
            int delivery = Integer.parseInt(txtOutgoingDelivery.getText());
            String facilityAndDeliverAmount = concatFacilityAndIdAndOrder(chosenFacility.getFacilityType().getFacilityName(), chosenFacility.getFacilityId(), delivery);
            txtOutgoingDelivery.clear();
            playerComponent.sendDelivery(chosenFacility, delivery);
            deliverFacilities.add(facilityAndDeliverAmount);
            playerComponent.sendDelivery(cmbChooseOutgoingDelivery.getValue(), delivery);
        }
    }

    private String concatFacilityAndIdAndOrder(String facilityName, int facilityid, int amount) {
        return facilityName.concat(" id: " + Integer.toString(facilityid)).concat(" Amount: " + Integer.toString(amount));
    }


    @FXML
    protected void submitTurnButtonClicked() {
        final boolean cmbChooseOutgoingDeliveryExisting = (cmbChooseOutgoingDelivery != null);
        final boolean comboboxExisting = (comboBox != null);

				submitTurnButton.setDisable(true);
				if(playerComponent.submitTurn()) {
					currentAlert = new Alert(Alert.AlertType.INFORMATION, "Your turn was successfully submitted, please wait for the new turn to begin", ButtonType.OK);
					currentAlert.show();
					orderFacilities.clear();
					deliverFacilities.clear();
				} else {
					currentAlert = new Alert(Alert.AlertType.ERROR, "Something went wrong while submitting your turn, please try again", ButtonType.OK, ButtonType.CLOSE);
					Optional<ButtonType> result = currentAlert.showAndWait();
					if (result.get() == ButtonType.OK) {
						currentAlert.close();
						submitTurnButtonClicked();
					}
					submitTurnButton.setDisable(false);
				}
    }

    @Override
    public void refreshInterfaceWithCurrentStatus(int roundId) {
    		BeerGame beerGame = playerComponent.getBeerGame();
        Facility facility = playerComponent.getPlayer().getFacility();
        Round round = playerComponent.getRound();
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

        for(FacilityTurnOrder facilityTurnOrder : round.getFacilityOrders()) {
        	if(facilityTurnOrder.getFacilityId() == facility.getFacilityId()) {
						String facilityAndOrderAmount = concatFacilityAndIdAndOrder(beerGame.getFacilityById(facilityTurnOrder.getFacilityIdOrderTo()).getFacilityType().getFacilityName(), facilityTurnOrder.getFacilityIdOrderTo(), facilityTurnOrder.getOrderAmount());
						orderFacilities.add(facilityAndOrderAmount);
					}
				}

				for(FacilityTurnDeliver facilityTurnDeliver : round.getFacilityTurnDelivers()) {
					if(facilityTurnDeliver.getFacilityId() == facility.getFacilityId()) {
						String facilityAndDeliverAmount = concatFacilityAndIdAndOrder(beerGame.getFacilityById(facilityTurnDeliver.getFacilityIdDeliverTo()).getFacilityType().getFacilityName(), facilityTurnDeliver.getFacilityIdDeliverTo(), facilityTurnDeliver.getDeliverAmount());
						deliverFacilities.add(facilityAndDeliverAmount);
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
    }
}
