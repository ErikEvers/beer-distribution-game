package org.han.ica.asd.c.model.domain_objects;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<Facility, List<Facility>> facilitiesLinkedTo;

    public Configuration(){
        //Empty constructor for GUICE
        this.facilities = new ArrayList<>();
        this.facilitiesLinkedTo = new HashMap<>();
    }

    public Configuration(int amountOfRounds, int amountOfFactories, int amountOfWholesales, int amountOfDistributors, int amountOfRetailers, int minimalOrderRetail, //NOSONAR
                         int maximumOrderRetail, boolean continuePlayingWhenBankrupt, boolean insightFacilities, List<Facility> facilities, //NOSONAR
                         Map<Facility, List<Facility>> facilitiesLinkedTo, List<FacilityType> facilityTypes) //NOSONAR
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
    }

    public Configuration(int amountOfRounds, int amountOfFactories, int amountOfWholesales, int amountOfDistributors,
                         int amountOfRetailers, int minimalOrderRetail, int maximumOrderRetail,
                         boolean insightFacilities, boolean continuePlayingWhenBankrupt){
        this.amountOfRounds = amountOfRounds;
        this.amountOfFactories = amountOfFactories;
        this.amountOfWholesales = amountOfWholesales;
        this.amountOfDistributors = amountOfDistributors;
        this.amountOfRetailers = amountOfRetailers;
        this.minimalOrderRetail = minimalOrderRetail;
        this.maximumOrderRetail = maximumOrderRetail;
        this.continuePlayingWhenBankrupt = continuePlayingWhenBankrupt;
        this.insightFacilities = insightFacilities;
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

    public Map<Facility, List<Facility>> getFacilitiesLinkedTo() {
        return facilitiesLinkedTo;
    }

    public void setFacilitiesLinkedTo(Map<Facility, List<Facility>> facilitiesLinkedTo) {
        this.facilitiesLinkedTo = facilitiesLinkedTo;
    }
}
