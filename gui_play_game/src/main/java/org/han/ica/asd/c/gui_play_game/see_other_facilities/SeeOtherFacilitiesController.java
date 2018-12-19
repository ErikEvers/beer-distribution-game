package org.han.ica.asd.c.gui_play_game.see_other_facilities;

import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.han.ica.asd.c.FacilityDAO;
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

    private FacilityDAO facilityDAO;

    private String gameId;

    /**
     * Initialises the facility overview screen by calling the loadFacilityView() method.
     * @throws FacilityLoadingError
     */
    public void initialize() throws FacilityLoadingError {
        mainContainer.getChildren().addAll();
        playerComponent = new PlayerComponent();
        facilityDAO = new FacilityDAO();
        gameId = "";

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
        if(!drawnFacilities.contains(facilityDAO.readSpecificFacility(link.getFacilityIdDeliver(), gameId))) {
            drawnFacilityRectangles.add(drawFacility(facilityDAO.readSpecificFacility(link.getFacilityIdDeliver(), gameId)));
            drawnFacilities.add(facilityDAO.readSpecificFacility(link.getFacilityIdDeliver(), gameId));
        }

        if(!drawnFacilities.contains(facilityDAO.readSpecificFacility(link.getFacilityIdOrder(), gameId))) {
            drawnFacilityRectangles.add(drawFacility(facilityDAO.readSpecificFacility(link.getFacilityIdOrder(), gameId)));
            drawnFacilities.add(facilityDAO.readSpecificFacility(link.getFacilityIdOrder(), gameId));
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
        FacilityRectangle rectangleDeliver = new FacilityRectangle(new Facility("",0,null,"",""));
        FacilityRectangle rectangleOrder = new FacilityRectangle(new Facility("",0,null,"",""));

        for(FacilityRectangle rectangle : drawnFacilityRectangles) {
            if(rectangle.getFacility() == facilityDAO.readSpecificFacility(link.getFacilityIdDeliver(), gameId)) {
                rectangleDeliver = rectangle;
            }
            if(rectangle.getFacility() == facilityDAO.readSpecificFacility(link.getFacilityIdOrder(), gameId)) {
                rectangleOrder = rectangle;
            }
        }

        line.drawLine(rectangleDeliver, rectangleOrder, rectangleDeliver.getTranslateX(), rectangleDeliver.getTranslateY());
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
     * Calls the drawFacilityOnScreen method in different ways depending on the type of the given facility.
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
            drawFacilityOnScreen(facility, factories, 0);
            return factories.get(factories.size()-1);
        } else if(facility.getFacilityType().equals("Regional warehouse")) {
            drawFacilityOnScreen(facility, warehouses, 0);
            return warehouses.get(warehouses.size()-1);
        } else if(facility.getFacilityType().equals("Wholesale")) {
            drawFacilityOnScreen(facility, wholesalers, 0);
            return wholesalers.get(wholesalers.size()-1);
        } else if(facility.getFacilityType().equals("Retailer")) {
            drawFacilityOnScreen(facility, retailers, 0);
            return retailers.get(retailers.size()-1);
        } else {
            throw new FacilityLoadingError("Error while drawing facility of an unkwown type: "+facility.toString());
        }
    }

    /**
     * Draws the facility on the screen.
     * @param facility
     * Facility to be drawn.
     * @param facilityList
     * List of drawn facility rectangles.
     * @param y
     * Y-axis on which the facility is to be drawn.
     */

    private void drawFacilityOnScreen(Facility facility, ArrayList<FacilityRectangle> facilityList, int y) {
        double rows = (facilitiesContainer.getPrefHeight()/4);
        double collumns = 60;
        facilitiesContainer.getChildren().removeAll(facilityList);
        facilityList.add(createRectangle(facility));
        for (int i = 0; i < facilityList.size(); i++) {
            facilityList.get(i).setTranslateX(collumns*i);
            facilityList.get(i).setTranslateY(rows*y);
            if ((facilityList.get(i).getTranslateX()+ facilityList.get(i).getWidth()) > facilitiesContainer.getMinWidth()){
                facilitiesContainer.setMinWidth(warehouses.get(i).getTranslateX()+ warehouses.get(i).getWidth());
            }
            facilitiesContainer.getChildren().add(warehouses.get(i));
        }
    }

    /**
     * Method creates a new FacilityRectangle based on the facility given to the method.
     * @return New FacilityRectangle.
     */
    private FacilityRectangle createRectangle(Facility facility) {
        FacilityRectangle rectangle = new FacilityRectangle(facility);
        installTooltip(facility, rectangle);
        return rectangle;
    }

    /**
     * Installs the tooltip with information regarding the facility on the rectangle.
     */
    private void installTooltip(Facility facility, FacilityRectangle rectangle) {
        Tooltip tooltip = new Tooltip(playerComponent.requestFacilityInfo(facility));
        Tooltip.install(rectangle, tooltip);
    }
}
