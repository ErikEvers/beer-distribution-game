package org.han.ica.asd.c;

import org.han.ica.asd.c.gameleader.GameLeader;
import org.han.ica.asd.c.gameleader.TurnHandler;
import org.han.ica.asd.c.gameleader.componentInterfaces.IConnectorForLeader;
import org.han.ica.asd.c.gameleader.componentInterfaces.ILeaderGameLogic;
import org.han.ica.asd.c.model.BeerGame;
import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.Round;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.inject.Inject;

public class GameLeaderTest {
    @Mock
    private IConnectorForLeader connectorForLeader;
    @Mock
    private ILeaderGameLogic gameLogic;

    @Mock
    private BeerGame game;
    @Mock
    private TurnHandler turnHandler;
    @Mock
    private Round currentRoundData;

    private FacilityTurn facilityTurnModel;

    @InjectMocks
    private
    GameLeader gameLeader;

    private int turnsExpected;
    private int turnsReceived;

    @Test
    public void IsANextRoundAdded() {
//        this.facilityTurnModel = new FacilityTurn();
//        facilityTurnModel.setOrder(0);
//        facilityTurnModel.setStock(10);
//
//        gameLeader.turnModelReceived(facilityTurnModel);
    }
}
