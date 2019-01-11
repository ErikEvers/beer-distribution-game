package org.han.ica.asd.c.gui_play_game;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.converter.IntegerStringConverter;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayGame;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityTurn;
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

    @Inject
    @Named("SeeOtherFacilities")
    IGUIHandler seeOtherFacilities;

    @FXML
    protected Label inventory;

    @FXML
    protected Label backOrders;

		@FXML
		protected Label stockHoldingCost;

		@FXML
		protected Label openOrderCost;

    @Inject
    @Named("PlayerComponent") protected IPlayerComponent playerComponent;

    @FXML
    protected TextField incomingGoodsNextRound;

    @FXML
    protected ComboBox<Facility> comboBox;

    @FXML
    protected ComboBox<Facility> cmbChooseOutgoingDelivery;

    @FXML
    protected TextField txtOutgoingDelivery;

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
        playerComponent.setUi(this);
        playerComponent.startNewTurn();
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

    public abstract void fillComboBox();

    protected void fillOutGoingDeliveryFacilityComboBox(ComboBox comboBox) {
        ArrayList<Facility> facilities = new ArrayList<>();

				playerComponent.getBeerGame().getConfiguration().getFacilities().forEach(
                t -> {
                    Facility facilityPlayedByPlayer = playerComponent.getPlayer().getFacility();

                    if (t != facilityPlayedByPlayer) {
                        List<Facility> facilitiesLinkedToFacilities = playerComponent.getBeerGame().getConfiguration().getFacilitiesLinkedToFacilities(t);
                        if (facilitiesLinkedToFacilities != null) {
                            if (facilitiesLinkedToFacilities.contains(facilityPlayedByPlayer)) {
                                facilities.add(t);
                            }
                        }
                    }
                }
        );

        ObservableList<Facility> facilityListView = FXCollections.observableArrayList();
        facilityListView.addAll(facilities);
				comboBox.setItems(facilityListView);
    }

    protected void fillOutGoingOrderFacilityComboBox(ComboBox comboBox) {
        ObservableList<Facility> facilityListView = FXCollections.observableArrayList();
				facilityListView.addAll(playerComponent.getBeerGame().getConfiguration().getFacilitiesLinkedToFacilities(playerComponent.getPlayer().getFacility()));
				comboBox.setItems(facilityListView);
    }

    /**
     * Button event handling the order sending.
     */
    protected void handleSendOrderButtonClick() {
        if (!outgoingOrderTextField.getText().isEmpty()) {
            int order = Integer.parseInt(outgoingOrderTextField.getText());
            Facility facility = comboBox.getValue();
            playerComponent.placeOrder(facility, order);
        }
    }

    protected void handleSendDeliveryButtonClick() {
        if (!txtOutgoingDelivery.getText().isEmpty()) {
            int delivery = Integer.parseInt(txtOutgoingDelivery.getText());
            playerComponent.sendDelivery(cmbChooseOutgoingDelivery.getValue(), delivery);
        }
    }

    @FXML
    protected void submitTurnButtonClicked() {
				submitTurnButton.setDisable(true);
        if(playerComponent.submitTurn()) {
					currentAlert = new Alert(Alert.AlertType.INFORMATION, "Your turn was successfully submitted, please wait for the new turn to begin", ButtonType.OK);
					currentAlert.show();
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
    }
}
