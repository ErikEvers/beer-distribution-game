package org.han.ica.asd.c.gui_play_game;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.converter.IntegerStringConverter;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.player.PlayerComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;


public abstract class PlayGame {
    private List<Facility> facilitiesDeliverySubmited;
    private List<Facility> faciltiesOrderSubmitted;

    protected BeerGame beerGame;

    @FXML
    private GridPane playGridPane;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    protected TextField incomingOrderTextField;

    @FXML
    protected TextField outgoingOrderTextField;

    @FXML
    protected TextField outgoingGoodsNextRound;

    @Inject
    @Named("SeeOtherFacilities")
    private IGUIHandler seeOtherFacilities;

    @FXML
    protected Label inventory;

    protected OrderFake orderFake;

    protected int roundNumber = 0;

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

    /**
     * superInitialization of the two controller subclasses. Has code needed for both initializations.
     */
    protected void superInitialize() {
        mainContainer.getChildren().addAll();
        playGridPane.setStyle("-fx-border-style: solid inside;" + "-fx-border-color: black;" + "-fx-border-radius: 40;");

        //TODO remove the orderfake class when integrating the game so it is playable.
        orderFake = new OrderFake();

        //Make sure only numbers can be filled in the order textBox. This is done using a textFormatter
        UnaryOperator<TextFormatter.Change> textFieldFilter = getChangeUnaryOperator();

        outgoingOrderTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, textFieldFilter));
        playerComponent.startNewTurn();
        faciltiesOrderSubmitted = new ArrayList<>();
        facilitiesDeliverySubmited = new ArrayList<>();
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

    /**
     * Calculates inventory.
     * @param ingoingGoods goods the facility is receiving from another facility.
     * @param outgoingGoods goods the facility is giving to another facility.
     * @return the new inventory
     */
    public String calculateInventory(int ingoingGoods, int outgoingGoods){
        //TODO calculation is wrongly stubbed for now... make sure the outcome of the real calculation is being displayed on the screen from the game logic.
        int inventoryValue = Integer.parseInt(inventory.getText());
        int result = (inventoryValue + ingoingGoods) - outgoingGoods;

        return Integer.toString(result);
    }

    public void seeOtherFacilitiesButtonClicked() {
        seeOtherFacilities.setupScreen();
    }

    public void setBeerGame(BeerGame beerGame) {
        this.beerGame = beerGame;
    }

    public abstract void fillComboBox();

    protected void fillOutGoingDeliveryFacilityComboBox(ComboBox comboBox) {
        List<Facility> facilitiesToDeliverTo = new ArrayList<>();
        beerGame.getConfiguration().getFacilities().forEach(
            t -> {
                Facility facilityPlayedByPlayer = PlayerComponent.getPlayer().getFacility();

                if (t != facilityPlayedByPlayer) {
                    List<Facility> facilitiesLinkedToFacilities = beerGame.getConfiguration().getFacilitiesLinkedToFacilities(t);
                    if (facilitiesLinkedToFacilities != null && facilitiesLinkedToFacilities.contains(facilityPlayedByPlayer)) {
                        facilitiesToDeliverTo.add(t);
                    }
                }
            }
        );

        ObservableList<Facility> facilityListView = FXCollections.observableArrayList();
        facilityListView.addAll(facilitiesToDeliverTo);
        comboBox.setItems(facilityListView);
    }

    protected void fillOutGoingOrderFacilityComboBox(ComboBox comboBox) {
        List<Facility> facilitiesToOrderTo = beerGame.getConfiguration().getFacilitiesLinkedToFacilities(PlayerComponent.getPlayer().getFacility());
        ObservableList<Facility> facilityListView = FXCollections.observableArrayList();
        facilityListView.addAll(facilitiesToOrderTo);
        comboBox.setItems(facilityListView);
    }

    /**
     * Button event handling the order sending.
     */
    protected void handleSendOrderButtonClick() {
        if (!outgoingOrderTextField.getText().isEmpty() && comboBox.getValue() != null) {
            int order = Integer.parseInt(outgoingOrderTextField.getText());
            Facility facility = comboBox.getValue();
            if (!faciltiesOrderSubmitted.contains(facility)) {
                faciltiesOrderSubmitted.add(facility);
            }

            playerComponent.placeOrder(facility, order);
        }
    }

    private boolean handleTextSettingOnSendOrderClick(int order) {
        if (order < 0) {
            outgoingOrderTextField.setText("");
            return false;
        }

        if (!incomingGoodsNextRound.getText().isEmpty() && !outgoingGoodsNextRound.getText().isEmpty()) {
            int incomingGoodsNextRoundAmount = Integer.parseInt(incomingGoodsNextRound.getText());
            int outgoingGoodsNextRoundAmount = Integer.parseInt(outgoingGoodsNextRound.getText());

            //TODO get the real calculation result from the game logic component/from the game leader.
            inventory.setText(calculateInventory(incomingGoodsNextRoundAmount, outgoingGoodsNextRoundAmount));
        }


        //TODO get the real order from the facility ordering from this one.
        outgoingGoodsNextRound.setText(Integer.toString(orderFake.orders()[roundNumber]));
        roundNumber++;

        incomingGoodsNextRound.setText(outgoingOrderTextField.getText());

        outgoingOrderTextField.setText("");
        return true;
    }

    protected void handleSendDeliveryButtonClick() {
        if (!txtOutgoingDelivery.getText().isEmpty()) {
            int delivery = Integer.parseInt(txtOutgoingDelivery.getText());
            Facility chosenFacility = cmbChooseOutgoingDelivery.getValue();
            if (!facilitiesDeliverySubmited.contains(chosenFacility)) {
                facilitiesDeliverySubmited.add(chosenFacility);
            }
            playerComponent.sendDelivery(cmbChooseOutgoingDelivery.getValue(), delivery);
        }
    }

    @FXML
    protected void submitTurnButonClicked() {
        final boolean cmbChooseOutgoingDeliveryExisting = (cmbChooseOutgoingDelivery != null);
        final boolean comboboxExisting = (comboBox != null);

        if (cmbChooseOutgoingDelivery.getItems().size() != facilitiesDeliverySubmited.size() || comboBox.getItems().size() != faciltiesOrderSubmitted.size()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("");
            alert.setContentText("You haven't set an order or delivery to every facility, do you want to order or deliver 0 to these facilities?");
            ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(okButton, noButton);
            alert.showAndWait().ifPresent(type -> {
                if (type.getButtonData() == ButtonBar.ButtonData.YES) {
                    if (cmbChooseOutgoingDeliveryExisting) autoFillDelivery();
                    if (comboboxExisting) autoFillOrdering();
                } else if (type.getButtonData() != ButtonBar.ButtonData.NO) {
                    return;
                }
            });
        }


        playerComponent.submitTurn();
    }

    private void autoFillDelivery() {
        cmbChooseOutgoingDelivery.getItems().forEach( t -> {
            if(!facilitiesDeliverySubmited.contains(t)) {
                playerComponent.sendDelivery(t, 0);
            }
        });
    }

    private void autoFillOrdering() {
        comboBox.getItems().forEach( t -> {
            if(!faciltiesOrderSubmitted.contains(t)) {
                playerComponent.placeOrder(t, 0);
            }
        });
    }
}
