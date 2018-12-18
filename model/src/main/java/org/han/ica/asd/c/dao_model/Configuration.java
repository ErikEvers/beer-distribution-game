package org.han.ica.asd.c.dao_model;

import org.han.ica.asd.c.model.Facility;
import org.han.ica.asd.c.model.FacilityLinkedTo;
import org.han.ica.asd.c.model.FacilityType;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private String gameId;
    private int amountOfRounds;
    private int amountOfFactories;
    private int amountOfWholesales;
    private int amountOfDistributors;
    private int amountOfRetailers;
    private int minimalOrderRetail;
    private int maximumOrderRetail;
    private boolean continuePlayingWhenBankrupt;
    private boolean insightFacilities;
    private List<org.han.ica.asd.c.model.Facility> facilities;
    private List<org.han.ica.asd.c.model.FacilityLinkedTo> facilitiesLinkedTo;
    private List<org.han.ica.asd.c.model.FacilityType> facilityTypes;

    public Configuration(String gameId, int amountOfRounds, int amountOfFactories, int amountOfWholesales, int amountOfDistributors, int amountOfRetailers, int minimalOrderRetail, int maximumOrderRetail, boolean continuePlayingWhenBankrupt, boolean insightFacilities) { //NOSONAR
        this.gameId = gameId;
        this.amountOfRounds = amountOfRounds;
        this.amountOfFactories = amountOfFactories;
        this.amountOfWholesales = amountOfWholesales;
        this.amountOfDistributors = amountOfDistributors;
        this.amountOfRetailers = amountOfRetailers;
        this.minimalOrderRetail = minimalOrderRetail;
        this.maximumOrderRetail = maximumOrderRetail;
        this.continuePlayingWhenBankrupt = continuePlayingWhenBankrupt;
        this.insightFacilities = insightFacilities;
        facilities = new ArrayList<>();
        facilitiesLinkedTo = new ArrayList<>();
        facilityTypes = new ArrayList<>();
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getAmountOfRounds() {
        return amountOfRounds;
    }

    public void setAmountOfRounds(int amountOfRounds) {
        this.amountOfRounds = amountOfRounds;
    }

    public int getAmountOfFactories() {
        return amountOfFactories;
    }

    public void setAmountOfFactories(int amountOfFactories) {
        this.amountOfFactories = amountOfFactories;
    }

    public int getAmountOfWholesales() {
        return amountOfWholesales;
    }

    public void setAmountOfWholesales(int amountOfWholesales) {
        this.amountOfWholesales = amountOfWholesales;
    }

    public int getAmountOfDistributors() {
        return amountOfDistributors;
    }

    public void setAmountOfDistributors(int amountOfDistributors) {
        this.amountOfDistributors = amountOfDistributors;
    }

    public int getAmountOfRetailers() {
        return amountOfRetailers;
    }

    public void setAmountOfRetailers(int amountOfRetailers) {
        this.amountOfRetailers = amountOfRetailers;
    }

    public int getMinimalOrderRetail() {
        return minimalOrderRetail;
    }

    public void setMinimalOrderRetail(int minimalOrderRetail) {
        this.minimalOrderRetail = minimalOrderRetail;
    }

    public int getMaximumOrderRetail() {
        return maximumOrderRetail;
    }

    public void setMaximumOrderRetail(int maximumOrderRetail) {
        this.maximumOrderRetail = maximumOrderRetail;
    }

    public boolean isContinuePlayingWhenBankrupt() {
        return continuePlayingWhenBankrupt;
    }

    public void setContinuePlayingWhenBankrupt(boolean continuePlayingWhenBankrupt) {
        this.continuePlayingWhenBankrupt = continuePlayingWhenBankrupt;
    }

    public boolean isInsightFacilities() {
        return insightFacilities;
    }

    public void setInsightFacilities(boolean insightFacilities) {
        this.insightFacilities = insightFacilities;
    }

    public List<org.han.ica.asd.c.model.Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<org.han.ica.asd.c.model.Facility> facilities) {
        this.facilities = facilities;
    }

    public void addFacility(Facility facility){
        facilities.add(facility);
    }

    public List<org.han.ica.asd.c.model.FacilityLinkedTo> getFacilitiesLinkedTo() {
        return facilitiesLinkedTo;
    }

    public void setFacilitiesLinkedTo(List<org.han.ica.asd.c.model.FacilityLinkedTo> facilitiesLinkedTo) {
        this.facilitiesLinkedTo = facilitiesLinkedTo;
    }

    public void addFacilityLinkedTo(FacilityLinkedTo facilityLinkedTo){
        facilitiesLinkedTo.add(facilityLinkedTo);
    }

    public List<org.han.ica.asd.c.model.FacilityType> getFacilityTypes() {
        return facilityTypes;
    }

    public void setFacilityTypes(List<org.han.ica.asd.c.model.FacilityType> facilityTypes) {
        this.facilityTypes = facilityTypes;
    }

    public void addFacilityType(FacilityType facilityType){
        facilityTypes.add(facilityType);
    }
}
