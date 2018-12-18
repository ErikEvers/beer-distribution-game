package org.han.ica.asd.c.model.domain_objects;


import java.util.List;

public class Configuration implements IDomainModel{
    private int amountOfRounds;
    private int amountOfFactories;
    private int amountOfWholesales;
    private int amountOfDistributors;
    private int amountOfRetailers;
    private int minimalOrderRetail;
    private int maximumOrderRetail;
    private boolean continuePlayingWhenBankrupt;
    private boolean insightFacilities;
    private List<Facility> facilities;
    private List<FacilityLinkedTo> facilitiesLinkedTo;
    private List<FacilityType> facilityTypes;

    public Configuration(){}

    public Configuration(int amountOfRounds, int amountOfFactories, int amountOfWholesales, int amountOfDistributors, int amountOfRetailers, int minimalOrderRetail, //NOSONAR
                         int maximumOrderRetail, boolean continuePlayingWhenBankrupt, boolean insightFacilities, List<Facility> facilities, //NOSONAR
                         List<FacilityLinkedTo> facilitiesLinkedTo, List<FacilityType> facilityTypes) //NOSONAR
    {
        this.amountOfRounds = amountOfRounds;
        this.amountOfFactories = amountOfFactories;
        this.amountOfWholesales = amountOfWholesales;
        this.amountOfDistributors = amountOfDistributors;
        this.amountOfRetailers = amountOfRetailers;
        this.minimalOrderRetail = minimalOrderRetail;
        this.maximumOrderRetail = maximumOrderRetail;
        this.continuePlayingWhenBankrupt = continuePlayingWhenBankrupt;
        this.insightFacilities = insightFacilities;
        this.facilities = facilities;
        this.facilitiesLinkedTo = facilitiesLinkedTo;
        this.facilityTypes = facilityTypes;
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

    public List<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }

    public List<FacilityLinkedTo> getFacilitiesLinkedTo() {
        return facilitiesLinkedTo;
    }

    public void setFacilitiesLinkedTo(List<FacilityLinkedTo> facilitiesLinkedTo) {
        this.facilitiesLinkedTo = facilitiesLinkedTo;
    }

    public List<FacilityType> getFacilityTypes() {
        return facilityTypes;
    }

    public void setFacilityTypes(List<FacilityType> facilityTypes) {
        this.facilityTypes = facilityTypes;
    }
}
