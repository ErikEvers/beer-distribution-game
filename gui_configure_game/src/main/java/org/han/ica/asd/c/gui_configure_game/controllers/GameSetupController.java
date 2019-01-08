package org.han.ica.asd.c.gui_configure_game.controllers;

import javafx.fxml.Initializable;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.gui_configure_game.graph.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.gui_configure_game.graphUtil.GraphConverterToDomain;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;


import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import java.net.URL;
import java.util.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.collect.Iterables.isEmpty;


public class GameSetupController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(Logger.class.getName());

    private ArrayList<FacilityRectangle> factories = new ArrayList<FacilityRectangle>();
    private ArrayList<FacilityRectangle> wholesalers = new ArrayList<FacilityRectangle>();
    private ArrayList<FacilityRectangle> warehouses = new ArrayList<FacilityRectangle>();
    private ArrayList<FacilityRectangle> retailers = new ArrayList<FacilityRectangle>();
    private ObservableList<GraphFacility> graphFacilityListView = FXCollections.observableArrayList();

    private final Provider<Facility> facilityProvider;


    private int mouseClickedCount = 0;
    private static final int NOT_CLICKED_YET = 0;
    private static final int CLICKED_ONCE = 1;

    private FacilityRectangle firstRectangleClicked;

    private double firstRectangleX;
    private double firstRectangleY;


    @Inject
    GraphConverterToDomain graphConverterToDomain;

    @Inject
    @Named("GameSetupStart")
    private IGUIHandler gameSetupStart;

    @Inject
    @Named("GameSetupType")
    private IGUIHandler gameSetupType;

    @Inject
    private Graph graph;


    private Configuration configuration;

    @FXML
    private ComboBox<GraphFacility> comboBox;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private AnchorPane facilitiesContainer;

    @FXML
    private Button back;

    @FXML
    private Button testB;
    @FXML
    private Button testB1;
    @FXML
    private Button testB2;

    @FXML
    private Button next;

    @Inject
    public GameSetupController(Provider<Facility> facilityProvider) {
        this.facilityProvider = facilityProvider;
    }

    /**
     * Method to initialize the controller. Will only be called once when the fxml is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainContainer.getChildren().addAll();
        fillComboBox();
        setBackButtonAction();
        setNextScreen();
        test();
        test1();
        test2();

    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Method fires when the add facility button is clicked.
     */
    @FXML
    public void handleAddFacilityButtonClick() {
        drawRectangle();
        GraphFacility graphFacility = comboBox.getSelectionModel().getSelectedItem();
        if (graphFacility instanceof Factory) {
            graph.addFacility(new Factory());
        }
        if (graphFacility instanceof RegionalWarehouse) {
            graph.addFacility(new RegionalWarehouse());
        }
        if (graphFacility instanceof Wholesale) {
            graph.addFacility(new Wholesale());
        }
        if (graphFacility instanceof Retailer) {
            graph.addFacility(new Retailer());
        }

        setBackButtonAction();
    }

    /**
     * Method draws a rectangle based on the selected facility.
     */
    private void drawRectangle() {
        GraphFacility graphFacility = comboBox.getSelectionModel().getSelectedItem();

        if (graphFacility instanceof Factory) {
            drawFactory();
        } else if (graphFacility instanceof RegionalWarehouse) {
            drawRegionalWarehouse();
        } else if (graphFacility instanceof Wholesale) {
            drawWholesaler();
        } else if (graphFacility instanceof Retailer) {
            drawRetailer();
        }
    }

    /**
     * Method to keep track of what rectangle(facility) is clicked and draws a line between rectangles.
     *
     * @param rectangle facility that is clicked.
     * @throws GraphException
     */
    private void handleFacilityRectangleClick(FacilityRectangle rectangle) throws GraphException {
        if (mouseClickedCount == NOT_CLICKED_YET) {
            firstRectangleClicked = rectangle;
            firstRectangleX = rectangle.getTranslateX();
            firstRectangleY = rectangle.getTranslateY();
            mouseClickedCount++;
        } else if (mouseClickedCount == CLICKED_ONCE) {
            if (firstRectangleClicked.getGraphFacility() != rectangle.getGraphFacility() && !checkIfEdgeExists(firstRectangleClicked, rectangle)) {
                facilitiesContainer.getChildren().add(createLine(firstRectangleClicked, rectangle, firstRectangleX, firstRectangleY));
                mouseClickedCount = NOT_CLICKED_YET;
            } else {
                mouseClickedCount = NOT_CLICKED_YET;
            }
        }
    }

    /**
     * Method that removes a rectangle(facility) when right clicked.
     *
     * @param rectangle facility that is clicked.
     */
    private void handleFacilityRectangleRightClick(FacilityRectangle rectangle) {

        GraphFacility graphFacility = rectangle.getGraphFacility();

        if (graphFacility instanceof Factory) {
            factories.remove(rectangle);
        }
        if (graphFacility instanceof RegionalWarehouse) {
            warehouses.remove(rectangle);
        }
        if (graphFacility instanceof Wholesale) {
            wholesalers.remove(rectangle);
        }
        if (graphFacility instanceof Retailer) {
            retailers.remove(rectangle);
        }

        for (EdgeLine line : rectangle.getLines()) {
            facilitiesContainer.getChildren().remove(line);
        }
        facilitiesContainer.getChildren().remove(rectangle);
        graph.removeFacility(rectangle.getGraphFacility());
    }

    /**
     * Method fills the combobox with facility options.
     */
    private void fillComboBox() {
        GraphFacility factory = new Factory();
        GraphFacility retailer = new Retailer();
        GraphFacility wholesale = new Wholesale();
        GraphFacility regionalWarehouse = new RegionalWarehouse();
        graphFacilityListView.add(factory);
        graphFacilityListView.add(regionalWarehouse);
        graphFacilityListView.add(wholesale);
        graphFacilityListView.add(retailer);
        comboBox.setItems(graphFacilityListView);
    }

    /**
     * Method creates a new FacilityRectangle based on the graphFacility that is selected in the combobox.
     *
     * @param graphFacility Selected graphFacility in combobox.
     * @return New FacilityRectangle.
     */
    private FacilityRectangle createRectangle(GraphFacility graphFacility) {
        final FacilityRectangle rectangle = new FacilityRectangle(graphFacility);

        rectangle.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                try {
                    handleFacilityRectangleClick(rectangle);
                } catch (GraphException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }
            }
            if (event.isSecondaryButtonDown()) {
                handleFacilityRectangleRightClick(rectangle);
            }
        });

        return rectangle;
    }

    /**
     * Method creates a line between rectangles.
     *
     * @param r1 First rectangle.
     * @param r2 Second rectangle.
     * @param x  x coordinate of first rectangle.
     * @param y  y coordinate of first rectangle.
     * @return New line.
     * @throws GraphException
     */
    private EdgeLine createLine(FacilityRectangle r1, FacilityRectangle r2, double x, double y) throws GraphException {
        final EdgeLine line = new EdgeLine();
        line.setCursor(Cursor.HAND);

        if (r1.getGraphFacility() instanceof Factory && r2.getGraphFacility() instanceof RegionalWarehouse) {
            connect(r1, r2, x, y, line);
        }

        if (r1.getGraphFacility() instanceof RegionalWarehouse && r2.getGraphFacility() instanceof Wholesale) {
            connect(r1, r2, x, y, line);
        }

        if (r1.getGraphFacility() instanceof Wholesale && r2.getGraphFacility() instanceof Retailer) {
            connect(r1, r2, x, y, line);
        }

        line.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                graph.removeEdge((ISupplier) line.getSupplier().getGraphFacility(), (IBuyer) line.getBuyer().getGraphFacility());
                line.getSupplier().getLines().remove(line);
                facilitiesContainer.getChildren().remove(line);
            }
        });

        return line;
    }

    /**
     * Method checks if an edge between rectangles(facilities) already exists.
     *
     * @param supplier Supplier in edge.
     * @param buyer    Buyer in edge.
     * @return false if the edgde does not exist yet.
     */
    private boolean checkIfEdgeExists(FacilityRectangle supplier, FacilityRectangle buyer) {
        if (supplier.getLines().isEmpty()) {
            return false;
        }
        for (EdgeLine line : supplier.getLines()) {
            if (line.getSupplier().equals(supplier) && line.getBuyer().equals(buyer)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Draws the line between rectangles. Also adds an edge to the graph.
     *
     * @param r1   First rectangle.
     * @param r2   Second rectangle.
     * @param x    x coordinate of first rectangle.
     * @param y    y coordinate of first rectangle.
     * @param line Line to be drawn.
     * @throws GraphException
     */
    private void connect(FacilityRectangle r1, FacilityRectangle r2, double x, double y, EdgeLine line) throws GraphException {
        line.drawLine(r1, r2, x, y);
        line.setSupplier(r1);
        line.setBuyer(r2);
        r1.getLines().add(line);
        r2.getLines().add(line);
        graph.addEdge((ISupplier) r1.getGraphFacility(), (IBuyer) r2.getGraphFacility());
    }

    /**
     * Method draws a rectangle(Factory).
     */
    private void drawFactory() {
        double collumns = 60;
        facilitiesContainer.getChildren().removeAll(factories);
        factories.add(createRectangle(comboBox.getSelectionModel().getSelectedItem()));
        for (int i = 0; i < factories.size(); i++) {
            factories.get(i).setTranslateX(collumns * i);
            factories.get(i).setTranslateY(0);
            if ((factories.get(i).getTranslateX() + factories.get(i).getWidth()) > facilitiesContainer.getMinWidth()) {
                facilitiesContainer.setMinWidth(factories.get(i).getTranslateX() + factories.get(i).getWidth());
            }
            facilitiesContainer.getChildren().add(factories.get(i));
        }
    }

    /**
     * Method draws a rectangle(RegionalWarehouse).
     */
    private void drawRegionalWarehouse() {
        double rows = (facilitiesContainer.getPrefHeight() / 4);
        double collumns = 60;
        facilitiesContainer.getChildren().removeAll(warehouses);
        warehouses.add(createRectangle(comboBox.getSelectionModel().getSelectedItem()));
        for (int i = 0; i < warehouses.size(); i++) {
            warehouses.get(i).setTranslateX(collumns * i);
            warehouses.get(i).setTranslateY(rows * 1);
            if ((warehouses.get(i).getTranslateX() + warehouses.get(i).getWidth()) > facilitiesContainer.getMinWidth()) {
                facilitiesContainer.setMinWidth(warehouses.get(i).getTranslateX() + warehouses.get(i).getWidth());
            }
            facilitiesContainer.getChildren().add(warehouses.get(i));
        }
    }

    /**
     * Method draws a rectangle(Wholesaler).
     */
    private void drawWholesaler() {
        double rows = (facilitiesContainer.getPrefHeight() / 4);
        double collumns = 60;
        facilitiesContainer.getChildren().removeAll(wholesalers);
        wholesalers.add(createRectangle(comboBox.getSelectionModel().getSelectedItem()));
        for (int i = 0; i < wholesalers.size(); i++) {
            wholesalers.get(i).setTranslateX(collumns * i);
            wholesalers.get(i).setTranslateY(rows * 2);
            if ((wholesalers.get(i).getTranslateX() + wholesalers.get(i).getWidth()) > facilitiesContainer.getMinWidth()) {
                facilitiesContainer.setMinWidth(wholesalers.get(i).getTranslateX() + wholesalers.get(i).getWidth());
            }
            facilitiesContainer.getChildren().add(wholesalers.get(i));
        }
    }

    /**
     * Method draws a rectangle(Retailer).
     */
    private void drawRetailer() {
        double rows = (facilitiesContainer.getPrefHeight() / 4);
        double collumns = 60;
        facilitiesContainer.getChildren().removeAll(retailers);
        retailers.add(createRectangle(comboBox.getSelectionModel().getSelectedItem()));
        for (int i = 0; i < retailers.size(); i++) {
            retailers.get(i).setTranslateX(collumns * i);
            retailers.get(i).setTranslateY(rows * 3);
            if ((retailers.get(i).getTranslateX() + retailers.get(i).getWidth()) > facilitiesContainer.getMinWidth()) {
                facilitiesContainer.setMinWidth(retailers.get(i).getTranslateX() + retailers.get(i).getWidth());
            }
            facilitiesContainer.getChildren().add(retailers.get(i));
        }
    }

    /**
     * Button function to return to the previous screen
     */
    @FXML
    public void setBackButtonAction() {
        back.setOnAction(event -> gameSetupStart.setupScreen());
    }

    /**
     * Button function to proceed to the next screen
     */
    @FXML
    public void setNextScreen() {
        //  graphToFacilityChecker.graphChecker(graph.getFacilities().size(), graph);
        // open popUp scherm.
        next.setOnAction(event -> gameSetupType.setupScreen());
    }

    public void addIdToGraph() {

        graphConverterToDomain.setGraph(graph);
        int[] countAll = graphConverterToDomain.countAll();
        System.out.println("factories = " + countAll[0]);
        System.out.println("RegionalWarehouse = " + countAll[1]);
        System.out.println("Wholesale = " + countAll[2]);
        System.out.println("Retailer = " + countAll[3]);

        int factoryindex = 1;
        int regionalWharehouseindex = 1 + countAll[0];
        int wholsaleindex = 1 + countAll[0] + countAll[1];
        int retailerindex = 1 + countAll[0] + countAll[1] + countAll[2];


        for (GraphFacility current : graph.getFacilities()) {
            if (current instanceof Factory) {
                current.setId(factoryindex);
                factoryindex++;
            } else if (current instanceof RegionalWarehouse) {
                current.setId(regionalWharehouseindex);
                regionalWharehouseindex++;
            } else if (current instanceof Wholesale) {
                current.setId(wholsaleindex);
                wholsaleindex++;
            } else if (current instanceof Retailer) {
                current.setId(retailerindex);
                retailerindex++;
            }
        }
    }

    /**
     * further fills the confirguration by de graph made in the GUI
     */
    public void fillConfiguration() {

        addIdToGraph();

        graphConverterToDomain.setGraph(graph);

        int[] countAll = graphConverterToDomain.countAll();

        configuration.setFacilities(graphConverterToDomain.allToList());
        configuration.setFacilitiesLinkedTo(graphConverterToDomain.convertToDomeinGraph());
        configuration.setAmountOfFactories(countAll[0]);
        configuration.setAmountOfDistributors(countAll[1]);
        configuration.setAmountOfWholesales(countAll[2]);
        configuration.setAmountOfRetailers(countAll[3]);

    }


    @FXML
    public void test() {
        testB.setOnAction(event ->
                addIdToGraph());
    }

    @FXML
    public void test1() {
        testB1.setOnAction(event ->
                printAll());
    }


    public void printAll() {
        for (GraphFacility current : graph.getFacilities()) {
            if (current instanceof Factory) {
                System.out.println("factoryId = " + current.getId());
            }
            if (current instanceof RegionalWarehouse) {
                System.out.println("RegionalWarehouse = " + current.getId());
            }
            if (current instanceof Wholesale) {
                System.out.println("Wholesale = " + current.getId());
            }
            if (current instanceof Retailer) {
                System.out.println("Retailer = " + current.getId());
            }
        }
    }


    @FXML
    public void test2() {
        testB2.setOnAction(event ->
                System.out.println(graph.getFacilities().size()));
    }

}
