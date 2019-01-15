package org.han.ica.asd.c.gui_configure_game.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.dao.DaoConfig;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.communication.IConnectorForSetup;
import org.han.ica.asd.c.interfaces.persistence.IGameStore;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityTurn;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.Round;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class GameSetupTypeController implements Initializable {

    private String facString = "Factory";
    private String regWarehouseString = "Regional Warehouse";
    private String wholeString = "Wholesaler";
    private String retailString = "Retailer";

    private static final String WARNING = "Warning";

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

    @Inject
		private IConnectorForSetup connector;

    @Inject
		private IGameStore persistence;

    @FXML
    private AnchorPane mainContainer;

    private Provider<Round> roundProvider;

    /**
     * Configuration variables that should be passed down to the next screen
     */
    @Inject
    private BeerGame beerGame;
    private Configuration configuration;
    private String gameName = "";
    private boolean onlineGame = true;

    @Inject
		private GameSetupTypeController(Provider<Round> roundProvider) {
    	this.roundProvider = roundProvider;
		}

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
        alert.setHeaderText(WARNING);
        alert.setTitle(WARNING);
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

    void isOnlineGame(boolean onlineGame) {
        this.onlineGame = onlineGame;
    }

    public void nextScreenButton() {
        String uuid = UUID.randomUUID().toString();
        if (!emptyFields()) {
            popUpNotAssigned();
        } else {
            fillConfigurationList();
            fillConfigurationGraph();

            beerGame.setConfiguration(this.configuration);
            beerGame.setGameName(this.gameName);

            beerGame.setGameId(uuid);
            beerGame.setGameDate(new Date().toString());
            DaoConfig.setCurrentGameId(uuid);
            createFirstTurn();

            if(onlineGame) {
							connector.start();
							connector.createRoom(gameName, "", beerGame);
						}
						assignAgents.setData(new Object[]{beerGame});
            assignAgents.setupScreen();
        }
    }

    /**
     * Fills the list of the configuration with the parameters from the textfield in the GUI
     */
    private void fillConfigurationList() {
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
    private void fillConfigurationGraph() {

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
                current.setFacilityType(setChildType(current));


            }
        }

    }

    private void createFirstTurn() {
			Round firstRound = roundProvider.get();
			firstRound.setRoundId(1);

			List<FacilityTurn> turns = new ArrayList<>();
			for(Facility facility: beerGame.getConfiguration().getFacilities()) {
				turns.add(
						new FacilityTurn(
								facility.getFacilityId(),
								1,
								facility.getFacilityType().getStartingStock(),
								0,
								facility.getFacilityType().getStartingBudget(),
								false));
			}

			firstRound.setFacilityTurns(turns);
			beerGame.getRounds().add(firstRound);
		}

    /**
     * Sets the facilityType for the child of a node
     *
     * @param facility to set
     * @return facility type
     */
    private FacilityType setChildType(Facility facility) {
        if (facility.getFacilityType().getFacilityName().equals(facString)) {
            return setFactoryType(facility.getFacilityType());
        }
        if (facility.getFacilityType().getFacilityName().equals(wholeString)) {
            return (setWholsaleType(facility.getFacilityType()));
        }
        if (facility.getFacilityType().getFacilityName().equals(regWarehouseString)) {
            return setRegionalWarehouseType(facility.getFacilityType());
        }
        if (facility.getFacilityType().getFacilityName().equals(retailString)) {
            return setRetailerType(facility.getFacilityType());
        }
        return null;
    }

    /**
     * fills the factory with the types
     *
     * @param facility facility instance
     * @return facilitytype
     */
    private FacilityType setFactoryType(FacilityType facility) {
        return getFacilityType(facility, inGoodsFactory, outGoodsFactory, stockHoldingFactory, startingBudgetFactory, startingOrderFactory, startingStockFactory, openOrderCostFactory);
    }

    /**
     * fills the wholesale with the types
     *
     * @param facility facility instance
     * @return facilitytype
     */
    private FacilityType setWholsaleType(FacilityType facility) {
        return getFacilityType(facility, inGoodsWholesale, outGoodsWholesale, stockHoldingWholesale, startingBudgetWholesale, startingOrderWholesale, startingStockWholesale, openOrderCostWholesale);
    }

    /**
     * fills the Regional warehouse with the types
     *
     * @param facility facility instance
     * @return facilitytype
     */
    private FacilityType setRegionalWarehouseType(FacilityType facility) {
        return getFacilityType(facility, inGoodsRegionalWharehouse, outGoodsRegionalWharehouse, stockHoldingRegionalWharehouse, startingBudgetRegionalWharehouse, startingOrderRegionalWharehouse, startingStockRegionalWharehouse, openOrderCostRegionalWarehouse);
    }

    /**
     * fills the retailer with the types
     *
     * @param facility facility instance
     * @return facilitytype
     */
    private FacilityType setRetailerType(FacilityType facility) {
        return getFacilityType(facility, inGoodsRetailer, outGoodsRetailer, stockHoldingRetailer, startingBudgetRetailer, startingOrderRetailer, startingStockRetailer, openOrderCostRetailer);
    }

    /**
     * fills a facility with the types
     *
     * @param facility facility instance
     * @return facilitytype
     */
    private FacilityType getFacilityType(FacilityType facility, TextField inGoods, TextField outGoods, TextField
            stockHolding, TextField startingBudget, TextField startingOrder, TextField startingStock, TextField
                                                 openOrderCost) {
        facility.setValueIncomingGoods(Integer.parseInt(inGoods.getText()));
        facility.setValueOutgoingGoods(Integer.parseInt(outGoods.getText()));
        facility.setStockHoldingCosts(Integer.parseInt(stockHolding.getText()));
        facility.setStartingBudget(Integer.parseInt(startingBudget.getText()));
        facility.setStartingOrder(Integer.parseInt(startingOrder.getText()));
        facility.setStartingStock(Integer.parseInt(startingStock.getText()));
        facility.setOpenOrderCosts(Integer.parseInt(openOrderCost.getText()));

        return facility;
    }

    /**
     * checks for empty textfield
     *
     * @param textField to be checked
     * @return true if not empty
     */
    private boolean notEmptyTextfield(TextField textField) {
        return textField.getText() != null && !textField.getText().isEmpty();
    }

    private boolean checkEmptyFieldsRetailer() {
        return notEmptyTextfield(getInGoodsRetailer()) && notEmptyTextfield(getOutGoodsRetailer()) && notEmptyTextfield(getStartingBudgetRetailer()) && notEmptyTextfield(getStartingOrderRetailer()) && notEmptyTextfield(getStockHoldingRetailer()) && notEmptyTextfield(getOpenOrderCostRetailer()) && notEmptyTextfield(getStartingStockRetailer());
    }

    private boolean checkEmptyFieldsRegionalWarehouse() {
        return notEmptyTextfield(getInGoodsRegionalWharehouse()) && notEmptyTextfield(getOutGoodsRegionalWharehouse()) && notEmptyTextfield(getStartingBudgetRegionalWharehouse()) && notEmptyTextfield(getStartingOrderRegionalWharehouse()) && notEmptyTextfield(getStockHoldingRegionalWharehouse()) && notEmptyTextfield(getOpenOrderCostRegionalWarehouse()) && notEmptyTextfield(getStartingStockRegionalWharehouse());

    }

    private boolean checkEmptyFieldsWolesaler() {
        return notEmptyTextfield(getInGoodsWholesale()) && notEmptyTextfield(getOutGoodsWholesale()) && notEmptyTextfield(getStartingBudgetWholesale()) && notEmptyTextfield(getStartingOrderWholesale()) && notEmptyTextfield(getStockHoldingWholesale()) && notEmptyTextfield(getOpenOrderCostWholesale()) && notEmptyTextfield(getStartingStockWholesale());

    }

    private boolean checkEmptyFieldsFactory() {
        return notEmptyTextfield(getInGoodsFactory()) && notEmptyTextfield(getOutGoodsFactory()) && notEmptyTextfield(getStartingBudgetFactory()) && notEmptyTextfield(getStartingOrderFactory()) && notEmptyTextfield(getStockHoldingFactory()) && notEmptyTextfield(getOpenOrderCostFactory()) && notEmptyTextfield(getStartingStockFactory());

    }

    private boolean emptyFields() {
        return checkEmptyFieldsFactory() && checkEmptyFieldsRegionalWarehouse() && checkEmptyFieldsRetailer() && checkEmptyFieldsWolesaler();
    }

    private void popUpNotAssigned() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "One or more fields do not have a value");
        alert.setHeaderText(WARNING);
        alert.setTitle(WARNING);
        alert.show();
    }

    private TextField getInGoodsFactory() {
        return inGoodsFactory;
    }

    private TextField getOutGoodsFactory() {
        return outGoodsFactory;
    }

    private TextField getStockHoldingFactory() {
        return stockHoldingFactory;
    }

    private TextField getStartingBudgetFactory() {
        return startingBudgetFactory;
    }

    private TextField getStartingOrderFactory() {
        return startingOrderFactory;
    }

    private TextField getStartingStockFactory() {
        return startingStockFactory;
    }

    private TextField getOpenOrderCostFactory() {
        return openOrderCostFactory;
    }

    private TextField getInGoodsWholesale() {
        return inGoodsWholesale;
    }

    private TextField getOutGoodsWholesale() {
        return outGoodsWholesale;
    }

    private TextField getStockHoldingWholesale() {
        return stockHoldingWholesale;
    }

    private TextField getStartingBudgetWholesale() {
        return startingBudgetWholesale;
    }

    private TextField getStartingOrderWholesale() {
        return startingOrderWholesale;
    }

    private TextField getStartingStockWholesale() {
        return startingStockWholesale;
    }

    private TextField getOpenOrderCostWholesale() {
        return openOrderCostWholesale;
    }

    private TextField getInGoodsRegionalWharehouse() {
        return inGoodsRegionalWharehouse;
    }

    private TextField getOutGoodsRegionalWharehouse() {
        return outGoodsRegionalWharehouse;
    }

    private TextField getStockHoldingRegionalWharehouse() {
        return stockHoldingRegionalWharehouse;
    }

    private TextField getStartingBudgetRegionalWharehouse() {
        return startingBudgetRegionalWharehouse;
    }

    private TextField getStartingOrderRegionalWharehouse() {
        return startingOrderRegionalWharehouse;
    }

    private TextField getStartingStockRegionalWharehouse() {
        return startingStockRegionalWharehouse;
    }

    private TextField getOpenOrderCostRegionalWarehouse() {
        return openOrderCostRegionalWarehouse;
    }

    private TextField getInGoodsRetailer() {
        return inGoodsRetailer;
    }

    private TextField getOutGoodsRetailer() {
        return outGoodsRetailer;
    }

    private TextField getStockHoldingRetailer() {
        return stockHoldingRetailer;
    }

    private TextField getStartingBudgetRetailer() {
        return startingBudgetRetailer;
    }

    private TextField getStartingOrderRetailer() {
        return startingOrderRetailer;
    }

    private TextField getStartingStockRetailer() {
        return startingStockRetailer;
    }

    private TextField getOpenOrderCostRetailer() {
        return openOrderCostRetailer;
    }
}

