package org.han.ica.asd.c.replay_data.fakes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoundsFake {
    private List<RoundFake> rounds;
    private Random rand = new Random();

    public RoundsFake(){
        rounds = new ArrayList<>();

        for(int i = 0; i < rand.nextInt(10)+5; i++){
            RoundFake round = new RoundFake();
            round.setRoundId(i);

            ArrayList<FacilityTurnFake> turns = new ArrayList<>();
            turns.add(new FacilityTurnFake(1, rand.nextInt(10), rand.nextInt(200)-100, false));
            turns.add(new FacilityTurnFake(2, rand.nextInt(10), rand.nextInt(200)-100, false));
            turns.add(new FacilityTurnFake(3, rand.nextInt(10), rand.nextInt(200)-100, false));
            turns.add(new FacilityTurnFake(4, rand.nextInt(10), rand.nextInt(200)-100, false));

            round.setFacilityTurns(turns);

            rounds.add(round);
        }
    }

    public List<RoundFake> getRounds(){
        return rounds;
    }
}
