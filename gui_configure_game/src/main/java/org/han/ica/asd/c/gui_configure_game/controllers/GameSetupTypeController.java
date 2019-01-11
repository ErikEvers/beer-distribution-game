package org.han.ica.asd.c.gui_configure_game.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import org.han.ica.asd.c.dao.DaoConfig;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.URL;
import java.util.*;

public class GameSetupTypeController implements Initializable {

    private String facString = "Factory";
    private String wholeString = "Wholesaler";
    private String regWarehouseString = "Regional warehouse";
    private String retailString = "Retailer";


    /**
     * Factory for FXML
     */
    @FXML
    TextField inGoodsFactory;
    @FXML
    TextField outGoodsFactory;
    @FXML
    TextField stockHoldingFactory;
    @FXML
    TextField startingBudgetFactory;
    @FXML
    TextField startingOrderFactory;
    @FXML
    TextField startingStockFactory;
    @FXML
    TextField openOrderCostFactory;

    /**
     * Wholesale for FXML
     */
    @FXML
    TextField inGoodsWholesale;
    @FXML
    TextField outGoodsWholesale;
    @FXML
    TextField stockHoldingWholesale;
    @FXML
    TextField startingBudgetWholesale;
    @FXML
    TextField startingOrderWholesale;
    @FXML
    TextField startingStockWholesale;
    @FXML
    TextField openOrderCostWholesale;
    /**
     * RegionalWharehouse for FXML
     */
    @FXML
    TextField inGoodsRegionalWharehouse;
    @FXML
    TextField outGoodsRegionalWharehouse;
    @FXML
    TextField stockHoldingRegionalWharehouse;
    @FXML
    TextField startingBudgetRegionalWharehouse;
    @FXML
    TextField startingOrderRegionalWharehouse;
    @FXML
    TextField startingStockRegionalWharehouse;
    @FXML
    TextField openOrderCostRegionalWarehouse;
    /**
     * Retailer for FXML
     */
    @FXML
    TextField inGoodsRetailer;
    @FXML
    TextField outGoodsRetailer;
    @FXML
    TextField stockHoldingRetailer;
    @FXML
    TextField startingBudgetRetailer;
    @FXML
    TextField startingOrderRetailer;
    @FXML
    TextField startingStockRetailer;
    @FXML
    TextField openOrderCostRetailer;

    @Inject
    @Named("MainMenu")
    private IGUIHandler mainMenu;

    @Inject
    @Named("AssignAgents")
    private IGUIHandler assignAgents;

    @FXML
    private AnchorPane mainContainer;

    /**
     * Configuration variables that should be passed down to the next screen
     */
    @Inject
    private BeerGame beerGame;
    private Configuration configuration;
    private String gameName = "";
    private String onlineGame = "TRUE";

    /**
     * Method to initialize the controller. Will only be called once when the fxml is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainContainer.getChildren().addAll();
    }

    /**
     * Button function to return to the previous screen
     */

    @FXML
    private void backButton() {
        popUpError();

    }

    public void popUpError() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Back to main menu. All settings will be lost");
        alert.setHeaderText("Warning");
        alert.setTitle("Warning");
        Optional<ButtonType> clicked = alert.showAndWait();

        if (clicked.get() == ButtonType.OK) {
            mainMenu.setupScreen();
        }
    }

    void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    void setGameName(String gameName) {
        this.gameName = gameName;
    }

    void isOnlineGame(String onlineGame) {
        this.onlineGame = onlineGame;
    }

    public void nextScreenButton() throws Exception {
        for (Map.Entry<Facility, List<Facility>> entry : configuration.getFacilitiesLinkedTo().entrySet()) {
            System.out.println("Parent: " + entry.getKey().getFacilityType().getFacilityName() + " id: " + entry.getKey().getFacilityId());
            for (Facility f : entry.getValue()) {
                System.out.println("-------Child: " + f.getFacilityType().getFacilityName() + " id: " + f.getFacilityId());
            }

            fillConfigurationList();
            fillConfigurationGraph();
            beerGame.setConfiguration(this.configuration);
            beerGame.setGameName(this.gameName);
            beerGame.setGameId(DaoConfig.getCurrentGameId());
            assignAgents.setData(new Object[] { beerGame });
            assignAgents.setupScreen();
        }
    }

        /**
         * Fills the list of the configuration with the parameters from the textfield in the GUI
         */
        private void fillConfigurationList () {
            for (Facility current : configuration.getFacilities()) {

                if (current.getFacilityType().getFacilityName().equals(facString)) {
                    current.setFacilityType(setFactoryType(current.getFacilityType()));
                }
                if (current.getFacilityType().getFacilityName().equals(wholeString)) {
                    current.setFacilityType(setWholsaleType(current.getFacilityType()));
                }
                if (current.getFacilityType().getFacilityName().equals(regWarehouseString)) {
                    current.setFacilityType(setRegionalWarehouseType(current.getFacilityType()));
                }
                if (current.getFacilityType().getFacilityName().equals(retailString)) {
                    current.setFacilityType(setRetailerType(current.getFacilityType()));
                }
            }
        }

        /**
         * Fills the Graph of the configuration with the parameters from the textfield in the GUI
         */
        private void fillConfigurationGraph () {

            for (Map.Entry<Facility, List<Facility>> entry : configuration.getFacilitiesLinkedTo().entrySet()) {
                if (entry.getKey().getFacilityType().getFacilityName().equals(facString)) {
                    entry.getKey().setFacilityType(setFactoryType(entry.getKey().getFacilityType()));
                }
                if (entry.getKey().getFacilityType().getFacilityName().equals(wholeString)) {
                    entry.getKey().setFacilityType(setWholsaleType(entry.getKey().getFacilityType()));
                }
                if (entry.getKey().getFacilityType().getFacilityName().equals(regWarehouseString)) {
                    entry.getKey().setFacilityType(setRegionalWarehouseType(entry.getKey().getFacilityType()));
                }
                if (entry.getKey().getFacilityType().getFacilityName().equals(retailString)) {
                    entry.getKey().setFacilityType(setRetailerType(entry.getKey().getFacilityType()));
                }
                for (Facility current : entry.getValue()) {
                    if (current.getFacilityType().getFacilityName().equals(facString)) {
                        current.setFacilityType(setFactoryType(entry.getKey().getFacilityType()));
                    }
                    if (current.getFacilityType().getFacilityName().equals(wholeString)) {
                        current.setFacilityType(setWholsaleType(entry.getKey().getFacilityType()));
                    }
                    if (current.getFacilityType().getFacilityName().equals(regWarehouseString)) {
                        current.setFacilityType(setRegionalWarehouseType(entry.getKey().getFacilityType()));
                    }
                    if (current.getFacilityType().getFacilityName().equals(retailString)) {
                        current.setFacilityType(setRetailerType(entry.getKey().getFacilityType()));
                    }

                }
            }

        }

        /**
         * fills the factory with the types
         *
         * @param facility facility instance
         * @return facilitytype
         */
        private FacilityType setFactoryType (FacilityType facility){
            return getFacilityType(facility, inGoodsFactory, outGoodsFactory, stockHoldingFactory, startingBudgetFactory, startingOrderFactory, startingStockFactory, openOrderCostFactory);
        }

        /**
         * fills the wholesale with the types
         *
         * @param facility facility instance
         * @return facilitytype
         */
        private FacilityType setWholsaleType (FacilityType facility){
            return getFacilityType(facility, inGoodsWholesale, outGoodsWholesale, stockHoldingWholesale, startingBudgetWholesale, startingOrderWholesale, startingStockWholesale, openOrderCostWholesale);
        }

        /**
         * fills the Regional warehouse with the types
         *
         * @param facility facility instance
         * @return facilitytype
         */
        private FacilityType setRegionalWarehouseType (FacilityType facility){
            return getFacilityType(facility, inGoodsRegionalWharehouse, outGoodsRegionalWharehouse, stockHoldingRegionalWharehouse, startingBudgetRegionalWharehouse, startingOrderRegionalWharehouse, startingStockRegionalWharehouse, openOrderCostRegionalWarehouse);
        }

        /**
         * fills the retailer with the types
         *
         * @param facility facility instance
         * @return facilitytype
         */
        private FacilityType setRetailerType (FacilityType facility){
            return getFacilityType(facility, inGoodsRetailer, outGoodsRetailer, stockHoldingRetailer, startingBudgetRetailer, startingOrderRetailer, startingStockRetailer, openOrderCostRetailer);
        }

        /**
         * fills a facility with the types
         *
         * @param facility facility instance
         * @return facilitytype
         */
        private FacilityType getFacilityType (FacilityType facility, TextField inGoods, TextField outGoods, TextField
        stockHolding, TextField startingBudget, TextField startingOrder, TextField startingStock, TextField
        openOrderCost){
            if ((inGoods.getText() != null && !inGoods.getText().isEmpty())) {
                facility.setValueIncomingGoods(Integer.parseInt(inGoods.getText()));
            }
            if ((outGoods.getText() != null && !outGoods.getText().isEmpty())) {
                facility.setValueOutgoingGoods(Integer.parseInt(outGoods.getText()));
            }
            if ((stockHolding.getText() != null && !stockHolding.getText().isEmpty())) {
                facility.setStockHoldingCosts(Integer.parseInt(stockHolding.getText()));
            }
            if ((startingBudget.getText() != null && !startingBudget.getText().isEmpty())) {
                facility.setStartingBudget(Integer.parseInt(startingBudget.getText()));
            }
            if ((startingOrder.getText() != null && !startingOrder.getText().isEmpty())) {
                facility.setStartingOrder(Integer.parseInt(startingOrder.getText()));
            }
            if ((startingStock.getText() != null && !startingStock.getText().isEmpty())) {
                facility.setStartingStock(Integer.parseInt(startingStock.getText()));
            }
            if ((openOrderCost.getText() != null && !openOrderCost.getText().isEmpty())) {
                facility.setOpenOrderCosts(Integer.parseInt(openOrderCost.getText()));
            }
            return facility;
        }

    }

