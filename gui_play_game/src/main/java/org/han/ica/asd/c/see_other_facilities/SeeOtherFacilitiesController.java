package org.han.ica.asd.c.see_other_facilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.fakes.PlayerComponentFake;
import org.han.ica.asd.c.model.Facility;
import org.han.ica.asd.c.model.FacilityLinkedTo;

import java.util.ArrayList;

public class SeeOtherFacilitiesController {
    @FXML
    private AnchorPane mainContainer;
    
    @FXML
    private AnchorPane facilitiesContainer;

    private ArrayList<FacilityRectangle> factories = new ArrayList<FacilityRectangle>();
    private ArrayList<FacilityRectangle> wholesalers = new ArrayList<FacilityRectangle>();
    private ArrayList<FacilityRectangle> warehouses = new ArrayList<FacilityRectangle>();
    private ArrayList<FacilityRectangle> retailers = new ArrayList<FacilityRectangle>();
    private ObservableList<Facility> facilityListView = FXCollections.observableArrayList();
    private int mouseClickedCount = 0;
    private static final int NOT_CLICKED_YET = 0;
    private static final int CLICKED_ONCE = 1;
    private FacilityRectangle firstRectangleClicked;
    private double firstRectangleX;
    private double firstRectangleY;

    //Fake player component for testing purposes;
    private PlayerComponentFake playerComponent;
    
    public void initialize() throws FacilityLoadingError {
        mainContainer.getChildren().addAll();
        playerComponent = new PlayerComponentFake();

        loadFacilityView();
    }

    /**
     * Method that loads facilities with its relevant edges.
     *
     * Throws facilityloadingerror when a facility is of an unknown type.
     *
     * @throws FacilityLoadingError
     */

    private void loadFacilityView() throws FacilityLoadingError {
        FacilityLinkedTo[] links = playerComponent.seeOtherFacilities();

        ArrayList<Facility> drawnFacilities = new ArrayList<>();
        ArrayList<Facility> drawnEdges = new ArrayList<>();

        for(FacilityLinkedTo link : links) {
            if(!drawnFacilities.contains(link.getFacilityDeliver())) {
                drawFacility(link.getFacilityDeliver());
                drawnFacilities.add(link.getFacilityDeliver());
            }

            if(!drawnFacilities.contains(link.getFacilityOrder())) {
                drawFacility(link.getFacilityOrder());
                drawnFacilities.add(link.getFacilityOrder());
            }
        }
    }

    private void drawFacility(Facility facility) throws FacilityLoadingError {

        if(facility.getFacilityType().equals("Factory")) {
            drawFactory(facility);
        } else if(facility.getFacilityType().equals("Regional warehouse")) {
            drawRegionalWarehouse(facility);
        } else if(facility.getFacilityType().equals("Wholesale")) {
            drawWholesaler(facility);
        } else if(facility.getFacilityType().equals("Retailer")) {
            drawRetailer(facility);
        } else {
            throw new FacilityLoadingError("Error while drawing facility: "+facility.toString());
        }
    }

    /**
     * Method creates a new FacilityRectangle based on the facility given to the method.
     * @return New FacilityRectangle.
     */
    private FacilityRectangle createRectangle(Facility facility) {
        return new FacilityRectangle(facility);
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
