package org.han.ica.asd.c.gui_configure_game.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.gui_configure_game.graph.EdgeLine;
import org.han.ica.asd.c.gui_configure_game.graph.FacilityRectangle;
import org.han.ica.asd.c.gui_configure_game.graph.Factory;
import org.han.ica.asd.c.gui_configure_game.graph.Graph;
import org.han.ica.asd.c.gui_configure_game.graph.GraphException;
import org.han.ica.asd.c.gui_configure_game.graph.GraphFacility;
import org.han.ica.asd.c.gui_configure_game.graph.RegionalWarehouse;
import org.han.ica.asd.c.gui_configure_game.graph.Retailer;
import org.han.ica.asd.c.gui_configure_game.graph.Wholesale;
import org.han.ica.asd.c.gui_configure_game.graphutil.GraphConverterToDomain;
import org.han.ica.asd.c.gui_configure_game.graphutil.GraphToFacilityChecker;
import org.han.ica.asd.c.model.domain_objects.Configuration;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GameSetupController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(Logger.class.getName());

    private ArrayList<FacilityRectangle> factories = new ArrayList<>();
    private ArrayList<FacilityRectangle> wholesalers = new ArrayList<>();
    private ArrayList<FacilityRectangle> warehouses = new ArrayList<>();
    private ArrayList<FacilityRectangle> retailers = new ArrayList<>();
    private ObservableList<GraphFacility> graphFacilityListView = FXCollections.observableArrayList();


    private int mouseClickedCount = 0;
    private static final int NOT_CLICKED_YET = 0;
    private static final int CLICKED_ONCE = 1;

    private FacilityRectangle firstRectangleClicked;

    private double firstRectangleX;
    private double firstRectangleY;


    @Inject
    GraphConverterToDomain graphConverterToDomain;

    @Inject
    GraphToFacilityChecker graphToFacilityChecker;


    @Inject
    @Named("GameSetupStart")
    private IGUIHandler gameSetupStart;

    @Inject
    @Named("GameSetupType")
    private IGUIHandler gameSetupType;

    @Inject
    private Graph graph;

    /**
     * Configuration variables that should be passed down to the next screen
     */
    private Configuration configuration;
    private String gameName = "";
    private boolean onlineGame = true;

    @FXML
    private ComboBox<GraphFacility> comboBox;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private AnchorPane facilitiesContainer;


    /**
     * Method to initialize the controller. Will only be called once when the fxml is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainContainer.getChildren().addAll();
        fillComboBox();
    }

    void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    void setOnlineGame(boolean onlineGame) {
        this.onlineGame = onlineGame;
    }

    void setGameName(String gameName) {
        this.gameName = gameName;
    }

    /**
     * Method fires when the add facility button is clicked.
     */
    @FXML
    public void handleAddFacilityButtonClick() {

        graphConverterToDomain.setGraph(graph);
        int[] countAll = graphConverterToDomain.countAll();

        int factoryindexIn = 1 + countAll[0];
        int regionalWharehouseindexIn = 1 + countAll[0] + countAll[1];
        int wholsaleindexIn = 1 + countAll[0] + countAll[1] + countAll[2];
        int retailerindexIn = 1 + countAll[0] + countAll[1] + countAll[2] + countAll[3];

        GraphFacility graphFacility = comboBox.getSelectionModel().getSelectedItem();

        if (graphFacility instanceof Factory) {
            GraphFacility toDraw = new Factory();
            toDraw.setId(factoryindexIn);
            graph.addFacility(toDraw);
            drawRectangle(toDraw);
        }
        if (graphFacility instanceof RegionalWarehouse) {
            GraphFacility toDraw = new RegionalWarehouse();
            toDraw.setId(regionalWharehouseindexIn);
            graph.addFacility(toDraw);
            drawRectangle(toDraw);
        }
        if (graphFacility instanceof Wholesale) {
            GraphFacility toDraw = new Wholesale();
            toDraw.setId(wholsaleindexIn);
            graph.addFacility(toDraw);
            drawRectangle(toDraw);
        }
        if (graphFacility instanceof Retailer) {
            GraphFacility toDraw = new Retailer();
            toDraw.setId(retailerindexIn);
            graph.addFacility(toDraw);
            drawRectangle(toDraw);
        }
        updateId();
    }

    /**
     * Method draws a rectangle based on the selected facility.
     */
    private void drawRectangle(GraphFacility graphFacility) {

        if (graphFacility instanceof Factory) {
            drawFactory(graphFacility);
        } else if (graphFacility instanceof RegionalWarehouse) {
            drawRegionalWarehouse(graphFacility);
        } else if (graphFacility instanceof Wholesale) {
            drawWholesaler(graphFacility);
        } else if (graphFacility instanceof Retailer) {
            drawRetailer(graphFacility);
        }
    }

    /**
     * Method to keep track of what rectangle(facility) is clicked and draws a line between rectangles.
     *
     * @param rectangle facility that is clicked.
     * @throws GraphException Facility not found in list
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
     * @throws GraphException when a facility is not in the list
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
                graph.removeChild(line.getSupplier().getGraphFacility(), line.getBuyer().getGraphFacility());
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
     * @throws GraphException when a facility is missing from the list
     */
    private void connect(FacilityRectangle r1, FacilityRectangle r2, double x, double y, EdgeLine line) throws GraphException {
        line.drawLine(r1, r2, x, y);
        line.setSupplier(r1);
        line.setBuyer(r2);

        r1.getLines().add(line);
        r2.getLines().add(line);
        graph.addEdge(r1.getGraphFacility(), r2.getGraphFacility());
    }

    /**
     * Method draws a rectangle(Factory).
     *
     * @param graphFacility graph version of facility
     */
    private void drawFactory(GraphFacility graphFacility) {
        double collumns = 60;
        facilitiesContainer.getChildren().removeAll(factories);
        factories.add(createRectangle(graphFacility));

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
     *
     * @param graphFacility graph version of facility
     */
    private void drawRegionalWarehouse(GraphFacility graphFacility) {
        double rows = (facilitiesContainer.getPrefHeight() / 4);
        double collumns = 60;
        facilitiesContainer.getChildren().removeAll(warehouses);
        warehouses.add(createRectangle(graphFacility));
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
     *
     * @param graphFacility graph version of facility
     */
    private void drawWholesaler(GraphFacility graphFacility) {
        double rows = (facilitiesContainer.getPrefHeight() / 4);
        double collumns = 60;
        facilitiesContainer.getChildren().removeAll(wholesalers);
        wholesalers.add(createRectangle(graphFacility));
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
     *
     * @param graphFacility graph version of facility
     */
    private void drawRetailer(GraphFacility graphFacility) {
        double rows = (facilitiesContainer.getPrefHeight() / 4);
        double collumns = 60;
        facilitiesContainer.getChildren().removeAll(retailers);
        retailers.add(createRectangle(graphFacility));
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
    private void setBackButtonAction() {
        Object[] data = new Object[3];
        data[0] = configuration;
        data[1] = gameName;
        data[2] = onlineGame;
        gameSetupStart.setData(data);
        gameSetupStart.setupScreen();
    }

    private void popUpError() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "The graph is either incomplete or does not have a complete line of facilities");
        alert.setHeaderText("Warning");
        alert.setTitle("Warning");
        alert.showAndWait();
    }

    /**
     * Button function to proceed to the next screen
     */
    @FXML
    private void setNextScreen() {
        if (graphToFacilityChecker.oneOfEverything(graph) && graphToFacilityChecker.graphChecker(graph.getFacilities())) {
            fillConfiguration();
            Object[] data = new Object[3];
            data[0] = configuration;
            data[1] = gameName;
            data[2] = onlineGame;
            gameSetupType.setData(data);
            gameSetupType.setupScreen();
        } else {
            popUpError();
        }

    }


    private void updateId() {

        graphConverterToDomain.setGraph(graph);
        int[] countAll = graphConverterToDomain.countAll();


        int factoryindexToGraph = 1;
        int regionalWharehouseindexToGraph = 1 + countAll[0];
        int wholsaleindexToGraph = 1 + countAll[0] + countAll[1];
        int retailerindexToGraph = 1 + countAll[0] + countAll[1] + countAll[2];


        for (GraphFacility current : graph.getFacilities()) {
            if (current instanceof Factory) {
                current.setId(factoryindexToGraph);
                factoryindexToGraph++;
            } else if (current instanceof RegionalWarehouse) {
                current.setId(regionalWharehouseindexToGraph);
                regionalWharehouseindexToGraph++;
            } else if (current instanceof Wholesale) {
                current.setId(wholsaleindexToGraph);
                wholsaleindexToGraph++;
            } else if (current instanceof Retailer) {
                current.setId(retailerindexToGraph);
                retailerindexToGraph++;
            }
        }
    }

    /**
     * further fills the confirguration by de graph made in the GUI
     */
    private void fillConfiguration() {

        graphConverterToDomain.setGraph(graph);

        int[] countAll = graphConverterToDomain.countAll();

        configuration.setFacilities(graphConverterToDomain.allToList());
        configuration.setFacilitiesLinkedTo(graphConverterToDomain.convertToDomeinGraph());
        configuration.setFacilities(graphConverterToDomain.allToList());
        configuration.setFacilitiesLinkedTo(graphConverterToDomain.convertToDomeinGraph());
        configuration.setAmountOfFactories(countAll[0]);
        configuration.setAmountOfWarehouses(countAll[1]);
        configuration.setAmountOfWholesalers(countAll[2]);
        configuration.setAmountOfRetailers(countAll[3]);

    }

}
