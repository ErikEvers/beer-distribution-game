package org.han.ica.asd.c.gui_play_game.see_other_facilities;

import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.han.ica.asd.c.dao.FacilityDAO;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.RoomModel;
import org.han.ica.asd.c.player.PlayerComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private RoomModel roomModel;

    /**
     * Initialises the facility overview screen by calling the loadFacilityView() method.
     * @throws FacilityLoadingError
     */
    public void initialize() throws FacilityLoadingError {
        mainContainer.getChildren().addAll();
        playerComponent = new PlayerComponent();
        facilityDAO = new FacilityDAO();

        loadFacilityView();
    }

    public void setGameRoom(RoomModel roomModel) {
        this.roomModel = roomModel;
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
        Map<Facility, List<Facility>> links = playerComponent.seeOtherFacilities();

        ArrayList<Facility> drawnFacilities = new ArrayList<>();
        ArrayList<FacilityRectangle> drawnFacilityRectangles = new ArrayList<>();

        for(Facility facility : links.keySet()) {
            for(Facility child : links.get(facility)) {
							drawFacilities(drawnFacilities, drawnFacilityRectangles, facility, child);
							drawLine(drawnFacilityRectangles, facility, child);
						}
        }
    }

    /**
     * Draws the facilities in the link on the screen.
     *
     * @param drawnFacilities
     * Facility objects represented on screen
     * @param drawnFacilityRectangles
     * Facility rectangles visible on screen
     * @param parent
     * Link/edge through which the facilities are connected
     * @throws FacilityLoadingError
     * When a facility is of an unknown type, this is thrown.
     */
    private void drawFacilities(ArrayList<Facility> drawnFacilities,
                                ArrayList<FacilityRectangle> drawnFacilityRectangles,
                                Facility parent, Facility child) throws FacilityLoadingError {
        if(!drawnFacilities.contains(facilityDAO.readSpecificFacility(parent.getFacilityId()))) {
            drawnFacilityRectangles.add(drawFacility(facilityDAO.readSpecificFacility(parent.getFacilityId())));
            drawnFacilities.add(facilityDAO.readSpecificFacility(parent.getFacilityId()));
        }

        if(!drawnFacilities.contains(facilityDAO.readSpecificFacility(child.getFacilityId()))) {
            drawnFacilityRectangles.add(drawFacility(facilityDAO.readSpecificFacility(child.getFacilityId())));
            drawnFacilities.add(facilityDAO.readSpecificFacility(child.getFacilityId()));
        }
    }

    /**
     * Draws the line between facilities in a facilitylinkedto on the screen.
     *
     * @param drawnFacilityRectangles
     * Facility rectangles visible on screen
     * @param parent
		 * @param child
     * Link/edge through which the facilities are connected
     */
    private void drawLine(ArrayList<FacilityRectangle> drawnFacilityRectangles, Facility parent, Facility child) {
        EdgeLine line = new EdgeLine();
        FacilityRectangle rectangleDeliver = new FacilityRectangle(new Facility(new FacilityType("Retailer", 0, 0, 0, 0, 0, 0, 0), 1));
        FacilityRectangle rectangleOrder = new FacilityRectangle(new Facility(new FacilityType("Retailer", 0, 0, 0, 0, 0, 0, 0), 1));

        for(FacilityRectangle rectangle : drawnFacilityRectangles) {

            if(rectangle.getFacility() == facilityDAO.readSpecificFacility(child.getFacilityId())) {
                rectangleDeliver = rectangle;
            }
            if(rectangle.getFacility() == facilityDAO.readSpecificFacility(parent.getFacilityId())) {
                rectangleOrder = rectangle;
            }
        }

        line.drawLine(rectangleDeliver, rectangleOrder, rectangleDeliver.getTranslateX(), rectangleDeliver.getTranslateY());
        setLineStroke(parent, child, line);

        facilitiesContainer.getChildren().add(line);
    }

    /**
     * Adjusts the colour based on the link being 'active'.
     *
     * @param parent
		 * @param child
     * Link/edge through which the facilities are connected
     * @param line
     * Visual representation of the link.
     */
    private void setLineStroke(Facility parent, Facility child, EdgeLine line) {
        line.setStroke(Color.BLACK);

//        if(!parent.isActive() || !child.isActive) {
//            line.setStroke(Color.RED);
//        }
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
