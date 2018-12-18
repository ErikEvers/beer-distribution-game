package org.han.ica.asd.c.gui_play_game.see_other_facilities;

import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.han.ica.asd.c.player.PlayerComponent;
import org.han.ica.asd.c.model.Facility;
import org.han.ica.asd.c.model.FacilityLinkedTo;
import java.util.ArrayList;

/**
 * Controller for the facility overview. Accessible when turned on in the game configuration.
 */

public class SeeOtherFacilitiesController {
    @FXML
    private AnchorPane mainContainer;
    
    @FXML
    private AnchorPane facilitiesContainer;

    private ArrayList<FacilityRectangle> factories = new ArrayList<>();
    private ArrayList<FacilityRectangle> wholesalers = new ArrayList<>();
    private ArrayList<FacilityRectangle> warehouses = new ArrayList<>();
    private ArrayList<FacilityRectangle> retailers = new ArrayList<>();

    private PlayerComponent playerComponent;

    /**
     * Initialises the facility overview screen by calling the loadFacilityView() method.
     * @throws FacilityLoadingError
     */
    public void initialize() throws FacilityLoadingError {
        mainContainer.getChildren().addAll();
        playerComponent = new PlayerComponent();

        loadFacilityView();
    }

    /**
     * Method that loads facilities with its relevant edges.
     *
     * First it draws the facilities connected through the edge (if they haven't been drawn yet). Then it draws the corresponding edge between the facilities.
     *
     * Throws facilityloadingerror when a facility is of an unknown type.
     *
     * @throws FacilityLoadingError
     */

    private void loadFacilityView() throws FacilityLoadingError {
        FacilityLinkedTo[] links = playerComponent.seeOtherFacilities();

        ArrayList<Facility> drawnFacilities = new ArrayList<>();
        ArrayList<FacilityRectangle> drawnFacilityRectangles = new ArrayList<>();

        for(FacilityLinkedTo link : links) {
            drawFacilities(drawnFacilities, drawnFacilityRectangles, link);
            drawLine(drawnFacilityRectangles, link);
        }
    }

    /**
     * Draws the facilities in the link on the screen.
     *
     * @param drawnFacilities
     * Facility objects represented on screen
     * @param drawnFacilityRectangles
     * Facility rectangles visible on screen
     * @param link
     * Link/edge through which the facilities are connected
     * @throws FacilityLoadingError
     * When a facility is of an unknown type, this is thrown.
     */
    private void drawFacilities(ArrayList<Facility> drawnFacilities,
                                ArrayList<FacilityRectangle> drawnFacilityRectangles,
                                FacilityLinkedTo link) throws FacilityLoadingError {
        if(!drawnFacilities.contains(link.getFacilityDeliver())) {
            drawnFacilityRectangles.add(drawFacility(link.getFacilityDeliver()));
            drawnFacilities.add(link.getFacilityDeliver());
        }

        if(!drawnFacilities.contains(link.getFacilityOrder())) {
            drawnFacilityRectangles.add(drawFacility(link.getFacilityOrder()));
            drawnFacilities.add(link.getFacilityOrder());
        }
    }

    /**
     * Draws the line between facilities in a facilitylinkedto on the screen.
     *
     * @param drawnFacilityRectangles
     * Facility rectangles visible on screen
     * @param link
     * Link/edge through which the facilities are connected
     */
    private void drawLine(ArrayList<FacilityRectangle> drawnFacilityRectangles, FacilityLinkedTo link) {
        EdgeLine line = new EdgeLine();
        FacilityRectangle rectangle1 = new FacilityRectangle(new Facility("",0,"","",""));
        FacilityRectangle rectangle2 = new FacilityRectangle(new Facility("",0,"","",""));

        for(FacilityRectangle rectangle : drawnFacilityRectangles) {
            if(rectangle.getFacility() == link.getFacilityDeliver()) {
                rectangle1 = rectangle;
            }
            if(rectangle.getFacility() == link.getFacilityOrder()) {
                rectangle2 = rectangle;
            }
        }

        line.drawLine(rectangle1, rectangle2, rectangle1.getTranslateX(), rectangle1.getTranslateY());
        setLineStroke(link, line);

        facilitiesContainer.getChildren().add(line);
    }

    /**
     * Adjusts the colour based on the link being 'active'.
     *
     * @param link
     * Link/edge through which the facilities are connected
     * @param line
     * Visual representation of the link.
     */
    private void setLineStroke(FacilityLinkedTo link, EdgeLine line) {
        line.setStroke(Color.BLACK);

        if(!link.isActive()) {
            line.setStroke(Color.RED);
        }
    }

    /**
     * Draws the individual facility according to their type.
     *
     *
     * @param facility
     * Facility to be drawn.
     * @return
     * Returns facility that was drawn.
     * @throws FacilityLoadingError
     * When a facility is of an unknown type, this is thrown.
     */

    private FacilityRectangle drawFacility(Facility facility) throws FacilityLoadingError {
        if(facility.getFacilityType().equals("Factory")) {
            drawFactory(facility);
            return factories.get(factories.size()-1);
        } else if(facility.getFacilityType().equals("Regional warehouse")) {
            drawRegionalWarehouse(facility);
            return warehouses.get(warehouses.size()-1);
        } else if(facility.getFacilityType().equals("Wholesale")) {
            drawWholesaler(facility);
            return wholesalers.get(wholesalers.size()-1);
        } else if(facility.getFacilityType().equals("Retailer")) {
            drawRetailer(facility);
            return retailers.get(retailers.size()-1);
        } else {
            throw new FacilityLoadingError("Error while drawing facility of an unkwown type: "+facility.toString());
        }
    }

    /**
     * Method creates a new FacilityRectangle based on the facility given to the method.
     * @return New FacilityRectangle.
     */
    private FacilityRectangle createRectangle(Facility facility) {
        FacilityRectangle rectangle = new FacilityRectangle(facility);
        Tooltip tooltip = new Tooltip(playerComponent.requestFacilityInfo(facility));
        Tooltip.install(rectangle, tooltip);
        return rectangle;
    }

    /**
     * Method draws a rectangle(Factory).
     */
    private void drawFactory(Facility factory){
        double collumns = 60;
        facilitiesContainer.getChildren().removeAll(factories);
        factories.add(createRectangle(factory));
        for (int i = 0; i < factories.size(); i++) {
            factories.get(i).setTranslateX(collumns*i);
            factories.get(i).setTranslateY(0);
            if ((factories.get(i).getTranslateX()+ factories.get(i).getWidth()) > facilitiesContainer.getMinWidth()){
                facilitiesContainer.setMinWidth(factories.get(i).getTranslateX()+ factories.get(i).getWidth());
            }
            facilitiesContainer.getChildren().add(factories.get(i));
        }
    }

    /**
     * Method draws a rectangle(RegionalWarehouse).
     */
    private void drawRegionalWarehouse(Facility regionalWarehouse){
        double rows = (facilitiesContainer.getPrefHeight()/4);
        double collumns = 60;
        facilitiesContainer.getChildren().removeAll(warehouses);
        warehouses.add(createRectangle(regionalWarehouse));
        for (int i = 0; i < warehouses.size(); i++) {
            warehouses.get(i).setTranslateX(collumns*i);
            warehouses.get(i).setTranslateY(rows*1);
            if ((warehouses.get(i).getTranslateX()+ warehouses.get(i).getWidth()) > facilitiesContainer.getMinWidth()){
                facilitiesContainer.setMinWidth(warehouses.get(i).getTranslateX()+ warehouses.get(i).getWidth());
            }
            facilitiesContainer.getChildren().add(warehouses.get(i));
        }
    }

    /**
     * Method draws a rectangle(Wholesaler).
     */
    private void drawWholesaler(Facility wholesale){
        double rows = (facilitiesContainer.getPrefHeight()/4);
        double collumns = 60;
        facilitiesContainer.getChildren().removeAll(wholesalers);
        wholesalers.add(createRectangle(wholesale));
        for (int i = 0; i < wholesalers.size(); i++) {
            wholesalers.get(i).setTranslateX(collumns*i);
            wholesalers.get(i).setTranslateY(rows*2);
            if ((wholesalers.get(i).getTranslateX()+ wholesalers.get(i).getWidth()) > facilitiesContainer.getMinWidth()){
                facilitiesContainer.setMinWidth(wholesalers.get(i).getTranslateX()+ wholesalers.get(i).getWidth());
            }
            facilitiesContainer.getChildren().add(wholesalers.get(i));
        }
    }

    /**
     * Method draws a rectangle(Retailer).
     */
    private void drawRetailer(Facility retailer){
        double rows = (facilitiesContainer.getPrefHeight()/4);
        double collumns = 60;
        facilitiesContainer.getChildren().removeAll(retailers);
        retailers.add(createRectangle(retailer));
        for (int i = 0; i < retailers.size(); i++) {
            retailers.get(i).setTranslateX(collumns*i);
            retailers.get(i).setTranslateY(rows*3);
            if ((retailers.get(i).getTranslateX()+ retailers.get(i).getWidth()) > facilitiesContainer.getMinWidth()){
                facilitiesContainer.setMinWidth(retailers.get(i).getTranslateX()+ retailers.get(i).getWidth());
            }
            facilitiesContainer.getChildren().add(retailers.get(i));
        }
    }
}
