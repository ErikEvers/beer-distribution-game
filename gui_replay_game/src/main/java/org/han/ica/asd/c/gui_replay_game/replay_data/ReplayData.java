package org.han.ica.asd.c.gui_replay_game.replay_data;

import org.han.ica.asd.c.model.Facility;
import org.han.ica.asd.c.model.FacilityType;
import org.han.ica.asd.c.model.Round;

import java.util.ArrayList;

public class ReplayData {
    private final int LOWEST_ROUND_POSSIBLE = 0;
    private final int FIRST_ROUND_TO_DISPLAY = 1;
    private ArrayList<Round> rounds;
    private int currentRound;
    private int totalRounds;

    public ReplayData() {
        rounds = new ArrayList<>();
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

        FacilityType type = new FacilityType("Factory", "GameId", 1, 1, 1, 1, 1, 1);

        Facility facility1 = new Facility("GameId", 1, type, "IpAddress", "gameAgentName");
        Facility facility2 = new Facility("GameId", 2, type, "IpAddress", "gameAgentName");
        Facility facility3 = new Facility("GameId", 3, type, "IpAddress", "gameAgentName");

        ArrayList<Facility> returnList = new ArrayList<>();

        returnList.add(facility1);
        returnList.add(facility2);
        returnList.add(facility3);

        return returnList;
    }
}
