package org.han.ica.asd.c.gui_replay_game.replay_game_controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.gamevalue.GameValue;
import org.han.ica.asd.c.interfaces.gui_replay_game.IVisualisedPlayedGameData;
import org.han.ica.asd.c.model.domain_objects.Facility;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;

public class ReplayGameScreenController {

    @FXML
    private LineChart<Double, Double> replayGraph;

    @FXML
    private CheckBox factoryCheckBox;

    @FXML
    private CheckBox warehouseCheckBox;

    @FXML
    private CheckBox wholesalerCheckBox;

    @FXML
    private CheckBox retailCheckBox;

    @FXML
    private ComboBox<Facility> facilityComboBox;

    @FXML
    private ComboBox<GameValue> attributeComboBox;

    @FXML
    private TextField currentRoundTextfield;

    @FXML
    private TextField totalRoundsTextfield;

    @Inject
    private IVisualisedPlayedGameData replayComponent;

    @Inject
    @Named("ReplayGameRound")
    IGUIHandler replayGameRound;

    @FXML
    public void currentRoundEntered(ActionEvent event) {
        replayComponent.updateCurrentRound(Integer.parseInt(currentRoundTextfield.getText()));

        updateCurrentRound();
        drawGraph();
    }

    @FXML
    public void nextRoundButtonClicked(ActionEvent event) {
        if (replayComponent.incrementCurrentRound()) {
            updateCurrentRound();
            drawGraph();
        }
    }

    @FXML
    public void prevRoundButtonClicked(ActionEvent event) {
        if (replayComponent.decrementCurrentRound()) {
            updateCurrentRound();
            drawGraph();
        }
    }

    @FXML
    void initialize() {
        currentRoundTextfield.setTextFormatter(NumericTextFormatter.getTextFormatter());

        totalRoundsTextfield.setText(replayComponent.getTotalRoundsString());

        initializeFacilityComboBox();
        initializeAttributeComboBox();
        initializeCheckBoxes();
        updateCurrentRound();
        drawGraph();
    }

    private void initializeAttributeComboBox() {
        setAttributeComboBoxConverter();

        attributeComboBox.valueProperty().addListener((obs, oldVal, newVal) ->
                attributeComboBoxUpdated(newVal));

        fillAttributeComboBox();

        attributeComboBox.getSelectionModel().select(GameValue.BUDGET);
    }

    private void fillAttributeComboBox() {
        ArrayList<GameValue> attributes = new ArrayList<>();

        for(GameValue gameValue:GameValue.values()){
            for(String synonym:gameValue.getValue()) {
                if (GameValue.isDisplayAttribute(synonym)){
                    attributes.add(gameValue);
                    break;
                }
            }
        }

        ObservableList<GameValue> observableList = FXCollections.observableArrayList(attributes);

        attributeComboBox.setItems(observableList);
    }

    private void setAttributeComboBoxConverter() {
        attributeComboBox.setConverter(new StringConverter<GameValue>() {
            @Override
            public String toString(GameValue object) {
                return object.getValue()[0];
            }

            @Override
            public GameValue fromString(String string) {
                for(GameValue gameValue:GameValue.values()){
                    if(gameValue.contains(string)){
                        return gameValue;
                    }
                }
                return null;
            }
        });
    }

    private void attributeComboBoxUpdated(GameValue newVal) {
        replayComponent.setDisplayedAttribute(newVal);
        drawGraph();
    }

    private void initializeFacilityComboBox() {
        facilityComboBox.getItems().clear();
        ObservableList<Facility> observableList = FXCollections.observableArrayList(replayComponent.getAllFacilities());
        facilityComboBox.setItems(observableList);
        facilityComboBox.valueProperty().addListener((obs, oldVal, newVal) ->
                facilityComboBoxUpdated(oldVal, newVal));
        facilityComboBox.setConverter(new StringConverter<Facility>() {
            @Override
            public String toString(Facility object) {
                return object.getFacilityType().getFacilityName() + " " + object.getFacilityId();
            }
            @Override
            public Facility fromString(String string) {
                return null;
            }
        });
    }

    private void facilityComboBoxUpdated(Facility facilityOld, Facility facilityNew) {
        replayComponent.removeDisplayedFacility(facilityOld);
        if (facilityNew != null) {
            clearCheckBoxes();
            replayComponent.addDisplayedFacility(facilityNew);
        }
        drawGraph();
    }

    private void clearCheckBoxes() {
        factoryCheckBox.selectedProperty().setValue(false);
        retailCheckBox.selectedProperty().setValue(false);
        warehouseCheckBox.selectedProperty().setValue(false);
        wholesalerCheckBox.selectedProperty().setValue(false);
    }

    private void clearComboBox() {
        facilityComboBox.getSelectionModel().clearSelection();
    }

    public void initializeCheckBoxes() {
        factoryCheckBox.selectedProperty().addListener((observable, oldValue, newValue) ->
                factoryCheckBoxUpdated(newValue));
        retailCheckBox.selectedProperty().addListener((observable, oldValue, newValue) ->
                retailCheckBoxUpdated(newValue));
        warehouseCheckBox.selectedProperty().addListener((observable, oldValue, newValue) ->
                warehouseCheckBoxUpdated(newValue));
        wholesalerCheckBox.selectedProperty().addListener((observable, oldValue, newValue) ->
                wholesalerCheckBoxUpdated(newValue));

        factoryCheckBox.selectedProperty().setValue(true);
        retailCheckBox.selectedProperty().setValue(true);
        warehouseCheckBox.selectedProperty().setValue(true);
        wholesalerCheckBox.selectedProperty().setValue(true);
    }

    private void wholesalerCheckBoxUpdated(Boolean newValue) {
        facilityCheckBoxUpdated(newValue, GameValue.WHOLESALER);
    }

    private void warehouseCheckBoxUpdated(Boolean newValue) {
        facilityCheckBoxUpdated(newValue, GameValue.REGIONALWAREHOUSE);
    }

    private void retailCheckBoxUpdated(Boolean newValue) {
        facilityCheckBoxUpdated(newValue, GameValue.RETAILER);
    }

    private void factoryCheckBoxUpdated(Boolean newValue) {
        facilityCheckBoxUpdated(newValue, GameValue.FACTORY);
    }

    private void facilityCheckBoxUpdated(Boolean newValue, GameValue facility) {
        if (!newValue) {
            replayComponent.removeDisplayedFacility(facility);
        } else {
            clearComboBox();
            replayComponent.addDisplayedFacility(facility);
        }
        drawGraph();
    }

    private void updateCurrentRound() {
        currentRoundTextfield.setText(replayComponent.getCurrentRoundString());
    }

    private void drawGraph() {
        replayGraph.setData(replayComponent.getChartData());
    }

    @FXML
    void backButtonClicked(ActionEvent event) {
        replayGameRound.setupScreen();
    }
}

