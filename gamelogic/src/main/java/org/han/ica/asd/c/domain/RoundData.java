package org.han.ica.asd.c.domain;

import java.util.LinkedList;
import java.util.List;

public class RoundData {
    private int roundNumber;
    private List<PlayerRoundData> roundDataList;

    public RoundData(PlayerRoundData roundData) {
        roundDataList = new LinkedList<>();
        roundDataList.add(roundData);
        roundNumber = 0;
    }

    public void addPlayerRoundData(PlayerRoundData playerRoundData) {
        roundDataList.add(playerRoundData);
    }
}
