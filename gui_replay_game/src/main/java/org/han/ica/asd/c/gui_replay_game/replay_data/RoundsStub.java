package org.han.ica.asd.c.gui_replay_game.replay_data;

import org.han.ica.asd.c.model.domain_objects.Facility;

import java.util.ArrayList;
import java.util.Random;

public class RoundsStub {
    private ArrayList<RoundStub> rounds;
    Random rand = new Random();
    private Facility facility;

    public RoundsStub(Facility facility){
        this.rounds = new ArrayList<>();

        rounds.add(new RoundStub(0, rand.nextInt(200)-100));
        rounds.add(new RoundStub(1, rand.nextInt(200)-100));
        rounds.add(new RoundStub(2, rand.nextInt(200)-100));
        rounds.add(new RoundStub(3, rand.nextInt(200)-100));
        rounds.add(new RoundStub(4, rand.nextInt(200)-100));
        rounds.add(new RoundStub(5, rand.nextInt(200)-100));

        this.facility = facility;
    }

    public ArrayList<RoundStub> getRounds() {
        return rounds;
    }

    public void setRounds(ArrayList<RoundStub> rounds) {
        this.rounds = rounds;
    }

    public Facility getFacility() {
        return facility;
    }
}
