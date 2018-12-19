package org.han.ica.asd.c.gui_replay_game.replay_data;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;

import java.util.ArrayList;

public class ReplayData {
    private static final int LOWEST_ROUND_POSSIBLE = 0;
    private static final int FIRST_ROUND_TO_DISPLAY = 1;
    private ArrayList<RoundsStub> rounds;
    private ArrayList<Facility> displayedFacilities;
    private int currentRound;
    private int totalRounds;

    public ReplayData() {
        displayedFacilities = new ArrayList<>();
        displayedFacilities.add(getAllFacilities().get(0));
        displayedFacilities.add(getAllFacilities().get(1));
        displayedFacilities.add(getAllFacilities().get(2));
        displayedFacilities.add(getAllFacilities().get(3));

        rounds = new ArrayList<>();
        rounds.add(new RoundsStub(displayedFacilities.get(0)));
        rounds.add(new RoundsStub(displayedFacilities.get(1)));
        rounds.add(new RoundsStub(displayedFacilities.get(2)));
        rounds.add(new RoundsStub(displayedFacilities.get(3)));


        currentRound = FIRST_ROUND_TO_DISPLAY;
        //totalRounds = rounds.size();
        totalRounds = 5;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public String getTotalRoundsString() {
        return String.valueOf(totalRounds);
    }

    public String getCurrentRoundString() {
        return String.valueOf(currentRound);
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public boolean incrementCurrentRound() {
        if (currentRound < totalRounds) {
            this.currentRound++;
            return true;
        }
        return false;
    }

    public boolean decrementCurrentRound() {
        if (currentRound > LOWEST_ROUND_POSSIBLE) {
            this.currentRound--;
            return true;
        }
        return false;
    }

    public void updateCurrentRound(int round) {
        if (round > totalRounds) currentRound = totalRounds;
        else if (round < LOWEST_ROUND_POSSIBLE) currentRound = LOWEST_ROUND_POSSIBLE;
        else currentRound = round;
    }

    public ArrayList<Facility> getAllFacilities() {
        //List<String> l = new ArrayList<String>(map.keySet());
        //Wanneer de rounddata aanwezig is kan het bovenstaande gebruikt worden om alle keys
        //dus de facilities op te halen
        //Wat hier nu aanwezig is is test code om zo de controller werkend te krijgen

        FacilityType factory = new FacilityType("Factory", 1, 1, 1, 1, 1, 1);
        FacilityType wholesaler = new FacilityType("Wholesaler", 1, 1, 1, 1, 1, 1);
        FacilityType retailer = new FacilityType("Retailer", 1, 1, 1, 1, 1, 1);
        FacilityType warehouse = new FacilityType("Warehouse", 1, 1, 1, 1, 1, 1);

        Facility facility1 = new Facility(factory, null, null, null, 1);
        Facility facility2 = new Facility(wholesaler, null, null, null, 2);
        Facility facility3 = new Facility(retailer, null, null, null, 3);
        Facility facility4 = new Facility(warehouse, null, null, null, 4);

        ArrayList<Facility> returnList = new ArrayList<>();

        returnList.add(facility1);
        returnList.add(facility2);
        returnList.add(facility3);
        returnList.add(facility4);

        return returnList;
    }

    public ObservableList<XYChart.Series<Double, Double>> getChartData() {
        ObservableList<XYChart.Series<Double, Double>> lineChartData = FXCollections.observableArrayList();

        for (Facility facility : displayedFacilities) {
            LineChart.Series<Double, Double> series = new LineChart.Series<>();
            series.setName(facility.getFacilityType().getFacilityName() + " " + facility.getFacilityId());

            //get rounds van deze facility uit rounds
            RoundsStub roundsstub;
            for (RoundsStub roundsStub : rounds) {
                if (roundsStub.getFacility().getFacilityId() == facility.getFacilityId()) {
                    roundsstub = roundsStub;

                    for (int i = 0; i <= currentRound; i++) {
                        series.getData().add(new XYChart.Data<>((double) i, (double) roundsstub.getRounds().get(i).getBudget()));
                    }
                }
            }

            lineChartData.add(series);
        }

        return lineChartData;
    }

    public void removeDisplayedFacility(int id) {
        for (Facility facility : displayedFacilities) {
            if (facility.getFacilityId() == id) {
                displayedFacilities.remove(facility);
                return;
            }
        }
    }

    public void addDisplayedFacility(int id) {
        for (Facility facility : getAllFacilities()) {
            if (facility.getFacilityId() == id) {
                displayedFacilities.add(facility);
                return;
            }
        }
    }
}
