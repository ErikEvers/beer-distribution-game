package org.han.ica.asd.c.model;

import java.util.ArrayList;

public class Configuration {
    private String gameName;
    private String gameDate;
    private String gameEndDate;
    private int amountOfRounds;
    private int amountOfFactories;
    private int amountOfWholesales;
    private int amountOfDistributors;
    private int amountOfRetailers;
    private int minimalOrderRetail;
    private int maximumOrderRetail;
    private boolean continuePlayingWhenBankrupt;
    private boolean insightFacilities;
    private ArrayList<Facility> facilities;
    private ArrayList<FacilityLinkedTo> facilitiesLinkedTo;
    private ArrayList<FacilityType> facilityTypes;

    public Configuration(String gameName, String gameDate, String gameEndDate, int amountOfRounds, int amountOfFactories, int amountOfWholesales, int amountOfDistributors, int amountOfRetailers, int minimalOrderRetail, int maximumOrderRetail, boolean continuePlayingWhenBankrupt, boolean insightFacilities) {
        this.gameName = gameName;
        this.gameDate = gameDate;
        this.gameEndDate = gameEndDate;
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

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public String getGameEndDate() {
        return gameEndDate;
    }

    public void setGameEndDate(String gameEndDate) {
        this.gameEndDate = gameEndDate;
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

    public ArrayList<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(ArrayList<Facility> facilities) {
        this.facilities = facilities;
    }

    public void addFacility(Facility facility){
        facilities.add(facility);
    }

    public ArrayList<FacilityLinkedTo> getFacilitiesLinkedTo() {
        return facilitiesLinkedTo;
    }

    public void setFacilitiesLinkedTo(ArrayList<FacilityLinkedTo> facilitiesLinkedTo) {
        this.facilitiesLinkedTo = facilitiesLinkedTo;
    }

    public void addFacilityLinkedTo(FacilityLinkedTo facilityLinkedTo){
        facilitiesLinkedTo.add(facilityLinkedTo);
    }

    public ArrayList<FacilityType> getFacilityTypes() {
        return facilityTypes;
    }

    public void setFacilityTypes(ArrayList<FacilityType> facilityTypes) {
        this.facilityTypes = facilityTypes;
    }

    public void addFacilityType(FacilityType facilityType){
        facilityTypes.add(facilityType);
    }
}
